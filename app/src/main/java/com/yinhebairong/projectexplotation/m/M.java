package com.yinhebairong.projectexplotation.m;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.yinhebairong.projectexplotation.widget.CommonDialog;
import com.yinhebairong.projectexplotation.widget.LoadDialog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class M {

    /*
      Created by liming on 2019/10/14.
     */

    public final static String M = "M";
    private static Gson gson = new Gson();

    public final static int success = 200; // 接口成功 200
    public final static int fail = 500; // 接口失败 500
    public final static String loginId = "loginTag";
    public final static String loginZh = "loginZh";
    public final static String loginName = "loginName";
    public final static String XitiTag = "XitiTag";
    //图片选择Tag
    public static int UserSelectPhotoCode = 8848;
    //本地视频选择Tag
    public static int UserSelectLocalVideoCode = 9090;
    //录取的视频Tag
    public static int UserRecordVideoCode = 9093;
    //打开本地相册 requestCode
    public final static int OpenPic = 8080;
    public final static int OpenPicLeft = 8081;
    public final static int OpenPicRight = 8082;

    // ? -> Main  Tag
    public final static String MainTag = "MainTag";
    // ? -> ? Id
    public final static String IdTag = "IdTag";
    // ? -> ?
    public final static String TypeTag = "TypeTag";
    // ? - > ?
    public final static String Pid = "";

    public final static String PicTextKey = "l5Aq2XXgdhHRodcPU5OEGv3f";
    public final static String PicTextValue = "gZF1GNs3nS5FfezaREZtqnRLrsWATVgd";

    //   -------------------------------------------------Class Tag   ---------------------------------------------------------
    public static String getTAG(Activity activity){
        return  activity.getClass().getSimpleName();
    }

    //   --------------------------------------------------加载进度的dialog----------------------------------------------------------------
    private static LoadDialog progressDialog;
    public static void showProgressDialog(Context context){
        if(progressDialog == null)progressDialog = LoadDialog.createDialog(context);
        if(progressDialog.isShowing()) progressDialog.dismiss();
        progressDialog.show();
    }

    public void hideProgressDialog(){
        if (progressDialog != null) progressDialog.dismiss();
    }

   //   --------------------------------------------------   Gilde加载图片   ---------------------------------------------------------------------------------

    // implementation 'com.github.bumptech.glide:glide:4.8.0'
    public static void Glide(String url, ImageView view, Context context) {
        Glide.with(context).load(url).apply(requestOptions).into(view);
    }

    private static RequestOptions requestOptions = new RequestOptions()
            .placeholder(new ColorDrawable(Color.GRAY))
            .error(new ColorDrawable(Color.GRAY))//     R.mipmap.anime
            .fallback(new ColorDrawable(Color.GRAY));

    //   ---------------------------------------------------   Bitmap 获取 Uri   ----------------------------------------------------------------------
    public static Uri bitmap2uri(Context c, Bitmap b) {
        File path = new File(c.getCacheDir() + File.separator + System.currentTimeMillis() + ".jpg");
        try {
            OutputStream os = new FileOutputStream(path);
            b.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.close();
            return Uri.fromFile(path);
        } catch (Exception ignored) {
        }
        return null;
    }


    //    -----------------------------------------------   Json解析转换   --------------------------------------------------------------------
    public static <T> T getEntity(String json, Class<T> t) {
        return gson.fromJson(json, t);
    }

    public static String getJson(Object obj) {
        return gson.toJson(obj);
    }

    //    -----------------------------------------------   权限请求   --------------------------------------------------------------------

    /**
     * implementation 'com.hjq:xxpermissions:5.5'
     *
     * @param activity
     * @param onPermissionSuccessListener
     * @param permissions
     */
    public static void getPermissions(Activity activity, OnPermissionSuccessListener onPermissionSuccessListener, String... permissions) {
        XXPermissions.with(activity).permission(permissions).constantRequest().request(new OnPermission() {
            @Override
            public void hasPermission(List<String> granted, boolean isAll) {
                if (isAll) {
                    onPermissionSuccessListener.permissionSuccess();
                }
            }

            @Override
            public void noPermission(List<String> denied, boolean quick) {
                new CommonDialog(activity)
                        .setTitle("权限提示")
                        .setContent("您当前操作的功能需要相应权限才能使用,请前往设置中开启权限")
                        .setPositiveButton("去设置")
                        .setNegativeButton("取消")
                        .setTouchOutSide(true)
                        .setOnCloseListener((dialog, confirm) -> {
                            if (confirm) {
                                toAppSetting(activity);
                                dialog.dismiss();
                            } else {
                                toast(activity, "没有权限");
                            }
                        }).show();
            }
        });
    }

    /**
     * 搜索监听
     */
    public interface OnPermissionSuccessListener {
        void permissionSuccess();
    }

    /*
      查看读写权限
       */
    public static boolean checkPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }

