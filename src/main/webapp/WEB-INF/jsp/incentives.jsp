<%--
  Created by IntelliJ IDEA.
  User: RubberFun
  Date: 2021/9/5
  Time: 22:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>业绩查询</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/incentives.js"></script>
    <style>
        #customers
        {
            font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
            width:100%;
            border-collapse:collapse;
        }
        #customers td, #customers th
        {
            font-size:1em;
            border:1px solid #CCECFF;
            padding:3px 7px 2px 7px;
        }
        #customers th
        {
            font-size:1.1em;
            text-align:left;
            padding-top:5px;
            padding-bottom:4px;
            background-color:#CCECFF;
            color:#000000;
        }
        #customers tr.alt td
        {
            color:#000000;
            background-color:#E5F6FF;
        }

<%-- 左右分版 --%>
        .left{
            width: 12%;
            height: 100%;
            float:left;
            background:#c0c0c0;
            cursor: pointer;
        }
        .right{
            height: 100%;
            width: 85%;
            float: right;
        }
    </style>
</head>
<body>
<div class="container">
<div class="left" style="height:100%">
    <ul>
        <li data-src="roadlist.html">合作管理平台</li>
        <li data-src="roleList.action">综合业务查询</li>
    </ul>
</div>
<div class="right">
    <table id="customers">
        <tr>
            <th>账期</th>
            <th>CP代码</th>
            <th>CP名称</th>
            <th>结算比例</th>
            <th>不含税结算费(元)</th>
        </tr>
    </table>
</div>
</div>
<%
    String incentivesforArray = (String) request.getAttribute("incentivesforArray");
%>
<script type="text/javascript">
    var incentivesformArray = <%= incentivesforArray %>
</script>
</body>
</html>
