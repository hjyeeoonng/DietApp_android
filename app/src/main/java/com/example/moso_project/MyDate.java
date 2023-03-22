package com.example.moso_project;

import androidx.room.Entity;
import androidx.room.PrimaryKey;



@Entity
public class MyDate {
    public String getWholeDate() {
        return WholeDate;
    }

    public void setWholeDate(String wholeDate) {
        WholeDate = wholeDate;
    }

    public String getFood1() {
        return Food1;
    }

    public void setFood1(String food1) {
        Food1 = food1;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace1() {
        return place1;
    }

    public void setPlace1(String place1) {
        this.place1 = place1;
    }


    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    @PrimaryKey(autoGenerate = true) //로컬데이트로 설정해줘야 나중에 화면 왔다갔다 가능할듯
    private int id = 0;
    private String WholeDate; //yyyy-mm-dd
    private String Food1;
    private String time;
    private String place1;
    private String review;
    private String num;
    private String cal;

    public void setNum(String num) {
        this.num = num;
    }
}