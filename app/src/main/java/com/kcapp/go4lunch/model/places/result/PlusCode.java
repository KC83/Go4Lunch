package com.kcapp.go4lunch.model.places.result;

import com.google.gson.annotations.SerializedName;

public class PlusCode {
    @SerializedName("compound_code")
    private String compoundCode;

    @SerializedName("global_code")
    private String globalCode;

    /**
     * GETTERS
     */
    public String getCompoundCode() {
        return compoundCode;
    }

    public String getGlobalCode() {
        return globalCode;
    }

    /**
     * SETTERS
     */
    public void setCompoundCode(String compoundCode) {
        this.compoundCode = compoundCode;
    }

    public void setGlobalCode(String globalCode) {
        this.globalCode = globalCode;
    }
}
