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
    public static String docxPath = "C:\\兴业材料\\医保\\青海省医保局\\编辑保存\\青海省医疗保障信息平台可行性研究报告-20190906-peng.docx";
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
        workerLogger.info("-------------demo start----------------");
        //初始化脑图中心主题
        getOrderCode("0");
        workerLogger.info("0,青海省XX可研");
        for (XWPFParagraph para : plist) {
            int levint = 0;
            String text = "";
            //写文件内容
                try {
                    //判断该段落是否设置了大纲级别
                    if (para.getCTP().getPPr().getOutlineLvl() != null) {
                        // System.out.println("getCTP()");
                        levint = Integer.parseInt(para.getCTP().getPPr().getOutlineLvl().getVal().toString()) + 1;
                        text = getOrderCode(levint+"") + "  " + para.getParagraphText().toString();
                        // System.out.print(para.getCTP().getPPr().getOutlineLvl().getVal()+",");
                        //System.out.println(para.getParagraphText());
                        //判断该段落的样式是否设置了大纲级别
                    } else if (styles.getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl() != null) {
                        // System.out.println("getStyle");
                        levint = Integer.parseInt(styles.getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal().toString())+1;
                        text = getOrderCode(levint+"") + "  " + para.getParagraphText();
                        //System.out.print(
                        //        styles.getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal()+",");
                        //System.out.println(para.getParagraphText());
                        //判断该段落的样式的基础样式是否设置了大纲级别
                    } else if (styles.getStyle(styles.getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal())
                            .getCTStyle().getPPr().getOutlineLvl() != null) {
                        // System.out.println("getBasedOn");
                        String styleName = styles.getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal();
                        levint = Integer.parseInt(styles.getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal().toString())+1;
                        text = getOrderCode(levint+"") + "  " + para.getParagraphText();
                        //System.out
                        //        .print(styles.getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal()+",");
                        //System.out.println(para.getParagraphText());

                        //没有设置大纲级别
                    } else {
                        // System.out.println("null");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if(levint>0){
                    workerLogger.info(levint + "," + text);
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
        workerLogger.info("-------------demo end----------------");
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

    /**
     * 获取标题编号
     * @param titleLvl
     * @return
     */
    private Map<String,Map<String,Object>> orderMap =new HashMap<String, Map<String,Object>>();

    private String getOrderCode(String titleLvl) {
        String order = "";

        if("0".equals(titleLvl)||Integer.parseInt(titleLvl)==8){//文档标题||正文
            return "";
        }else if(Integer.parseInt(titleLvl)>0&&Integer.parseInt(titleLvl)<8){//段落标题

            //设置最高级别标题
            Map<String,Object> maxTitleMap = orderMap.get("maxTitleLvlMap");
            if(null==maxTitleMap){//没有，表示第一次进来
                //最高级别标题赋值
                maxTitleMap = new HashMap<String, Object>();
                maxTitleMap.put("lvl", titleLvl);
                orderMap.put("maxTitleLvlMap", maxTitleMap);
            }else{
                String maxTitleLvl = maxTitleMap.get("lvl")+"";//最上层标题级别(0,1,2,3)
                if(Integer.parseInt(titleLvl)<Integer.parseInt(maxTitleLvl)){//当前标题级别更高
                    maxTitleMap.put("lvl", titleLvl);//设置最高级别标题
                    orderMap.put("maxTitleLvlMap", maxTitleMap);
                }
            }

            //查父节点标题
            int parentTitleLvl = Integer.parseInt(titleLvl)-1;//父节点标题级别
            Map<String,Object> cMap = orderMap.get(titleLvl);//当前节点信息
            Map<String,Object> pMap = orderMap.get(parentTitleLvl+"");//父节点信息

            if(0==parentTitleLvl){//父节点为文档标题，表明当前节点为1级标题
                int count= 0;
                //最上层标题，没有父节点信息
                if(null==cMap){//没有当前节点信息
                    cMap = new HashMap<String, Object>();
                }else{
                    count = Integer.parseInt(String.valueOf(cMap.get("cCount")));//当前序个数
                }
                count++;
                order = count+"";
                cMap.put("cOrder", order);//当前序
                cMap.put("cCount", count);//当前序个数
                orderMap.put(titleLvl, cMap);

            }else{//父节点为非文档标题
                int count= 0;
                //如果没有相邻的父节点信息，当前标题级别自动升级
                if(null==pMap){
                    return getOrderCode(String.valueOf(parentTitleLvl));
                }else{
                    String pOrder = String.valueOf(pMap.get("cOrder"));//父节点序
                    if(null==cMap){//没有当前节点信息
                        cMap = new HashMap<String, Object>();
                    }else{
                        count = Integer.parseInt(String.valueOf(cMap.get("cCount")));//当前序个数
                    }
                    count++;
                    order = pOrder+"."+count;//当前序编号
                    cMap.put("cOrder", order);//当前序
                    cMap.put("cCount", count);//当前序个数
                    orderMap.put(titleLvl, cMap);
                }
            }

            //字节点标题计数清零
            int childTitleLvl = Integer.parseInt(titleLvl)+1;//子节点标题级别
            Map<String,Object> cdMap = orderMap.get(childTitleLvl+"");//
            if(null!=cdMap){
                cdMap.put("cCount", 0);//子节点序个数
                orderMap.get(childTitleLvl+"").put("cCount", 0);
            }
        }
        return order;
    }

}
