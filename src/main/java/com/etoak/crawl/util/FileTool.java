package com.etoak.crawl.util;



import com.etoak.crawl.page.Page;
import org.jsoup.nodes.Document;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*  本类主要是 下载那些已经访问过的文件*/
public class FileTool {

    private static String dirPath;


    /**
     * getMethod.getResponseHeader("Content-Type").getValue()
     * 根据 URL 和网页类型生成需要保存的网页的文件名，去除 URL 中的非文件名字符
     */
    private static String getFileNameByUrl(String url, String contentType) {
        //去除 http://
        url = url.substring(7);
        //text/html 类型
        if (contentType.indexOf("html") != -1) {
            url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
            return url;
        }
        //如 application/pdf 类型
        else {
            return url.replaceAll("[\\?/:*|<>\"]", "_");// + "." +contentType.substring(contentType.lastIndexOf("/") + 1);
        }
    }

    /*
    *  生成目录
    * */
    private static void mkdir(String filePath,String fileName) {
        // 在内存中创建一个文件对象，注意：此时还没有在硬盘对应目录下创建实实在在的文件
        File f = new File(filePath,fileName);
        if(!f.exists()) {
            //  先创建文件所在的目录
            f.getParentFile().mkdirs();
            try {
                // 创建新文件
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("创建新文件时出现了错误。。。");
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存网页字节数组到本地文件，filePath 为要保存的文件的相对地址
     */

    public static void saveToLocal(Page page) {
        if (dirPath == null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            String date = df.format(new Date())+"\\";   //获取当前时间
            String s = page.getUrl().substring(7);      //获取网址名称
            dirPath = "E:\\Crawl\\"+date+"\\"+s+"\\";   //定义文件存储位置
        }
        String fileName =  getFileNameByUrl(page.getUrl(), page.getContentType()) ;
        String filePath = dirPath;
        mkdir(filePath,fileName);
        byte[] data = page.getContent();
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath,fileName)));
            for (int i = 0; i < data.length; i++) {
                out.write(data[i]);
            }
            out.flush();
            out.close();
            //System.out.println("文件："+ fileName + "已经被存储在"+ filePath  );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
