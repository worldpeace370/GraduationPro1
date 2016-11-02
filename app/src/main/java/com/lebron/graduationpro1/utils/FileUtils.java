package com.lebron.graduationpro1.utils;

import java.io.File;

/**用于对文件操作的类
 * Created by lebron on 16-10-31.
 * Contact by wuxiangkun2015@163.com
 */

public class FileUtils {

    /**
     * 根据文件返回文件大小,单位为M,遇到目录递归调用
     * @param file 传入的File 对象
     * @return 返回该文件(可以包含多个子目录,子文件)的大小
     */
    public static double getFileSize(File file){
        //如果文件存在
        if (file.exists()){
            //如果文件是目录形式
            if (file.isDirectory()){
                File [] files = file.listFiles();
                double size = 0.0;
                for (File childFile: files) {
                    size += getFileSize(childFile);
                }
                return size;
            }else {
                return (double) file.length() / 1024 / 1024;
            }
        }else {
            return 0.0;
        }
    }
}
