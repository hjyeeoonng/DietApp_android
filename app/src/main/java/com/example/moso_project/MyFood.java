package com.example.moso_project;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MyFood
{

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFood() {
        return Food;
    }
    public double getCalorie() {
        return Calorie;
    }

    public void setCalorie(double calorie) {
        Calorie = calorie;
    }
    public void setFood(String food) {
        Food = food;
    }

    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    private String Food;



    private double Calorie;

}
