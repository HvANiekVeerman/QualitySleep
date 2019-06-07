package com.example.qualitysleep.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.qualitysleep.Model.Recording;

import java.util.List;

@Dao
public interface RecordingDao {

    @Insert
    void insert(Recording recording);

    @Query("SELECT * from recordings_table")
    LiveData<List<Recording>> getAllRecordings();

}
