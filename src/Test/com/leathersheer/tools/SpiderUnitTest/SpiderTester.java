package com.leathersheer.tools.SpiderUnitTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leathersheer.tools.SpiderUnit.Listeners.ChengDuHouseStatement;
import com.leathersheer.tools.SpiderUnit.Shuaigay.Beans.SMArticleBean;
import com.leathersheer.tools.SpiderUnit.Shuaigay.Beans.SMArticlePostBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpiderTester {
    public static final Logger spiderTesterLogger = LogManager.getLogger();

    public static void main(String[] args) throws Exception {
        new SpiderTester().doGrab();
    }

    public void  compose(String url,ArrayList<SMArticlePostBean> articlePostBeanList,SMArticleBean smArticle){
        SimpleDateFormat format = new SimpleDateFormat("YYYYMMdd");
        String date = format.format(new Date());
        File file = new File(url+smArticle.title+"-已至"+smArticle.totalNumofPosts+"-threadId_"+smArticle.postId+"-Date_"+date+".txt");
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for(SMArticlePostBean beans:articlePostBeanList){
                if(beans.author.equals(smArticle.author)){
                    smArticle.content = smArticle.content +"\n 第" + beans.floor + "楼\n   " +
                            beans.content;
                }else{
                    smArticle.comment=smArticle.comment+beans.floor+"楼"+beans.author+" : "+beans.content+"\n   ";
                }
            }
            smArticle.content = smArticle.content.replaceAll("  \\n","").replaceAll(" \\n","").replaceAll("\\n\\n","");
            writer.write(smArticle.content );
            writer.write("\n评论区域>>>>>>>>>>\n");
            smArticle.comment = smArticle.comment.replaceAll("  \\n","").replaceAll(" \\n","").replaceAll("\\n\\n","");
            writer.write(smArticle.comment.replace("  \\n",""));
            writer.close();
        }catch(Exception e){
            System.out.println("写入文件异常：---"+e.getMessage());
        }

    }

    public void doGrab() throws Exception{
        String entrenceUrl = "https://www.shuaigay6.com/thread-1486373-1-1.html";
        SMArticleBean smArticle= new SMArticleBean();
        smArticle.postId= (entrenceUrl.split("-"))[1];
        ArrayList<SMArticlePostBean> postList = new ArrayList<>();
        Document doc = getDoc("D:\\TEMP\\article.html");
        postList.addAll(convertSinglePage(doc,smArticle));
        for(int currentPage=2;currentPage<smArticle.totalNumOfPages;currentPage++){
            String nextUrl = entrenceUrl.replace("-1-1","-"+currentPage+"-1");
            postList.addAll(convertSinglePage(doc,smArticle));
        }
        smArticle.totalNumofPosts=postList.size()+"";
        compose("D:/TEMP/",postList,smArticle);
    }

    public Document getDoc(String url) throws Exception{
        File infile = new File(url);
        Document doc = null;
        if (infile.isFile() && infile.exists()) { // 判断文件是否存在
            // 进行body元素提取
            doc = Jsoup.parse(infile, "UTF-8");
        }
        return doc;
    }

    public ArrayList<SMArticlePostBean> convertSinglePage(Document doc,SMArticleBean smArticle){
        ArrayList<SMArticlePostBean> postList = new ArrayList<>();
        //开始处理文档内容
        Elements elements = doc.select("div[id=pt] a[href*=thread]");
        //文章标题、总页数、链接获取
        smArticle.title = elements.get(0).text();
        smArticle.href = elements.get(0).attr("href");
        elements = doc.select("div[id=pgt] a[class=last]");
        String totalNumOfPages = "1";
        if(elements.size()==1){
            totalNumOfPages = elements.get(0).text();
        }else{
            elements = doc.select("div[id=pgt] div[class=pg] span");
            totalNumOfPages= elements.get(0).text();
        }
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(totalNumOfPages);
        smArticle.totalNumOfPages=Integer.valueOf(m.replaceAll("").trim());

        //获取贴子内容的post
        elements = doc.select("div[id~=^post_[0-9]+]"); //获取所有贴子post结构
        for(Element element : elements){
            SMArticlePostBean post = new SMArticlePostBean();
            post.floor=element.select("div[class=pi] strong em").text(); //获取贴子所在楼层
            Element contentelement=element.select("td[class=t_f]").get(0);  //获取贴子内容 start
            contentelement.select("font,span").remove();  //移除文章中乱码
            contentelement.select("br").append("\\n    \t ");
            post.content=contentelement.html().replaceAll("\\\\n", "\n ");
            post.content=Jsoup.clean(post.content, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
            //以下获取本层作者
            post.author=element.select("div[class=i y] strong a").text();
            //以一楼作者作为全文作者
            if(post.floor.equals("1")){
                smArticle.author= post.author;
            }
            postList.add(post);
        }
        return postList;
    }
}
