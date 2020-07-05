<%@ page import="java.util.ArrayList" %>
<%@ page import="java.lang.reflect.Array" %>

<%@ page contentType="text/html; charset=utf-8" %>

<html>

<head>
    <title>房源瞄点地图</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <link href="<%=request.getContextPath()%>/css/housemapper.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript"
            src="https://webapi.amap.com/maps?v=2.0&key=6dbd0a8a4bc84f8aa52ddb5827c00ea7&plugin=AMap.Scale,AMap.Transfer,AMap.ToolBar,AMap.Adaptor"></script>
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

    function showInfoM(e) {
        map.clearMap();
        //数据预处理，格式。
        var target =<%=targetGdLocation%>;
        console.log("showInfoM中的target ： " + e.target.getPosition());
        var frompoint = e.target.getPosition().toString();

        var fromArray = frompoint.split(",");
        var toArray = target[0].split(",");

        //处理公交线路
        transOptions = {
            map: map,
            city: '北京市',
            panel: 'panel',
            policy: AMap.TransferPolicy.LEAST_TIME
        };
        //构造公交换乘类
        var transfer = new AMap.Transfer(transOptions);
        //根据起、终点坐标查询公交换乘路线
        transfer.search(new AMap.LngLat(fromArray[0], fromArray[1]), new AMap.LngLat(toArray[0], toArray[1]), function (status, result) {
            // result即是对应的公交路线数据信息，相关数据结构文档请参考  https://lbs.amap.com/api/javascript-api/reference/route-search#m_TransferResult
            if (status === 'complete') {
                console.log('绘制公交路线完成')
                drawRoute(result.plans[0]);

                (new Lib.AMap.TransferRender()).autoRender({
                    data: result,
                    map: map,
                    panel: "panel"
                });

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
        map.setFitView([startMarker, endMarker].concat(routeLines))
    }

    var map = new AMap.Map('container', {
        center: [116.397428, 39.90923]
    });
    // map.addControl(new AMap.Scale());


    var houseinfoArray = <%= houseinfo%>;
    for (const house of houseinfoArray) {
        if (house.price < 4500 && house.area > 50) {
            var pointString = house.gdlocation;
            var ponitArray = pointString.split(",");
 //           console.log(ponitArray);
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
            marker.setLabel({
                direction: 'left',
                content: labelContent, //设置文本标注内容
            });
            var label = document.getElementById(house.houseurl);
            label.addEventListener("click", function (e) {
                window.open(house.houseurl, "_blank");
                console.log(e.target.className);
            });
            marker.on('dblclick', showInfoM);
        }
    }

</script>
</body>

</html>