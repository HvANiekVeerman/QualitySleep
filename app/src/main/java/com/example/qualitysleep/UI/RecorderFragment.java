package com.example.qualitysleep.UI;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.example.qualitysleep.Model.MainViewModel;
import com.example.qualitysleep.Model.Recording;
import com.example.qualitysleep.R;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class RecorderFragment extends Fragment implements Serializable {
    private static final String TAG = "RecorderFragment";

    // Declare variables
    private static final String AUDIO_FILE = "_audio_record.3gp";
    private static final int REQUEST_PERMISSION_CODE = 1000;

    private Button mBtnStopRecording;
    private Button mBtnPlayRecording;
    private Button mBtnStopPlaying;
    private Chronometer mChronometer;
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private FloatingActionButton mFab;
    private MainViewModel mMainViewModel;

    private String mPathSave = "";
    private boolean mChrRunning;
    private long mPauseOffset;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        View v = inflater.inflate(R.layout.recorder_fragment, container, false);

        // Request permission
        if (!checkPermissionFromDevice()) requestPermissions();

        // Initialize view
        mFab = v.findViewById(R.id.fab);
        mBtnStopRecording = v.findViewById(R.id.button_stop_recording);
        mBtnPlayRecording = v.findViewById(R.id.button_play_recording);
        mBtnStopPlaying = v.findViewById(R.id.button_stop_playing);

        mChronometer = v.findViewById(R.id.chronometer);
        mChronometer.setFormat("Duration: %s");
        mChronometer.setBase(SystemClock.elapsedRealtime());

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermissionFromDevice()) {
                    mPathSave = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            UUID.randomUUID().toString() + AUDIO_FILE;
                    setupMediaRecorder();
                    try {
                        if (!mChrRunning) {
                            mChronometer.setBase(SystemClock.elapsedRealtime() - mPauseOffset);
                            mMediaRecorder.prepare();
                            mMediaRecorder.start();
                            mChronometer.start();
                            mChrRunning = !mChrRunning;
                            mBtnStopRecording.setEnabled(true);
                            mBtnPlayRecording.setEnabled(false);
                            mBtnStopPlaying.setEnabled(false);
                            mFab.setEnabled(false);
                            Toast.makeText(getActivity(), R.string.recording, Toast.LENGTH_SHORT).show();
                        } else requestPermissions();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mBtnStopRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChrRunning) {
                    mChronometer.stop();
                    mMediaRecorder.stop();
                    mPauseOffset = SystemClock.elapsedRealtime() - mChronometer.getBase();
                    mChrRunning = !mChrRunning;
                    storeRecording(SystemClock.elapsedRealtime() - mChronometer.getBase());
                    mChronometer.setBase(SystemClock.elapsedRealtime());
                    mPauseOffset = 0;

                    mBtnStopRecording.setEnabled(false);
                    mBtnPlayRecording.setEnabled(true);
                    mFab.setEnabled(true);
                    mBtnStopPlaying.setEnabled(false);
                }
            }
        });

        mBtnPlayRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnStopPlaying.setEnabled(true);
                mBtnStopRecording.setEnabled(false);
                mFab.setEnabled(false);
                mBtnPlayRecording.setEnabled(false);
                mMediaPlayer = new MediaPlayer();
                try {
                    mMediaPlayer.setDataSource(mPathSave);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getActivity(), R.string.playing, Toast.LENGTH_SHORT).show();
            }
        });

        mBtnStopPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtnPlayRecording.setEnabled(true);
                mBtnStopPlaying.setEnabled(false);
                mFab.setEnabled(true);
                mBtnStopRecording.setEnabled(false);
                if (mMediaPlayer != null) {
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    setupMediaRecorder();
                }
            }
        });
        return v;
    }

    private void storeRecording(long mLength) {
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format));
        Calendar calendar = Calendar.getInstance();
        String mDateTime = sdf.format(calendar.getTime());
        Recording recording = new Recording(mDateTime, mLength);
        mMainViewModel.insert(recording);
    }

    private void setupMediaRecorder() {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mMediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setOutputFile(mPathSave);
    }

    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.RECORD_AUDIO);
        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        }, REQUEST_PERMISSION_CODE);
    }
}
