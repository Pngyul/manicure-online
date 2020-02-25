 //控制层 
app.controller('goodsController' ,function($scope,$controller,$location,goodsService,cartService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
	$scope.specificationItems={};//存储用户选择的规格
	$scope.skuList={}//存储sku
	
	//用户选择规格
	$scope.seleteSpecification=function(key,value){
		
		$scope.specificationItems[key]=value;
		//alert($scope.specificationItems[key]);
		//alert($scope.specificationItems);
		searchSku();
		
	}
	//判断某规格是否被选中
	$scope.isSelected=function(key,value){
		if($scope.specificationItems[key]==value){
			return true;
		}else{
			return false;
		}	
	}
	//数量加减
	$scope.num=1;
	$scope.addNum=function(x){
		$scope.num = parseInt($scope.num)+x;
		if($scope.num<1){
			$scope.num=1;
		}		
	}
	//页面初始化，加载商品数据
	
	$scope.findByGoodsId=function(){
		var id = $location.search().goodsId;
		goodsService.findByGoodsId(id).success(
				function(response){
					$scope.goods = response.goods;
					$scope.skuList = response.itemList;
					//alert(JSON.stringify($scope.skuList));
					$scope.goodsDesc=response.goodsDesc;
					$scope.goodsDesc.itemImages = JSON.parse(response.goodsDesc.itemImages);
					$scope.goodsDesc.specificationItems = JSON.parse(response.goodsDesc.specificationItems);
					$scope.category1Name = response.category1Name;
					$scope.category2Name = response.category2Name;
					$scope.category3Name = response.category3Name;
					loadSku();
				}
		);
	}
	
	//加载默认SKU
	loadSku=function(){
		//alert(JSON.stringify($scope.skuList));
		$scope.sku=$scope.skuList[0];
		//alert($scope.sku.spec);
		//alert($scope.sku);
		$scope.specificationItems= JSON.parse($scope.sku.spec) ;
	}
	
	//匹配两个对象
	matchObject = function(map1,map2){
		for(var k in map1){
			if(map1[k]!=map2[k]){
				return false;
			}
		}
		
		for(var k in map2){
			if(map2[k]!=map1[k]){
				return false;
			}
		}
		return true;
	}
	
	//查询SKU
	searchSku=function(){
		for(var i=0;i<$scope.skuList.length;i++ ){
			if( matchObject(JSON.parse($scope.skuList[i].spec) ,$scope.specificationItems ) ){
				
				$scope.sku=$scope.skuList[i];
				return ;
			}			
		}	
		$scope.sku={id:0,title:'--------',price:0};//如果没有匹配的		
	}
	
	//添加到购物车
	searchSku=function(){
		for(var i=0;i<$scope.skuList.length;i++ ){
			if( matchObject(JSON.parse($scope.skuList[i].spec) ,$scope.specificationItems ) ){
				
				$scope.sku=$scope.skuList[i];
				return ;
			}			
		}	
		$scope.sku={id:0,title:'--------',price:0};//如果没有匹配的		
	}
	
	//添加到购物车
	$scope.addGoodsToCartList=function(){
		cartService.addGoodsToCartList($scope.sku.id,$scope.num).success(
				function(response){
					if(response.success){
						alert(response.message);
						//location.href="cart.html";
					}else{
						alert(response.message);
					}
				}
		);
	}
	
	
});	
