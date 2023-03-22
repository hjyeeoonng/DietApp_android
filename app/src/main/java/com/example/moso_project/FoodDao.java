package com.example.moso_project;

import static java.nio.file.attribute.AclEntryPermission.DELETE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodDao {
    @Insert
    void setInsertMyFood(MyFood myFood);

    @Query("DELETE FROM MyFood")
    void Initialize();

    @Query("SELECT * FROM MyFood")
    List<MyFood> getMyFoodAll();

    @Query("SELECT Calorie FROM MyFood WHERE Food = :food")
    Double getCal(String food);

}
