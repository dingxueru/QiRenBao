package com.xiumi.qirenbao.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * Created by qianbailu on 2017/2/20.
 */
public class App extends Application {

    private static App mInstance = null;
    private Stack<WeakReference<Activity>> activities = new Stack<WeakReference<Activity>>();
    private ImageLoader mImgLoader;

    public static App getAppContext() {
        App instance = mInstance;  // <<< 在这里创建临时变量
        if (instance == null) {
            synchronized (App.class) {
                instance = mInstance;
                if (instance == null) {
                    instance = new App();
                    mInstance = instance;
                }
            }
        }
        return instance;  // <<< 注意这里返回的是临时变量
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public ImageLoader getImgLoader() {
        if (mImgLoader == null) {
            mImgLoader = ImageLoader.getInstance();
            mImgLoader.init(initImgloadConf());
        }
        return mImgLoader;
    }

    /**
     * ImageLoader Configuration (ImageLoaderConfiguration) is global for application
     *
     * @return ImageLoaderConfiguration
     */
    private ImageLoaderConfiguration initImgloadConf() {

        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "QiRenBao/img/cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                //.memoryCacheExtraOptions(1024, 800) // max width, max height，即保存的每个缓存文件的最大长宽
//                 .diskCacheExtraOptions(480, 800,null)  // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
                .threadPoolSize(3)//线程池内加载的数量,最好是1-5
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()

                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(20)
                //
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径

                // 将缓存下来的文件以什么方式命名
                // 里面可以调用的方法有
                // 1.new Md5FileNameGenerator() //使用MD5对UIL进行加密命名
                // 2.new HashCodeFileNameGenerator()//使用HASHCODE对UIL进行加密命名
//                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .imageDownloader(new BaseImageDownloader(this, 20 * 1000, 30 * 1000)) // connectTimeout (20 s), readTimeout (30 s)超时时间
                .imageDecoder(new BaseImageDecoder(false)) // default
                .writeDebugLogs() // Remove for release app
                .build();//开始构建

        return config;

    }
    // 释放activity
    public synchronized void popActivity() {
        WeakReference<Activity> rf = activities.pop();
        Activity activity = rf.get();
        if (null != activity) {
            Log.i("popActivity", "popActivity:" + activity.getLocalClassName().toString());
            activity.finish();
        }
    }
}
