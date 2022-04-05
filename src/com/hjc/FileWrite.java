package com.hjc;

import java.io.*;

public class FileWrite {
    public static void fileWrite(String filename,String content)throws Exception{
        FileOutputStream fos;
        //convert string to byte
        byte [] data  = content.getBytes();
        File file = new File(filename);

        //create the file
        if (!file.exists()) {
            file.createNewFile();
            //创建FileOutputStream对象，写入内容
            fos = new FileOutputStream(file);
        }
        else{
            fos = new FileOutputStream(file,true);
        }
        //write content
        fos.write(data);
        fos.close();
    }



}

