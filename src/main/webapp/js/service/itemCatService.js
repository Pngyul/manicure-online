//服务层
app.service('itemCatService',function($http){
	    	
	//根据id获取商品信息
	this.findByParentId=function(id){
		return $http.get('../itemCat/findByParentId.do?id='+id);		
	}
	
	
	//根据id获取商品信息
	this.findOne=function(id){
		
		return $http.get('../itemCat/findOne.do?id='+id);		
	}
	
	//根据id获取商品信息
	this.findAll=function(){
		
		return $http.get('../itemCat/findAll.do');		
	}
	
	
	
});