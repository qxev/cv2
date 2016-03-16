// JavaScript Document
function Apply(ID){
    document.location.href = "adveMeListAct.php?act=Apply&cid="+ID;
}

function AddFav(ID){
    document.location.href = "adveMeListAct.php?act=AddFav&cid="+ID;
}

function View(ID){
		document.location.href = "affListView.php?cid="+ID;
}

function Approve(ID){
	document.location.href = "affListAct.php?act=Approve&cid="+ID;
}

function Decline(ID){
	if(confirm(AFFLIST_DECLINE))
	document.location.href = "affListAct.php?act=Decline&cid="+ID;
}