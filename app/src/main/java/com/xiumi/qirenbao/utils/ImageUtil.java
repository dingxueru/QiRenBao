package com.xiumi.qirenbao.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.xiumi.qirenbao.R;
import com.xiumi.qirenbao.app.App;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUtil {
    private static final Pattern ALI_IMG_REGEX = Pattern.compile("http://oss\\.av-gd\\.com.+(\\..+)?@.*?(\\..+)?");
    private static final String TAG = ImageUtil.class.getSimpleName();

    public static void displayImage(String url, ImageView imageView, DisplayImageOptions options){
        App.getAppContext().getImgLoader().displayImage(url,imageView,options);
    }


    public  static void displayImageNetwork(String url, ImageView imageView){
        App.getAppContext().getImgLoader().displayImage(url,imageView);
    }

    public static  void displayImage(String url, ImageView imageView){
        App.getAppContext().getImgLoader().displayImage(url, imageView, getDisplayOptions());
    }

    public static void displayWebp(final String url, final ImageView imageView) {
        displayWebp(url, imageView, getDisplayOptions());
    }

    /**
     * 先加载webp，失败加载url
     *
     * @param url
     * @param imageView
     * @param options
     */
    public static void displayWebp(final String url, final ImageView imageView,
                                   DisplayImageOptions options) {
        App.getAppContext().getImgLoader().displayImage(convertOSSWebpUri(url),
                imageView, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        displayImage(url, imageView);
                    }
                });
    }


    public static DisplayImageOptions getLongImageDisplayImageOptions(){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_loading) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_load_fail)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_load_fail)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.NONE)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .resetViewBeforeLoading(true).build();//设置图片在下载前是否重置，复位
        return options;
    }

    private static String convertOSSWebpUri(String url) {
        if (TextUtils.isEmpty(url)) return url;
        String webpUrl = url;
        Matcher m = ALI_IMG_REGEX.matcher(webpUrl);
        if (m.matches()) {
            if (TextUtils.isEmpty(m.group(2))) {
                webpUrl += ".webp";
                Log.d(TAG, "convertOSSWebp uri: " + webpUrl);
                return webpUrl;
            }
            int idx = webpUrl.lastIndexOf(m.group(2));
            webpUrl = webpUrl.substring(0, idx);
            webpUrl += ".webp";
            Log.d(TAG, "convertOSSWebp uri: " + webpUrl);
            return webpUrl;
        }
        if (webpUrl.indexOf("http://oss.av-gd.com") >= 0) {
            webpUrl = webpUrl + "@.webp";
            Log.d(TAG, "convertOSSWebp uri: " + webpUrl);
            return webpUrl;
        }
        Log.d(TAG, "convertOSSWebp uri: " + webpUrl);
        return webpUrl;
    }

    public  static DisplayImageOptions.Builder getDisplayOptionsBuild(){
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.icon_loading) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.icon_load_fail)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.icon_load_fail)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .resetViewBeforeLoading(true);//设置图片在下载前是否重置，复位
                //.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
                //.displayer(new RoundedBitmapDisplayer(5))

        return builder;
    }

    public  static DisplayImageOptions getDisplayOptions(){
        return getDisplayOptionsBuild().build();
    }

    public static void clean(){
        App.getAppContext().getImgLoader().clearMemoryCache();
        App.getAppContext().getImgLoader().clearDiskCache();
    }
}
