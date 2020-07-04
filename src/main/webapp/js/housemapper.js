function labelOnclick(houseinfoArray) {
    for (const house of houseinfoArray) {
        if (house.price < 4500 && house.area > 50) {
            document.getElementById(house.houseurl).onclick = function () {
                window.open(house.houseurl, "_blank");
            }
            //移动端触屏设备支持
            document.getElementById(house.houseurl).touchend = function () {
                window.open(house.houseurl, "_blank");
            }
        }
    }
}

//marker click事件
/*
function showInfoM(e) {
    console.log(e.target);
    var marker = e.target;
    var map = marker.map;
    mapObj.plugin(["AMap.Transfer"], function () {
        transOptions = {
            map: map,
            city: '北京市',
            panel: 'panel',
            //cityd:'乌鲁木齐',
            //policy: AMap.TransferPolicy.LEAST_TIME
        };
        //构造公交换乘类
        var transfer = new AMap.Transfer(transOptions);
        //根据起、终点坐标查询公交换乘路线
        transfer.search(new AMap.LngLat(116.291035, 39.907899), new AMap.LngLat(116.427281, 39.903719), function (status, result) {
            // result即是对应的公交路线数据信息，相关数据结构文档请参考  https://lbs.amap.com/api/javascript-api/reference/route-search#m_TransferResult
            if (status === 'complete') {
                log.success('绘制公交路线完成')
            } else {
                log.error('公交路线数据查询失败' + result)
            }
        });
    });
}
 */