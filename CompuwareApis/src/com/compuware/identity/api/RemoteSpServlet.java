package com.compuware.identity.api;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.identity.saml2.meta.SAML2MetaManager;
import com.sun.identity.workflow.AddProviderToCOT;
import com.sun.identity.workflow.ImportSAML2MetaData;

public class RemoteSpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		generateResponse(resp, ApiResponse.SC_FORBIDDEN, Messages.getString("CompuwareApis.GET_NOT_SUPPORTED")); //$NON-NLS-1$
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		generateResponse(resp, ApiResponse.SC_FORBIDDEN, Messages.getString("CompuwareApis.PUT_NOT_SUPPORTED")); //$NON-NLS-1$
	}
	
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
        try {
        	validateDeleteParameters(req);
	        String realm = getString(req, ParameterKeys.P_REALM);
	        String entityId = getString(req, ParameterKeys.P_ENTITY_ID);
        	
            SAML2MetaManager metaManager = new SAML2MetaManager();
            metaManager.deleteEntityDescriptor(realm, entityId);
		    generateResponse(resp, ApiResponse.SC_OK, String.format(Messages.getString("CompuwareApis.DELETION_SUCCESSFUL"), entityId)); //$NON-NLS-1$
		} catch (ApiException ex) {
			log(ex.getMessage(), ex);
			generateResponse(resp, ex.getStatus(), ex.getMessage());
		} catch (Exception ex) {
			log(ex.getMessage(), ex);
			generateResponse(resp, ApiResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		}

    }	
    
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("application/json"); //$NON-NLS-1$
		resp.setCharacterEncoding("UTF-8"); //$NON-NLS-1$
		try {
			if (!req.isSecure()) {
				throw new ApiException(ApiResponse.SC_FORBIDDEN, Messages.getString("CompuwareApis.HTTP_NOT_SUPPORTED")); //$NON-NLS-1$
			}
			validatePostParameters(req);
	        String realm = getString(req, ParameterKeys.P_REALM);
	        String metadata = getString(req, ParameterKeys.P_META_DATA);
	        String cot = getString(req, ParameterKeys.P_COT);
	        
	        String[] results = ImportSAML2MetaData.importData(
	                realm, metadata, null);
	        String entityId = results[1];
	        AddProviderToCOT.addToCOT(realm, cot, entityId);
	        
		    generateResponse(resp, ApiResponse.SC_CREATED, String.format(Messages.getString("CompuwareApis.CREATION_SUCCESSFUL"), entityId)); //$NON-NLS-1$
		} catch (ApiException ex) {
			log(ex.getMessage(), ex);
			generateResponse(resp, ex.getStatus(), ex.getMessage());
		} catch (Exception ex) {
			log(ex.getMessage(), ex);
			generateResponse(resp, ApiResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
		}
	}
	
    private void generateResponse(HttpServletResponse resp,
			int status, String message) throws IOException {
    	PrintWriter out = resp.getWriter();
    	JSONObject json = new JSONObject();
    	try {
    	    json.put("status", status); //$NON-NLS-1$
    	    json.put("message", message); //$NON-NLS-1$
    	    String result = json.toString();
    	    out.print(result);
    	} catch (JSONException ex) {
    		// This really should not happen!
    		throw new AssertionError(ex);
    	}
    	
	}

	private void validatePostParameters(HttpServletRequest req)
            throws ApiException {
        String metadata = getString(req, ParameterKeys.P_META_DATA);
        if ((metadata == null) || (metadata.length() == 0)) {
            throw new ApiException(ApiResponse.SC_BAD_REQUEST, String.format(Messages.getString("CompuwareApis.MISSING_PARAMETER"), ParameterKeys.P_META_DATA)); //$NON-NLS-1$
        }
        String realm = getString(req, ParameterKeys.P_REALM);
        if ((realm == null) || (realm.length() == 0)) {
            throw new ApiException(ApiResponse.SC_BAD_REQUEST, String.format(Messages.getString("CompuwareApis.MISSING_PARAMETER"), ParameterKeys.P_REALM)); //$NON-NLS-1$
        }
        String cot = getString(req, ParameterKeys.P_COT);
        if ((cot == null) || (cot.length() == 0)) {
            throw new ApiException(ApiResponse.SC_BAD_REQUEST, String.format(Messages.getString("CompuwareApis.MISSING_PARAMETER"), ParameterKeys.P_COT)); //$NON-NLS-1$
        }
    }

	private void validateDeleteParameters(HttpServletRequest req)
            throws ApiException {
        String entityId = getString(req, ParameterKeys.P_ENTITY_ID);
        if ((entityId == null) || (entityId.length() == 0)) {
            throw new ApiException(ApiResponse.SC_BAD_REQUEST, String.format(Messages.getString("CompuwareApis.MISSING_PARAMETER"), ParameterKeys.P_ENTITY_ID)); //$NON-NLS-1$
        }
        String realm = getString(req, ParameterKeys.P_REALM);
        if ((realm == null) || (realm.length() == 0)) {
            throw new ApiException(ApiResponse.SC_BAD_REQUEST, String.format(Messages.getString("CompuwareApis.MISSING_PARAMETER"), ParameterKeys.P_REALM)); //$NON-NLS-1$
        }
    }
	
	
	private String getString(HttpServletRequest req, String key) {
        String result = req.getParameter(key);
        if (result != null) {
        	result = result.trim();
        }
        return result;
    }

}
