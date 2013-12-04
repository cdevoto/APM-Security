package com.compuware.apmng.sso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.compuware.apmng.util.ServletUtils;
import com.compuware.apmng.util.StringUtils;

/**
 * <p>
 * This filter should be applied whenever a protected resource is accessed.  A protected resource
 * is defined as a resource which should not be accessible unless the user has been authenticated
 * by the SSO Identity Provider. This filter determines whether the user has a valid
 * session with the IdP.  If so, it retrieves the user's attributes from the IdP via a REST call
 * (assuming this has not already been done during the current session), and makes these attributes
 * available to downstream servlet components by attaching a UserContext object to the request.
 * If the user does not have a valid session with the IdP, this filter redirects the user to the IdP's
 * login form. The IdP will authenticate the user, and then redirect back to the originally requested
 * resource.
 * </p>
 * <p>
 * This filter is parameterized to allow for changes to all of the following properties via filter configuration params:
 * <ul>
 * 	<li>The loginUrl of the IdP
 *  <li>The baseUrl of the REST endpoints exposed by the IdP (should be an HTTPS URL)
 *  <li>The name of the IdP cookie which stores the ssoToken (typically 'iPlanetDirectoryPro')
 *  <li>The names of the user attributes which should be retrieved from the IdP
 * </ul>
 * </p>
 * @author Carlos Devoto
 *
 */
//Using a deprecated HttpClient API in order to override the SSL host name verification logic to allow for self-signed certificates.
@SuppressWarnings("deprecation") 
public class SSOFilter implements Filter {
	// The names of various filter configuration parameters:
	private static final String IDP_REST_ENDPOINT_PARM = "idpRestEndpoint";
	private static final String IDP_LOGIN_URL_PARM = "idpLoginUrl";
	private static final String IDP_COOKIE_NAME_PARM = "idpCookieName";
	private static final String USER_ATTRIBUTES_PARM = "userAttributes";
	
	// Objects representing all of the possible states that the user can find himself in:
	private final SessionState SSO_COOKIE_ABSENT = new SsoCookieAbsentState(); 
	private final SessionState SSO_TOKEN_INVALID = new SsoTokenInvalidState(); 
	private final SessionState SSO_TOKEN_VALID = new SsoTokenValidState(); 
	private final SessionState SSO_TOKEN_ABSENT = new SsoTokenAbsentState(); 
	
	// An HttpClient connection manager for making REST calls
	private ClientConnectionManager connectionManager;

	// Values derived from the values of various filter configuration parameters:
	private String idpRestEndpoint;
	private String idpLoginUrl;
	private String idpCookieName;
	private List<NameValuePair>  userAttributes = new ArrayList<NameValuePair>();

	@Override
	public void init(FilterConfig config) throws ServletException {
		// Initialize all of the instance variables from the filter configuration parameters.
		this.idpRestEndpoint = getInitParameter(config, IDP_REST_ENDPOINT_PARM);
	    this.idpCookieName = getInitParameter(config, IDP_COOKIE_NAME_PARM);		
	    this.idpLoginUrl = getInitParameter(config, IDP_LOGIN_URL_PARM);	
	    if (this.idpLoginUrl.indexOf('?') != -1) {  // The Login URL already contains a query string, so append a new 'goto' param
	    	this.idpLoginUrl += "&goto=";
	    } else {  // The Login URL does not contain a query string, so create one to append the 'goto' param
	    	this.idpLoginUrl += "?goto=";
	    }
	    String userAttributes = getInitParameter(config, USER_ATTRIBUTES_PARM);	
	    List<String> userAttributeList = StringUtils.split(userAttributes, ",");
	    for (String attribute : userAttributeList) {
	    	this.userAttributes.add(new BasicNameValuePair("attributenames", attribute));
	    }
	
	    // Create the HttpClient connection manager, overriding the SSL host name verification logic to allow for self-signed certificates. 
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("TLS");
	        sslContext.init(null, null, null);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	
		SSLSocketFactory sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme httpsScheme = new Scheme("https", 443, sf);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(httpsScheme);
		this.connectionManager = new BasicClientConnectionManager(schemeRegistry);		
	    
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		// Do not apply this filter if the LogoutServlet is being requested!
		if ("/logout".equals(req.getServletPath())) {
			chain.doFilter(req, resp);
			return;
		}
		
		// Read the ssoToken from the OpenAM cookie
		Cookie idpCookie = ServletUtils.getCookie(req, this.idpCookieName);
		String ssoToken = null;
		if (idpCookie != null) {
			ssoToken = idpCookie.getValue();
		}
		
		// Use the ssoToken retrieved from the cookie to figure out which state we are in.
		// Each state object encapsulates its own processing logic
		SessionState sessionState = getSessionState(req, ssoToken);
		
		if (!sessionState.process(req, resp, ssoToken)) {
			// The process() method returned false, which means that we don't want to forward
			// the request on to the rest of the filter chain. 
			return;
		}
		// The process() method returned true, which means that we should go ahead and forward
		// the request on to the rest of the filter chain. 
		chain.doFilter(req, resp);
		
	}

