// JavaScript Document
function Delete(ID){
	if(confirm(PRODTYPE_DELETE)){
		document.location.href = "prodTypeAct"+FILE_EXT+"?act=delete&cid="+ID;
	}
}

function addCampaign(){
	document.location.href = "prodTypeInsert"+FILE_EXT;
}

function editCampaign(ID){
	document.location.href = "prodTypeEdit"+FILE_EXT+"?cid="+ID;
}

function copyCampaign(ID){
	document.location.href = "prodTypeInsert"+FILE_EXT+"?cid="+ID;
}

function Look(ID){
	document.location.href = "prodTypeView"+FILE_EXT+"?cid="+ID;
}