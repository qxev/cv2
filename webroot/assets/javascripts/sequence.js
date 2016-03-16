function sequenceTrust(selector, cacheKey) {
	jQuery(selector).change(function() {
		rebuildSequence(this, selector, cacheKey);
	});
	jQuery(selector).each(function(i) {
		jQuery(this).data(cacheKey, this.value);
	});
}
// 被rebuildSequence方法替换了，因为rebuildSequence方法有修复功能
function updateSequence(element, selector, cacheKey) {
	var id = jQuery(element).attr("id");
	var current = jQuery(element).attr("value");
	var old = jQuery(element).data(cacheKey);

	if (!/\d+/.test(current) || current === "0" || current > jQuery(selector).length) {
		alert("请输入1到" + jQuery(selector).length + "的数字");
		jQuery(element).attr("value", old);
		return;
	}

	jQuery(selector).each(function(i) {
		if (this.id !== id) {
			if (current > old) {
				if (this.value > old && this.value <= current) {
					this.value = this.value - 1;
				}
			} else {
				if (this.value < old && this.value >= current) {
					this.value = this.value - 0 + 1;
				}
			}
		}
		jQuery(this).data(cacheKey, this.value);
	});
}

function rebuildSequence(element, selector, cacheKey, isDel) {
	var length = jQuery(selector).length;
	var id = jQuery(element).attr("id");
	var current = isDel ? length : jQuery(element).attr("value");
	var old = jQuery(element).data(cacheKey);
	

	if(isDel){
		jQuery(element).removeData(cacheKey);
	}else{
		if (!/\d+/.test(current) || current === "0" || current > length) {
			alert("请输入1到" + length + "的数字");
			jQuery(element).attr("value", old);
			return;
		}
	}

	var list = jQuery(selector);

	list = jQuery.grep(list, function(e, i) {
		return e.id !== id;
	});

	list.sort(function(a, b) {
		return a.value - b.value
	});

	jQuery.each(list, function(i, e) {
		if (i < current - 1) {
			e.value = i + 1;
		} else {
			e.value = i + 2;
		}
		jQuery(e).data(cacheKey, e.value);
	});

	jQuery(element).data(cacheKey, current);
}