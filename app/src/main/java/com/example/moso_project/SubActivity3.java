package com.example.moso_project;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SubActivity3 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub3);

        Intent intentmain = getIntent();
        String datafrommain = intentmain.getStringExtra("key");

        TextView textView_date = (TextView) findViewById(R.id.textView_date);

        MyDao myDao = ((MainActivity) MainActivity.mContext).myDao;
        List<MyDate> findlist = myDao.getMyDateAll(); //데이터베이스의 모든 데이터 가져오기

        ArrayList<String> getMonth = new ArrayList<>();
        String nday = "";
        for (int i = 0; i < findlist.size(); i++) {
            if (i==0){
                nday = findlist.get(i).getWholeDate().substring(10,12);
                getMonth.add(nday+'일');
            }
            if(findlist.get(i).getWholeDate().substring(0,8).equals(datafrommain)){
                if(findlist.get(i).getWholeDate().substring(10,12).equals(nday)){
                    getMonth.add(findlist.get(i).getTime());
                }else{
                    nday = findlist.get(i).getWholeDate().substring(10,12);
                    getMonth.add(nday+'일');
                    getMonth.add(findlist.get(i).getTime());
                }
            }
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        RecyclerView recyclerView = findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        MyAdapter adapter = new MyAdapter(getMonth);
        recyclerView.setAdapter(adapter);
        Dialog dialog = new Dialog(this);


        String sub3_month = datafrommain.substring(6,8);
        String[] monthlist = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        for (int mon = 1; mon < 13; mon++) {
            if (Integer.parseInt(sub3_month) == mon){
                textView_date.setText(monthlist[mon-1]+"  "+datafrommain.substring(0,4));
            }
        }

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
                        + datelist2.get(pos).getReview() + "\n칼로리 : " + String.format("%.1f",Double.parseDouble(datelist2.get(pos).getCal())) +"kcal" + "\n장소 : ");
                ImageView iv = (ImageView) dialog.findViewById(R.id.custom_image);
                iv.setImageBitmap(getBitmapFromCacheDir(pos));

                dialog.show();
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
