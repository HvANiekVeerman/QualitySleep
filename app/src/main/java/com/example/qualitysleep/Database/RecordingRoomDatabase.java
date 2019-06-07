package com.example.qualitysleep.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.qualitysleep.Model.Recording;

@Database(entities = {Recording.class}, version = 1, exportSchema = false)

public abstract class RecordingRoomDatabase extends RoomDatabase {

    private static final String NAME_DATABASE = "recording_database";
    public abstract RecordingDao recordingDao();
    private static volatile RecordingRoomDatabase INSTANCE;

    public static RecordingRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecordingRoomDatabase.class) {
                if (INSTANCE == null) {

                    // Creating database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RecordingRoomDatabase.class, NAME_DATABASE).build();
                }
            }
        }
        return INSTANCE;
    }
}
