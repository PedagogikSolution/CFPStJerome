package com.pedagogiksolution.CFPStJerome.beans;

import java.io.Serializable;

public class NotificationsBean implements Serializable {

    private String categorie;
    private String segment;
    private String documentId;
    private boolean section;

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public boolean getSection() {
        return section;
    }

    public void setSection(boolean section) {
        this.section = section;
    }

}
