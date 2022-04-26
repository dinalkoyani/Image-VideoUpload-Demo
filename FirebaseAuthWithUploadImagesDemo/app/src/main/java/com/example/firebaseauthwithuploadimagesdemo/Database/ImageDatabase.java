package com.example.firebaseauthwithuploadimagesdemo.Database;

import android.content.Context;
import android.media.Image;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ImageEntity.class},
        version = 1, exportSchema = false)
public abstract class ImageDatabase extends RoomDatabase {

    public abstract ImageDao imageDao();

    public static final String DATABASE_NAME = "ImageDb";

    public static ImageDatabase instance;

    public static ImageDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context,ImageDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries().build();
        }

        return instance;
    }
}
