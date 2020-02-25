//控制层 
app.controller('messageController' ,function($scope,$controller,messageService){

	$controller('baseController',{$scope:$scope});//继承

	$scope.status=['未读','已读'];
	
	//用户提交留言
	$scope.entity={};
	$scope.add=function(){
		$scope.entity.content=$("#areaCont").val();
		messageService.add($scope.entity).success(
			function(response){
				alert(response.message);
				location.href="message.html";
			}
		);
	};

	$scope.searchEntity={status:'0'};//定义搜索对象
	$scope.search=function(page,rows){
		messageService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	};

	$scope.findOne=function(id){
		messageService.findOne(id).success(
			function(response){
				$scope.entity = response;

			}
		);
	};


	$scope.deleOne=function(id){
		var ids=[id];
		messageService.dele(ids).success(
			function(response){
				if(!response.success){
					alert(response.message);
				}
				$scope.reloadList();

			}
		);
	};

	//批量删除
	$scope.dele=function(){
		//获取选中的复选框
		messageService.dele( $scope.selectIds ).success(
			function(response){
				if(response.flag){
					$scope.reloadList();//刷新列表
					$scope.selectIds = [];
				}
			}
		);
	};



});
