// JavaScript Document
function Delete(ID){
	if(confirm(PRODADV_DELETE)){
		document.location.href = "prodAdvAct"+FILE_EXT+"?act=delete&cid="+ID;
	}
}

function addAdvertise(){
	document.location.href = "prodAdvInsert"+FILE_EXT;
}

function editAdvertise(ID){
	document.location.href = "prodAdvEdit"+FILE_EXT+"?cid="+ID;
}

function copyAdvertise(ID){
	document.location.href = "prodAdvInsert"+FILE_EXT+"?cid="+ID;
}

function viewBanners(ID1,ID2){
	document.location.href = "prodAd"+FILE_EXT+"?cid="+ID1+"&aid="+ID2;
}