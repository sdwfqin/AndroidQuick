package com.sdwfqin.quickseed.ui.example.sortlist;

import java.io.Serializable;
import java.util.List;

public class SortBean implements Serializable {

    private long id;
    private String title;
    private List<SortGroupBean> sortGroupBeans;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SortGroupBean> getSortGroupBeans() {
        return sortGroupBeans;
    }

    public void setSortGroupBeans(List<SortGroupBean> sortGroupBeans) {
        this.sortGroupBeans = sortGroupBeans;
    }

    @Override
    public String toString() {
        return "SortBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", sortGroupBeans=" + sortGroupBeans +
                '}';
    }
}
