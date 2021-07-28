package io.github.sdwfqin.quickseed.ui.example.sortlist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class SortGroupBean implements Parcelable {

    private long id;
    private String title;
    private List<SortDetailBean> sortDetailBeans;

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

    public List<SortDetailBean> getSortDetailBeans() {
        return sortDetailBeans;
    }

    public void setSortDetailBeans(List<SortDetailBean> sortDetailBeans) {
        this.sortDetailBeans = sortDetailBeans;
    }

    @Override
    public String toString() {
        return "SortBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", sortDetailBeans=" + sortDetailBeans +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.title);
        dest.writeList(this.sortDetailBeans);
    }

    public SortGroupBean() {
    }

    protected SortGroupBean(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.sortDetailBeans = new ArrayList<SortDetailBean>();
        in.readList(this.sortDetailBeans, SortDetailBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<SortGroupBean> CREATOR = new Parcelable.Creator<SortGroupBean>() {
        @Override
        public SortGroupBean createFromParcel(Parcel source) {
            return new SortGroupBean(source);
        }

        @Override
        public SortGroupBean[] newArray(int size) {
            return new SortGroupBean[size];
        }
    };
}
