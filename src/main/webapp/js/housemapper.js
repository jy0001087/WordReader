window.onload = function() {
    addclickToLabel();
}

function addclickToLabel(){
    for (const house of houseinfoArray) {
        if (house.price < maxprice && house.area > minimumarea) {
            var label = document.getElementById(house.houseurl);
            label.addEventListener("click", function (e) {
                window.open(house.houseurl, "_blank");
                console.log(e.target.className);
            });
        }
    }
}

function drawPoint(maxprice, minimumarea) {
    for (const house of houseinfoArray) {
        if (house.price < maxprice && house.area > minimumarea) {
            var pointString = house.gdlocation;
            var ponitArray = pointString.split(",");
            //           console.log(ponitArray);
            var marker = new AMap.Marker({
                position: ponitArray//位置
            });
            map.add(marker);//增加地图瞄点
            markers.push(marker); //增加地图点对象记录，用于批量删除

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
            //marker.on('dblclick', showInfoM);
            marker.on('click', showWindow);
        }
    }
}


function showInfoM(event) {

    showWindow(event);
    //数据预处理，格式。
    //console.log("showInfoM中的target ： " + event.target.getPosition());
    console.log("showInfoM中的target-id ： " + event.target.getAttribute("lnglat"));
    var frompoint = event.target.getAttribute("lnglat");

    var fromArray = frompoint.split(",");
    var targetid = event.target.id;
    if (targetid == 'target-yz') { //去四季青路线
        var toArray = target[0].split(",");
    } else {
        var toArray = target[1].split(",");
    }
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
            console.log('绘制公交路线完成');
            /*
             drawRoute(result.plans[0]);


             (new Lib.AMap.TransferRender()).autoRender({
                 data: result,
                 map: map,
                 panel: "panel"
             });
 */
        } else {
            console.log('公交路线数据查询失败')
        }
    });
}


function showWindow(e) {
    //地图清理部分
    map.clearMap();
    var panel = document.getElementById("panel");
    panel.innerHTML = "";
    //地图清理部分结束
    if (windowlnglatString == "init") {   //如果是第一次点击，则记录位置。否则不再重新记录位置
        windowlnglatString = e.target.getPosition().toString();
    }
    var content = '<input id="target-yz" lnglat=' + windowlnglatString + ' type="button" class="btn" value="去东冉村路径" onclick="showInfoM(event)"/>' +
        '<br>' +
        '<input id="target-rf" lnglat=' + windowlnglatString + ' type="button" class="btn" value="去朝阳门" onclick="showInfoM(event)"/>'+
        '<br>' +
        '<input id="return-to-map"  type="button" class="btn" value="返回房源信息大图" onclick="showInfoClose()"/>'
    var windowInglatArray = windowlnglatString.split(",");
    var lnglat = new AMap.LngLat(windowInglatArray[0], windowInglatArray[1])
    var infowindow = new AMap.AdvancedInfoWindow({
        content: content,
        asOrigin: false,
        asDestination: false,
        offset: new AMap.Pixel(0, -30)
    });
    infowindow.open(map, lnglat);
}

function showInfoClose() {
    console.log("触发关闭窗口事件");
    map.clearMap();
    drawPoint(maxprice, minimumarea);
    addclickToLabel();
    windowlnglatString = "init";  // 关闭搜索页后，重置windowlnglatString为初始状态
}