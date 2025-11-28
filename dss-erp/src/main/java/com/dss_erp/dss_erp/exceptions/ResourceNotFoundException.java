package com.dss_erp.dss_erp.exceptions;

public class ResourceNotFoundException  extends  RuntimeException{
    String resourceName;
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFoundException(String message, String fieldName, String field, String resourceName) {
        super(String.format("%s not found with %s: %s", resourceName,field,fieldName));
        this.fieldName = fieldName;
        this.field = field;
        this.resourceName = resourceName;
    }

    public ResourceNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s: %d", resourceName,field,fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }

    public ResourceNotFoundException() {
    }
}
