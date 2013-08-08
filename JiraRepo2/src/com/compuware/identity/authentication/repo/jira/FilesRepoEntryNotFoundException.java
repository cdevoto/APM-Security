package com.compuware.identity.authentication.repo.jira;

import com.sun.identity.idm.IdRepoException;

public class FilesRepoEntryNotFoundException extends IdRepoException {

    public FilesRepoEntryNotFoundException() {
        super();
    }

    public FilesRepoEntryNotFoundException(String rbName, String errorCode, 
            Object[] args) {
        super(rbName, errorCode, args);
    }

    public FilesRepoEntryNotFoundException(String msg, String errorCode) {
        super(msg, errorCode);
    }

    public FilesRepoEntryNotFoundException(String msg) {
        super(msg);
    }
    
}

