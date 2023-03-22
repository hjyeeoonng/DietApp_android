package com.example.moso_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {
    protected MyDao myDao;
    protected FoodDao myFoodDao;
    static Context mContext;
    int selectyear, selectmonth, selectday;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Button btn_sub = (Button) findViewById(R.id.btn_sub);
        Button btn_sub2 = (Button) findViewById(R.id.btn_sub2);
        MyDatabase database = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "Diet App")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
        myDao = database.mydao();
        myFoodDao = database.foodDao();
        myFoodDao.Initialize();
        CalendarView calenderView = (CalendarView) findViewById(R.id.calendarView);
        Calendar calendar = Calendar.getInstance();
        selectyear = calendar.get(Calendar.YEAR);
        selectmonth = calendar.get(Calendar.MONTH)+1;
        selectday = calendar.get(Calendar.DAY_OF_MONTH);

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("foods.xls");
            Workbook wb = Workbook.getWorkbook(is);
            if (wb != null) {
                Sheet sheet = wb.getSheet(0);
                if (sheet != null) {
                    int colTotal = sheet.getColumns();
                    int rowIndexStart = 1;
                    int rowTotal = sheet.getColumn(colTotal - 1).length;
                    StringBuilder sb;
                    for (int row = rowIndexStart; row < rowTotal; row++) {

                        sb = new StringBuilder();
                        MyFood myFood = new MyFood();
                        for (int col = 1; col < colTotal; col++) {
                            String contents = sheet.getCell(col, row).getContents();
                            if(col==2) {
                                myFood.setFood(contents);
                            }
                            if(col==1) {
                                myFood.setCalorie(Double.parseDouble(contents));
                            }
                        }
                        myFoodDao.setInsertMyFood(myFood);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                selectyear = year;
                selectmonth=month+1;
                selectday=day;
            }
        });
        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String month;
                String day;
                if(selectmonth<10) month = "0" + selectmonth;
                else month = ""+selectmonth;
                if(selectday<10) day = "0" + selectday;
                else day = ""+selectday;
                String caldate = Integer.toString(selectyear) + "  " + month + "  " + day;
                Intent intent2 = new Intent(MainActivity.this, SubActivity2.class);
                intent2.putExtra("key", caldate);
                startActivity(intent2);
            }
        });
        btn_sub2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String month;
                String day;
                if(selectmonth<10) month = "0" + selectmonth;
                else month = ""+selectmonth;
                String caldate = Integer.toString(selectyear) + "  " + month;
                Intent intent = new Intent(MainActivity.this, SubActivity3.class);
                intent.putExtra("key", caldate);
                startActivity(intent);
            }
        });


    }
}

