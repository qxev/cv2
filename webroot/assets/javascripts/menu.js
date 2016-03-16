// JavaScript Document

var cur_menu_id = "";
var submenu_timer = null;
function set_menu(menu_id) {
	if (menu_id == "NONE") {
		return;
	}
	sub = document.getElementById("submenu_" + menu_id);
	sub.style.display = "block";
}
function unset_menu(menu_id) {
	if (menu_id == "NONE") {
		return;
	}
	sub = document.getElementById("submenu_" + menu_id);
	sub.style.display = "none";
}
function change_submenu(menu_id) {
	if (cur_menu_id == "") {
		cur_menu_id = old_menu_id;
	}
	unset_menu(cur_menu_id);
	cur_menu_id = menu_id;
	set_menu(cur_menu_id);
	if (submenu_timer != null) {
		clearTimeout(submenu_timer);
	}
	document.getElementById(old_menu_id).className='nav_now';
}
function restore_submenu_clear() {
	unset_menu(cur_menu_id);
	cur_menu_id = old_menu_id;
	set_menu(cur_menu_id);
}
function restore_submenu() {
	// restore_submenu_clear();
	submenu_timer = setTimeout("restore_submenu_clear()", 500);
}
function redirect(select) {
	var res = true;
	var option = select.options[select.selectedIndex];
	var url = option.value;
	var msg = option.getAttribute("msg");
	var type = option.getAttribute("type");
	var parm = option.getAttribute("parm");
	select.selectedIndex = 0;
	if (msg){
		if(type=="alert"){
			alert(msg);
		}else if(type=="prompt"){
			res = prompt(msg);
			if (parm=="tapestryStyle"){
				url = url + "/" + res;
			} else {
				if(res && parm) url = url + "?" + parm + "=" + res;
			}
		}else{
			res = confirm(msg);
		}
	}
	if (res) {
		window.location = url;
	}
}
// checkbox全选
function checkAll(form) {
	for (var i = 0; i < form.elements.length; i++) {
		var e = form.elements[i];
		if (e.name != "chkall" && e.type == "checkbox") {
			e.checked = form.chkall.checked;
		}
	}
}
function back() {
	history.go(-1);
}
function performAction(sel) {
	if (sel.value != "-") {
		eval(sel.value);
	}
	return sel.selectedIndex = 0;
}
// 防止table溢出div外面
function preventTableOverFlowDiv() {
	var clientWidth = !!(window.attachEvent && !window.opera) ? document.body.clientWidth : document.documentElement.clientWidth; 
	var scrollWidth = !!(window.attachEvent && !window.opera) ? document.body.scrollWidth : document.documentElement.scrollWidth; 
	
	if (scrollWidth > clientWidth) {
		// modify wrap div width
		document.getElementsByTagName('div')[0].style.width = Math.ceil((scrollWidth / clientWidth) * 100) + "%";
	}
}

function validcheckAndUpdate(num,campaignId) {
	var r = /^[0-9]*$/　　 
	if(r.test(num.value))  {
		window.location.href = "./listpage:updateRank/"+num.value+"/"+campaignId;
	} else {
		alert("请输入正确的排名！只能是数字");
	}
}
 
