package com.waiwi.android.mylibrary.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;


public class FileUtils {
    /**
     * 复制文件或文件夹
     *
     * @param srcPath 图片地址
     * @param destDir 目标文件所在的目录
     * @return 文件
     */
    public static boolean copyGeneralFile(String srcPath, String destDir) {
        boolean flag = false;
        File file = new File(srcPath);
        if (!file.exists()) {
            System.out.println("源文件或源文件夹不存在!");
            return false;
        }
        if (file.isFile()) { // 源文件  
            System.out.println("下面进行文件复制!");
            flag = copyFile(srcPath, destDir);
        } else if (file.isDirectory()) {
            System.out.println("下面进行文件夹复制!");
            flag = copyDirectory(srcPath, destDir);
        }

        return flag;
    }

    public static boolean copyGeneralFile(File srcFile, String destDir) {
        boolean flag = false;
        if (!srcFile.exists()) {
            System.out.println("源文件或源文件夹不存在!");
            return false;
        }
        if (srcFile.isFile()) { // 源文件  
            System.out.println("下面进行文件复制!");
            flag = copyFile(srcFile, destDir);
        } else if (srcFile.isDirectory()) {
            System.out.println("下面进行文件夹复制!");
            flag = copyDirectory(srcFile, destDir);
        }
        return flag;
    }


    /**
     * 复制文件
     *
     * @param destDir 目标文件所在目录
     * @return boolean
     */
    private static boolean copyFile(File srcFile, String destDir) {
        boolean flag = false;
        if (!srcFile.exists()) { // 源文件不存在  
            System.out.println("源文件不存在");
            return false;
        }
        // 获取待复制文件的文件名  
        String fileName = srcFile.getName();
        String destPath = destDir + fileName;
        if (destPath.equals(srcFile.getAbsolutePath())) { // 源文件路径和目标文件路径重复  
            System.out.println("源文件路径和目标文件路径重复!");
            return false;
        }
        File destFile = new File(destPath);
        if (destFile.exists() && destFile.isFile()) { // 该路径下已经有一个同名文件  
            System.out.println("目标目录下已有同名文件!");
            return false;
        }

        File destFileDir = new File(destDir);
        destFileDir.mkdirs();
        try {
            FileInputStream fis = new FileInputStream(srcFile);
            FileOutputStream fos = new FileOutputStream(destFile);
            byte[] buf = new byte[1024];
            int c;
            while ((c = fis.read(buf)) != -1) {
                fos.write(buf, 0, c);
            }
            fis.close();
            fos.close();

            flag = true;
        } catch (IOException e) {
            //  
        }

        if (flag) {
            System.out.println("复制文件成功!");
        }

        return flag;
    }

    private static boolean copyFile(String srcPath, String destDir) {
        boolean flag = false;
        File srcFile = new File(srcPath);
        return copyFile(srcFile, destDir);
    }

    /**
     * @return
     */
    private static boolean copyDirectory(String srcPath, String destDir) {
        System.out.println("复制文件夹开始!");
        boolean flag = false;

        File srcFile = new File(srcPath);
        return copyDirectory(srcFile, destDir);
    }

    private static boolean copyDirectory(File srcFile, String destDir) {
        boolean flag = false;
        if (!srcFile.exists()) { // 源文件夹不存在  
            System.out.println("源文件夹不存在");
            return false;
        }
        // 获得待复制的文件夹的名字，比如待复制的文件夹为"E://dir"则获取的名字为"dir"  
        String dirName = getDirName(srcFile.getAbsolutePath());
        // 目标文件夹的完整路径  
        String destPath = destDir + File.separator + dirName;
        // System.out.println("目标文件夹的完整路径为：" + destPath);  

        if (destPath.equals(srcFile.getAbsoluteFile())) {
            System.out.println("目标文件夹与源文件夹重复");
            return false;
        }
        File destDirFile = new File(destPath);
        if (destDirFile.exists()) { // 目标位置有一个同名文件夹  
            System.out.println("目标位置已有同名文件夹!");
            return false;
        }
        destDirFile.mkdirs(); // 生成目录  

        File[] fileList = srcFile.listFiles(); // 获取源文件夹下的子文件和子文件夹
        if (fileList.length == 0) { // 如果源文件夹为空目录则直接设置flag为true，这一步非常隐蔽，debug了很久  
            flag = true;
        } else {
            for (File temp : fileList) {
                if (temp.isFile()) { // 文件  
                    flag = copyFile(temp.getAbsolutePath(), destPath);
                } else if (temp.isDirectory()) { // 文件夹  
                    flag = copyDirectory(temp.getAbsolutePath(), destPath);
                }
                if (!flag) {
                    break;
                }
            }
        }

        if (flag) {
            System.out.println("复制文件夹成功!");
        }
        return flag;
    }

    /**
     * 获取待复制文件夹的文件夹名
     *
     * @param dir
     * @return String
     */
    private static String getDirName(String dir) {
        if (dir.endsWith(File.separator)) { // 如果文件夹路径以"//"结尾，则先去除末尾的"//"
            dir = dir.substring(0, dir.lastIndexOf(File.separator));
        }
        return dir.substring(dir.lastIndexOf(File.separator) + 1);
    }

    /**
     * 删除文件或文件夹
     *
     * @param path 待删除的文件的绝对路径
     * @return boolean
     */
    public static boolean deleteGeneralFile(String path) {
        boolean flag = false;
        File file = new File(path);
        return deleteGeneralFile(file);
    }

