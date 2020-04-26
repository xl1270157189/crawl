package com.etoak.crawl.main;

import com.etoak.crawl.link.LinkFilter;
import com.etoak.crawl.link.Links;
import com.etoak.crawl.page.Page;
import com.etoak.crawl.page.PageParserTool;
import com.etoak.crawl.page.RequestAndResponseTool;
import com.etoak.crawl.util.FileTool;
import com.etoak.crawl.util.Mail;
import com.etoak.crawl.util.SMS;
import jdk.nashorn.internal.objects.annotations.Function;
import org.jsoup.select.Elements;

import javax.xml.bind.Element;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MyCrawler {

    /**
     * 使用种子初始化 URL 队列
     *
     * @param seeds 种子 URL
     * @return
     */
    private void initCrawlerWithSeeds(String[] seeds) {
        for (int i = 0; i < seeds.length; i++){
            Links.addUnvisitedUrlQueue(seeds[i]);
        }
    }


    /**
     * 抓取过程
     *
     * @param seeds
     * @return
     */
    public void crawling(String[] seeds) throws ParseException {

        //初始化 URL 队列
        initCrawlerWithSeeds(seeds);

        //定义过滤器，提取以 http://www.baidu.com 开头的链接
//        LinkFilter filter = new LinkFilter() {
//            public boolean accept(String url) {
//                if (url.startsWith("http://www.baidu.com"))
//                    return true;
//                else
//                    return false;
//            }
//        };

        //循环条件：待抓取的链接不空且抓取的网页不多于 1000
        while (!Links.unVisitedUrlQueueIsEmpty()  && Links.getVisitedUrlNum() <= 1000) {

            //先从待访问的序列中取出第一个；
            String visitUrl = (String) Links.removeHeadOfUnVisitedUrlQueue();
            if (visitUrl == null){
                continue;
            }

            //根据URL得到page;
            Page page = RequestAndResponseTool.sendRequstAndGetResponse(visitUrl);

            //对page进行处理： 访问DOM的某个标签
            Elements es = PageParserTool.select(page,"a");
            if(!es.isEmpty()){
                for (int i = 0; i<es.size();i++){
                   if(!es.get(i).attributes().get("title").equals("")){
                       String s = es.get(i).attributes().get("title");
                       String s1[] = s.split(" ");
                       int a = s1.length;
                       for(String s2 : s1){
                           if(s2.indexOf("更新时间")!=-1){
                               String s3[] = s2.split("：");
                               //读取文件内容
                               String lastUpdateTime = IOBuffered("d:\\lastUpdateTime.txt",0,null);
                               SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm");
                               Date date1=sdf.parse(lastUpdateTime);
                               Date date2=sdf.parse(s3[1]+" "+s1[a-1]);
                               if(date1.before(date2)){
                                   lastUpdateTime = s3[1]+" "+s1[a-1];
                                   //存文件
                                   IOBuffered("d:\\lastUpdateTime.txt",1,lastUpdateTime);
//                                   System.out.println("123123");
                                   SMS.sendSmss();  //发送短信

                                   String adderss = "849437676@qq.com,1270157189@qq.com";
                                   String subject = "小说提示";
                                   String test = "您当前正在体验小说更新邮箱服务，您追踪的小说已更新，如非本人操作，请忽略此邮件!";
                                   Mail.SendMailText(adderss,subject,test); //发送邮件
                               }

                           }
                       }
                   }

                }

//                System.out.println("下面将打印所有a标签： ");
//                System.out.println(es);
            }else {
                // 访问异常时

            }

//            //将保存文件
//            FileTool.saveToLocal(page);
//
//            //将已经访问过的链接放入已访问的链接中；
//            Links.addVisitedUrlSet(visitUrl);
//
//            //得到超链接
//            Set<String> links = PageParserTool.getLinks(page,"img");
//            for (String link : links) {
//                Links.addUnvisitedUrlQueue(link);
//               // System.out.println("新增爬取路径: " + link);
//            }
        }
    }

    //文件读写
    public  String IOBuffered(String url,Integer type,String file) {
        try {
            //读取文件(缓存字节流)
            BufferedInputStream in = null;
            //写入相应的文件
            BufferedOutputStream out = null;
            if(0 == type){
                //读取文件(缓存字节流)
                in = new BufferedInputStream(new FileInputStream(url));
                //读取数据
                //一次性取多少字节
                byte[] bytes = new byte[2048];
                //接受读取的内容(n就代表的相关数据，只不过是数字的形式)
                int n = -1;

                //循环取出数据
                while ((n = in.read(bytes,0,bytes.length)) != -1) {
                    //转换成字符串
                    String str = new String(bytes,0,n,"GBK");
                    //关闭流
                    in.close();
                    return str;
                }
            }else{
                if("".equals(file) || file == null || file.length() == 0){
                    return "请传入需要保持的参数！";
                }
                out = new BufferedOutputStream(new FileOutputStream(url));
                out.write(file.getBytes(),0,file.getBytes().length);
                //清楚缓存
                out.flush();
                out.close();
                return "上传成功!";
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return "";
    }



    //main 方法入口
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                MyCrawler crawler = new MyCrawler();
                String [] s = {"http://book.zongheng.com/showchapter/672340.html"};
                try {
                    crawler.crawling(s);
                }catch (Exception e){
                    System.out.println(e);
                }
            }
         }, 1000, 30*60*1000);



//        String a = crawler.IOBuffered("d:\\1.txt",1,"456789");
//        System.out.println(a);

    }






}
