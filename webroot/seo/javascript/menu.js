function menuFix() {
    var sfEls = document.getElementById("nav").getElementsByTagName("li");
    var url= self.location.href;
    if (url.length>28){
    	var menu =url.substring(25,28);
    	if(menu=='dar'){
    		 sfEls[1].className = "nav-now";
    	} else if(menu=='sol'){
    		 sfEls[6].className = "nav-now";
    	} else if(menu=='cap'){
    		 sfEls[10].className = "nav-now";
    	} else if(menu=='TEC'){
    		 sfEls[15].className = "nav-now";
    	} else if(menu=='car'){
    		 sfEls[19].className = "nav-now";
    	} else if(menu=='mar'){
   		   sfEls[22].className = "nav-now";
      }
    }
    for (var i=0; i<sfEls.length; i++) {
        sfEls[i].onmouseover=function() {
        this.className+=(this.className.length>0? " ": "") + "Sfhover";
        }
        sfEls[i].onMouseDown=function() {
        this.className+=(this.className.length>0? " ": "") + "Sfhover";
        }
        sfEls[i].onMouseUp=function() {
        this.className+=(this.className.length>0? " ": "") + "Sfhover";
        }
        sfEls[i].onmouseout=function() {
        this.className=this.className.replace(new RegExp("( ?|^)Sfhover\\b"), 
"");
        }
    }
}
window.onload=menuFix;