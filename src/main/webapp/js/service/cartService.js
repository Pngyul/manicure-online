//服务层
app.service('cartService',function($http){
	    	
	
	
	//获取购物列表
	this.findCartList=function(){
		return $http.get('../cart/findCartList.do');
	}    
	
	//添加到购物车
	this.addGoodsToCartList=function(itemId,num){
		return $http.get('../cart/addGoodsToCartList.do?itemId='+itemId+"&num="+num);
	} 
	
	//求合计数
	this.total=function(cartList){
		var totalValue={totalNum:0,totalMoney:0,cartListSelected:[]};
			
		for(var i=0;i<cartList.length ;i++){
			var cart=cartList[i];//购物车对象
			if(cart.seleted==true){
				var orderItem=  cart.orderItem;
				totalValue.cartListSelected.push(orderItem);
				totalValue.totalNum+=orderItem.num;//累加数量
				totalValue.totalMoney+=orderItem.totalFee;//累加金额	
			}
		}
		return totalValue;
	}
//	cartListSelected

	//是否已选
	this.updateSelection=function(itemId,isSelected){
		return $http.get('../cart/updateSelection.do?itemId='+itemId+"&isSelected="+isSelected);
	} 
	
	//是否已选
	this.findCartListSelected=function(){
		return $http.get('../cart/findCartListSelected.do');
	} 
	
	//保存订单
	this.submitOrder=function(order){
		return $http.post('../order/add.do',order);		
	}
		
});
