package com.example.qualitysleep.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "recordings_table")

public class Recording implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "length")
    private long mLength;

    @ColumnInfo(name = "dateTime")
    private String mDate;

    public Recording(String mDate, long mLength) {
        this.mLength = mLength;
        this.mDate = mDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMLength() {
        return mLength;
    }

    public void setMLength(int mLength) { this.mLength = mLength; }

    public String getMDate() {
        return mDate;
    }

    public void setmDate(String mDate) { this.mDate = mDate; }
}
