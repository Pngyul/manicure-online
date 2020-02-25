//服务层
app.service('orderService',function($http){
	    	
	
	
	//根据id获取order和ordeItems  
	this.findById=function(id){
		return $http.get('../order/findById.do?id='+id);		
	};
	
	//查找订单
	this.findPage = function(page,rows,time){
		return $http.get('../order/findPage.do?page='+page+'&rows='+rows+'&time='+time);
	};

	//管理员查找订单

	this.search = function(page,rows,entity){
		return $http.post('../order/search.do?page='+page+'&rows='+rows,entity);
	};

	//更新状态
	this.updateStatus = function(ids,status){
		return $http.get('../order/updateStatus.do?ids='+ids+'&status='+status);
	}
});
