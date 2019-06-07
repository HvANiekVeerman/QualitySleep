package com.example.qualitysleep.Database;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.qualitysleep.Model.Recording;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RecordingRepository {

    private RecordingRoomDatabase mAppDatabase;
    private RecordingDao mRecordingDao;
    private LiveData<List<Recording>> mRecordings;
    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public RecordingRepository (Context context) {
        mAppDatabase = RecordingRoomDatabase.getDatabase(context);
        mRecordingDao = mAppDatabase.recordingDao();
        mRecordings = mRecordingDao.getAllRecordings();
    }

    public LiveData<List<Recording>> getAllRecordings() {
        return mRecordings;
    }

    public void insert(final  Recording recording) {
        mExecutor.execute((new Runnable() {
            @Override
            public void run() {
                mRecordingDao.insert(recording);
            }
        }));
    }
}
