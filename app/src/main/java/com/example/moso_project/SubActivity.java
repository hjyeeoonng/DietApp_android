package com.example.moso_project;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class SubActivity extends AppCompatActivity{
    String foods="";
    String num="";
    String cal="";
    String show="";
    EditText editText_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        MyDao myDao = ((MainActivity)MainActivity.mContext).myDao;
        FoodDao myFoodDao = ((MainActivity)MainActivity.mContext).myFoodDao;
        Button btn_save = (Button)findViewById(R.id.btn_save);
        Button btn_map = (Button) findViewById(R.id.button_map);
        Button btn_addfood = (Button) findViewById(R.id.btn_addfood);
        TextView textView = (TextView)findViewById(R.id.textView);
        EditText editText_food = (EditText)findViewById(R.id.editText_food);
        EditText editText_num = (EditText)findViewById(R.id.editText_num);
        EditText editText_rev = (EditText)findViewById(R.id.editText_rev);
        EditText editText_cal = (EditText)findViewById(R.id.editText_cal);
        editText_map = findViewById(R.id.editText_map);
        TimePicker tmpk = (TimePicker)findViewById(R.id.timepicker);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView showview = (TextView)findViewById(R.id.showview);
        Intent intent = getIntent();
        Button btn_calorie = (Button)findViewById(R.id.btn_calorie);
        String datafrommain = intent.getStringExtra("key");
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        ContentResolver resolver = getContentResolver();
                        InputStream instream;
                        try {
                            instream = resolver.openInputStream(uri);
                            Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                            imageView.setImageBitmap(imgBitmap);
                            instream.close();
                            saveBitmapToJpeg(imgBitmap,"Diet_"+Integer.toString(myDao.getMaxID()));
                            Log.d("Uri: ",getCacheDir()+"/"+uri.getEncodedPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        textView.setText(datafrommain);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubActivity.this, MapActivity.class);
                startActivityForResult(intent,0);
            }
        });

        btn_calorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show="";
                showview.setText("");
                cal+=Double.parseDouble(editText_cal.getText().toString())*Double.parseDouble(editText_num.getText().toString())+",";

                String[] str = foods.split(",");
                String[] strnum = num.split(",");
                String[] strcal = cal.split(",");
                for (int i = 0; i < str.length; i++) {
                    show+=str[i]+" "+strnum[i]+ " " + strcal[i] + "kcal"  + "\n";
                }
                showview.setText(show);
                editText_cal.setVisibility(View.INVISIBLE);
                btn_calorie.setVisibility(View.INVISIBLE);
                editText_food.setText("");
                editText_num.setText("");
                editText_cal.setText("");
            }
        });
        btn_addfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText_food.getText().toString().equals("") || editText_num.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "음식 또는 수량을 입력하세요.",Toast.LENGTH_SHORT).show();
                }
                else {
                    foods += editText_food.getText().toString() + ",";
                    num += editText_num.getText().toString() + ",";
                    if(myFoodDao.getCal(editText_food.getText().toString())==null)
                    {
                        editText_cal.setVisibility(View.VISIBLE);
                        btn_calorie.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "Database에 없는 음식입니다. 칼로리를 입력하세요.",Toast.LENGTH_LONG).show();
                    }
                    else {
                        cal += Double.toString(myFoodDao.getCal(editText_food.getText().toString()) * Double.parseDouble(editText_num.getText().toString())) + ",";
                        editText_food.setText("");
                        editText_num.setText("");
                        String[] str = foods.split(",");
                        String[] strnum = num.split(",");
                        String[] strcal = cal.split(",");
                        show="";
                        for (int i = 0; i < str.length; i++) {

                            show+=str[i]+" "+strnum[i]+ " " + strcal[i] + "kcal"  + "\n";
                        }
                        showview.setText(show);
                    }
                }

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubActivity.this, SubActivity2.class);
                String shour;
                String sminute;
                String AMPM;
                MyDate mydate = new MyDate();
                mydate.setFood1("");
                mydate.setReview("");
                mydate.setPlace1("");
                mydate.setTime("");
                mydate.setNum("");
                mydate.setCal("");
                if(tmpk.getHour()>=12) {
                    AMPM = "오후 ";
                    if(tmpk.getHour()>=22) shour = Integer.toString(tmpk.getHour()-12);
                    else shour = "0" + Integer.toString(tmpk.getHour()-12);
                }
                else {
                    AMPM = "오전 ";
                    if(tmpk.getHour()>=10) shour = Integer.toString(tmpk.getHour());
                    else shour = "0" + Integer.toString(tmpk.getHour());
                }
                if(tmpk.getMinute()>=10) sminute = Integer.toString(tmpk.getMinute());
                else sminute = "0" + Integer.toString(tmpk.getMinute());
                String stime = AMPM + shour + "시 " + sminute + "분";
                double calorie=0;
                mydate.setWholeDate(datafrommain);
                mydate.setFood1(foods.substring(0, foods.length()-1));
                mydate.setReview(editText_rev.getText().toString());
                mydate.setNum(num.substring(0, num.length()-1));
                mydate.setPlace1(editText_map.getText().toString());
                mydate.setTime(stime);

                String[] str = foods.split(",");
                String[] strnum = num.split(",");
                String[] strcal = cal.split(",");
                for (int i = 0; i < str.length; i++) {
                    calorie += Double.parseDouble(strcal[i]);
                }
                mydate.setCal(Double.toString(Math.round(calorie*100)/100));
                myDao.setInsertMyDate(mydate);
                intent.putExtra("key", datafrommain);
                startActivity(intent);
                finish();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetContent.launch("image/*");
            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0){
            if (resultCode==RESULT_OK) {
                String address = data.getStringExtra("address");
                editText_map.setText(address);
            }
        }
    }

    private void saveBitmapToJpeg(Bitmap bitmap, String name) {
        File storage = getCacheDir();
        String fileName = name + ".jpg";
        File tempFile = new File(storage, fileName);
        try {
            tempFile.createNewFile();
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
        }
    }

}