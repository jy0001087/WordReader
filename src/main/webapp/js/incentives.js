window.onload = function() {
    FillInForm();
}

function FillInForm(){
    var i=0;
    for(const incentive of incentivesformArray) {
        i++;
        var incentiveTable=document.getElementById("customers");
        var trnode = document.createElement("tr");
        var tdnode1 = document.createElement("td");
        tdnode1.innerText=incentive.payment_days;
        trnode.appendChild(tdnode1);
        var tdnode2 = document.createElement("td");
        tdnode2.innerText=incentive.CP_code;
        trnode.appendChild(tdnode2);
        var tdnode3 = document.createElement("td");
        tdnode3.innerText=incentive.CP_name;
        trnode.appendChild(tdnode3);
        var tdnode4 = document.createElement("td");
        tdnode4.innerText=incentive.settle_rate;
        trnode.appendChild(tdnode4);
        var tdnode5 = document.createElement("td");
        tdnode5.innerText=insertStr(incentive.settle_amount.toString(),-2,".");
        trnode.appendChild(tdnode5);
        if(i%2 == 0) {
            trnode.setAttribute("class","alt");
        }
        incentiveTable.appendChild(trnode);
    }
}

function insertStr(soure, start, newStr){
    return soure.slice(0,start) + newStr + soure.slice(start) ;
}