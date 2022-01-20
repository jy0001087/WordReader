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

//添加 ”增加一行“以及“提交” 按钮
function tableAlter(){
    if(document.getElementById("cloumn_adder_button")){
        null;
    }else {
        alert("进入编辑状态，点击‘增加一行’来新增记录，点击‘提交修改’保存至数据库");
        addButton("新增一行","cloumn_adder_button",addCloumn,"right-botton");
        addButton("提交修改","cloum_adder_commit_button",commitCloumn,"right-botton");
    }
}

//增加按钮
function addButton(text,buttonId,clickFunction,parentNodeId){
    var adder_button = document.createElement("button");
    adder_button.textContent = text;
    adder_button.id = buttonId;
    adder_button.addEventListener("click",clickFunction);
    var right_botton_div = document.getElementById(parentNodeId);
    right_botton_div.append(adder_button);
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
    tdnode5.contentEditable="true";
    trnode.appendChild(tdnode5);
    //if(i%2 == 0) {
    trnode.setAttribute("class","dynamic-row");
    //}

    var table_customers = document.getElementById("customers");
    table_customers.appendChild(trnode);
}

function commitCloumn(){
    //数据检查
    //获取最后一行数据
    var table=document.getElementById("customers");
    var insertData=[];
    var x=0;
    for(var i=0,rows=lastRowIndex=table.rows.length;i<rows;i++){
        if(table.rows[i].cells[0].contentEditable=="true"){  //判断是否为新增行
           for(var j=0,cells=table.rows[i].cells.length;j<cells;j++) {
               if (!insertData[x]) {
                   insertData[x] = new Array;
               }
               var content = table.rows[i].cells[j].innerHTML;
               if (j==0 || j==4) {
                   content=content.replace(/[^0-9]/ig,"");
               if (typeof content == "undefined" || content == null || content == "") {
                   alert("检查检查 填好再提交 靴靴");
                   return;
               }
           }
               insertData[x][j]=encodeURIComponent(content);
           }
           x++;  //每当有一行新增，x记录一行
        }
    }
    //数据库操作
    req= new XMLHttpRequest();
    req.onreadystatechange=function()
    {
        if (req.readyState==4 && req.status==200) {
            alert("ojbk");
            rmElement("cloumn_adder_button");
            rmElement("cloum_adder_commit_button");
            queryWithData();
        }
    }
    req.open("POST","CloumnCommitProcess",true);
    req.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
    req.send("ajaxflag=ajax&insertdata="+insertData);
}

function rmElement(elementId){
    var self = document.getElementById(elementId);
    var parent = self.parentElement;
    var removed = parent.removeChild(self);
    removed === self;
}