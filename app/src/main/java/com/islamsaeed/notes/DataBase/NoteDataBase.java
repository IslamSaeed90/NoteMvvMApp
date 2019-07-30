package com.islamsaeed.notes.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Note.class, version = 1)
public abstract class NoteDataBase extends RoomDatabase {

    /**
     * 1 / turn this class to single ton
     */
    private static NoteDataBase instance;

    /**
     * 2 / create NoteDao method to access our Dao
     */
    public abstract NoteDAO noteDAO();

    /**
     * 3/ create our dataBase and again we want to create a single ton
     */
    public static synchronized NoteDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext()
                    , NoteDataBase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }


}
