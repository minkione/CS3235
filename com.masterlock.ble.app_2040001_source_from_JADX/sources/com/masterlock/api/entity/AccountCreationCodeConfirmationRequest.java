package com.masterlock.api.entity;

public class AccountCreationCodeConfirmationRequest {
    private String validationType;
    private String verificationCode;
    private String verificationId;

    public enum ValidationType {
        EMAIL("email"),
        SMS("mobilephonenumber");
        
        String value;

        private ValidationType(String str) {
            this.value = str;
        }
    }

    public void setVerificationId(String str) {
        this.verificationId = str;
    }

    public void setVerificationCode(String str) {
        this.verificationCode = str;
    }

    public void setValidationType(ValidationType validationType2) {
        this.validationType = validationType2.value;
    }

    public String getVerificationId() {
        return this.verificationId;
    }

    public String getVerificationCode() {
        return this.verificationCode;
    }

    public String getValidationType() {
        return this.validationType;
    }
}
