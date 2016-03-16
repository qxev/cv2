// JavaScript Document

function Delete(ID){
	if(confirm("Are you sure you want to delete this category?"))
		document.location.href = "userLinksAct"+FILE_EXT+"?act=Delete&cid="+ID;
}

function Add(ID){
	document.location.href = "userLinksInsert"+FILE_EXT+"?cid="+ID;
}

function Edit(ID){
	document.location.href = "webs_user_Links_Edit"+FILE_EXT+"?cid="+ID;
}

function Look(ID){
	document.location.href = "webs_user_Links_Tousers"+FILE_EXT+"?cid="+ID;
}

function StatusYes(ID){
		document.location.href = "userLinksAct"+FILE_EXT+"?act=StatusYes&cid="+ID;
}

function StatusNo(ID){
		document.location.href = "userLinksDecline"+FILE_EXT+"?act=StatusNo&cid="+ID;
}