function changesave(act,md){
	switch(act){
		case "subjectinsert":
			if(validateSubjectInsert()){
				document.postForm.turntopage.value=md;
				document.postForm.submit();
			}
		break;
		
		case "typeinsert":
			if(validate()){
				document.postForm.turntopage.value=md;
				document.postForm.submit();
			}
		break;
		
		case "advinsert":
			if(validateAdvInsert()){
				document.postForm.turntopage.value=md;
				document.postForm.submit();
			}
		break;
	}
}
function commissionChange(ID1,ID2){
	document.location.href = "prodTypeEdit"+FILE_EXT+"?cid="+ID2+"&campaign_type="+ID1;
}

function commissionChange3(ID1,ID2){
	document.location.href = "prodTypeInsert"+FILE_EXT+"?cid="+ID2+"&campaign_type="+ID1;
}

function commissionChange2(ID){
	switch (ID){
		case "":
		document.getElementById('testid1').innerHTML="";
		document.getElementById('testid2').innerHTML="";
		break;
		
		case "1":
		document.getElementById('testid1').innerHTML=PRODTYPE_CAMPAIGN_APPROVAL_CPC+PRODTYPE_CAMPAIGN_APPROVAL;
		document.getElementById('testid2').innerHTML="<select name=\"campcate_commtype_cpc\"><option value=\"cash\">"+PRODTYPE_CAMPCATE_COMMTYPE_CASH+"</option></select><input name=\"campcate_commission_cpc\" id=\"campcate_commission_cpc\" type=\"text\" />";
		break;
		
		case "2":
		document.getElementById('testid1').innerHTML=PRODTYPE_CAMPAIGN_APPROVAL_CPS+PRODTYPE_CAMPAIGN_APPROVAL;
		document.getElementById('testid2').innerHTML="<select name=\"campcate_commtype_cps\"><option value=\"cash\">"+PRODTYPE_CAMPCATE_COMMTYPE_CASH+"</option><option value=\"percent\">"+PRODTYPE_CAMPCATE_COMMTYPE_PERCENT+"</option></select><input name=\"campcate_commission_cps\" id=\"campcate_commission_cps\" type=\"text\" />";
		break;
		
		case "3":
		document.getElementById('testid1').innerHTML=PRODTYPE_CAMPAIGN_APPROVAL_CPL+PRODTYPE_CAMPAIGN_APPROVAL;
		document.getElementById('testid2').innerHTML="<select name=\"campcate_commtype_cpl\"><option value=\"cash\">"+PRODTYPE_CAMPCATE_COMMTYPE_CASH+"</option><option value=\"percent\">"+PRODTYPE_CAMPCATE_COMMTYPE_PERCENT+"</option></select><input name=\"campcate_commission_cpl\" id=\"campcate_commission_cpl\" type=\"text\" />";
		break;
	}
}

function show_ex(obj,content){
	obj.style.padding = '2px 2px 2px 0px';
	if(!document.all){
		obj.style.border = '#666 1px solid';
	}
	obj.style.background = '#fff url(./images/green.gif) right top no-repeat';
	var temp = obj.id + '_ex';
	temp = document.getElementById(temp);
	temp.style.padding = '0px 0px 0px 25px';
	temp.style.background = '#e2f5ff url(./images/alert.gif) 5px 0px no-repeat';
	temp.style.border = '#00a8ff 1px solid';
	temp.innerHTML = content;
}

function restore(obj,content){
	obj.style.background = '#fff';
	var temp = obj.id + '_ex';
	temp = document.getElementById(temp);
	temp.style.padding = '0px';
	temp.style.background = '#eee';
	temp.style.border = '#fff 0px solid';
	temp.innerHTML = content;
}



//function addSubject2(md){
//			if(validateSubjectInsert()){
//				document.location.href = md+FILE_EXT;
//			}
//	}
//}
