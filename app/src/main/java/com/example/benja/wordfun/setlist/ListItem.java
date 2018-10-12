package com.example.benja.wordfun.setlist;

/**
 * Created by benja on 2018/9/10.
 */

public class ListItem {
    private String title;
    private String subTitle;
    private int termCount;
    private long createTime;
    private String author;
    private String folder;

    public ListItem() {
    }

    public ListItem(String title, int termCount, int createTime) {
        this.title = title;
        this.termCount = termCount;
        this.createTime = createTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTermCount() {
        return termCount;
    }

    public void setTermCount(int termCount) {
        this.termCount = termCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
