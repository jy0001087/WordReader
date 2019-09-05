package com.leathersheer.tools.WordWorker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

public class WordWorker implements Workers {

    public static final Logger workerLogger = LogManager.getLogger();
    public static CTStyles wordStyles = null;
    public static String docxPath = "C:\\兴业材料\\医保\\青海省医保局\\编辑保存\\青海省医疗保障信息平台可行性研究报告-20190902-peng-gai.docx";
    static {
        XWPFDocument template;
        try {
            // 读取模板文档
            final URL formatResource = Workers.class.getClassLoader().getResource("format.docx");
            template = new XWPFDocument(new FileInputStream(formatResource.getFile()));
            // 获得模板文档的整体样式
            wordStyles = template.getStyle();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        workerLogger.trace("logger start");
        WordWorker worker= new WordWorker();
        worker.getTitles(docxPath);
        //worker.modworker();  //读取demo.txt生成word文档。
    }


    public void getTitles(String filepath) throws Exception {
        File binFile = new File(filepath);
        FileInputStream inputstream = new FileInputStream(binFile);
        workerLogger.trace("docx status ： " + inputstream.available());
        XWPFDocument doc = new XWPFDocument(inputstream);
        List<XWPFParagraph> plist = doc.getParagraphs();
        //获取doc样式
        XWPFStyles styles = doc.getStyles();

        int j = 0;
        for (XWPFParagraph para : plist) {
            try {
                //判断该段落是否设置了大纲级别
                if (para.getCTP().getPPr().getOutlineLvl() != null) {
                    // System.out.println("getCTP()");
                    System.out.print(para.getCTP().getPPr().getOutlineLvl().getVal()+",");
                    System.out.println(para.getParagraphText());
                    //判断该段落的样式是否设置了大纲级别
                } else if (styles.getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl() != null) {
                    // System.out.println("getStyle");
                    System.out.print(
                            styles.getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal()+",");
                    System.out.println(para.getParagraphText());

                    //判断该段落的样式的基础样式是否设置了大纲级别
                } else if (styles.getStyle(styles.getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal())
                        .getCTStyle().getPPr().getOutlineLvl() != null) {
                    // System.out.println("getBasedOn");
                    String styleName = styles.getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal();
                    System.out
                            .print(styles.getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal()+",");
                    System.out.println(para.getParagraphText());

                    //没有设置大纲级别
                } else {
                    // System.out.println("null");
                }
            }catch (Exception e) {
                // TODO: handle exception
            }
            /**
            ArrayList<String> al = new ArrayList<String>();
            try {
                String paraName = styles.getStyle(para.getStyle()).getCTStyle().getName().getVal();
                if (paraName.contains("heading")) {
                    al.add(styles.getStyle(para.getStyle()).getCTStyle().getName().getVal());
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            al.add(para.getParagraphText());
            if (!(al.isEmpty())) {
                workerLogger.info(j++ + ":" + al);
            }
            **/
        }
    }


     public void creatDocx( String style,String text,XWPFDocument docx) throws Exception{
     //

         FileOutputStream docxout = new FileOutputStream("target.docx");

        //写文件核心，先设置格式，再设置内容
        int intstyle=Integer.parseInt(style)+1;
        XWPFParagraph para = docx.createParagraph();
        para.setStyle(intstyle+"");
        XWPFRun run = para.createRun();
        run.setText(text);
/**
       //写文件核心，先设置格式，再设置内容
        XWPFParagraph para1 = docx.createParagraph();
        para1.setStyle("2");
        XWPFRun run1 = para1.createRun();
        run1.setText("2标题");

        XWPFParagraph para2 = docx.createParagraph();
        para2.setStyle("2");
        XWPFRun run2 = para2.createRun();
        run2.setText("2标题");

        XWPFParagraph para3 = docx.createParagraph();
        para3.setStyle("1");
        XWPFRun run3 = para3.createRun();
        run3.setText("大标题");
 **/
        //写文件
        docx.write(docxout);

        docxout.close();
    }

    public void modworker() throws Exception {

        ArrayList<String> modlist = new ArrayList<String>();
        XWPFDocument docx = new XWPFDocument();
        XWPFStyles styles = docx.createStyles();
        styles.setStyles(wordStyles);

        URL demofileurl = this.getClass().getClassLoader().getResource("demo.txt");
        InputStream mod = new FileInputStream(demofileurl.getFile());
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(mod));
        String content = new String("");
        int i = 1;
        workerLogger.trace("----------------------------------modlist------------------------------start");
        while ((content = reader.readLine()) != null) {
            //数据清洗同时录入modlist
            workerLogger.trace("read file get content is i=" + i + "：" + content);
            String contentarray[] = content.split(",");

            //数据处理，1.标题题级+1给中心主题留空间。
            //        2.标题内容为空的直接退出循环。

            try {
                if (contentarray[1].length() <= 0) {
                    //判断是否为空
                }
            }catch (NullPointerException e) {
                continue;
            }catch (ArrayIndexOutOfBoundsException e){
                workerLogger.error("第"+i+"行发生异常,标题内容为空");
                continue;
            }
            int levelint = Integer.parseInt(contentarray[0]) + 1;
            String levelstring = levelint+"";
            String text = contentarray[1];
            creatDocx(levelstring,text,docx);
            Thread.sleep(2000);
            i++;
        }
        workerLogger.trace("----------------------------------modlist------------------------------end");
        /**
         if(i%2==0){
         level = content;
         workerLogger.trace("level now is "+level);
         if(content.length()>0){
         modlist.add(level+","+context);
         }
         }else {
         context = content;
         workerLogger.trace("content now is "+ content);
         }


        workerLogger.trace("----------------------------------modlist------------------------------start");
        for(String tmp:modlist){
            workerLogger.trace(tmp);
            String array[] = tmp.split(",");
            String levell=array[0];
            String text=array[1];
            creatDocx(levell,text,docx);
            Thread.sleep(2000);
        }
        workerLogger.trace("----------------------------------modlist------------------------------end");

         **/
        }

}
