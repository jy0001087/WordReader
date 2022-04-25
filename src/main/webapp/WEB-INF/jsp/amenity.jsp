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
<form action="shuaigayServlet" method="post">
    <input type="text" name="threadId">
    <input type="submit" value="找文章">
</form>
<%
    try {
        out.println(request.getAttribute("TimmerMessage"));
        out.println(request.getAttribute("Article"));
    } catch (Exception e) {
        out.println("获取TimmerMessage异常");
    }
%>

<%
    response.setContentType("application/x-download");//设置为下载application/x-download
    String filedownload = "/要下载的文件名"; //即将下载的文件的相对路径
    String filedisplay = "最终要显示给用户的保存文件名"; //下载文件时显示的文件保存名称
    String filenamedisplay = URLEncoder.encode(filedisplay,"UTF-8");
    response.addHeader("Content-Disposition","attachment;filename=" + filenamedisplay);
    try{
        RequestDispatcher dis = application.getRequestDispatcher(filedownload);
        if(dis!= null){
            dis.forward(request,response);
        }
        response.flushBuffer();
    }catch(Exception e){
        e.printStackTrace();
    }finally{

    }
%>
</body>
</html> 