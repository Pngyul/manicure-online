//服务层
app.service('goodsService',function($http){
	    	
	//根据id获取商品信息
	this.findByGoodsId=function(id){
		return $http.get('../goods/findByGoodsId.do?id='+id);		
	}
	
	   	
});
