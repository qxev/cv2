function validateForm(form){

	

    initSpan();



	var email = form.email.value;    

    var pattern = /^([.a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;    



	var flag = false;



	if(form.name.value == ''){

		$('tip_name').innerHTML = "  请填写你的名字!";

		form.name.focus();

		flag = true;

	}

	if(!pattern.test(email)){

		$('tip_email').innerHTML = "  请填写你的email!";

		form.email.focus();

		flag = true;

	}



	if(form.selectInquire.value == ""){

		$('tip_inquire').innerHTML = " 请选择你的服务类别!";

		form.selectInquire.focus();

		flag = true;

	}
	if(form.selectInquire.value == "Others" && form.requirements.value == ""){
		$('strong-comments').innerHTML = "评论&nbsp;";
		$('tip_comment').innerHTML = "  请填写评论";
		form.selectInquire.focus();
		flag = true;
	}

	if(form.code.value == ""){
		$('tip_code').innerHTML = " 请输入验证玛";
		form.code.focus();
		flag = true;

	}

	if(flag){

		return false;

	}

}



function initSpan(){

	$('tip_name').innerHTML = "";

	$('tip_email').innerHTML = "";

	$('tip_contact').innerHTML = "";

	$('tip_inquire').innerHTML = "";

}



function $(id){

	return document.getElementById(id);

}
