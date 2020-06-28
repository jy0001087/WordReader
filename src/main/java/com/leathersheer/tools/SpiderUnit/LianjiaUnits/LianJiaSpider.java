package com.leathersheer.tools.SpiderUnit.LianjiaUnits;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.leathersheer.tools.SpiderUnit.*;
import com.leathersheer.tools.SpiderUnit.DBUnits.DBTools;
import com.leathersheer.tools.SpiderUnit.PubToolUnit.GaoDeUnit;
import com.leathersheer.tools.SpiderUnit.SpiderBeans.HouseBean;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LianJiaSpider extends Spider {
    public static final Logger LianjiaLogger = LogManager.getLogger();

    public static void main(String[] args) {
        LianJiaSpider lj = new LianJiaSpider();
        lj.engine("https://bj.lianjia.com/ditiezufang/li46107350/rt200600000001/");
    }

    public String engine(String starturl) {
        LianjiaLogger.trace("-----------LianjiaSpider start!!");
        LianJiaSpider spider = new LianJiaSpider();

        String targetPage = starturl;
        while (!targetPage.equals("Finished")) {

            Document doc = spider.getContent(targetPage, Document.class);
            spider.saveHouse(spider.getHouse(doc));// 北京房源搜索-整租

            Elements pageEles = doc.getElementsByClass("content__pg"); // 页面分析得来“下一页”元素
            if (pageEles.isEmpty()) {
                LianjiaLogger.error("未找到目标class内容:content__pg");
                targetPage = "Finished";
            } else {
                LianjiaLogger.trace("找到元素：" + pageEles);
                String pagePatten = pageEles.attr("data-url");
                String pageTotal = pageEles.attr("data-totalPage");
                String pageCur = pageEles.attr("data-curPage");
                LianjiaLogger.trace("操作参数为"+pagePatten+pageTotal+pageCur);
                Integer targetPageInt = getDigit(pageCur) + 1;
                pagePatten.replace("{page}", "pg" + targetPageInt);
                LianjiaLogger.trace("下一页是："+pagePatten);
                if (targetPageInt == Integer.getInteger(pageTotal)) {
                    targetPage = "Finished";
                }
            }
        }
        return "Success";

    }

    /**
     * 提取房源地址信息
     */
    public LinkedList<HouseBean> getHouse(Document doc) {
        Elements elements = doc.getElementsByClass("content__list--item");// 筛选页面中包含的房源信息块
        Elements addresselements = new Elements();
        GaoDeUnit gaode = new GaoDeUnit();

        LinkedList<HouseBean> houselist = new LinkedList<>();
        if (elements.isEmpty()) {
            LianjiaLogger.error("未找到目标class内容:content__list--item");
        } else {
            for (Element element : elements) {
                HouseBean house = new HouseBean();
                // 获取房源编码
                house.house_code = element.attr("data-house_code");
                addresselements = element.getElementsByClass("content__list--item--des");// 筛选房源信息中的具体信息字符串
                if (addresselements.size() == 1) {
                    LianjiaLogger.debug("原页面房屋情况字串：" + addresselements.text());
                    String[] addressArray = addresselements.text().split("/");
                    for (String text : addressArray) {
                        int house_element_order = 0;
                        house_element_order = text.matches(".+-+.+") ? 1 : house_element_order;
                        house_element_order = text.matches(".+㎡+.+") ? 2 : house_element_order;
                        house_element_order = text.matches(".+室+.+") ? 3 : house_element_order;
                        house_element_order = text.matches("东?南?西?北?") ? 4 : house_element_order;
                        house_element_order = text.matches(".+层+.+") ? 5 : house_element_order;
                        switch (house_element_order) {
                            case 1:
                                house.address = text.replace(" ", ""); // 地址
                                break;
                            case 2:
                                house.area = this.getDigit(text); // 面积(面积格式为 XX平方米，转为数字面积)
                                break;
                            case 3:
                                house.housetype = text.replace(" ", ""); // 户型
                                break;
                            case 4:
                                house.oriented = text.replace(" ", ""); // 朝向
                                break;
                            case 5:
                                house.floor = text.replace(" ", ""); // 楼层
                                break;
                            default:
                                break;
                        }
                    }

                } else {
                    LianjiaLogger.error("单条content__list--item中存在两个地址信息，查看网页源代码搂错误");
                    return houselist;
                }
                // 获取价格部分（价格格式为 “XXXX 元/月”）
                house.price = this.getDigit(element.getElementsByClass("content__list--item-price").text());
                // 获取高德location信息
                house.gdlocation = gaode.getLocate("北京", house.address);
                houselist.add(house);
                LianjiaLogger.debug("加入houselist的房源地址：" + house.address + "  面积 ：" + house.area + " 价格：" + house.price
                        + " 楼层：" + house.floor + "  GDlocation :" + house.gdlocation + "房源编码 ： " + house.house_code);
            }
        }
        LianjiaLogger.info("本页共找到房源----" + houselist.size() + "个,具体信息如下：");
        int i = 0;
        for (HouseBean houseinfo : houselist) {
            LianjiaLogger.debug("第" + i + "套：" + houseinfo.address + "/" + houseinfo.floor);
            i++;
        }
        return houselist;
    }

    /**
     * 从字符串获取整数
     * 
     * @param text
     * @return
     */
    public Integer getDigit(String text) {
        Integer result = null;
        Pattern p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(text);
        if (m.find()) {
            result = Integer.parseInt(m.group());
        } else {
            LianjiaLogger.error("正则表达式匹配错误---匹配原字串为：" + text);
            return null;
        }
        return result;
    }

    public void saveHouse(List<HouseBean> houseinfo) {
        DBTools db = new DBTools();
        try (SqlSession sqlsession = db.getSqlSession().openSession()) {
            LianjiaMapper mapper = sqlsession.getMapper(LianjiaMapper.class);
            db.dblogger.debug("开始插入数据：" + mapper.insertHouseinfo(houseinfo));
            sqlsession.commit();
        } catch (Exception e) {
            db.dblogger.error("数据库操作异常！！");
            db.dblogger.error(e.toString(), e);
        }
    }
}