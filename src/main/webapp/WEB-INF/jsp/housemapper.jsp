<%@ page import="java.util.ArrayList" %>
<%@ page import="java.lang.reflect.Array" %>

<%@ page contentType="text/html; charset=utf-8" %>

<html>

<head>
    <title>房源瞄点地图</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <link href="<%=request.getContextPath()%>/css/housemapper.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript"
            src="https://webapi.amap.com/maps?v=1.4.15&key=6dbd0a8a4bc84f8aa52ddb5827c00ea7&plugin=AMap.Transfer,Amap.Adaptor,AMap.AdvancedInfoWindow"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/housemapper.js"></script>
    <script type="text/javascript" src="https://cache.amap.com/lbs/static/TransferRender1230.js"></script>
</head>

<body>
<form action="HouseMapper">
    <input type="submit" value="返回点列表">
</form>
<div id="container"></div>
<div id="panel"></div>
<%
    String houseinfo = (String) request.getAttribute("HouseInfoJson");
    String targetGdLocation = (String) request.getAttribute("targetGdLocation");
    //   out.println(houseinfo);
%>
<br>
<script type="text/javascript">

    var width = document.body.clientWidth;
    var height = document.body.clientHeight;
    document.getElementById("container").style.width = width;
    document.getElementById("container").style.height = height;

    var map = new AMap.Map('container', {
        center: [116.397428, 39.90923]
    });

    var markers = [];

    var houseinfoArray = <%= houseinfo%>;
    var target =<%=targetGdLocation%>;
    var maxprice = 4500;
    var minimumarea = 50;

    drawPoint(maxprice, minimumarea);

</script>
</body>

</html>