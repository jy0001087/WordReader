<%@ page contentType="text/html; charset=utf-8" %>

<html>
<head>
    <title>Your Information</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/amenity.js"></script>
</head>

<body>
<form action="HouseMapper">
    <input type="submit" value="看地图">
</form>
<form action="shuaigayServlet" method="post">
    <input id="threadId" type="text" name="threadId">
    <input type="submit" value="找文章" onclick="">
    <input type="button" value="文章列表" onclick="fetchNovel();">
    <input type="button" value="金科" onclick="fetchCDJK();">
</form>
<div>
    <input id="DataServlet" type="button" value="模拟安卓访问" onclick="fetchDataServlet();" >
</div>
</body>
</html> 