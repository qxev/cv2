var CPC = 100;
var CPL = 101;
var CPS = 102;
var CPM = 105;

function changeDiv(){
//佣金的下拉框
var select = document.getElementById("select");
var value = select.value;

//根据佣金类型展示对应的下拉框
var style1 = document.getElementById("style1");
var valueType =document.getElementById("valueType");
var obj = valueType.options;

var addOption = document.createElement("option");
	addOption.value = "2";
	//per 百分比的国际化信息
	addOption.text = "百分比";
	var vtLength = valueType.options.length;

if(value == CPC){
	style1.style.display = "";
	
	for(var i=0;i<obj.length;i++){
		if (obj[i].value == 2){
			valueType.remove(i);
		}
	}
}
else if(value == CPL){
	
	if(vtLength < 2){
	valueType.options[valueType.options.length] = addOption;
}
	style1.style.display = "";
}
else if(value == CPS){
		
	if(vtLength < 2){
	valueType.options[valueType.options.length] = addOption;
}
	style1.style.display = "";
}
else if(value == CPM){
	style1.style.display = "";
	
	for(var i=0;i<obj.length;i++){
		if (obj[i].value == 2){
			valueType.remove(i);
		}
	}
}

else{
	style1.style.display = "none";
}

//window.setTimeout("changeDiv();",1000);
}

function changeInputStyle(){
	var select = document.getElementById("select");
	var style1 = document.getElementById("style1");
	var style2 = document.getElementById("style2");
	var value = select.value;
	if(value == "1"){
		style1.style.display = "";
		style2.style.display = "none";
	};
	if(value == "2"){
		style1.style.display = "none";
		style2.style.display = "";
	}
}

function showText(){
	document.getElementById("iframeTextTd").style.display="";
}

function hiddenText(){
	document.getElementById("iframeTextTd").style.display="none";
}
