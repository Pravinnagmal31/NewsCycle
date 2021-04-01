package com.pravin.newscyclejava.Model;



public class SpinnerModel {
    private String string;
    private String domain;

    public SpinnerModel(String string, String domain) {
        this.string = string;
        this.domain = domain;
    }

    public String getSpinnerString()
    {
        return string;
    }

    public String getDomain() {
        return domain;
    }
}