jQuery(function(){
	jQuery("a[name='qq']").click(function(){
		
		if(__DW_SV2){
			__DW_SV2.callTrack(5);
		}
		
//		var url = _qqInterface + '?svcd=' + _qqSVCD + '&cvcd=' + '&rnd=' + Math.random() + '&trk_sa=&trk_cm=&trk_er=&_to_page=&trk_ck=365&trk_od=' + _qqAffiliateId + '&trk_cid=1&trk_sp=1&trk_am=0&t=s&lp=http&trk_rr=' + _qqCP;
//		var qqtrk = '<img id="qqtrk" width="1" height="1" border="0" src="' + url + '" />';
//		if(jQuery("#qqtrk").length === 0){
//			jQuery('body').append(qqtrk);
//		}else{
//			jQuery('#qqtrk').replaceWith(qqtrk);
//		}
		
		
//		var key=jQuery.query_params.parse_url(jQuery(this).attr('href')).params.sigkey;
//		var tempSrc='http://sighttp.qq.com/wpa.js?rantime='+Math.random()+'&sigkey=' + key;
//		var oldscript=document.getElementById('testJs');
//		var newscript=document.createElement('script');
//		newscript.setAttribute('type','text/javascript');
//		newscript.setAttribute('id', 'testJs');
//		newscript.setAttribute('src',tempSrc);
//		if(oldscript == null){
//			document.body.appendChild(newscript);
//		}else{
//			oldscript.parentNode.replaceChild(newscript, oldscript);
//		}		
//		return false;
	});
});