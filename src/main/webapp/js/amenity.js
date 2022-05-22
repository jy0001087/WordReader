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
    alert(resultArrayString);
}