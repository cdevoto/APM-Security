
package com.compuware.identity.authentication.repo.jira.webservice.login;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.compuware.identity.authentication.repo.jira.webservice.login package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _LoginRestResponse_QNAME = new QName("http://login.webservice.dtmanager.dynatrace.com/", "loginRestResponse");
    private final static QName _LoginCredentials_QNAME = new QName("http://login.webservice.dtmanager.dynatrace.com/", "loginCredentials");
    private final static QName _LoginResponse_QNAME = new QName("http://login.webservice.dtmanager.dynatrace.com/", "loginResponse");
    private final static QName _Logout_QNAME = new QName("http://login.webservice.dtmanager.dynatrace.com/", "logout");
    private final static QName _Exception_QNAME = new QName("http://login.webservice.dtmanager.dynatrace.com/", "Exception");
    private final static QName _SessionToken_QNAME = new QName("http://login.webservice.dtmanager.dynatrace.com/", "sessionToken");
    private final static QName _LogoutResponse_QNAME = new QName("http://login.webservice.dtmanager.dynatrace.com/", "logoutResponse");
    private final static QName _LogoutRest_QNAME = new QName("http://login.webservice.dtmanager.dynatrace.com/", "logoutRest");
    private final static QName _LoginRest_QNAME = new QName("http://login.webservice.dtmanager.dynatrace.com/", "loginRest");
    private final static QName _Login_QNAME = new QName("http://login.webservice.dtmanager.dynatrace.com/", "login");
    private final static QName _LogoutRestResponse_QNAME = new QName("http://login.webservice.dtmanager.dynatrace.com/", "logoutRestResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.compuware.identity.authentication.repo.jira.webservice.login
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LogoutResponse }
     * 
     */
    public LogoutResponse createLogoutResponse() {
        return new LogoutResponse();
    }

    /**
     * Create an instance of {@link LogoutRest }
     * 
     */
    public LogoutRest createLogoutRest() {
        return new LogoutRest();
    }

    /**
     * Create an instance of {@link LoginResponse }
     * 
     */
    public LoginResponse createLoginResponse() {
        return new LoginResponse();
    }

    /**
     * Create an instance of {@link LoginRest }
     * 
     */
    public LoginRest createLoginRest() {
        return new LoginRest();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link Login }
     * 
     */
    public Login createLogin() {
        return new Login();
    }

    /**
     * Create an instance of {@link SessionToken }
     * 
     */
    public SessionToken createSessionToken() {
        return new SessionToken();
    }

    /**
     * Create an instance of {@link LogoutRestResponse }
     * 
     */
    public LogoutRestResponse createLogoutRestResponse() {
        return new LogoutRestResponse();
    }

    /**
     * Create an instance of {@link Logout }
     * 
     */
    public Logout createLogout() {
        return new Logout();
    }

    /**
     * Create an instance of {@link LoginCredentials }
     * 
     */
    public LoginCredentials createLoginCredentials() {
        return new LoginCredentials();
    }

    /**
     * Create an instance of {@link LoginRestResponse }
     * 
     */
    public LoginRestResponse createLoginRestResponse() {
        return new LoginRestResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginRestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.webservice.dtmanager.dynatrace.com/", name = "loginRestResponse")
    public JAXBElement<LoginRestResponse> createLoginRestResponse(LoginRestResponse value) {
        return new JAXBElement<LoginRestResponse>(_LoginRestResponse_QNAME, LoginRestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginCredentials }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.webservice.dtmanager.dynatrace.com/", name = "loginCredentials")
    public JAXBElement<LoginCredentials> createLoginCredentials(LoginCredentials value) {
        return new JAXBElement<LoginCredentials>(_LoginCredentials_QNAME, LoginCredentials.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.webservice.dtmanager.dynatrace.com/", name = "loginResponse")
    public JAXBElement<LoginResponse> createLoginResponse(LoginResponse value) {
        return new JAXBElement<LoginResponse>(_LoginResponse_QNAME, LoginResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Logout }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.webservice.dtmanager.dynatrace.com/", name = "logout")
    public JAXBElement<Logout> createLogout(Logout value) {
        return new JAXBElement<Logout>(_Logout_QNAME, Logout.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.webservice.dtmanager.dynatrace.com/", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SessionToken }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.webservice.dtmanager.dynatrace.com/", name = "sessionToken")
    public JAXBElement<SessionToken> createSessionToken(SessionToken value) {
        return new JAXBElement<SessionToken>(_SessionToken_QNAME, SessionToken.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogoutResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.webservice.dtmanager.dynatrace.com/", name = "logoutResponse")
    public JAXBElement<LogoutResponse> createLogoutResponse(LogoutResponse value) {
        return new JAXBElement<LogoutResponse>(_LogoutResponse_QNAME, LogoutResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogoutRest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.webservice.dtmanager.dynatrace.com/", name = "logoutRest")
    public JAXBElement<LogoutRest> createLogoutRest(LogoutRest value) {
        return new JAXBElement<LogoutRest>(_LogoutRest_QNAME, LogoutRest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LoginRest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.webservice.dtmanager.dynatrace.com/", name = "loginRest")
    public JAXBElement<LoginRest> createLoginRest(LoginRest value) {
        return new JAXBElement<LoginRest>(_LoginRest_QNAME, LoginRest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Login }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.webservice.dtmanager.dynatrace.com/", name = "login")
    public JAXBElement<Login> createLogin(Login value) {
        return new JAXBElement<Login>(_Login_QNAME, Login.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogoutRestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://login.webservice.dtmanager.dynatrace.com/", name = "logoutRestResponse")
    public JAXBElement<LogoutRestResponse> createLogoutRestResponse(LogoutRestResponse value) {
        return new JAXBElement<LogoutRestResponse>(_LogoutRestResponse_QNAME, LogoutRestResponse.class, null, value);
    }

}
