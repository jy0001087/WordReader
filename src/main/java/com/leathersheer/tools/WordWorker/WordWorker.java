package com.leathersheer.tools.WordWorker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFStyles;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WordWorker implements Workers {
    public static final Logger workerLogger = LogManager.getLogger();
    public static void main(String[] args) throws Exception{
        workerLogger.trace("logger start");
        WordWorker worker= new WordWorker();
        worker.getTitles();
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

            ArrayList<String> al = new ArrayList<String>();

            try {
                al.add(styles.getStyle(para.getStyle()).getCTStyle().getName().getVal());
            } catch (Exception e) {
                // TODO: handle exception
            }
            al.add(para.getParagraphText());
            workerLogger.info(j++ + ":" + al);

        }
    }
}
