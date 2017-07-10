package com.example.hbb.mytheme;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hbb.mytheme.core.FaceSDK;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivityLog";
    private Button btnOpenFile;
    private TextView tv;
    private String mFilePath;
    private ImageView image;
    private Bitmap bitmap;
    private Button btnReco;
    private Button btnOpenCamera;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.openFile:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Tips test").setPositiveButton("YES", listener).setNegativeButton("NO", null).setMessage("Open ths file browset?");
                builder.show();
                break;
            case R.id.reco:
                FaceSDK face = new FaceSDK();
                Log.d(TAG,"timeStart="+getNowTime());
                Bitmap bmap = face.DetectionBitmap(bitmap);
                Log.d(TAG,"timeEnd="+getNowTime());
                image.setImageBitmap(bmap);
                break;
            case R.id.openCamera:
                Intent intent = new Intent(this,UdpActivity.class);
                startActivity(intent);
                break;
        }
    }

    private String getNowTime()
    {
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss:SSS");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    private int infile_code = 1;
    /**
     * 打开浏览文件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            /**
             * 下面是打开文件浏览器
             * 可直接放到button单击事件里面
             */
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, infile_code);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            finish();
        } else if (requestCode == infile_code) {
            String path = data.getDataString();
            tv.setText(path);

            bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setImageBitmap(bitmap);
            Log.d(TAG, "load image execute");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btnOpenFile = (Button) findViewById(R.id.openFile);
        tv = (TextView) findViewById(R.id.tv);
        image = (ImageView) findViewById(R.id.iv);
        btnReco = (Button) findViewById(R.id.reco);
        btnOpenCamera = (Button) findViewById(R.id.openCamera);

        btnOpenFile.setOnClickListener(this);
        btnReco.setOnClickListener(this);
        btnOpenCamera.setOnClickListener(this);
    }

}
