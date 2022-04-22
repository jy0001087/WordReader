package com.leathersheer.tools.SpiderUnit.Shuaigay;

import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.Listeners.DBUnits.ChengDuHouseBean;
import com.leathersheer.tools.SpiderUnit.Listeners.DBUnits.ChengDuHouseMapper;
import com.leathersheer.tools.SpiderUnit.Shuaigay.Beans.SMArticleBean;
import com.leathersheer.tools.SpiderUnit.Shuaigay.Beans.SMArticlePostBean;
import com.leathersheer.tools.SpiderUnit.Shuaigay.Beans.ShuaigayMapper;
import com.leathersheer.tools.SpiderUnit.Shuaigay.Beans.SociaGameBean;
import com.leathersheer.tools.SpiderUnit.SpiderServer.Spider;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShuaigayFetcher {
    public static final Logger shuaigayLogger = LogManager.getLogger();

    /**
     * 模拟登录模块，返回cookies计入httpclient的 context中，供后续直接使用。
     * @param url
     * @param spider
     * @param step
     * @return
     */
    public Document doFetch(String url,Spider spider,String step){
        Document doc = null;
        spider.setOriginUrl(url);

        switch(step) {
            case "login":
                Map<String, String> para = new HashMap<String, String>();
                para.put("username", "jy0001087");
                para.put("password", "woai28552@");
                spider.setHttpClient();
                spider.setPara(para);
                spider.getCookieswithPara();
            break;
            case "fetch":
                doc = spider.getContentwithCookies(Document.class);
        }
        try{
            Thread.sleep(5000);
        }catch (Exception e){
            shuaigayLogger.error("线程休眠异常",e);
        }
        return  doc;
    }

    /**
     * json字串转化为ArrayList，供下一步读取做分解。
     * @param subJson
     * @return
     */
    public ArrayList<Map<String,String>> getParainfoFromJson(JSONObject subJson){
        ArrayList paraList = new ArrayList();
        Iterator it = subJson.keys();
        while(it.hasNext()){
            Map<String ,String > jsoutResoutMap = new HashMap<>();
            String key  = (String)it.next();
            jsoutResoutMap.put("beanParaName",key);
            Object interValue = subJson.get(key);
            if(interValue instanceof String){
                String value = interValue.toString();
            }else if(interValue instanceof JSONObject){
                JSONObject value = subJson.getJSONObject(key);
                Iterator it1 = value.keys();
                while(it1.hasNext()){
                    String location = (String)it1.next();
                    String paraValue = (String)value.get(location);
                    jsoutResoutMap.put("beanParaLocation",location);
                    jsoutResoutMap.put("beanParaValue",paraValue);
                }
            }
            paraList.add(jsoutResoutMap);
        }
        return paraList;
    }

    /**
     * 另类交友抓取服务入口
     */
    public void doSocialGameGrab(){
        ShuaigayFetcher sf=new ShuaigayFetcher();
        Spider spider= new Spider();
        String url = "https://www.shuaigay6.com/member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LhLvE&inajax=1";
        sf.doFetch(url,spider,"login");
        //url = "https://www.shuaigay6.com/forum.php?mod=viewthread&tid=1536090";
        url = "https://www.shuaigay6.com/forum-125-1.html";  // SocialGame-url
        Document doc=sf.doFetch(url,spider,"fetch");
        Map<String,ArrayList<String>> keywordMap = new HashMap<>();
        ArrayList<String> kewordList = new ArrayList<String>() {{
            add("北京");add("1m");add("锁");add("粗口");add("sub");add("dom");add("羞辱");add("丝袜");add("控制");add("乳胶");    }};
        keywordMap.put("postTitle",kewordList);
        JSONObject tarJsobj = new JSONObject("{\"uid\":{\"tbody[id~=^normalthread]\":\"id\"},\"url\":{\"tbody[id~=^normalthread] a[class=s xst]\":\"href\"},\"postTitle\":{\"tbody[id~=^normalthread] a[class=s xst]\":\"text\"}}"
        );
        ArrayList<SociaGameBean> beanList = sf.fileTitleList(keywordMap,tarJsobj,doc);
        sf.saveToDB(beanList);
    }
    /**
     * 另类交友专用模块，分解交友部份的html，获取socialGameBean
     * @param keywordMap
     * @param target
     * @param doc
     * @return
     */
    public ArrayList<SociaGameBean> fileTitleList(Map<String,ArrayList<String>> keywordMap, JSONObject target , Document doc ){
        ArrayList<SociaGameBean> beanList = new ArrayList<>();
        ArrayList<Map<String,String>> paraList = this.getParainfoFromJson(target);
        Map<String,String> uidMap = paraList.get(0);
        if(!(uidMap.get("beanParaName").equals("uid"))){
            shuaigayLogger.error("json内容异常，未找到uid内容");
            return null;
        }
        Elements uidElements=doc.select(uidMap.get("beanParaLocation"));
        for(Element element : uidElements){
            int addFlag = 0 ;
            SociaGameBean bean = new SociaGameBean();
            for(Map<String,String> jsonMap:paraList) {
                Elements finalElements = element.select(jsonMap.get("beanParaLocation"));
                Element finalElement = finalElements.get(0);
                String currentParaName = jsonMap.get("beanParaName");
                switch (currentParaName) {
                    case "uid":
                        bean.uid = finalElement.attr(jsonMap.get("beanParaValue"));
                        break;
                    case "url":
                        bean.url = finalElement.attr(jsonMap.get("beanParaValue"));
                        break;
                    case "postTitle":
                        bean.postTitle = finalElement.text();
                        ArrayList<String> keywordList = keywordMap.get("postTitle");
                        for(String str : keywordList){
                            if(bean.postTitle.contains(str)){
                                addFlag =1;
                            }
                        }
                        break;
                }
            }
            if(addFlag == 1) {
                beanList.add(bean);
            }
        }
        shuaigayLogger.info("finish");
        return beanList;
    }
    /**
     * 另类交友数据存储
     * @param beanList
     */
    public void saveToDB(ArrayList<SociaGameBean> beanList){
        DBTools db = new DBTools();
        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            ShuaigayMapper mapper = sqlsession.getMapper(ShuaigayMapper.class);
            try {
                db.dblogger.info("开始插入数据");
                for (SociaGameBean bean : beanList) {
                    mapper.insertColumn(bean);
                }
            } catch (Exception e) {
                db.dblogger.error("数据插入异常：");
                db.dblogger.error(e.toString(), e);
            }
            sqlsession.commit();
        }
    }

    public void doArticleGrab(String entrenceUrl){
        Spider spider= new Spider();
        entrenceUrl="https://www.shuaigay6.com/thread-"+entrenceUrl+"-1-1.html"; //拼接url
        String url = "https://www.shuaigay6.com/member.php?mod=logging&action=login&loginsubmit=yes&loginhash=LhLvE&inajax=1";
        this.doFetch(url,spider,"login");

        SMArticleBean smArticle= new SMArticleBean();

        ArrayList<SMArticlePostBean> postList = new ArrayList<>();
        smArticle.postId= (entrenceUrl.split("-"))[1];
        Document doc=this.doFetch(entrenceUrl,spider,"fetch");

        postList.addAll(convertSinglePage(doc,smArticle));
        for(int currentPage=2;currentPage<smArticle.totalNumOfPages;currentPage++){
            String nextUrl = entrenceUrl.replace("-1-1","-"+currentPage+"-1");
            doc=this.doFetch(nextUrl,spider,"fetch");
            postList.addAll(convertSinglePage(doc,smArticle));
            try{
                Thread.sleep(1000);
            }catch(Exception e){
                shuaigayLogger.error("线程休眠异常",e);
            }
            shuaigayLogger.info("-----已处理至页码："+currentPage);
        }
        smArticle.totalNumofPosts=postList.size()+"";
        //写文件
        File dir = new File("NovelDir");
        dir.mkdir();
        compose("NovelDir/",postList,smArticle);
        return;
    }

    public ArrayList<SMArticlePostBean> convertSinglePage(Document doc, SMArticleBean smArticle){
        ArrayList<SMArticlePostBean> postList = new ArrayList<>();
        //开始处理文档内容
        Elements elements = doc.select("div[id=pt] a[href*=thread]");
        if(smArticle.totalNumOfPages==null || smArticle.totalNumOfPages.equals("")) {
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
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(totalNumOfPages);
            smArticle.totalNumOfPages = Integer.valueOf(m.replaceAll("").trim());
        }
        //获取贴子内容的post
        elements = doc.select("div[id~=^post_[0-9]+]"); //获取所有贴子post结构
        for(Element element : elements){
            SMArticlePostBean post = new SMArticlePostBean();
            post.floor=element.select("div[class=pi] strong em").text(); //获取贴子所在楼层
            Element contentelement=element.select("td[class=t_f]").get(0);  //获取贴子内容 start
            contentelement.select("font,span").remove();  //移除文章中乱码
            contentelement.select("br").append("\\n    \t ");
            post.content=contentelement.html().replaceAll("\\\\n", "\n ");
            post.content= Jsoup.clean(post.content, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
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
            smArticle.content = smArticle.content.replaceAll("  \\n","").replaceAll(" \\n","").replaceAll("\\n\\n","")
                    .replaceAll("白[色]*[的]*[棉]*袜[子]*","军绿色及膝丝袜").replaceAll("黑[色]*[的]*[棉]*袜[子]*","黑色透明绅士丝袜").replaceAll("棉[的]*袜[子]*","戴了袜带固定的绅士黑丝袜")
                    .replaceAll("[黑,白]*[色]*精英袜[子]*","酒红色过膝锦纶透明丝袜").replaceAll("臭袜子","透明尿味臭丝袜");//xp替换
            writer.write(smArticle.content );
            writer.write("\n评论区域>>>>>>>>>>\n");
            smArticle.comment = smArticle.comment.replaceAll("  \\n","").replaceAll(" \\n","").replaceAll("\\n\\n","");
            writer.write(smArticle.comment.replace("  \\n",""));
            writer.close();
        }catch(Exception e){
            System.out.println("写入文件异常：---"+e.getMessage());
        }

    }

    /**
     * 测试模块
     * @param args
     */
    public static void main(String[] args){
        new ShuaigayFetcher().doArticleGrab("1098340");
    }

}
