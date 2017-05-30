var loadImg_complete_sum = 0;
var imgUrlArr = new Array();
var loadingRate;

function loadImgs(imgsUrl,object){
	imgUrlArr = imgsUrl;
	var isAllComplete = true;
	for(var i = 0 ; i <imgUrlArr.length ; i++){
		var img = new Image();
		img.src = imgUrlArr[i];
		if(!img.complete){
			isAllComplete = false;
		}
	}
	if (isAllComplete) {
		clearInterval(loadingImgs);
		loadComplete(0);
	}else{
		$(".loading-container").css("display","block");
		for(var i = 0 ; i <imgUrlArr.length ; i++){
			loadImg(imgUrlArr[i]);
		}
		var loadingImgs = setInterval(function(){
			loadingRate = (loadImg_complete_sum/imgUrlArr.length)*100+"%";
			object.width(loadingRate);
			if (loadingRate=="100%") {
				clearInterval(loadingImgs);
			}
		},10)
	}
}

function loadImg(url){
	var img = new Image();
	img.src = url;
	img.onload = function(){
		loadImg_complete_sum++;
	}
}

var loadingImgs = setInterval(function(){
	loadingRate = (loadImg_complete_sum/imgUrlArr.length)*100+"%";
	if (loadingRate=="100%") {
		clearInterval(loadingImgs);
		loadComplete(1100);
	}
},10)