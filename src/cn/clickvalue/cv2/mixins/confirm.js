var Confirm = Class.create();
Confirm.prototype = {
    initialize: function(element, message) {
    	this.message = message;
    	this.element = element;
    	Event.observe($(element), 'change', this.doConfirm.bindAsEventListener(this));
    },
    
    doConfirm: function(e) {
    	if(! confirm(this.message)) {
    		e.stop();
    	} else {
    		var options = $(this.element).getElementsByTagName("option");
    		
    		for(var i = 0;i <　options.length;i++) {
    			if(options[i].selected) {
    				window.location = options[i].value;
    			}
    		}
    	}
    },
    
    redirect: function() {
    }
}

