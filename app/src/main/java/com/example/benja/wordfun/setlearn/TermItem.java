package com.example.benja.wordfun.setlearn;

/**
 * Created by benja on 2018/9/14.
 */

public class TermItem {
    private String termText;
    private String defText;
    private boolean mWrited;
    private boolean mMatrixed;

    public TermItem() {
    }

    public TermItem(String termText, String defText, boolean mWrited, boolean mMatrixed) {
        this.termText = termText;
        this.defText = defText;
        this.mWrited = mWrited;
        this.mMatrixed = mMatrixed;
    }

    public String getTermText() {
        return termText;
    }

    public void setTermText(String termText) {
        this.termText = termText;
    }

    public String getDefText() {
        return defText;
    }

    public void setDefText(String defText) {
        this.defText = defText;
    }

    public boolean ismWrited() {
        return mWrited;
    }

    public void setmWrited(boolean mWrited) {
        this.mWrited = mWrited;
    }

    public boolean ismMatrixed() {
        return mMatrixed;
    }

    public void setmMatrixed(boolean mMatrixed) {
        this.mMatrixed = mMatrixed;
    }
}
