<%@ page import="java.util.ArrayList" %>
<%@ page import="java.lang.reflect.Array" %>

<%@ page contentType="text/html; charset=utf-8" %>

<html>

<head>
    <title>房源瞄点地图</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <link href="<%=request.getContextPath()%>/css/housemapper.css" rel="stylesheet" type="text/css"/>
    <script src="https://webapi.amap.com/loader.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/housemapper.js"></script>
</head>

<body>
<dif id="houseinfo">submition</dif>
<div id="container"></div>

<%
    String houseinfo = (String) request.getAttribute("HouseInfoJson");
    //   out.println(houseinfo);
%>

<script type="text/javascript">
    var width = document.body.clientWidth;
    var height = document.body.clientHeight;

    document.getElementById("container").style.width = width;
    document.getElementById("container").style.height = height;

    AMapLoader.load({
        key: '6dbd0a8a4bc84f8aa52ddb5827c00ea7', //首次调用load必须填写key
        version: '2.0',     //JSAPI 版本号
        plugins: ['AMap.Scale']  //同步加载的插件列表
    }).then((AMap) => {
        var map = new AMap.Map('container', {
            center: [116.397428, 39.90923]
        });
        map.addControl(new AMap.Scale());


        var houseinfoArray = <%= houseinfo%>;
        console.log(houseinfoArray.constructor);
        for (const house of houseinfoArray) {
            var pointString = house.gdlocation;
            var ponitArray = pointString.split(",");
            console.log(ponitArray);
            var marker = new AMap.Marker({
                position: ponitArray//位置
            });
            map.add(marker);//增加地图瞄点

            var labelContent = "<div class=\"labelcontent\" id = " + house.houseurl + ">" +
                "租金：" + house.price + "<br>" +
                "面积：" + house.area + "<br>" +
                "楼层：" + house.floor + "<br>" +
                "地址：" + house.address + "<br>" +
                "房型：" + house.housetype +
                "</div>";
            if (house.price < 4500 && house.area > 50) {
                marker.setLabel({
                    direction: 'left',
                    content: labelContent, //设置文本标注内容
                });
            }

        }
        ;

    }).catch((e) => {
        console.error(e);  //加载错误提示
    });

    window.onload = function () {
        var houseinfoArray = <%= houseinfo%>;
        labelOnclick(houseinfoArray);
    }
</script>
</body>

</html>