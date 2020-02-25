//服务层
app.service('adminGoodsService',function($http){
	    	
	//根据id获取商品信息
	this.add=function(entity){
		
		return $http.post('../goods/add.do',entity);		
	}
	
	//根据id获取商品信息
	this.search=function(page,rows,entity){
		
		return $http.post('../goods/search.do?page='+page+'&rows='+rows,entity);		
	}
	
	//根据id修改商品
	this.findOne=function(id){
		return $http.get('../goods/findOne.do?id='+id);		
	}
	
	//更新商品
	this.update=function(entity){
		return $http.post('../goods/update.do',entity);		
	}
	
	//更改状态
	this.updateStatus=function(ids,status){
		return $http.get('../goods/updateStatus.do?ids='+ids+"&status="+status);
	}  
	
	//删除
	this.dele=function(ids){
		return $http.get('../goods/delete.do?ids='+ids);
	}
	   	
});
