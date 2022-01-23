<%@ page contentType="text/html; charset=utf-8" %>

<html>
<head>
    <title>Your Information</title>
</head>

<body>
<form action="HouseMapper">
    <input type="submit" value="看地图">
</form>
<form action="Timmer">
    <input type="submit" value="查房源">
</form>
<%
    try {
        out.println(request.getAttribute("TimmerMessage"));
    } catch (Exception e) {
        out.println("获取TimmerMessage异常");
    }
%>
</body>
</html> 