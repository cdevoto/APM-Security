
package com.compuware.identity.authentication.repo.jira.webservice.auth;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.compuware.identity.authentication.repo.jira.webservice.auth package. 
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

    private final static QName _GetUserDetails_QNAME = new QName("http://auth.webservice.dtmanager.dynatrace.com/", "getUserDetails");
    private final static QName _GetUserDetailsResponse_QNAME = new QName("http://auth.webservice.dtmanager.dynatrace.com/", "getUserDetailsResponse");
    private final static QName _UserDetails_QNAME = new QName("http://auth.webservice.dtmanager.dynatrace.com/", "userDetails");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.compuware.identity.authentication.repo.jira.webservice.auth
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetUserDetailsResponse }
     * 
     */
    public GetUserDetailsResponse createGetUserDetailsResponse() {
        return new GetUserDetailsResponse();
    }

    /**
     * Create an instance of {@link GetUserDetails }
     * 
     */
    public GetUserDetails createGetUserDetails() {
        return new GetUserDetails();
    }

    /**
     * Create an instance of {@link UserDetails }
     * 
     */
    public UserDetails createUserDetails() {
        return new UserDetails();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://auth.webservice.dtmanager.dynatrace.com/", name = "getUserDetails")
    public JAXBElement<GetUserDetails> createGetUserDetails(GetUserDetails value) {
        return new JAXBElement<GetUserDetails>(_GetUserDetails_QNAME, GetUserDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetUserDetailsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://auth.webservice.dtmanager.dynatrace.com/", name = "getUserDetailsResponse")
    public JAXBElement<GetUserDetailsResponse> createGetUserDetailsResponse(GetUserDetailsResponse value) {
        return new JAXBElement<GetUserDetailsResponse>(_GetUserDetailsResponse_QNAME, GetUserDetailsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UserDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://auth.webservice.dtmanager.dynatrace.com/", name = "userDetails")
    public JAXBElement<UserDetails> createUserDetails(UserDetails value) {
        return new JAXBElement<UserDetails>(_UserDetails_QNAME, UserDetails.class, null, value);
    }

}
