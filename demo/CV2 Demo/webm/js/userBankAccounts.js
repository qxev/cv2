// JavaScript Document

function Delete(ID){
	if(confirm("Are you sure you want to delete this Accounts?"))
		document.location.href = "userBankAccountsAct"+FILE_EXT+"?act=Delete&cid="+ID;
}

function Add(){
	document.location.href = "webs_user_Bank_Accounts_Insert"+FILE_EXT;
}
function Add2(){
	document.location.href = "webs_user_Links_Insert"+FILE_EXT;
}
function Edit(ID){
	document.location.href = "userBankAccountsEdit"+FILE_EXT+"?cid="+ID;
}

function Look(ID){
	document.location.href = "userBankAccountsView"+FILE_EXT+"?cid="+ID;
}

function Approve(ID){
	document.location.href = "userBankAccountsAct"+FILE_EXT+"?act=Approve&cid="+ID;
}

function Decline(ID){
	var message = prompt("输入拒绝理由","");
	if(message != '' && message != null)
		document.location.href = "userBankAccountsAct"+FILE_EXT+"?act=Decline&cid="+ID+"&message="+message;
}