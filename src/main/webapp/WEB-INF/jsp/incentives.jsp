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
            border:1px solid #98bf21;
            padding:3px 7px 2px 7px;
        }
        #customers th
        {
            font-size:1.1em;
            text-align:left;
            padding-top:5px;
            padding-bottom:4px;
            background-color:#A7C942;
            color:#ffffff;
        }
        #customers tr.alt td
        {
            color:#000000;
            background-color:#EAF2D3;
        }

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
            <th>>不含税结算费(元)</th>
        </tr>
        <tr>
            <td>Alfreds Futterkiste</td>
            <td>Maria Anders</td>
            <td>Germany</td>
        </tr>
        <tr class="alt">
            <td>Berglunds snabbköp</td>
            <td>Christina Berglund</td>
            <td>Sweden</td>
        </tr>
        <tr>
            <td>Centro comercial Moctezuma</td>
            <td>Francisco Chang</td>
            <td>Mexico</td>
        </tr>
        <tr class="alt">
            <td>Ernst Handel</td>
            <td>Roland Mendel</td>
            <td>Austria</td>
        </tr>
        <tr>
            <td>Island Trading</td>
            <td>Helen Bennett</td>
            <td>UK</td>
        </tr>
        <tr class="alt">
            <td>Königlich Essen</td>
            <td>Philip Cramer</td>
            <td>Germany</td>
        </tr>
        <tr>
            <td>Laughing Bacchus Winecellars</td>
            <td>Yoshi Tannamuri</td>
            <td>Canada</td>
        </tr>
        <tr class="alt">
            <td>Magazzini Alimentari Riuniti</td>
            <td>Giovanni Rovelli</td>
            <td>Italy</td>
        </tr>
        <tr>
            <td>North/South</td>
            <td>Simon Crowther</td>
            <td>UK</td>
        </tr>
        <tr class="alt">
            <td>Paris spécialités</td>
            <td>Marie Bertrand</td>
            <td>France</td>
        </tr>
    </table>
</div>
</div>
</body>
</html>
