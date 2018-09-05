package com.waiwi.android.mylibrary.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Tools {


    //判断多个字段是否为空
    public static boolean isNull(String... ss) {
        for (int i = 0; i < ss.length; i++) {
            if (null == ss[i] || ss[i].equals("") || ss[i].equalsIgnoreCase("null")) {
                return true;
            }
        }
        return false;
    }

    //判断一个字段是否为空
    public static boolean isNull(String s) {

        return null == s || s.equals("") || s.equalsIgnoreCase("null");
    }

    //获取文本控件上显示的文字TextView
    public static String getText(TextView v) {
        if (v != null) {
            return v.getText().toString().trim();
        }
        return "";
    }

    // 获取文本控件上显示的文字EditText
    public static String getText(EditText v) {
        if (v != null) {
            return v.getText().toString().trim();
        }
        return "";
    }

    //判断一个字段的值是否已为空(TextView)
    public static boolean isNull(TextView v) {
        return null == v || Tools.isNull(Tools.getText(v));
    }

    //判断一个字段的值 是否为空(EditText)
    public static boolean isNull(EditText v) {
        return null == v || Tools.isNull(Tools.getText(v));
    }

    // 验证手机号

    public static boolean validatePhone(String phone) {
        if (isNull(phone)) {
            return false;
        }
        String pattern = "^1[3,4,5,6,7,8]+\\d{9}$";
        return phone.matches(pattern);
    }

    //验证国内电话:固定电话格式如：027-87124563 及手机号

    public static boolean validateTel(String tel) {
        if (isNull(tel)) {
            return false;
        }
        String pattern = "^((0\\d{2,3}-\\d{7,8})|(1[3584]\\d{9}))$";
        return tel.matches(pattern);
    }

    //将数字替换成字母
    public static String numToLetter(int i) {
        char c1 = (char) (i + 65);
        return String.valueOf(c1);
    }

    // 判断是否符合身份证号码的规范
    public static boolean isIDCard(String IDCard) {
        if (IDCard.toUpperCase().matches("(^\\d{15}$)|(^\\d{17}([0-9]|X)$)")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return 大小
     */
    public static long getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        //Log.i("hello", "blockSize = " + blockSize + ", availableBlocks = " + availableBlocks);
        long availableSize = blockSize * availableBlocks;
        //Log.i(TAG, "spaceAvailable = " + spaceAvailable + ", availableSize = " + availableSize);
        return availableSize / 1024 / 1024;//单位是MB
    }

    /**
     * 判断list的值 是否为空(List)
     * @param v list集合
     * @return  返回v
     */
    public static boolean ListisNull(List v) {
        return null == v || v.size() == 0;
    }

    /*
      * 压缩旋转并保存图片
      * */
    public static String getcompressImage(Context context, String filePath, String targetPath, int quality, String name) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Toast.makeText(context, "没有检测到存储卡", Toast.LENGTH_LONG).show();
            return null;
        }
        Bitmap bitmap = null;
        try {
            bitmap = revitionImageSize(filePath);
        } catch (IOException e) {
        }
        File outputFile = new File(Environment.getExternalStorageDirectory(), "/" + targetPath);
        FileOutputStream out = null;
        String fileName = outputFile.getAbsolutePath() + "/" + name;
        try {
            LogUtils.i("test", "outputFile.exists()" + outputFile.exists());
            if (!outputFile.exists()) {
                outputFile.mkdirs();
                //outputFile.createNewFile();
            } else if (outputFile.exists()) {
                outputFile.delete();
            }
            try {
                out = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);// 把数据写入文件
            } catch (FileNotFoundException e) {

            } finally {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        } catch (Exception e) {
        }
        return fileName;
    }

    /**
     * 上传服务器时把图片调用下面方法压缩后 保存到临时文件夹 图片压缩后小于200KB，失真度不明显
     *
     * @param path 图片地址
     * @return bitmap
     * @throws IOException 异常
     */
    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        // options.inJustDecodeBounds=true那么将不返回实际的bitmap对象，不给其分配内存空间但是可以得到一些解码边界信息即图片大小等信息
        // outHeight(图片原始高度)和 outWidth(图片的原始宽度)
        // inSampleSize表示缩略图大小为原始图片大小的几分之一
        // options.outWidth >> i(右移运算符)表示：outWidth/(2^i)
        while (true) {
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i); // 幂运算 i为几次方
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    /*
      * 拍照Uri转化为path
      * */
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null) {
            return null;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            LogUtils.i("test", "11111");
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            LogUtils.i("test", "content");
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            LogUtils.i("test", "file");
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(+90);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 将图片转换成Base64编码的字符串
     * @param path 图片路径
     * @return base64编码的字符串
     */
    public static String imageToBase64(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        InputStream is = null;
        byte[] data = null;
        String result = null;
        try {
            is = new FileInputStream(path);
            //创建一个字符流大小的数组。
            data = new byte[is.available()];
            //写入数组
            is.read(data);
            //用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }


}
