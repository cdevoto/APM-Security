I. DEPLOY THE API TO OPENAM:
------------------------------------------

1. Add compuware-apis.jar to WEB-INF/lib directory.
2. Modify web.xml to include a <servlet> and <servlet-mapping> tag for the RemoteSpServlet class.

<servlet>
   <servlet-name>RemoteSpServlet</servlet-name>
   <servlet-class>com.compuware.identity.api.RemoteSpServlet</servlet-class>
</servlet>


<servlet-mapping>
    <servlet-name>RemoteSpServlet</servlet-name>
    <url-pattern>/compuware/api/remoteServiceProviders</url-pattern>
</servlet-mapping>


II. CONFIGURE MUTUAL SSL FOR THE CLIENT:
------------------------------------------
1. Export the certificate which is currently being used by the OpenAM server

    EXAMPLE: keytool -export -alias tomcat -file /Users/bfhcpd0/openam-staging.crt -keystore .keystore
    
2. Import the server certificate into the trusted keystore currently being used by the REST client

    EXAMPLE: keytool -import -trustcacerts -file /Users/bfhcpd0/openam-staging.crt -alias openam-staging -keystore truststore.jks -storepass sso2013! 
    
III. CONFIGURE MUTUAL SSL FOR THE OPENAM SERVER:
------------------------------------------------
1. Create a truststore which includes the client's certificate ssoclient.crt.

   EXAMPLE: keytool -import -trustcacerts -file /Users/bfhcpd0/projects/OTracker-Prototypes/TestCompuwareApis/keystores/ssoclient.crt -alias compuware-client -keystore .truststore -storepass claudia2d
    
2. Configure server.xml to use the truststore created above.

   EXAMPLE:
   
    <Connector 
         SSLEnabled="true"
         clientAuth="false"
         keystoreFile="/Users/bfhcpd0/.keystore" 
         keystorePass="sso2013!" 
         maxThreads="150" 
         port="7443" 
         protocol="HTTP/1.1" 
         scheme="https" 
         secure="true" 
         sslProtocol="TLS" 
         truststoreFile="/Users/bfhcpd0/.truststore" 
         truststorePass="sso2013!"
         maxSavePostSize="-1"/>
         
   Note that in order for this connector to work, you may also need to comment out the following line in server.xml
   
   <Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" />         
   
3. Update the tomcat-users.xml file to include the following role and user definitions:

  <role rolename="secure-connection"/>
  <user password="null" roles="secure-connection" username="CN=SSO Client, OU=APM Business Unit, O=Compuware Corporation, L=Detroit, ST=MI, C=US"/>


4. Configure the openam application to perform client certificate authentication when the Compuware REST APIs are called by adding the following to web.xml:

	<security-constraint>
		<web-resource-collection>
			<web-resource-name>Secure Compuware REST APIs</web-resource-name>
			<url-pattern>/compuware/api/*</url-pattern>
		</web-resource-collection>
		<auth-constraint>
			<role-name>secure-connection</role-name>
		</auth-constraint>
	</security-constraint>
	
	<login-config>
		<auth-method>CLIENT-CERT</auth-method>
		<realm-name>Secure Compuware REST APIs</realm-name>
	</login-config>
	<security-role>
		<role-name>secure-connection</role-name>
	</security-role>
	
IV. TO DEBUG SSL HANDSHAKE ON CLIENT OR SERVER

1. Add the following command-line switch when you launch the JVM

    -Djavax.net.debug=ssl,handshake

	
      