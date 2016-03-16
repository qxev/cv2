
function changeRange(radio){
	if(radio.checked){
		var td = document.getElementById("endCommission");
		var span = td.getElementsByTagName("SPAN")[0];
		if(radio.value==1){
			span.style.display = "";
		}else{
			span.style.display = "none";
		}
	}
}



function initRange(){
	var noRange = document.getElementById("noRange");
	var isRange = document.getElementById("isRange");
	var td = document.getElementById("endCommission");
	var span = td.getElementsByTagName("SPAN")[0];
	if(noRange.checked){
		span.style.display = "none";
	}else if(isRange.checked){
		span.style.display = "";
	}
}



function showOrHidenByRadio(element,targetId,isHidden){
	var text = document.getElementById(targetId);
	if(element.checked && isHidden){
		text.style.display = "none";
	}else{
		text.style.display = "";
	}
}

function initShowOrHidenByRadio(targetId){
	var text = document.getElementById(targetId);
	if(document.getElementById("clear").checked){
		text.style.display = "none";
	}else{
		text.style.display = "";
	}
}