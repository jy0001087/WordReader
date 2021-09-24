<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">

    <style type="text/css">
        body, td, th {
            font-size: 12px;
        }

        body {
            margin-left: 0px;
            margin-top: 0px;
            margin-right: 0px;
            margin-bottom: 0px;
        }

        a:link {
            color: #000000;
            text-decoration: none;
        }

        a:visited {
            text-decoration: none;
            color: #000000;
        }

        a:hover {
            text-decoration: none;
            color: #FF0000;
        }

        a:active {
            text-decoration: none;
            color: #FF0000;
        }
    </style>
    <script language="javascript">
        function textLimitCheck(maxLength, asdf) {
            if (thisArea.value.length > maxLength)
                nr.innerText = thisArea.value.length;
        }

        function Juge(theform) {
            if (theform.admin_yy.value == "") {
                alert("请输入用户");
                theform.admin_yy.focus();
                return (false);
            }

            if (theform.admin_mm.value == "") {
                alert("请输入密码");
                theform.admin_mm.focus();
                return (false);
            }

            if (theform.yzm.value == "") {
                alert("请输入验证");
                theform.yzm.focus();
                return (false);
            }

        }
    </script>
    <script type="text/javascript">
        function $(id){
            return document.getElementById(id);
        }
        function changeCode(){
            $("vcode").src = "ValidateCode";
        }
    </script>

    <title>中央音乐平台内容管理系统</title>
</head>
<body>
<table width="100%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0">
    <tbody>
    <tr>
        <td align="center" valign="middle" bgcolor="#E6F2FE">&nbsp;</td>
        <td align="center" valign="middle" bgcolor="#E6F2FE">&nbsp;</td>
        <td align="center" valign="middle" bgcolor="#E6F2FE">&nbsp;</td>
    </tr>
    <tr>
        <td height="375" align="center" valign="middle" background="img/login_z_bj.jpg">&nbsp;</td>
        <td align="center" valign="middle" background="img/login_z_bj.jpg">
            <table border="0" cellpadding="0" cellspacing="0">
                <tbody>
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" background="img/login_02.jpg">
                            <tbody>
                            <tr>
                                <td width="15"><img src="img/login_01.jpeg" width="15" height="69"></td>
                                <td align="center">
                                    <table width="95%" height="55" border="0" cellpadding="0" cellspacing="0">
                                        <tbody>
                                        <tr>
                                            <td valign="bottom"><img src="img/login_14.jpeg"></td>
                                            <td align="right" valign="bottom"><img src="img/login_15.jpeg"></td>
                                        </tr>

                                        </tbody>
                                    </table>
                                </td>
                                <td width="15"><img src="img/login_03.jpeg" width="15" height="69"></td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#FFFFFF">
                            <tbody>
                            <tr>
                                <td width="1" bgcolor="#90BED6"></td>
                                <td align="center" background="img/login_12.jpg"><img src="img/login_13.jpeg"
                                                                                      width="210" height="138"></td>
                                <td width="1" bgcolor="#99CCFF"></td>
                                <td width="200" align="center" bgcolor="#DDEFFB">
                                    <form action="Login" method="post">
                                        <table height="110" border="0" cellpadding="0" cellspacing="0">
                                            <tbody>
                                            <tr>
                                                <td>用户:</td>
                                                <td><input name="username" type="text" id="admin_yy" size="20"></td>
                                                <td>&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td>密码:</td>
                                                <td><input name="password" type="password" id="admin_mm" size="20"></td>
                                                <td>&nbsp;</td>
                                            </tr>
                                            <tr>
                                                <td>验证:</td>
                                                <td><input name="validatecode" type="text" id="validatecode" size="5">
                                                    <img src="ValidateCode" align="absmiddle" border="0" id="vcode">
                                                </td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <td></td>
                                                <td align="right"><a href="javascript:changeCode();">换一张</a></td>
                                                <td></td>
                                            </tr>
                                            <tr>
                                                <td></td>
                                                <td><input name="Submit" type="submit" class="asdf" value="登陆"> </td>
                                                <td></td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </form>
                                </td>
                                <td width="1" bgcolor="#99CCFF"></td>
                                <td width="50" background="img/login_12.jpg">&nbsp;</td>
                                <td width="1" bgcolor="#90BED6"></td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" background="img/login_07.jpg">
                            <tbody>
                            <tr>
                                <td width="3"><img src="img/login_06.jpeg" width="10" height="31"></td>
                                <td align="center">
                                    <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                        <tbody>
                                        <tr>
                                            <td align="right">&nbsp;</td>
                                            <td width="210" align="center">
                                                <table border="0" cellpadding="0" cellspacing="0">
                                                    <tbody>
                                                    <tr>
                                                        <td valign="top">
                                                            本系统推荐使用chrome浏览器。
                                                        </td>
                                                    </tr>
                                                    </tbody>
                                                </table>
                                            </td>
                                            <td width="37" align="right">&nbsp;</td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                                <td width="3"><img src="img/login_08.jpeg" width="10" height="31"></td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" background="img/login_10.jpg">
                            <tbody>
                            <tr>
                                <td width="3"><img src="img/login_09.jpeg" width="10" height="106"></td>
                                <td>&nbsp;</td>
                                <td width="3"><img src="img/login_11.jpeg" width="10" height="106"></td>
                            </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                </tbody>
            </table>
        </td>
        <td align="center" valign="middle" background="img/login_z_bj.jpg">&nbsp;</td>
    </tr>
    <tr>
        <td align="center" valign="middle" bgcolor="#4599DF">&nbsp;</td>
        <td align="center" valign="middle" bgcolor="#4599DF">&nbsp;</td>
        <td align="center" valign="middle" bgcolor="#4599DF">&nbsp;</td>
    </tr>
    </tbody>
</table>
</body>
</html>