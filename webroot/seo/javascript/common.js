
window.onload = function (){
	initDiv();
}

function showDialog() {
           var iWidth=664;
           var iHeight=380;
           var iTop=(window.screen.height-iHeight)/2;
           var iLeft=(window.screen.width-iWidth)/2;
           window.open("http://www.darwinmarketing.com/form-window.html","Detail","Scrollbars=no,Toolbar=no,Location=no,Direction=no,Resizeable=no,Width="+iWidth+" ,Height="+iHeight+",top="+iTop+",left="+iLeft);
          } 

String.prototype.replaceAll = function(s1,s2){
	return this.replace(new RegExp(s1,"gmi"),s2);
}

RegExp.escape = (function() {
	var punctuationChars = /([$.*+?|/(){}[\]\\])/g;
	return function(text) {
		return text.replace(punctuationChars, '\\\\$1');
	}
})();

// keywords, div id, title, banner
var a = ['china_search', 'china search', 'China Search', '/en/images/images-sem/banner-0612-greatwall.gif'];
var b = ['market_in_china', 'market in china', 'Market in China', '/en/images/images-sem/banner-0612-shanghai.gif'];
var c = ['china_search_engine', 'china search engine', 'China Search Engine', '/en/images/images-sem/banner-0612-greatwall.gif'];
var d = ['china_market_entry', 'china market entry', 'China Market Entry', '/en/images/images-sem/banner-0612-greatwall.gif'];
var e = ['outsource_seo', 'outsource seo', 'Outsource Seo', '/en/images/images-sem/banner-0612-shanghai.gif'];
	
var keywords = [
	['China Search', 'c', a],
	['Chinese Search', 'c', a],
	['China Internet Search', 'c', a],
	['Market in China', 'c', b],
	['Sell in China', 'c', b],
	['Market to China', 'c', b],
	['Make Business in China', 'c', b],
	['Sell China Online', 'c', b],
	['Start Business in China', 'c', b],
	['Marketing in China', 'c', b],
	['Advertising in China', 'c', b],
	['Marketing to China', 'c', b],
	['Selling China Online', 'c', b],
	['Direct Marketing in China', 'c', b],
	['Online Advertising in China', 'c', b],
	['Online Marketing in China', 'c', b],
	['Starting Business in China', 'c', b],
	['China Search Engine', 'c', c],
	['China Search Engines', 'c', c],
	['Search Engine Chinese', 'c', c],
	['Search Engines China', 'c', c],
	['Chinese Search Engine', 'c', c],
	['Chinese Search Engines', 'c', c],
	['China Market Entry', 'c', d],
	['Enter China Market', 'c', d],
	['China Internet Market', 'c', d],
	['China Online Market', 'c', d],
	['China Advertising', 'c', d],
	['China Marketing', 'c', d],
	['Chinese Marketing', 'c', d],
	['China Online Advertising', 'c', d],
	['Chinese Online Advertising', 'c', d],
	['China Online Marketing', 'c', d],
	['China Internet Marketing', 'c', d],
	['Chinese Internet Marketing', 'c', d],
	['China Internet Advertising', 'c', d],
	['Outsource Seo', 'c', e],
	['Seo Outsourcing', 'c', e]
];

function initDiv(){
        var referrerKeyword = getKeyword();
	for(var i=0;i<keywords.length;i++){
		var keyw = keywords[i];
		if(keyw[1] == 'c'){
		   if(new RegExp("^"+RegExp.escape(keyw[0])+"$","i").test(referrerKeyword)){
                        replaceContent(keyw[2], keyw[0]);
			return;
		   }else{
                     renderDefaultDiv();
                   }
		}else if(keyw[1] == 'd'){
			if(new RegExp("^.*"+RegExp.escape(keyw[0])+".*$","i").test(referrerKeyword)){
			 replaceContent(keyw[2], keyw[0]);
		         return;
			}else{
                            renderDefaultDiv();
                        }
		}
    }
}

function renderDefaultDiv(){
     document.getElementById('dynamic_div').innerHTML = document.getElementById('china_search').innerHTML;
}

function getKeyword(){
	var referrer = document.referrer;
	var patten = /(?:\?|&)q=(.*?)(?:$|&)/;
	if(patten.test(referrer)){
		var keyword = RegExp.$1;
		keyword =keyword.replaceAll("\\+", " ");
		return keyword;
    }else{
		return '';
	}
}

function replaceContent(replacement, keyword){
      document.getElementById('dynamic_div').innerHTML = document.getElementById(replacement[0]).innerHTML;
	  var referrerKeyword = getKeyword().replace(/c(hina|hinese)/, "C$1");
		if (isNeedReplace(referrerKeyword)) {
			getContentObj().innerHTML = getContent().replaceAll(replacement[1], referrerKeyword);
				  document.title = replacement[2];
				  document.getElementById('img_banner').src = replacement[3];
				  document.getElementById('navigate').firstChild.nodeValue = keyword;
				  document.getElementById('subject').firstChild.nodeValue = keyword;
		}
}

function getContent(){
	return getContentObj().innerHTML;
}

function getContentObj(){
	var divs = document.getElementsByTagName('div');
	for(var i=0;i<divs.length;i++){
		if(divs[i].className == 'content'){
			return divs[i];
		}
	}
}


function isNeedReplace(referrerKeyword){
	for(var i=0;i<keywords.length;i++){
		var keyw = keywords[i];
		if(keyw[1] == 'c'){
		   if(new RegExp("^"+RegExp.escape(keyw[0])+"$","i").test(referrerKeyword)){
			return true;
		   }
		}else if(keyw[1] == 'd'){
			if(new RegExp("^.*"+RegExp.escape(keyw[0])+".*$","i").test(referrerKeyword)){
				return true;
			}
		}else{return false;}
}
}
