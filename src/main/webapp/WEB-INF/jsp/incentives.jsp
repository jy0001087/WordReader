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
    <link href="<%=request.getContextPath()%>/css/incentives.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="toppanel">
    <li>标题占位符</li>
</div>
<div id="container">
    <div id="leftpanel">
        <div id="functionselection">

        </div>
<div id="left-negivation">
    <div id="negivation-title">导航栏</div>
    <div id="negivation-content">
    <ul class="domtree">
        <li>
            <input type="checkbox" id="check"/>
            <label for="check">合作管理平台</label>
            <ul class="column">
                <li>
                    <input type="checkbox" id="check1"/>
                    <label for="check1">结算管理</label>
                    <ul class="column">
                        <li onclick="rightpaneljiazai();">结算单管理</li>
                    </ul>
                </li>
            </ul>
        </li>
        <li>
            综合信息管理
            <ul class="column">
                <li>2级菜单</li>
            </ul>
        </li>
    </ul>
    </div>
</div>
    </div>
<div class="right" id="rightpanel">
    <table id="titletable">
        <tr>
            <th><img src="img/title-form.png" id="title-form">合作管理平台>>结算管理>>结算单管理</th>
        </tr>
    </table>
    <div class="right-top" id="righttop">
        <form action="IncentivesProcess" method="post" id="queryform">
            <table id="optionpanel">
                <tr>
                    <td>时间： <input type="text" name="querydata" id="querydata">请输入6位数字，如“202001”</td>
                    <td>   </td>
                    <td>业务类型：<select><option>订阅服务</option></select></td>
                </tr>
            </table>
            <table style="text-align: center" id="buttonpanel">
                <tr>
                    <td>                </td>
                    <td width="80px"> <button type="button" onclick="queryWithData();" class="butt" >查询</button> </td>
                    <td width="80px"> <button type="button" onclick="clearTable();" class="butt" >重置</button> </td>
                    <td width="140px"> <button type="button"  class="butt" >导出Excel文件</button> </td>
                    <td width="120px" id="extractele"><input class="extractinput">ᅳ<input  class ="extractinput">页</td>
                    <td>                </td>
                </tr>
            </table>
        </form>
    </div>
    <div id="right-botton">
    <table id="customers">
        <tr>
            <th>账期</th>
            <th>CP代码</th>
            <th>CP名称</th>
            <th>结算比例</th>
            <th>不含税结算费(元)</th>
        </tr>
    </table>
        <table id="customers-bottom">
            <tr>
                <th>1 </th>
            </tr>
        </table>
    </div>
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
