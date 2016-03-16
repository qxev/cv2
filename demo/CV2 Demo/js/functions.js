// JavaScript Document
function performAction(sel){
	if(sel.value!='-')
		eval(sel.value);
	return sel.selectedIndex = 0;
}

function help_box(content, event){
	var help = document.getElementById("content_help");
 	var event = event;
	help.style.display = "block";
	help.innerHTML = content + " ";
 	help.style.top = (event.clientY + document.body.scrollTop - 10) + "px";
	help.style.left = (event.clientX + document.body.scrollLeft + 10) + "px";
}

function clear_help_box(){
    var help = document.getElementById("content_help");
	clear_it(help);
}

function clear_it(o){
    o.style.display = "none";
}

function markRowsInit(){
    // for every table row ...
	var rows = document.getElementsByTagName('tr');
	for(i=0;i<rows.length;i++){
	    // ... with the class 'odd' or 'even' ...
		if('row1' != rows[i].className && 'row2' != rows[i].className){
		    continue;
		}
	    // ... add event listeners ...
        // ... to highlight the row on mouseover ...
	    if( navigator.appName == 'Microsoft Internet Explorer'){
	        // but only for IE, other browsers are handled by :hover in css
			rows[i].onmouseover = function(){
			    this.className += ' hover';
			}
			rows[i].onmouseout = function(){
			    this.className = this.className.replace(' hover','');
			}
	    }
	}
}
function isBetween(val,lo,hi){
	if((val < lo) || (val > hi)){
		return(false);
	}
	else{
		return(true);
	}
}

function isDate(theStr){
	var the1st = theStr.indexOf('-');
	var the2nd = theStr.lastIndexOf('-');
	
	if(the1st == the2nd){
		return(false);
	}
	else{
		var y = theStr.substring(0,the1st);
		var m = theStr.substring(the1st+1,the2nd);
		var d = theStr.substring(the2nd+1,theStr.length);
		var maxDays = 31;
		
		if(isInt(m)==false || isInt(d)==false || isInt(y)==false){
			return(false);
		}
		else if(y.length < 4){
			return(false);
		}
		else if(!isBetween (m, 1, 12)){
			return(false);
		}
		else if(m==4 || m==6 || m==9 || m==11){
			maxDays = 30;
		}
		else if(m==2){
			if(y % 4 > 0){
				maxDays = 28;
			}
			else if(y % 100 == 0 && y % 400 > 0){
				maxDays = 28;
			}
			else{
				maxDays = 29;
			}
		}
		if(isBetween(d, 1, maxDays) == false){
			return(false);
		}
		else{
			return(true);
		}
	}
}
 
function isTime(theStr){
	var colonDex = theStr.indexOf(':');
	if((colonDex<1) || (colonDex>2)){
		return(false);
	}
	else{
		var hh = theStr.substring(0,colonDex);
		var ss = theStr.substring(colonDex+1, theStr.length);
		if((hh.length<1) || (hh.length>2) || (!isInt(hh))){
			return(false);
		}
		else if((ss.length<1) || (ss.length>2) || (!isInt(ss))){
			return(false);
		}
		else if((!isBetween(hh,0,23)) || (!isBetween(ss,0,59))){
			return(false);
		}
		else{
			return(true);
		}
	}
}

function isDigit(theNum){
	var theMask = '0123456789';
	if(isEmpty(theNum)){
		return(false);
	}
	else if(theMask.indexOf(theNum) == -1){
		return(false);
	}
	return(true);
}

function isChart(theNum){
	var theMask = 'abcdefghijklmnopqrstuvwxyz - ABCDEFGHIJKLMNOPQRSTUVWXYZ_,.';
	if(isEmpty(theNum)){
		return(false);
	}
	else{
		for(i=0;i<theNum.length;i++){
			if(theMask.indexOf(theNum.substring(i,+i+1)) == -1){
				return(false);
			}
		}
	}
	return(true);
}
 
  /***************************************************************** 
*ʼĸʽǷϷ 
*****************************************************************/ 

function isEmail(theStr){
	var atIndex = theStr.indexOf('@');
	var dotIndex = theStr.indexOf('.',atIndex);
	var flag = true;
	theSub = theStr.substring(0, dotIndex+1)
	if((atIndex < 1)||(atIndex != theStr.lastIndexOf('@'))||(dotIndex < atIndex + 2)||(theStr.length <= theSub.length)){
		flag = false;
	} 
	else{
		flag = true;
	}
	return(flag);
} 

/***************************************************************** 
*ֵǷΪ 
*****************************************************************/ 

function isEmpty(str){
	if((str==null)||(str.length==0)){
		return true;
	}
	else{
		return(false);
	}
} 

/***************************************************************** 
*ֵǷΪ 
*****************************************************************/ 

function isInt(theStr){
	var flag = true;
	if(isEmpty(theStr)){
		flag=false;
	}
	else{
		for(i=0;i<theStr.length;i++){
			if(isDigit(theStr.substring(i,i+1)) == false){
				flag = false;
				break;
			}
		}
	}
	return(flag);
}

/***************************************************************** 

*****************************************************************/ 

function isReal(theStr,decLen){
	var dot1st = theStr.indexOf('.');
	var dot2nd = theStr.lastIndexOf('.');
	var OK = true;
	
	if(isEmpty(theStr)){
		return false;
	}
	if(dot1st == -1){
		if(!isInt(theStr)){
			return(false);
		}
		else{
			return(true);
		}
	}  
	else if(dot1st != dot2nd){
		return (false);
	}
	else if(dot1st==0){
		return (false);
	}
	else{
		var intPart = theStr.substring(0, dot1st);
		var decPart = theStr.substring(dot2nd+1);
		if(decPart.length > decLen){
			return(false);
		}
		else if(!isInt(intPart) || !isInt(decPart)){
			return (false);
		}
		else if(isEmpty(decPart)){
			return (false);
		}
		else{
			return(true);
		}
	}
}
window.onload=markRowsInit;
