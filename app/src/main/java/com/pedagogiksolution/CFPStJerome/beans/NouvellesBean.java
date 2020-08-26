package com.pedagogiksolution.CFPStJerome.beans;

import java.io.Serializable;

public class NouvellesBean implements Serializable {

    private String titre;
    private String body;
    private String url;
    private String date;

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }




}
