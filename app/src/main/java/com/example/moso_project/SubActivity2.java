package com.example.moso_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SubActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub2);
        ArrayList<String> getTime = new ArrayList<>();
        MyDao myDao = ((MainActivity) MainActivity.mContext).myDao;
        List<MyDate> datelist = myDao.getMyDateAll();
        Button btn_update = (Button) findViewById(R.id.btn_update);
        Button btn_add = (Button) findViewById(R.id.btn_add);
        TextView textView_date = (TextView) findViewById(R.id.textView_date);
        TextView totalcal = (TextView) findViewById(R.id.totalcal);
        Intent intentmain = getIntent();
        String datafrommain = intentmain.getStringExtra("key");
        List<MyDate> findlist = myDao.getRelated(datafrommain); //날짜별
        Double totalcalories = 0.0;//총칼로리 선언

        // System.out.println(datelist);
        for (int i = 0; i < findlist.size(); i++) {
            getTime.add(findlist.get(i).getTime());
        }
        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        MyAdapter adapter = new MyAdapter(getTime);
        recyclerView.setAdapter(adapter);
        textView_date.setText(datafrommain);
        Dialog dialog = new Dialog(this);

        //total calories 계산 및 Display
        for (int i = 0; i < datelist.size(); i++) {
            if(datelist.get(i).getWholeDate().equals(datafrommain)){
                totalcalories += Double.parseDouble(datelist.get(i).getCal());
            }
        }
        totalcal.setText("Day Total Calories : "+totalcalories.toString());

        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                System.out.println("원 포스 = " + pos);
                List<MyDate> datelist2 = myDao.getMyDateAll();
                for (int i = 0; i < datelist2.size(); i++) {
                    if(findlist.get(pos).getWholeDate().equals(datelist2.get(i).getWholeDate()) && findlist.get(pos).getTime().equals(datelist2.get(i).getTime()))
                    {
                        pos=i; //전체 pos 로 전환
                        System.out.println("전체 포스 = " + pos);
                        break;
                    }
                }
                dialog.setContentView(R.layout.custom_dialog);
                TextView title = (TextView) dialog.findViewById(R.id.title);
                TextView tv = (TextView) dialog.findViewById(R.id.custom_text);
                title.setText(datelist2.get(pos).getWholeDate() + "\n" + datelist2.get(pos).getTime());
                tv.setText("음식 : " + datelist2.get(pos).getFood1() + "\n수량 : " + datelist2.get(pos).getNum() + "\n리뷰 : "
                        + datelist2.get(pos).getReview() + "\n칼로리 : " + String.format("%.1f",Double.parseDouble(datelist2.get(pos).getCal())) +"kcal" + "\n장소 : " + datelist2.get(pos).getPlace1());
                ImageView iv = (ImageView) dialog.findViewById(R.id.custom_image);
                iv.setImageBitmap(getBitmapFromCacheDir(pos));

                dialog.show();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubActivity2.this, SubActivity.class);
                intent.putExtra("key", datafrommain);
                startActivity(intent);
                finish();
            }
        });
    }

    private Bitmap getBitmapFromCacheDir(int pos) {
        File file = new File(getCacheDir().toString());
        File[] files = file.listFiles();
        for (File tempFile : files) {
            if (tempFile.getName().contains("Diet_"+ Integer.toString(pos))) {
                String path = getCacheDir() + "/" + "Diet_" + Integer.toString(pos) + ".jpg";
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                return bitmap;
            }
        }
        return null;
    }

}


