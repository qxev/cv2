function checkedNumber(number){
	if(number == 0) return;
	var checkboxContener = document.getElementById("affiliateCategoryCheckboxes");
	var checkboxes = checkboxContener.getElementsByTagName("input");
	var j = 0;
	for(var i = 0; i < checkboxes.length; i++){
		if(checkboxes[i].checked){
			++j;
		}
		if(j == number){
			break;
		}
	}

	if(j == number){
		for(var i = 0; i < checkboxes.length; i++){
			if(!checkboxes[i].checked){
				checkboxes[i].disabled=true;
			}
		}
	}else{
		for(var i = 0; i < checkboxes.length; i++){
			if(!checkboxes[i].checked){
				checkboxes[i].disabled=false;
			}
		}
	}
}

