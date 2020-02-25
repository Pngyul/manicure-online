//服务层
app.service('bookService',function($http){
	
	 //预约
	this.add=function(entity){
		return $http.post('../book/add.do',entity);
	};

	//更改预约状态
	this.updateStatus=function(ids,status){
		return $http.get('../book/updateStatus.do?ids='+ids+"&status="+status);
	};

	//获取预约记录
	this.getByUsername=function(){
		return $http.get('../book/getByUsername.do');
	};

	//查找预约
	this.search=function(page,rows,entity){
		return $http.post('../book/search.do?page='+page+'&rows='+rows,entity);
	};

	//查看预约详情
	this.findOne=function(id){
		return $http.get('../book/findOne.do?id='+id);
	}
	

		
});
