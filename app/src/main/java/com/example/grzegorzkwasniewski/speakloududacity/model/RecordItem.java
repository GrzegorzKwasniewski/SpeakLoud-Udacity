package com.example.grzegorzkwasniewski.speakloududacity.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by grzegorz.kwasniewski on 2018-07-19.
 */

public class RecordItem implements Parcelable {

    //region Class Fields
    private String mFileName;
    private String mFilePath;
    private int mDatabaseID;
    private int mRecordLength;
    private long mRecordTime;
    //endregion

    //region Class Constructors
    public RecordItem() {}

    public RecordItem(Parcel parcel) {
        mFileName = parcel.readString();
        mFilePath = parcel.readString();
        mDatabaseID = parcel.readInt();
        mRecordLength = parcel.readInt();
        mRecordTime = parcel.readLong();
    }
    //endregion

    //region Public Methods
    public String getFileName() {
        return mFileName;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public int getmDatabaseID() {
        return mDatabaseID;
    }

    public int getRecordLength() {
        return mRecordLength;
    }

    public long getRecordTime() {
        return mRecordTime;
    }

    public void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.mFilePath = filePath;
    }

    public void setDatabaseID(int databaseID) {
        this.mDatabaseID = databaseID;
    }

    public void setRecordLength(int recordLength) {
        this.mRecordLength = recordLength;
    }

    public void setRecordTime(long recordTime) {
        this.mRecordTime = recordTime;
    }

    public static final Parcelable.Creator<RecordItem> CREATOR = new Parcelable.Creator<RecordItem>() {
        public RecordItem createFromParcel(Parcel in) {
            return new RecordItem(in);
        }
        public RecordItem[] newArray(int size) {
            return new RecordItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFileName);
        dest.writeString(mFilePath);
        dest.writeInt(mDatabaseID);
        dest.writeInt(mRecordLength);
        dest.writeLong(mRecordTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordItem model = (RecordItem) o;

        if (mDatabaseID != model.getmDatabaseID()) return false;
        return mFileName != null ? mFileName.equals(model.getFileName()) : model.getFileName() == null;

    }

    @Override
    public int hashCode() {
        int result = (int) (mDatabaseID ^ (mDatabaseID >>> 32));
        result = 31 * result + (mFileName != null ? mFileName.hashCode() : 0);
        return result;
    }
    //endregion
}
