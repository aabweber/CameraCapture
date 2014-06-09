var CameraCapture = {
	getPicture: function(success, failure){
		cordova.exec(success, failure, "CameraCapture", "get", []);
	}
};