	@Override
	public void destroy() {
		// No special destruction logic needed
	}

	private SessionState getSessionState(HttpServletRequest req,
			String cookieValue) {
		if (cookieValue == null) { // The OpenAM cookie was not found
			return SSO_COOKIE_ABSENT;
		}
		SessionState sessionState = SSO_TOKEN_ABSENT;  // An 'ssoToken' attribute is not present in the session object 
		HttpSession session = req.getSession();
		if (session != null) {
			String ssoToken = (String) session.getAttribute(SSOParams.SSO_TOKEN);
			if (ssoToken != null) {
				if (ssoToken.equals(cookieValue)) {
					sessionState = SSO_TOKEN_VALID;  // An 'ssoToken' attribute is present in the session, and it matches the value of the OpenAM cookie.
				} else {
					sessionState = SSO_TOKEN_INVALID; // An 'ssoToken' attribute is present in the session, but it doesn't match the value of the OpenAM cookie.
				}
			}
		}
		return sessionState;
	}

	private void invalidateSession(HttpServletRequest req) {
		HttpSession session = req.getSession();
		if (session != null) {
			session.invalidate();
		}
	}

	private void doLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// If the user has an active session, we need to invalidate it!
		invalidateSession(req);
		String loginUrl = null;
		// We want to append the URL of the currently requested resource to the loginUrl as the value of the 'goto' param
		// so that OpenAM will redirect back to that resource after a successful login.
		try {
			loginUrl = this.idpLoginUrl +  URLEncoder.encode(ServletUtils.getURL(req), "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			// It should never be the case that the UTF-8 encoding is unsupported!
			throw new ServletException(e);
		}
		resp.sendRedirect(loginUrl);
	}

	private boolean retrieveUserAttributes(HttpServletRequest req,
			HttpServletResponse resp, String ssoToken) throws ClientProtocolException, IOException, JSONException {
	    // First, confirm that the ssoToken is valid
		JSONObject isTokenValidResponse = invokeRestApi(this.idpRestEndpoint + "/identity/isTokenValid", new BasicNameValuePair("tokenid", ssoToken));	
		boolean tokenValid = isTokenValidResponse.getBoolean("boolean");
		if (!tokenValid) {
			// The ssoToken is not valid, so return false
			return false;
		}
	    // The ssoToken is valid, so use it to retrieve the user attributes 
		List<NameValuePair> parms = new ArrayList<NameValuePair>();
		parms.add(new BasicNameValuePair("subjectid", ssoToken));
		parms.addAll(this.userAttributes);
		JSONObject attributesResponse = invokeRestApi(this.idpRestEndpoint + "/identity/attributes", 
				parms);
		if (attributesResponse.has("exception.name")) {
			new RuntimeException(attributesResponse.getString("exception.name")).printStackTrace();
			return false;
		}
		
		// We got the user attributes as a JSONObject, let's convert it to a UserContext object 
		UserContext userContext = UserContext.builder(attributesResponse).build();
		
		// Attach the ssoToken and the userContext object to the session
		HttpSession session = req.getSession(true);
		session.setAttribute(SSOParams.USER_CONTEXT, userContext);
		session.setAttribute(SSOParams.SSO_TOKEN, ssoToken);
		return true;
	}

