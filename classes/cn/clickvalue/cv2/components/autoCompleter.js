/**
 * autocompleter version 0.2, based Prototype.
 **/
AutoFieldFromLocal = Class.create({
	initialize: function (element, items) {
		this.element = $(element);
		this.value = this.element.value;
		this.items = items;
		this.timer = null;
		this.observer = null;
		this.counter = 0;
		this.index = 0;
		this.listElements = [];
		this.listsObserved = false;
		this.maskShowed = false;
		this.divId = "auto_complete_for_" + this.element.id;
		this.listsId = "lists_of_" + this.element.id;
		this.maskId = "mask_" + this.element.id;
		this.element.setAttribute('autocomplete', 'off');
		// TODO: extract element style and use css class, className should be in options.
		var options = {limit: true, max: 50}; // render max lists when limit is true.
		this.options = Object.extend(options, arguments[2] || {});
		this.divStyle = "position:absolute; background-color:#fff; border:1px solid #888; margin:0; padding:0; display:none; height:200px; opacity:0.90; overflow:auto; overflow-x: hidden; z-index:1; filter:alpha(opacity=90);";
		this.maskStyle = "position:absolute; background-color:blue; border:1px solid #888; margin:0; padding:0; display:none; height:0; opacity:0; z-index:2; filter:alpha(opacity=0);";
		this.ulStyle = "margin:0; padding:0; list-style-type:none; width:100%;";
		this.liStyle = "line-height:20px; padding:0; margin:0; display:block; width:100%; cursor:default; white-space:pre;";
		// IE has render bug, if there are continuous blank space in list item
		// (such as "ss  ss   sss")
		this.observerStart();
	},
	insertAfter: function () {
		new Insertion.After(this.element, 
			'<div id="' + this.divId + '" style="' + this.divStyle + '">' + 
			'<ul id="' + this.listsId + '" style="' + this.ulStyle + '"></ul></div>' +
			'<div id="' + this.maskId + '" style="' + this.maskStyle + '"/>'
		);
		this.completerDiv = $(this.divId);
		this.list = $(this.listsId);
		this.mask = $(this.maskId);
	},
	observerStart: function () {
		this.insertAfter();
		// safari/ie中监听keypress时，arrow keys箭头无法正确激活绑定的事件，改用keydown监听
		// Arrow keys no longer result in keypress events, now processed in keydown default event handler
		// Reference: https://lists.webkit.org/pipermail/webkit-dev/2007-December/002992.html
		// keydown/keypress区别参考ppk的文章: http://www.quirksmode.org/dom/events/keys.html
		// script.aculo.us controls.js has modified keypress to keydown for all browser.
		if (Prototype.Browser.WebKit || Prototype.Browser.IE) Event.observe(this.element, 'keydown', this.renderLists.bindAsEventListener(this));
		else Event.observe(this.element, 'keypress', this.renderLists.bindAsEventListener(this));
		Event.observe(this.element, 'blur', this.onBlured.bindAsEventListener(this));
		Event.observe(document, 'mousemove', this.hideMask.bindAsEventListener(this));
	},
	addListsObserver: function () {
		var self = this;
		this.listElements.each(function (li) {					
			Event.observe(li, 'mouseover', self.onMouseOverList.bindAsEventListener(self));
			Event.observe(li, 'click', self.updateField.bindAsEventListener(self));
		});
		this.hideMask();
	},
	hideMask: function () {
		if (this.maskShowed) {
			this.maskShowed = false;
			this.mask.style.height = '0px';
			this.mask.style.display = "none";
		}
	},
	showMask: function () {
		if (!this.maskShowed) {
			this.maskShowed = true;
			this.mask.style.height = this.completerDiv.clientHeight + 'px';
			this.mask.style.display = "block";
		}
	},
	onBlured: function () {
		var self = this;
		// make sure input field has been updated before input lost cursor and completerDiv hide.
		// input will not be updated when mouse left button pressed long time and not release it 
		// because input lost focus.
		// if (this.timer) clearTimeout(this.timer);
		// this.timer = setTimeout(function () {self.completerDiv.hide();self.hideMask();}, 100);
		Event.observe(document, 'mouseup', function(){self.completerDiv.hide();self.hideMask();});
	},
	updateField: function () {
		this.element.focus();
		if(this.listElements.length > 0 && this.index != -1)
		this.element.value = this.listElements[this.index].innerHTML.replace(/&amp;/, "&");
		this.completerDiv.hide();
		this.hideMask();
	},
	listsShowed: function () {
		return this.completerDiv.style.display.toLowerCase() == 'block';
	},
	onSelectList: function () {
		this.listElements.each(function (li) {
			li.style.backgroundColor = "#fff";			
		});
		this.listElements[this.index].style.backgroundColor = "#ffb";
	},
	divScrollTop: function () {
		var maxScrollTop = this.completerDiv.scrollHeight - this.completerDiv.clientHeight;
		var scrollBottom = this.completerDiv.clientHeight + this.completerDiv.scrollTop;
		var listHeight = this.listElements[0] ? this.listElements[0].clientHeight : 20; 
		// TODO, get li.clientHeight after insert li element, then remove it if ul list has no li child.
		if (this.index == 0) 
		this.completerDiv.scrollTop = 0;
		else if (this.index == this.listElements.length - 1)
		this.completerDiv.scrollTop = maxScrollTop;
		else if (this.index * listHeight < this.completerDiv.scrollTop)
		this.completerDiv.scrollTop = this.index * listHeight;
		else if (this.index * listHeight >= scrollBottom)
		this.completerDiv.scrollTop = this.completerDiv.scrollTop + listHeight;
	},
	markNextList: function () {
		this.showMask();
		if(this.index < this.counter - 1) this.index++
		else this.index = 0;
		this.divScrollTop();
		this.onSelectList();
	},
	markPrevList: function () {
		this.showMask();
		if(this.index > 0 ) this.index--
		else this.index = this.counter - 1;
		this.divScrollTop();
		this.onSelectList();
	},
	onMouseOverList: function (event) {
		var elementLI = Event.findElement(event, 'LI');
		this.index = this.listElements.indexOf(elementLI);
		this.listElements.each(function (li) {
			li.style.backgroundColor = "#fff";					
		});
		elementLI.style.backgroundColor = "#ffb";
	},
	renderLists: function (event) {
		switch(event.keyCode) {
			case Event.KEY_LEFT:
			case Event.KEY_RIGHT:
			return;
			case Event.KEY_ESC:
			this.completerDiv.hide();
			this.hideMask();
			// esc will also empty input field in IE by default, prevent default event.
			Event.stop(event);
			this.index = -1;
			return;
			case Event.KEY_TAB:
			case Event.KEY_RETURN:
			if (!this.listsShowed()) return;
			this.updateField();
			Event.stop(event);
			return;
			case Event.KEY_DOWN:
			if (this.listsShowed()) {
				this.markNextList();
				return;
			}
			case Event.KEY_UP:
			if (this.listsShowed()) {
				this.markPrevList();
				return;
			}
		}
		if (this.observer) clearTimeout(this.observer);
		var self = this;
		this.observer = setTimeout(function () {self.onFieldChange(event)}, 100);				
	},
	onFieldChange: function (event) {
		var value = this.element.value.strip();
		var autoWidth = this.element.getWidth();
		var find = false;
		var listItems = "";
		this.counter = 0;
		this.index = -1;
		var self = this;
		// Down arrow key should render all lists items.
		if (Prototype.Browser.IE || this.value != value || event.keyCode == Event.KEY_DOWN) {
			this.value = value;
			// if items length > 2000, IE will slowly to render list, can limit items to render in ie.
			var foundItems = this.items.select(function(item){
				return item.include(self.value);
			});
			var size = foundItems.length > this.options.max ? this.options.max : foundItems.length;
			var loopItems = this.options.limit ? foundItems.slice(0, size) : foundItems;
			loopItems.each(function(item) {
				self.counter ++;
				// li element defaults line-height is different in different browser: 
				// safari 13px, firefox3 14px
				// li width must be set 100% and display block for IE, and ie scrollbar movement has related with li width, sign...
				// font size should be little than 14px in ie
				listItems += '<li style="' + self.liStyle + '">' + item + '</li>';
				if (!find) find = true;
			});
		}
		if (find) {
			this.list.innerHTML = listItems;
			var pos = Position.cumulativeOffset(this.element);
			this.completerDiv.style.width = this.mask.style.width = (autoWidth - 2) + "px";
			this.completerDiv.style.left = this.mask.style.left = pos[0] + "px";
			this.completerDiv.style.top = this.mask.style.top = (pos[1] + this.element.getHeight() - 1) + "px";
			this.completerDiv.style.display = "block";
			this.listElements = this.list.childElements();
			this.addListsObserver();
		} else {
			this.completerDiv.hide();
			this.hideMask();
		}			
	}		
});
