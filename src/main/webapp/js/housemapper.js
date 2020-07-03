function labelOnclick(houseinfoArray) {
    for (const house of houseinfoArray) {
        if (house.price < 4500 && house.area > 50) {
            document.getElementById(house.houseurl).onclick = function () {
                window.open(house.houseurl,"_blank");
            }
        }
    }
}