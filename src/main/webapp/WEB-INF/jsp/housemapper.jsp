<%@ page import="java.util.ArrayList" %>
<%@ page import="java.lang.reflect.Array" %>

<%@ page contentType="text/html; charset=utf-8" %>

<html>

<head>
    <title>房源瞄点地图</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <link href="<%=request.getContextPath()%>/css/housemapper.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript"
            src="https://webapi.amap.com/maps?v=2.0&key=6dbd0a8a4bc84f8aa52ddb5827c00ea7&plugin=AMap.Scale,AMap.Transfer,AMap.ToolBar"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/housemapper.js"></script>
</head>

<body>
<dif id="houseinfo">submition</dif>
<div id="container"></div>
<div id="panel"></div>
<%
    String houseinfo = (String) request.getAttribute("HouseInfoJson");
    //   out.println(houseinfo);
%>
<br>
<button id="testButton">试试</button>
<script type="text/javascript">
    var width = document.body.clientWidth;
    var height = document.body.clientHeight;

    document.getElementById("container").style.width = width;
    document.getElementById("container").style.height = height;

    function showInfoM(e) {
        transOptions = {
            map: map,
            city: '北京市',
            panel: 'panel',
            policy: AMap.TransferPolicy.LEAST_TIME
        };
        //构造公交换乘类
        var transfer = new AMap.Transfer(transOptions);
        //根据起、终点坐标查询公交换乘路线
        transfer.search(new AMap.LngLat(116.291035, 39.907899), new AMap.LngLat(116.427281, 39.903719), function (status, result) {
            // result即是对应的公交路线数据信息，相关数据结构文档请参考  https://lbs.amap.com/api/javascript-api/reference/route-search#m_TransferResult
            if (status === 'complete') {
                console.log('绘制公交路线完成')
                /*
        (new Lib.AMap.TransferRender()).autoRender({
            data:result,
            map:map,
            panel:"panel"
        });

                 */
                drawRoute(result.plans[0])
            } else {
                console.log('公交路线数据查询失败')
            }
        });
    }

    function drawRoute(route) {
        var startMarker = new AMap.Marker({
            position: route.segments[0].transit.origin,
            icon: 'https://webapi.amap.com/theme/v1.3/markers/n/start.png',
            map: map
        })

        var endMarker = new AMap.Marker({
            position: route.segments[route.segments.length - 1].transit.destination,
            icon: 'https://webapi.amap.com/theme/v1.3/markers/n/end.png',
            map: map
        })

        var routeLines = []

        for (var i = 0, l = route.segments.length; i < l; i++) {
            var segment = route.segments[i]
            var line = null

            // 绘制步行路线
            if (segment.transit_mode === 'WALK') {
                line = new AMap.Polyline({
                    path: segment.transit.path,
                    isOutline: true,
                    outlineColor: '#ffeeee',
                    borderWeight: 2,
                    strokeWeight: 5,
                    strokeColor: 'grey',
                    lineJoin: 'round',
                    strokeStyle: 'dashed'
                })


                line.setMap(map)
                routeLines.push(line)
            } else if (segment.transit_mode === 'SUBWAY' || segment.transit_mode === 'BUS') {
                line = new AMap.Polyline({
                    path: segment.transit.path,
                    isOutline: true,
                    outlineColor: '#ffeeee',
                    borderWeight: 2,
                    strokeWeight: 5,
                    strokeColor: '#0091ff',
                    lineJoin: 'round',
                    strokeStyle: 'solid'
                })

                line.setMap(map)
                routeLines.push(line)
            } else {
                // 其它transit_mode的情况如RAILWAY、TAXI等，该示例中不做处理
            }
        }
    }

    var map = new AMap.Map('container', {
        center: [116.397428, 39.90923]
    });
   // map.addControl(new AMap.Scale());


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
            marker.on('click', showInfoM);
        }
    }


    window.onload = function () {
        var houseinfoArray = <%= houseinfo%>;
        labelOnclick(houseinfoArray);
    }
</script>
</body>

</html>