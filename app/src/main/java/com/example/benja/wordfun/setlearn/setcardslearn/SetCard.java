package com.example.benja.wordfun.setlearn.setcardslearn;

/**
 * Created by benja on 2018/9/28.
 */

public class SetCard {
    private String Term;
    private String Definition;
    private String author;
    private boolean mflashed;
    private long vid;

    public SetCard() {
    }

    public SetCard(String term, String definition) {
        Term = term;
        Definition = definition;
    }

    public String getTerm() {
        return Term;
    }

    public void setTerm(String term) {
        Term = term;
    }

    public String getDefinition() {
        return Definition;
    }

    public void setDefinition(String definition) {
        Definition = definition;
    }

    public boolean ismflashed() {
        return mflashed;
    }

    public void setmflashed(boolean mflashed) {
        this.mflashed = mflashed;
    }

    public long getVid() {
        return vid;
    }

    public void setVid(long vid) {
        this.vid = vid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
