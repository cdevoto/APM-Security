package com.compuware.apmng.sso;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.compuware.apmng.util.ServletUtils;

/**
 * This servlet is invoked whenever the "Logout" link is clicked.
 * It invalidates the user's session, and then redirects to OpenAM
 * in order to initiate the SAML Single Logout protocol.
 * 
 * @author Carlos Devoto
 *
 */
public class LogoutServlet extends HttpServlet {

	private static final String IDP_BASE_URL_PARM = "idpBaseUrl";

	private static final long serialVersionUID = 1L;
	
	String logoutUrl;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		// Here we build up as much of the logoutUrl as we can, using a context parameter
		super.init(config);
		ServletContext context = config.getServletContext();
		String baseUrl = context.getInitParameter(IDP_BASE_URL_PARM);
		if (baseUrl == null) {
			throw new ServletException(String.format("Expected an initialization parameter named '%s'.", IDP_BASE_URL_PARM));
		}
		this.logoutUrl = baseUrl + "/saml2/jsp/idpSingleLogoutInit.jsp?binding=urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST&logoutAll=true&RelayState=";
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// Invalidate the current session
		HttpSession session = req.getSession();
		if (session != null) {
			session.invalidate();
		}
		// Redirect the OpenAM in order to trigger single logout 
		// (passing the URL of the welcome page for this app as the 'RelayState' will cause
		// OpenAM to redirect back to this app after the logout process is complete).
		resp.sendRedirect(logoutUrl + URLEncoder.encode(ServletUtils.getWelcomeURL(req), "UTF-8"));
	}
	
	

}
