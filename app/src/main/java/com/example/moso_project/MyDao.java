package com.example.moso_project;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDao {
    @Insert
    void setInsertMyDate(MyDate myDate);

    @Update
    void setUpdateMyDate(MyDate myDate);

    @Delete
    void setDeleteMyDate(MyDate myDate);

    @Query("SELECT * FROM MyDate")
    List<MyDate> getMyDateAll();

    @Query("SELECT * FROM MYDATE WHERE WholeDate= :curdate")
    List<MyDate> getRelated(String curdate);

    @Query("SELECT MAX(id) FROM MyDate")
    int getMaxID();

}
