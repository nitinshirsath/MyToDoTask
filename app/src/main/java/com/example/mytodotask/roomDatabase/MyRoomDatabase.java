package com.example.mytodotask.roomDatabase;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mytodotask.model.Task;

@Database(entities = {Task.class}, version = 2, exportSchema = false)
public abstract class MyRoomDatabase extends RoomDatabase {
    public abstract DAO dao();
}
