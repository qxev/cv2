var msgReader = "http://localhost:8081/effecter/efs";
document.write("<div class=\"xScrollStick helper\">");
document.write("<table align='right' height='88' cellspacing='0' cellpadding='0'><tr>");
document.write("<td><table id='helper_container' width='100%' height='88' cellspacing='0' cellpadding='0'><tr>");
document.write("<td width='20' height='88' align='right'><img src='/assets/images/msg_left.jpg'/></td>");
document.write("<td background='/assets/images/msg_center.jpg' id='helpermsg'>哈哈，我最喜欢在这里玩啦</td>");
document.write("<td width='47' height='88' align='left'><img src='/assets/images/msg_right.jpg'/></td>");
document.write("</tr></table></td>");
document.write("<td width='60' align='right'><img id='helperman' style='cursor:pointer;' src='/assets/images/0001.gif' width='60' title='我是你的小秘书，鼠标点击我查看历史消息' onclick=\"$('helper_msg_details').style.display='';\"></img></td>");
document.write("</tr></table></div>");
document.write("<div id='helper_msg_details' class='helper_details' style='display:none;cursor:pointer;' title='鼠标双击隐藏本窗口' ondblclick=\"this.style.display='none';\"></div>");
var xScrollStickCssFilePath="";
var BROWSERNAME="";
switch(navigator.appName.toLowerCase()){
 case "netscape":
  BROWSERNAME="ns";
 break;
 case "microsoft internet explorer":
 default:
  BROWSERNAME="ie";
 break;
}
switch(BROWSERNAME){
 case "ns":
  window.addEventListener("load",_xScrollStick_init,false);
 break;
 case "ie":
 default:
  window.attachEvent("onload",_xScrollStick_init);
}
function _xScrollStick_init(){
 var allTheScrollSticks=document.getElementsByTagName("div");
 for(var i=0;i<allTheScrollSticks.length;i++){
  if(allTheScrollSticks[i].className.match(/^((xScrollStick)|(.+ +xScrollStick)|(xScrollStick +.+)|(.+ +xScrollStick +.+))$/))_xScrollStick_event_doInit(allTheScrollSticks[i]);
 }
 window_event_scroll();
}
function _xScrollStick_event_doInit(element){
 element.offX=element.offsetLeft;
 element.offY=element.offsetTop;
 element.Stick=_xScrollStick_method_Stick;
 switch(BROWSERNAME){
  case "ns":
   window.addEventListener("scroll",window_event_scroll,false);
  break;
  case "ie":
  default:
   window.attachEvent("onscroll",window_event_scroll);
 }
 document.body.parentNode.onscroll=window_event_scroll;
}
function window_event_scroll(){
 var allTheScrollSticks=document.getElementsByTagName("div");
 for(var i=0;i<allTheScrollSticks.length;i++){
  if(allTheScrollSticks[i].className.match(/^((xScrollStick)|(.+ +xScrollStick)|(xScrollStick +.+)|(.+ +xScrollStick +.+))$/))try{allTheScrollSticks[i].Stick();}catch(e){}
 }
}
function _xScrollStick_method_Stick(){
 var x=this.offX, y=this.offY, po=this;
 this.style.position="absolute";
 x+=document.body.parentNode.scrollLeft;
 y+=document.body.parentNode.scrollTop;
 this.style.left=x+"px";
 this.style.top=y+"px";
}
function _xReadMsgLib(){
	var pars = "uid="+sessionId+"&action=Message&type=read&rnd="+escape(Math.random());
	//$('helper_msg_details').innerHTML = $('helper_msg_details').innerHTML + "<br/>" + msgReader + "?" + pars;
	var myAjax = new Ajax.Request(msgReader, 
		{
			method: 'get',
			parameters: pars,
			asynchronous:  true,
			onComplete: _xShowResponse
		} 
	);
}
var _xprevious_msg = "";
var _xShowMsg = false;
var _xScanTimes = 0;
function _xShowResponse(cRequest){
	var cts = cRequest.responseText;
	//alert(cts);
	if(cts==""){
		_xScanTimes ++;
		if(_xScanTimes > 30){
			_xShowMsg = false;
			_xScanTimes = 0;
		}
	}else{
		if(_xprevious_msg != cts){
			_xShowMsg = true;
			var cdt = new Date();
			var times = "[" + cdt.getYear() + "-" + (cdt.getMonth()+1) + "-" + cdt.getDate() + " " + cdt.getHours() + ":" + cdt.getMinutes() + ":" + cdt.getSeconds() + "] ";
			var ind = Math.round(Math.random()*10);
			ind = (ind<1)?1:ind;
			var sind = (ind>9)?"00"+ind:"000"+ind;
			$('helperman').src = "/assets/images/"+sind+".gif";
			$('helpermsg').innerHTML = cts;
			$('helper_msg_details').innerHTML = $('helper_msg_details').innerHTML + "<br/>" + times + cts;			
		}
		_xprevious_msg = cts;
	}
	$('helper_container').style.display = (_xShowMsg)?"":"none";
}
window.setInterval("_xReadMsgLib();", 500);