/**
 * jQuery http request query string parse plugin
 * http://blog.yuweijun.co.cc/2008/08/get-parameters-value-from-url-query.html
 */
(function($){
    $.query_params = {
    	params: {}, 
    	names : [], 
    	values : [],
        parse: function(query_str){
            query_str = query_str || window.location.search.slice(1);
            query_str = query_str ? query_str.split(/&(?:amp;)*/) : [];
            for (var i = 0; i < query_str.length; i++) {
                var single = query_str[i].split("=");
                if (single[0]) {
                    this.params[decodeURIComponent(single[0])] = decodeURIComponent(single[1]);
                    this.names.push(decodeURIComponent(single[0]));
                    this.values.push(decodeURIComponent(single[1]));
                }
            }
            return this;
        },
    	parse_url: function(url){
        	var arr = url.split('?');
        	if(arr[1]){
        		return this.parse(arr[1]);
        	}else{
        		return this;
        	}
        }
    };
})(jQuery);