	private void loadUserContext(HttpServletRequest req) {
		// Retrieve the UserContext from the session and attach it to the request as an attribute
		// This method is only ever called after we have confirmed that a valid ssoToken is attached to the session.  
		// Hence, the assertions
		HttpSession session = req.getSession();  
		assert(session != null) : "Expected the HttpSession to exist.";
		
		UserContext userContext = (UserContext) session.getAttribute(SSOParams.USER_CONTEXT); 
		assert(userContext != null) : "Expected a UserContext object to be stored within the HttpSession";
		
		req.setAttribute(SSOParams.USER_CONTEXT, userContext);
	}

	private JSONObject invokeRestApi(String url, NameValuePair ... parms)
			throws UnsupportedEncodingException, IOException,
			ClientProtocolException, JSONException {
		return invokeRestApi(url, Arrays.asList(parms));
	}

	private JSONObject invokeRestApi(String url, List<NameValuePair> parms)
			throws UnsupportedEncodingException, IOException,
			ClientProtocolException, JSONException {
		// Invoke a REST API exposed by OpenAM and return the result as a JSONObject
		HttpClient httpClient = new DefaultHttpClient(this.connectionManager);
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> nvp = new ArrayList<NameValuePair>();
		for (NameValuePair parm : parms) {
		    nvp.add(parm);
		}
		httpPost.setEntity(new UrlEncodedFormEntity(nvp));
		HttpResponse tokenInfoResponse = httpClient.execute(httpPost);
		HttpEntity entity = tokenInfoResponse.getEntity();
		JSONObject responseObject = new JSONObject();
		if (entity != null) {
			String responseString = EntityUtils.toString(entity).trim();
			responseObject = ResponseParser.parse(responseString);
		}
		return responseObject;
	}

	private String getInitParameter(FilterConfig config, String paramName) throws ServletException {
		// Utility method used to retrieve a configuration parameter, and validate that it is present
		String result = config.getInitParameter(paramName);
		if (result == null) {
			throw new ServletException(String.format("Expected an initialization parameter named '%s'.", paramName));
		}
		return result;
	}

	
	// Leveraging an implementation of the State Design Pattern in order to eliminate a lot of conditional logic!
	private static interface SessionState {
		public boolean process(HttpServletRequest req, HttpServletResponse resp, String ssoToken) throws ServletException, IOException;
	}
	
	private class SsoTokenInvalidState implements SessionState {

		@Override
		public boolean process(HttpServletRequest req, HttpServletResponse resp,
				String ssoToken) throws ServletException, IOException {
			// The current session contains an invalid ssoToken, so we need to invalidate the whole session
			invalidateSession(req);
			try {
				// 
				if (!retrieveUserAttributes(req, resp, ssoToken)) {
					// The SSO Token was not recognized by OpenAM, so we need to login
					doLogin(req, resp);
					return false;
				}
			} catch (Exception ex) {
				throw new ServletException(ex);
			}
			// Read the userContext object from the session and attach it to the request
			loadUserContext(req);
			return true;
		}
	}

	private class SsoTokenAbsentState implements SessionState {

		@Override
		public boolean process(HttpServletRequest req, HttpServletResponse resp,
				String ssoToken) throws ServletException, IOException {
			try {
				if (!retrieveUserAttributes(req, resp, ssoToken)) {
					// The SSO Token not recognized by OpenAM, so we need to login
					doLogin(req, resp);
					return false;
				}
			} catch (Exception ex) {
				throw new ServletException(ex);
			}
			// Read the userContext object from the session and attach it to the request
			loadUserContext(req);
			return true;
		}
	}

	private class SsoTokenValidState implements SessionState {

		@Override
		public boolean process(HttpServletRequest req, HttpServletResponse resp,
				String ssoToken) throws ServletException, IOException {
			// Read the userContext object from the session and attach it to the request
			loadUserContext(req);
			return true;
		}
	}

	private class SsoCookieAbsentState implements SessionState {

		@Override
		public boolean process(HttpServletRequest req, HttpServletResponse resp,
				String ssoToken) throws ServletException, IOException {
			// The OpenAM cookie is not present, so we need to login
			doLogin(req, resp);
			return false;
		}
	}
}
