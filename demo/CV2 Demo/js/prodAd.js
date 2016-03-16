function addBanner(Type){
	document.location.href = "prodAdInsert"+Type+FILE_EXT;
}

function copyBanner(ID,Type){
	document.location.href = "prodAdEditCopy"+FILE_EXT+"?act=copy&cid="+ID;
}

function viewBanner(ID){
	document.location.href = "prodAdView"+FILE_EXT+"?cid="+ID;
}

function editBanner(ID){
	document.location.href = "prodAdEditCopy"+FILE_EXT+"?act=update&cid="+ID;
}

function deleteBanner(ID){
	if(confirm(PRODAD_DELETE)){
		document.location.href = "prodAdAct"+FILE_EXT+"?act=bannerdelete&cid="+ID;
	}
}

function adminGetCode(RID,ID){
	document.location.href = "adminGetCode"+FILE_EXT+"?banner_refid="+RID+"&uid="+ID;
}

function showfilename(name,file){
	var re = document.getElementById(file).value.match(/\\([^\\]+)\.([^ ]+)$/);
	document.getElementById(name).value = re[1];
}

function ImageSubmit(){
	var a = 0;
	if(document.postForm.advertise_id.value == ""){
		alert(PRODAD_ADVERTISE_ID);
		return false;
	}
	for(j=0;j<10;j++){
		if(document.getElementById('image'+j).value != ""){
			if(document.getElementById('banner_name'+j).value == ""){
				alert(PRODAD_BANNER_NAME+(j+1)+"！");
				return false;
			}
			else{
				a = 1;
			}
		}
	}
	if(a == 0){
		alert(PRODAD_BANNER_TYPE_IMAGE_NULL);
		return false;
	}
	else{
		return true;
	}
}

function TextSubmit(){
	var a = 0;
	if(document.postForm.advertise_id.value == ""){
		alert(PRODAD_ADVERTISE_ID);
		return false;
	}
	for(j=0;j<10;j++){
		if(document.getElementById('banner_desc'+j).value != ""){
			if(document.getElementById('banner_name'+j).value == ""){
				alert(PRODAD_BANNER_NAME+(j+1)+"！");
				return false;
			}
			else{
				a = 1;
			}
		}
	}
	if(a == 0){
		alert(PRODAD_BANNER_TYPE_TEXT_NULL);
		return false;
	}
	else{
		return true;
	}
}

function HtmlSubmit(){
	var a = 0;
	if(document.postForm.advertise_id.value == ""){
		alert(PRODAD_ADVERTISE_ID);
		return false;
	}
	for(j=0;j<10;j++){
		if(document.getElementById('banner_desc'+j).value != ""){
			if(document.getElementById('banner_name'+j).value == ""){
				alert(PRODAD_BANNER_NAME+(j+1)+"！");
				return false;
			}
			else if(document.getElementById('width'+j).value == ""){
				alert(PRODAD_BANNER_NAME+(j+1)+PRODAD_BANNER_WIDTH);
				return false;
			}
			else if(document.getElementById('height'+j).value == ""){
				alert(PRODAD_BANNER_NAME+(j+1)+PRODAD_BANNER_HEIGHT);
				return false;
			}
			else if(!isInt(document.getElementById('width'+j).value)){
				alert(PRODAD_BANNER_NAME+(j+1)+PRODAD_BANNER_WIDTH_FORM_ERROR);
				return false;
			}
			else if(!isInt(document.getElementById('height'+j).value)){
				alert(PRODAD_BANNER_NAME+(j+1)+PRODAD_BANNER_HEIGHT_FORM_ERROR);
				return false;
			}
			else{
				a = 1;
			}
		}
	}
	if(a == 0){
		alert(PRODAD_BANNER_TYPE_HTML_NULL);
		return false;
	}
	else{
		return true;
	}
}

function FlashSubmit(){
	var a = 0;
	if(document.postForm.advertise_id.value == ""){
		alert(PRODAD_ADVERTISE_ID);
		return false;
	}
	for(j=0;j<10;j++){
		if(document.getElementById('image'+j).value != ""){
			if(document.getElementById('banner_name'+j).value == ""){
				alert(PRODAD_BANNER_NAME+(j+1)+"！");
				return false;
			}
			else if(document.getElementById('width'+j).value == ""){
				alert(PRODAD_BANNER_NAME+(j+1)+PRODAD_BANNER_WIDTH);
				return false;
			}
			else if(document.getElementById('height'+j).value == ""){
				alert(PRODAD_BANNER_NAME+(j+1)+PRODAD_BANNER_HEIGHT);
				return false;
			}
			else if(!isInt(document.getElementById('width'+j).value)){
				alert(PRODAD_BANNER_NAME+(j+1)+PRODAD_BANNER_WIDTH_FORM_ERROR);
				return false;
			}
			else if(!isInt(document.getElementById('height'+j).value)){
				alert(PRODAD_BANNER_NAME+(j+1)+PRODAD_BANNER_HEIGHT_FORM_ERROR);
				return false;
			}
			else{
				a = 1;
			}
		}
	}
	if(a == 0){
		alert(PRODAD_BANNER_TYPE_FLASH_NULL);
		return false;
	}
	else{
		return true;
	}
}