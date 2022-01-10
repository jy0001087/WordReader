window.onload = function() {
    FillInForm(incentivesformArray);
    document.getElementById("rightpanel").style.display="none";
}

function FillInForm(incentivesformArray){
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
        //if(i%2 == 0) {
            trnode.setAttribute("class","dynamic-row");
        //}
        incentiveTable.appendChild(trnode);
    }
}

function insertStr(soure, start, newStr){
    return soure.slice(0,start) + newStr + soure.slice(start) ;
}

function clearTable(){
    var clearTarget = document.getElementById("querydata");
    clearTarget.value="";
}

function rightpaneljiazai(){

    setTimeout("document.getElementById('rightpanel').style.display='block'",1000);

}

function queryWithData(){
    tableCleaner();
    var querydata=document.getElementById("querydata").value;
    req= new XMLHttpRequest();
    req.onreadystatechange=function()
    {
        if (req.readyState==4 && req.status==200)
        {
            var queryformArray=req.responseText;
            FillInForm(JSON.parse(queryformArray));
        }
    }
    req.open("POST","IncentivesProcess",true);
    req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    req.send("ajaxflag=ajax&querydata="+querydata);
}
var queryformArray="";

function tableCleaner(){
    var table = document.getElementById("customers");
    var trCollection = table.getElementsByClassName("dynamic-row");
    console.log(trCollection)
    while(trCollection.length>0)
    {
        var tr=trCollection.item(0);
        tr.remove();
    }
}

//添加 增加一行 按钮
function tableAlter(){
    if(document.getElementById("cloumn_adder_button")){
        null;
    }else {
        alert("进入编辑状态，，点击‘增加一行’来新增记录");
        var cloumn_adder_button = document.createElement("button");
        cloumn_adder_button.textContent = "增加一行";
        cloumn_adder_button.id = "cloumn_adder_button";
        cloumn_adder_button.addEventListener("click",addCloumn);

        var right_botton_div = document.getElementById("right-botton");
        right_botton_div.append(cloumn_adder_button);
    }
}

//绑定增加一行 按钮
function addCloumn(){
    var trnode = document.createElement("tr");
    var tdnode1 = document.createElement("td");
    tdnode1.innerText=" ";
    tdnode1.contentEditable="true";
    trnode.appendChild(tdnode1);
    var tdnode2 = document.createElement("td");
    tdnode2.innerText="699396";
    trnode.appendChild(tdnode2);
    var tdnode3 = document.createElement("td");
    tdnode3.innerText="四川萌点科技有限公司";
    trnode.appendChild(tdnode3);
    var tdnode4 = document.createElement("td");
    tdnode4.innerText="0.50";
    trnode.appendChild(tdnode4);
    var tdnode5 = document.createElement("td");
    tdnode5.innerText="  ";
    tdnode1.contentEditable="true";
    //if(i%2 == 0) {
    trnode.setAttribute("class","dynamic-row");
    //}

    var table_customers = document.getElementById("customers");
    table_customers.appendChild(trnode);
}