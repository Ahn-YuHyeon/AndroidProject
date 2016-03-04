package com.project.john.bef.component;

public class DuplicateException extends Exception {
    DuplicateException( ) {
        super( );
    }

    public DuplicateException(String msg) {
        super(msg);
    }
}
