package com.example.moso_project;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {MyDate.class, MyFood.class}, version = 2)
public abstract class MyDatabase extends RoomDatabase {
    public abstract MyDao mydao();
    public abstract FoodDao foodDao();

}
