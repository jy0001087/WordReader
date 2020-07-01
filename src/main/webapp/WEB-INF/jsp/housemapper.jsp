<%@ page import="java.util.ArrayList" %>
<%@ page import="java.lang.reflect.Array" %>
<%@ page contentType="text/html; charset=utf-8" %>

<html>

<head>
    <title>房源瞄点地图</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <style>
        #container {
            width: 600px;
            height: 1460px;
        }
    </style>
</head>

<body>
<%
    out.println((request.getAttribute("HouseInfoJson")).getClass());
    String houseinfo =  (String)request.getAttribute("HouseInfoJson");
 //   out.println(houseinfo);
%>
<script src="https://webapi.amap.com/loader.js"></script>
<script type="text/javascript">
    AMapLoader.load({
        key: '6dbd0a8a4bc84f8aa52ddb5827c00ea7', //首次调用load必须填写key
        version: '2.0',     //JSAPI 版本号
        plugins: ['AMap.Scale']  //同步加载的插件列表
    }).then((AMap) => {
        var map = new AMap.Map('container', {
            center: [116.397428, 39.90923]
        });
        map.addControl(new AMap.Scale());
        var marker = new AMap.Marker({
            position: [116.617944, 39.926346]//位置
        });
        map.add(marker);//增加地图瞄点

    }).catch((e) => {
        console.error(e);  //加载错误提示
    });
</script>
<h>第一个jsp</h>

<div id="container">
</div>
<dif id="houseinfo"></dif>
</body>

</html>