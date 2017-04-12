package com.xiumi.qirenbao.team.partnership;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.xiumi.qirenbao.MainActivity;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.widget.SystemBarTintManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * Created by qianbailu on 2017/3/11.
 * 上传活动图片
 */
public class AddPicActivity extends AppCompatActivity {
    @Bind(R.id.back)
    TextView back;
    @Bind(R.id.head)
    ImageView head;
    private File file;
    private Uri ImgUri;
    private Type type;
    private TeamPoPuWindow puWindow;
    private static String id;
    public enum Type {
        PHONE, CAMERA
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pic);
        ButterKnife.bind(this);
        setStatusBar();
        id=getIntent().getStringExtra("id");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ActivityCompat.requestPermissions(AddPicActivity.this, new String[]{android
                .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }
    @OnClick(R.id.head)
    public void changeHead(View view){
        puWindow = new TeamPoPuWindow(AddPicActivity.this, AddPicActivity.this);
        puWindow.showPopupWindow(findViewById(R.id.total));
        puWindow.setOnGetTypeClckListener(new TeamPoPuWindow.onGetTypeClckListener() {
            @Override
            public void getType(Type type) {
                AddPicActivity.this.type = type;
            }

            @Override
            public void getImgUri(Uri ImgUri, File file) {
                AddPicActivity.this.ImgUri = ImgUri;
                AddPicActivity.this.file = file;
            }

        });
    }
    protected void setStatusBar() {
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);

        tintManager.setTintColor(getResources().getColor(R.color.title_bg));
    }
    /**
     * 将Bitmap保存图片到指定的路径/sdcard/XiuMi/Third_path下
     */
    public static File saveImage(Bitmap bitmap) {
        File appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/QiRenBao");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = "activity"+id+ ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    @OnClick(R.id.commit)
    public void setCommit(View view){
        //把文件传到服务器上
        head.setDrawingCacheEnabled(true);//获取bm前执行，否则无法获取
        file=saveImage(head.getDrawingCache());
        Log.e("file",file.getPath()+"/"+file.getName());
        updateHead();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //创建文件夹
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File file = new File(Environment.getExternalStorageDirectory() + "/QiRenBao/");
                        if (!file.exists()) {
                            Log.d("jim", "path1 create:" + file.mkdirs());
                        }
                    }
                    break;
                }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", type + "");
        if (requestCode == 1) {
            if (ImgUri != null) {
                head.setImageURI(ImgUri);
                head.setImageBitmap(BitmapFactory.decodeFile(ImgUri.getPath()));
            }
        } else if (requestCode == 2) {
            if (data != null) {
                Uri uri = data.getData();
                head.setImageURI(uri);
            }
        }
    }
    /**
     * 上传用户头像
     */
    private void updateHead(){
        String url= "https://qrb.shoomee.cn/api/uploadImages";
        OkHttpUtils
                .post()
                .url(url)
                .addHeader("Accept","application/json")
                .addHeader("Authorization", MainActivity.access_token)
                .addFile("img_url",file.getName(),file)
                .addParams("id",id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(AddPicActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.e("上传图片",obj.toString());
                            String result=obj.getString("result");
                            if(result.equals("success")){
                                new AlertDialog.Builder(AddPicActivity.this).setMessage("添加活动图片成功")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddPicActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
