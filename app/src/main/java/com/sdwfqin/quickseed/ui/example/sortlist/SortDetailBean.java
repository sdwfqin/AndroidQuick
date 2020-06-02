package com.sdwfqin.quickseed.ui.example.sortlist;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class SortDetailBean implements Parcelable, MultiItemEntity {

    private long id;
    private String title;
    private String imgUrl;
    private boolean isGroupTitle;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isGroupTitle() {
        return isGroupTitle;
    }

    public void setGroupTitle(boolean groupTitle) {
        isGroupTitle = groupTitle;
    }

    @Override
    public String toString() {
        return "SortDetailBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
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
        dest.writeString(this.imgUrl);
        dest.writeByte(this.isGroupTitle ? (byte) 1 : (byte) 0);
    }

    public SortDetailBean() {
    }

    protected SortDetailBean(Parcel in) {
        this.id = in.readLong();
        this.title = in.readString();
        this.imgUrl = in.readString();
        this.isGroupTitle = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SortDetailBean> CREATOR = new Parcelable.Creator<SortDetailBean>() {
        @Override
        public SortDetailBean createFromParcel(Parcel source) {
            return new SortDetailBean(source);
        }

        @Override
        public SortDetailBean[] newArray(int size) {
            return new SortDetailBean[size];
        }
    };

    @Override
    public int getItemType() {
        return isGroupTitle ? SortDetailAdapter.GROUP : SortDetailAdapter.DETAIL;
    }
}
