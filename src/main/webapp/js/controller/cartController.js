 //控制层 
app.controller('cartController' ,function($scope,$controller,cartService,addressService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
	$scope.cartList={}
	//获取购物列表
	$scope.findCartList=function(){
		cartService.findCartList().success(
				function(response){
					$scope.cartList = response;
					$scope.totalValue= cartService.total($scope.cartList);
					//alert(JSON.stringify($scope.totalValue.cartListSelected));
					//$scope.cartListSelected = cartService.cartListSelected($scope.cartList);
				}
		);
	}
	
	//数量加减
	$scope.addGoodsToCartList=function(itemId,num){
		cartService.addGoodsToCartList(itemId,num).success(
			function(response){
				if(response.success){//如果成功
					$scope.findCartList();//刷新列表
				}else{
					alert(response.message);
				}				
			}		
		);		
	}
	

	//更新是否选择该商品
	$scope.updateSelection = function($event,itemId){
		var isSelected  = 0;
		if($event.target.checked){
			isSelected=1;	
		}
		cartService.updateSelection(itemId,isSelected).success(
				function(response){
					if(response.success){//如果成功
						$scope.findCartList();//刷新列表
					}else{
						alert(response.message);
					}				
				}		
			);
	}
	
	//查找所有地址
	$scope.findAllAddress =function(){
		addressService.findAllAddress().success(
			function(response){
				$scope.addressList=response;
				//alert("55");
				//alert(response=="");
				//设置默认地址
				for(var i=0;i< $scope.addressList.length;i++){
					if($scope.addressList[i].isDefault=='1'){
						$scope.address=$scope.addressList[i];
						break;
					}				
				}
				if($scope.addressList.length==0){
					$scope.address==null;
				}
			}		
		);
		
	}
	
	//选择地址
	$scope.selectAddress=function(address){
		$scope.address=address;		
	}
	
	//判断是否是当前选中的地址
	$scope.isSelectedAddress=function(address){
		if(address==$scope.address){
			return true;
		}else{
			return false;
		}		
	}
	
	//保存 地址
	$scope.save=function(){	
		var serviceObject;//服务层对象  
		if($scope.entity.id!=null){//如果有ID
			serviceObject=addressService.update( $scope.entity ); //修改  
		}else{
			serviceObject=addressService.add( $scope.entity  );//增加 
		}	
		serviceObject.success(
			function(response){
				if(response.flag){
					//重新查询 
					$scope.findAllAddress();
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	//选择支付方式
	$scope.order={paymentType:'1'};	
	$scope.selectPayType=function(type){
		$scope.order.paymentType= type;
	}
	
	//保存订单
	$scope.submitOrder=function(){
		if($scope.address==null){
			alert("请选择或者新增收货地址");
			return;
		}
		
		$scope.order.receiverAreaName=$scope.address.address;//地址
		$scope.order.receiverMobile=$scope.address.mobile;//手机
		$scope.order.receiver=$scope.address.contact;//联系人
		cartService.submitOrder( $scope.order ).success(
			function(response){
				if(response.result.success){
					//页面跳转
					if($scope.order.paymentType=='1'){//如果是微信支付，跳转到支付页面
						location.href="pay.html?id="+response.orderId+"&pay="+response.money;
					}else{//如果货到付款，跳转到提示页面
						location.href="paysuccess.html?orderId="+response.orderId+"&pay="+response.money+"&payType="+$scope.order.paymentType;
					}					
				}else{
					alert(response.result.message);	//也可以跳转到提示页面				
				}				
			}				
		);		
	}
	
	
});	
