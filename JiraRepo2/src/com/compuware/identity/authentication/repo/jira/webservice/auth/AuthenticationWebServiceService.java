
package com.compuware.identity.authentication.repo.jira.webservice.auth;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import com.compuware.identity.authentication.repo.jira.webservice.login.LoginWebserviceService;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebServiceClient(name = "AuthenticationWebServiceService", targetNamespace = "http://auth.webservice.dtmanager.dynatrace.com/", wsdlLocation = "classpath:authwebservice.wsdl")
public class AuthenticationWebServiceService
    extends Service
{

    private final static URL AUTHENTICATIONWEBSERVICESERVICE_WSDL_LOCATION;
    private final static Logger logger = Logger.getLogger(com.compuware.identity.authentication.repo.jira.webservice.auth.AuthenticationWebServiceService.class.getName());

    static {
        URL url = LoginWebserviceService.class.getClassLoader().getResource("authwebservice.wsdl");
        if (url == null) {
        	logger.log(Level.WARNING, "Cannot initialize the default wsdl from {0}", "classpath:authwebservice.wsdl");
        }

        AUTHENTICATIONWEBSERVICESERVICE_WSDL_LOCATION = url;
    }

    public AuthenticationWebServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AuthenticationWebServiceService() {
        super(AUTHENTICATIONWEBSERVICESERVICE_WSDL_LOCATION, new QName("http://auth.webservice.dtmanager.dynatrace.com/", "AuthenticationWebServiceService"));
    }

    /**
     * 
     * @return
     *     returns AuthenticationWebService
     */
    @WebEndpoint(name = "AuthenticationWebServicePort")
    public AuthenticationWebService getAuthenticationWebServicePort() {
        return super.getPort(new QName("http://auth.webservice.dtmanager.dynatrace.com/", "AuthenticationWebServicePort"), AuthenticationWebService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AuthenticationWebService
     */
    @WebEndpoint(name = "AuthenticationWebServicePort")
    public AuthenticationWebService getAuthenticationWebServicePort(WebServiceFeature... features) {
        return super.getPort(new QName("http://auth.webservice.dtmanager.dynatrace.com/", "AuthenticationWebServicePort"), AuthenticationWebService.class, features);
    }

}