    public static boolean deleteGeneralFile(File file) {
        boolean flag = false;
        if (!file.exists()) { // 文件不存在  
            System.out.println("要删除的文件不存在！");
        }

        if (file.isDirectory()) { // 如果是目录，则单独处理  
            flag = deleteDirectory(file.getAbsolutePath());
        } else if (file.isFile()) {
            flag = deleteFile(file);
        }

        if (flag) {
            System.out.println("删除文件或文件夹成功!");
        }

        return flag;
    }

    /**
     * 删除文件
     *
     * @param file
     * @return boolean
     */
    private static boolean deleteFile(File file) {
        return file.delete();
    }

    /**
     * 删除目录及其下面的所有子文件和子文件夹，注意一个目录下如果还有其他文件或文件夹
     * 则直接调用delete方法是不行的，必须待其子文件和子文件夹完全删除了才能够调用delete
     *
     * @param path path为该目录的路径
     */
    private static boolean deleteDirectory(String path) {
        boolean flag = true;
        File dirFile = new File(path);
        if (!dirFile.isDirectory()) {
            return flag;
        }
        File[] files = dirFile.listFiles();
        for (File file : files) { // 删除该文件夹下的文件和文件夹
            // Delete file.  
            if (file.isFile()) {
                flag = deleteFile(file);
            } else if (file.isDirectory()) {// Delete folder  
                flag = deleteDirectory(file.getAbsolutePath());
            }
            if (!flag) { // 只要有一个失败就立刻不再继续  
                break;
            }
        }
        flag = dirFile.delete(); // 删除空目录  
        return flag;
    }

    /**
     * 由上面方法延伸出剪切方法：复制+删除
     *
     * @param srcPath 图片地址
     * @param destDir 同上
     * @return 是否成功
     */
    public static boolean cutGeneralFile(String srcPath, String destDir) {
        if (!copyGeneralFile(srcPath, destDir)) {
            System.out.println("复制失败导致剪切失败!");
            return false;
        }
        if (!deleteGeneralFile(srcPath)) {
            System.out.println("删除源文件(文件夹)失败导致剪切失败!");
            return false;
        }

        System.out.println("剪切成功!");
        return true;
    }

    public static boolean cutGeneralFile(File file, String destDir) {
        if (!copyGeneralFile(file, destDir)) {
            System.out.println("复制失败导致剪切失败!");
            return false;
        }
        if (!deleteGeneralFile(file)) {
            System.out.println("删除源文件(文件夹)失败导致剪切失败!");
            return false;
        }

        System.out.println("剪切成功!");
        return true;
    }


    //以行为单位读取文件，常用于读面向行的格式化文件
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        readFileByLines(file);
    }

    public static String readFileByLines(File file) {
        BufferedReader reader = null;
        String tempString = null;
        String result = "";
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                result += tempString;
                //System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            return result.trim();
        }
    }

    //随机读取文件内容
    public static void readFileByRandomAccess(String fileName) {
        RandomAccessFile randomFile = null;
        try {
            System.out.println("随机读取一段文件内容：");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(fileName, "r");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            // 读文件的起始位置
            int beginIndex = (fileLength > 4) ? 4 : 0;
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(beginIndex);
            byte[] bytes = new byte[10];
            int byteread = 0;
            // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
            // 将一次读取的字节数赋给byteread
            while ((byteread = randomFile.read(bytes)) != -1) {
                System.out.write(bytes, 0, byteread);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    public static boolean writeLocalFile(String dir, String fileName, String msg) {
        boolean flag = false;
        if (!(dir.endsWith("/") || dir.endsWith("\\")))
            dir += "/";
        String path = Environment.getExternalStorageDirectory() + "/" + dir;
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();// 创建文件夹
//        String fileName = path+name+".txt";
        String filePath = path + fileName;
        //FileOutputStream b = null;
        BufferedWriter bw = null;
        try {
            //b = new FileOutputStream(filePath);
            bw = new BufferedWriter(new FileWriter(filePath));
//            byte[] contentInBytes = msg.getBytes();
//			b.write(contentInBytes);
            bw.write(msg);
//        	bw.flush();
//        	bw.close();
//			b.flush();
//			b.close();
            flag = true;
        } catch (FileNotFoundException e) {
            // Logger.info(UITools.class,"SD Card ",e);
            //Toast.makeText(activity, "无法写入，请检查手机sd卡", Toast.LENGTH_SHORT).show();
            Log.e("GIS", "无法写入，filePath:" + filePath);
            Log.e("GIS", "无法写入，请检查手机sd卡");
            flag = false;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
        } finally {
            //Toast.makeText(activity, "保存成功", Toast.LENGTH_SHORT).show();
            try {
                bw.flush();
                bw.close();
                return flag;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }


    }

    public static boolean deleteFile(String path) {
        if ((path == null || path.trim().length() == 0)) {
            return true;
        }


        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    @SuppressLint("ApplySharedPref")
    public static void decoderBase64File(String base64Code, String savePath) throws Exception {
        String path = "/sdcard/data/" + System.currentTimeMillis() + ".png";
        byte[] buffer = Base64.decode(base64Code.split(",")[1], Base64.DEFAULT);

//        SharedPreferences sp = UIApplication.getInstance().getSharedPreferences("runjian_icon", Context.MODE_PRIVATE);
//        if(!sp.getString(md5(path),null).equals("")){
//            SharedPreferences.Editor spEdit = sp.edit();
//            spEdit.putString(md5(path), path).commit();
//        }
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();


    }
}
