// JavaScript Document
function viewDetails(ID){
	document.location.href = "adveSubdetail.php?cid="+ID;
}

function viewAdvertises(ID){
	document.location.href = "adveLinkList.php?cid="+ID;
}

function viewBanners(ID){
	document.location.href = "adveBannerList.php?cid="+ID;
}

function apply(ID){
	document.location.href = "adveMeListAct.php?act=Apply&cid="+ID;
}