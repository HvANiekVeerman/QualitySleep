package com.example.qualitysleep.Model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.qualitysleep.Database.RecordingRepository;

import java.util.List;

public class MainViewModel extends  AndroidViewModel {

    private RecordingRepository mRepository;
    private LiveData<List<Recording>> mRecordings;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = new RecordingRepository(application.getApplicationContext());
        mRecordings = mRepository.getAllRecordings();
    }

    public LiveData<List<Recording>> getRecordings() {
        return mRecordings;
    }

    public void insert(Recording recording) { mRepository.insert(recording); }
}
