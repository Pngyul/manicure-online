app.controller("contentController",function($scope,$controller,contentService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
	$scope.contentList=[];//广告集合	
	$scope.findByCategoryId=function(categoryId){
		
		contentService.findByCategoryId(categoryId).success(
			function(response){
				$scope.contentList[categoryId]=response;
				
			}
		);		
	}	
	
	//搜索跳转
	$scope.search=function(){
		location.href="search.html#?keywords="+$scope.keywords;
	}
});