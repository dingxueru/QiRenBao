package com.xiumi.qirenbao.team.partnership;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.mine.company.ScreenUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by qianbailu on 2017/3/27.
 */

public class TeamPoPuWindow extends PopupWindow implements View.OnClickListener {

    public Context mContext;

    private AddPicActivity.Type type;

    public Activity mActivity;

    private File file;
    private Uri ImgUri;

    private TextView mTakePhoto, mAlbumPhoto, mCancel;

    public TeamPoPuWindow(Context context, Activity mActivity) {
        initView(context);
        this.mActivity = mActivity;
    }

    private void initView(Context mContext) {
        this.mContext = mContext;
        View v = LayoutInflater.from(mContext).inflate(R.layout.chose_pic_layout,
                null);
        setContentView(v);

        mTakePhoto = (TextView) v.findViewById(R.id.photo_take);
        mAlbumPhoto = (TextView) v.findViewById(R.id.photo_album);
        mCancel = (TextView) v.findViewById(R.id.photo_cancel);

        mTakePhoto.setOnClickListener(this);
        mAlbumPhoto.setOnClickListener(this);
        mCancel.setOnClickListener(this);

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ScreenUtils.getScreenHeight(mContext));

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setTouchable(true);
        this.setFocusable(true);
        this.setOutsideTouchable(true);

        // 刷新
        this.update();
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popuwindow_from_bottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x50000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    public void showPopupWindow(View parent) {

        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            this.dismiss();

        }
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.photo_take:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File picDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/QiRenBao");
                Log.e("msg",Environment.getExternalStorageDirectory().getAbsolutePath()
                        + "/QiRenBao");
                if (!picDir.exists())
                    picDir.mkdirs();
                Log.e("picDir.exists()",picDir.exists()+"");
                file = new File(picDir, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+ ".jpg");
                ImgUri = Uri.fromFile(file);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, ImgUri);
                mActivity.startActivityForResult(intent, 1);
                type = AddPicActivity.Type.CAMERA;
                if (listener != null) {
                    listener.getType(type);
                    listener.getImgUri(ImgUri, file);
                }
                this.dismiss();
                break;
            case R.id.photo_album:
                Intent intent2 = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                mActivity.startActivityForResult(intent2, 2);
                type = AddPicActivity.Type.PHONE;
                if (listener != null) {
                    listener.getType(type);
                }
                this.dismiss();
                break;
            case R.id.photo_cancel:
                this.dismiss();
                break;
            default:
                break;
        }
    }

    public interface onGetTypeClckListener {
        void getType(AddPicActivity.Type type);

        void getImgUri(Uri ImgUri, File file);
    }

    private  TeamPoPuWindow.onGetTypeClckListener listener;

    public void setOnGetTypeClckListener(TeamPoPuWindow.onGetTypeClckListener listener) {
        this.listener = listener;
    }

}
