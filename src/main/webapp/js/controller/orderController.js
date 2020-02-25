 //控制层 
app.controller('orderController' ,function($scope,$location,$controller,orderService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
	
	//根据id获取order和ordeItems  
	$scope.findById=function(){
		$scope.orderId = $location.search().orderId;
		orderService.findById($scope.orderId).success(
			function(response){
				if(response.result.success){
					$scope.order=response.order;
					//alert($scope.order.orderId);
					$scope.orderItemList=response.orderItemList
				}else{
					alert(response.result.message);
				}
			}			
		);
	};
	
	
	//分页查看订单列表
	$scope.time="";
	$scope.findByPage=function(page,rows){
		orderService.findPage(page,rows,$scope.time).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};

	$scope.paymenyType=['','微信支付','货到付款'];
	$scope.status=['','未付款','已付款','未发货','已发货','交易成功','交易关闭'];
	//管理员查看订单列表
	$scope.searchEntity={};
	$scope.search=function(page,rows){
		orderService.search(page,rows,$scope.searchEntity).success(
			function(response){

				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	};

	//批量发货
	$scope.updateStatus=function(status){
		alert($scope.selectIds);
		orderService.updateStatus($scope.selectIds,status).success(
			function(response){
				$scope.reloadList();
			}
		);
	};



	$scope.updateOneStatus=function(id,status){
		$scope.id=[id];
		orderService.updateStatus(id,status).success(
			function(response){
				$scope.reloadList();
			}
		);
	};

	//时间筛选订单
	$scope.$watch('time', function(newValue, oldValue) {
		$scope.reloadList();
	});
	
});	
