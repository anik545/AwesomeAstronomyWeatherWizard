function remove(selector) {
//	try {
		var node = document.querySelector(selector);
		console.log(node);
		node.parentNode.removeChild(node);
//	} catch(err) {
//		throw err;
//	}
}

function removeall() {
	remove("#RightMenu");
	remove("#map > div.ol-viewport > div.ol-overlaycontainer");
	remove("#rightMenuButtonCont");
	remove("#map > div.olControlZoom.toolBar");
	remove("#map > div.ol-viewport > div");
	remove("body > div.jqmOverlay");
	remove("#dialog");
}

removeall();
