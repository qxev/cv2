function validCheck(){
	var userSelect = false;
	var question = document.getElementsByName("radiogroup");
	for (var i=0;i<4;i++){
		if(question[i].checked==true){
			userSelect = true;
		}
	}
	if (userSelect == false) {
		alert("请选择第1题");
		return false;
	}
	userSelect = false;
	question = document.getElementsByName("radiogroup_0");
	for (var i=0;i<4;i++){
		if(question[i].checked==true){
			userSelect = true;
		}
	}
	if (userSelect == false) {
		alert("请选择第2题");
		return false;
	}
	userSelect = false;
	question = document.getElementsByName("radiogroup_1");
	for (var i=0;i<4;i++){
		if(question[i].checked==true){
			userSelect = true;
		}
	}
	if (userSelect == false) {
		alert("请选择第3题");
		return false;
	}
	userSelect = false;
	question = document.getElementsByName("radiogroup_2");
	for (var i=0;i<4;i++){
		if(question[i].checked==true){
			userSelect = true;
		}
	}
	if (userSelect == false) {
		alert("请选择第4题");
		return false;
	}
	userSelect = false;
	question = document.getElementsByName("radiogroup_3");
	for (var i=0;i<5;i++){
		if(question[i].checked==true){
			userSelect = true;
		}
	}
	if (userSelect == false) {
		alert("请选择第5题");
		return false;
	}
	userSelect = false;
	question = document.getElementsByName("radiogroup_4");
	for (var i=0;i<4;i++){
		if(question[i].checked==true){
			userSelect = true;
		}
	}
	if (userSelect == false) {
		alert("请选择第6题");
		return false;
	}
	userSelect = false;
	question = document.getElementsByName("radiogroup_5");
	for (var i=0;i<4;i++){
		if(question[i].checked==true){
			userSelect = true;
		}
	}
	if (userSelect == false) {
		alert("请选择第7题");
		return false;
	}
	userSelect = false;
	question = document.getElementsByName("radiogroup_6");
	for (var i=0;i<4;i++){
		if(question[i].checked==true){
			userSelect = true;
		}
	}
	if (userSelect == false) {
		alert("请选择第8题");
		return false;
	}
	userSelect = false;
	question = document.getElementsByName("radiogroup_7");
	for (var i=0;i<3;i++){
		if(question[i].checked==true){
			userSelect = true;
		}
	}
	if (userSelect == false) {
		alert("请选择第10题");
		return false;
	}
	userSelect = false;
	question = document.getElementsByName("radiogroup_8");
	for (var i=0;i<5;i++){
		if(question[i].checked==true){
			userSelect = true;
		}
	}
	if (userSelect == false) {
		alert("请选择第11题");
		return false;
	}
	userSelect = false;
	question = document.getElementsByName("radiogroup_9");
	for (var i=0;i<4;i++){
		if(question[i].checked==true){
			userSelect = true;
		}
	}
	if (userSelect == false) {
		alert("请选择第12题");
		return false;
	}
	var userName = document.getElementsByName("textfield_0");
	if (userName[0].value==''){
		alert("请填写您的用户名");
		return false;
	}
	alert("谢谢您参与投票！");
	window.close(); 
}
