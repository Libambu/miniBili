package com.miniBili.utils;

import java.io.*;

public  class FileUtils {

    /**
     * 复制一个目录及其子目录、文件到另外一个目录
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdirs();
            }
            String files[] = src.list();
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // 递归复制
                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;

            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }

    /**
     * 递归删除文件夹
     * @param file
     */
    public static void deleteFile(File file){
        if(file.exists()){
            if(file.isFile()){
                file.delete();
            }else {
                File[] files = file.listFiles();
                for(File f : files){
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        String src = "E:\\program\\workspace\\miniBili\\file\\temp\\2025-09-04\\bMCWg8UUMj_941wiW6AbY";
        String tar = "E:\\program\\workspace\\miniBili\\file\\video\\2025-09-04\\bMCWg8UUMj_941wiW6AbY";
        deleteFile(new File(src));
    }

}
