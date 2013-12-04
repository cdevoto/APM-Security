NOTES:

1. When invoking idpSingleLogoutInit.jsp, the RelayState for this app was not passing the validation logic. In order to resolve this, I made modifications to the following two JSP files:
	a. openam-11.0.0/WebContent/saml2/jsp/idpSingleLogoutInit.jsp
	b. openam-11.0.0/WebContent/saml2/jsp/idpSingleLogoutRedirect.jsp