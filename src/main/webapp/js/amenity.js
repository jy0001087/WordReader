function fetchNovel(){
    req= new XMLHttpRequest();
    req.onreadystatechange=function()
    {
        if (req.readyState==4 && req.status==200)
        {
            var resultArrayString=req.responseText;
            FileList(JSON.parse(resultArrayString));
        }
    }
    req.open("POST","ShuaigayNovelContent",true);
    req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    req.send();
}

function FileList(resultArrayString){
    let targteTableDiv = document.getElementById("novel-table-div");
    if(targteTableDiv !=null){
        document.body.removeChild(targteTableDiv);
    }

    let appendedtablediv = document.createElement("div");
    appendedtablediv.setAttribute("id","novel-table-div");
    document.body.appendChild(appendedtablediv);

    let appendedtable = document.createElement("table");
    appendedtable.setAttribute("id","novel-table");
    let tablediv = document.getElementById("novel-table-div");
    tablediv.appendChild(appendedtable);

    let table = document.getElementById("novel-table");
    for(const novel of resultArrayString){
        let novelle=novel.split("\\");
        title=novelle.pop();
        let appendedtr = document.createElement("tr");
        let appendedtd1 = document.createElement("td");
        appendedtd1.innerText= title;
        appendedtr.appendChild(appendedtd1);

        let appendedtd2 = document.createElement("td");
        appendedtd2.setAttribute("url",novel);
        appendedtd2.innerHTML="<input type='button' value='下载' onclick='DownloadNovel(event);'>";
        appendedtr.appendChild(appendedtd2);
        table.appendChild(appendedtr);
    }

}

function DownloadNovel(event){
    req= new XMLHttpRequest();
    req.responseType = 'blob';
    var button = event.target;
    var onclicktb = button.parentNode
    var urls =onclicktb.getAttribute("url").split("\\");
    var filename= urls.pop();
    var params = "filename="+encodeURI(encodeURI(filename));
    req.onreadystatechange=function()
    {
        if (req.readyState==4 && req.status==200)
        {
            var blob = new Blob([req.response], {type: 'text/xls'})
            var a = document.createElement("a");
            a.download=filename
            a.href=URL.createObjectURL(blob);
            a.click();
        }
    }
    req.open("POST","NovelDownload",true);
    req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
    req.send(params);
}