//    -----------------------------------------------   Edittext操作   --------------------------------------------------------------------

    /**
     * 获取Edittext  内容
     *
     * @param editText
     * @return
     */
    public static String getEditTextString(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * 判断Empty是否为空 true 为空
     *
     * @param editText
     * @return
     */
    public static boolean getEditTextStringIsEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }

//-----------------------------------------------   跳转到权限设置界面   --------------------------------------------------------------------

    /**
     * 跳转到权限设置界面
     */
    public static void toAppSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }
//---------------------------------------------------   Toast弹窗   -------------------------------------------------------------------

    public static void toast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
//-------------------------------------------------   Log输出   ---------------------------------------------------------------------
    /**
     * LOG 输出
     *
     * @param tag
     * @param name
     */
    public static boolean LogSwitch = true;

    public static void log(String tag, Object name) {
        if (LogSwitch) {
            Log.e(tag, name + "");
        }
    }


//—————————————————————————   保存文件   —————————————————————————————————————

    /**
     * 保存音频到SD卡的指定位置
     */
    public static void saveVoiceToSD(String path1, String path2, OnSaveToSdCardListener listener) {
        //创建输入输出
        InputStream isFrom = null;
        OutputStream osTo = null;
        try {
            //设置输入输出流
            isFrom = new FileInputStream(path1);
            osTo = new FileOutputStream(path2);
            byte bt[] = new byte[1024];
            int len;
            while ((len = isFrom.read(bt)) != -1) {
                Log.d("录音", "len = " + len);
                osTo.write(bt, 0, len);
            }
            Log.d("录音", "保存录音完成。");
            listener.onOver();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (osTo != null) {
                try {
                    //不管是否出现异常，都要关闭流
                    osTo.close();
                    Log.d("录音", "关闭输出流");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isFrom != null) {
                try {
                    isFrom.close();
                    Log.d("录音", "关闭输入流");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存照片到SD卡的指定位置
     */
    public static void savePhotoToSD(Bitmap bitmap, File file, OnSaveToSdCardListener listener) {
        Log.d("录音", "将图片保存到指定位置。");
        //创建输出流缓冲区
        BufferedOutputStream os = null;
        try {
            //设置输出流
            os = new BufferedOutputStream(new FileOutputStream(file));
            Log.d("录音", "设置输出流。");
            //压缩图片，100表示不压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            Log.d("录音", "保存照片完成。");
            listener.onOver();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            listener.onError();
        } finally {
            if (os != null) {
                try {
                    //不管是否出现异常，都要关闭流
                    os.flush();
                    os.close();
                    Log.d("录音", "刷新、关闭流");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            listener.onError();
        }
    }

    /**
     * 保存监听
     */
    public interface OnSaveToSdCardListener {
        void onOver();

        void onError();
    }


    //    —————————————————————————   毫秒转换年月日 时分秒   —————————————————————————————————————
    static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String timeYMDToDate(long time) {
        return formatter.format(time);
    }

    //    —————————————————————————   毫秒转换 分钟   —————————————————————————————————————
    public static StringBuilder timeSToM(int time) {
        StringBuilder stringBuilder = new StringBuilder();

        int min = time / 60;
        int second = time % 60;
        if (min < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(min + ":");
        if (second < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(second);
        return stringBuilder;
    }

    //    —————————————————————————   获取文件总时长   —————————————————————————————————————
    public static int getRecordTime(String path) {
        int time = 0;
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            time = mediaPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.release();
        return time / 1000;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            //由context.getContentResolver()获取contentProvider再获取cursor(游//标）用游标获取文件路径返回
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            cursor.moveToFirst();
            int column_indenx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            return cursor.getString(column_indenx);
        } finally {

            if (cursor != null) {

                cursor.close();
            }
        }
    }

    //    —————————————————————————   遍历文件   —————————————————————————————————————
    public static List<File> getFiles(String filePath) {
        List<File> list = new ArrayList<>();
        File[] allFiles = new File(filePath).listFiles();
        if (allFiles != null) { // 若文件不为空，则遍历文件长度
            for (int i = 0; i < allFiles.length; i++) {
                File file = allFiles[i];
                if (file.isFile()) {
                    list.add(file);
                }
            }
        }
        return list;
    }

    //   ----------------------------------------------------------------------------------------------------------
    public static void showDelDialog(Context context, OnDelListener onDelListener) {
        new CommonDialog(context)
                .setTitle("提示")
                .setContent("确定删除吗?")
                .setPositiveButton("删除")
                .setNegativeButton("取消")
                .setTouchOutSide(true)
                .setOnCloseListener((dialog, confirm) -> {
                    if (confirm) {
                        onDelListener.onDel();
                    }
                    dialog.dismiss();
                }).show();
    }

    /**
     * 删除监听
     */
    public interface OnDelListener {
        void onDel();
    }

    /**
     * 设置背景颜色 (红米的部分版本不然会出现跳屏到桌面的问题)
     * @param bgAlpha
     */
    public static void setPopwindow_alph(float bgAlpha, Context mContext) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    //    ——————————————————————————————————————————————————————————————

}
