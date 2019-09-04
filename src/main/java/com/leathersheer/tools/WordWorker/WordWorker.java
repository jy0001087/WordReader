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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WordWorker implements Workers {

    public static final Logger workerLogger = LogManager.getLogger();
    public static CTStyles wordStyles = null;

    static {
        XWPFDocument template;
        try {
            // 读取模板文档
            template = new XWPFDocument(new FileInputStream("D:/format.docx"));
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
        //worker.getTitles();
        ArrayList<String> modlist = worker.readMod();
        worker.creatDocx(modlist);
        //worker.readMod();
    }


    public void getTitles() throws Exception {
        File binFile = new File("E:/1.docx");
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
                    System.out.println(para.getParagraphText());
                    System.out.println(para.getCTP().getPPr().getOutlineLvl().getVal());

                    //判断该段落的样式是否设置了大纲级别
                } else if (styles.getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl() != null) {
                    // System.out.println("getStyle");
                    System.out.println(para.getParagraphText());
                    System.out.println(
                            styles.getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal());

                    //判断该段落的样式的基础样式是否设置了大纲级别
                } else if (styles.getStyle(styles.getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal())
                        .getCTStyle().getPPr().getOutlineLvl() != null) {
                    // System.out.println("getBasedOn");
                    System.out.println(para.getParagraphText());
                    String styleName = styles.getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal();
                    System.out
                            .println(styles.getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal());

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

    public void creatDocx( ArrayList<String> modlist) throws Exception{
        XWPFDocument docx = new XWPFDocument();
        XWPFStyles styles = docx.createStyles();
        styles.setStyles(wordStyles);

        XWPFParagraph para = docx.createParagraph();
        para.setStyle("1");
        XWPFRun run = para.createRun();
        run.setText("大标题");

        FileOutputStream docxout = new FileOutputStream("D:/2.docx");
        docx.write(docxout);
        docxout.close();
    }

    public ArrayList<String> readMod() throws Exception {

        ArrayList<String> modlist = new ArrayList<String>();

        InputStream mod = new FileInputStream("D:/1.txt");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(mod));
        String content = new String("");
        int i = 1;
        String context=null;
        String level;
        while((content = reader.readLine())!=null) {
            workerLogger.trace("read file get content is i=" + i +"：" + content);
            if(i%2==0){
                level = content;
                workerLogger.trace("level now is "+level);
                modlist.add(level+","+context);
            }else {
                context = content;
                workerLogger.trace("content now is "+ content);
            }
            i++;
        }

        workerLogger.trace("----------------------------------modlist------------------------------start");
        for(String tmp:modlist){
            workerLogger.trace(tmp);
        }
        workerLogger.trace("----------------------------------modlist------------------------------end");
        return modlist;
    }

}
