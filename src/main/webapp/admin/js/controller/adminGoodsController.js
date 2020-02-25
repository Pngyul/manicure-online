 //控制层 
app.controller('adminGoodsController' ,function($scope,$controller,$location,adminGoodsService,uploadService,itemCatService,typeTemplateService){	
	
	$controller('baseController',{$scope:$scope});//继承
	
	/**
	 * 添加商品
	 */
	$scope.save= function(){
		$scope.entity.goodsDesc.introduction=editor.html();
		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=adminGoodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=adminGoodsService.add( $scope.entity  );//增加 
		}
		serviceObject.success(
			function(response){
				if(response.success){
					alert("保存成功!");
					$scope.entity={};
					editor.html('');//清空富文本编辑器
				}else{
					alert(response.message);
				}
			
		});
	}
	
	/**
	 * 上传图片
	 */
	$scope.uploadFile=function(){	  
		uploadService.uploadFile().success(function(response) {        	
        	if(response.success){//如果上传成功，取出url
        		$scope.image_entity.url=response.message;//设置文件地址
        	}else{
        		alert(response.message);
        	}
        }).error(function() {           
        	     alert("上传发生错误");
        });        
    };
    
    $scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]}};//定义页面实体结果
    
    /**
     * 添加到图片列表
     */
    $scope.add_image_entity = function(){
    	$scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    };
    
   /**
    * 列表中移除图片
    */
    $scope.remove_image_entity=function(index){
    	 $scope.entity.goodsDesc.itemImages.splice(index,1);
    }
    
    
    /**
     * 读取一级分类
     */
	$scope.selectItemCat1List=function(){
	      itemCatService.findByParentId(0).success(
	    		 function(response){
	    			 $scope.itemCat1List=response; 
	    		 }
	      );
	}   
	
	/**
	 * 读取二级分类
	 */
	$scope.$watch('entity.goods.category1Id', function(newValue, oldValue) {          
	    	//根据选择的值，查询二级分类
	    	itemCatService.findByParentId(newValue).success(
	    		function(response){
	    			$scope.itemCat2List=response; 	    			
	    		}
	    	);    	
	}); 
	
	/**
	 * 读取三级分类
	 */
	$scope.$watch('entity.goods.category2Id', function(newValue, oldValue) {          
	    	//根据选择的值，查询二级分类
	    	itemCatService.findByParentId(newValue).success(
	    		function(response){
	    			$scope.itemCat3List=response; 	    			
	    		}
	    	);    	
	 });
	
	//三级分类选择后  读取模板ID
    $scope.$watch('entity.goods.category3Id', function(newValue, oldValue) {   
       	itemCatService.findOne(newValue).success(
       		  function(response){
       			    $scope.entity.goods.typeTemplateId=response.typeId; //更新模板ID    
       		  }
        );    
    });
    
    //模板ID选择后  更新模板对象
    $scope.$watch('entity.goods.typeTemplateId', function(newValue, oldValue) {    
    	/*typeTemplateService.findOne(newValue).success(
       		  function(response){
       			  $scope.typeTemplate=response;//获取类型模板
       			  $scope.typeTemplate.brandIds= JSON.parse( $scope.typeTemplate.brandIds);//品牌列表
       			  $scope.entity.goodsDesc.customAttributeItems=JSON.parse( $scope.typeTemplate.customAttributeItems);//扩展属性
       		  }
        );*/ 
    	//查询规格列表
    	typeTemplateService.findSpecList(newValue).success(
    		  function(response){
    			  $scope.specList=response;
    		  }
    	);    	
    }); 
    
    $scope.updateSpecAttribute=function($event,name,value){
    	var object= $scope.searchObjectByKey(
    			$scope.entity.goodsDesc.specificationItems ,'attributeName', name);		
    	if(object!=null){	
			if($event.target.checked ){
				object.attributeValue.push(value);		
			}else{//取消勾选				
				object.attributeValue.splice( object.attributeValue.indexOf(value ) ,1);//移除选项
				//如果选项都取消了，将此条记录移除
				if(object.attributeValue.length==0){
					$scope.entity.goodsDesc.specificationItems.splice(
					$scope.entity.goodsDesc.specificationItems.indexOf(object),1);
				}				
			}
		}else{				
			$scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
		}		
	};
    
   // 创建SKU的信息:
	$scope.createItemList = function(){
		// 初始化基础数据:
		$scope.entity.itemList = [{spec:{},price:0,num:9999,status:'0',isDefault:'0'}];
		
		var items = $scope.entity.goodsDesc.specificationItems;
		
		for(var i=0;i<items.length;i++){
			// 
			$scope.entity.itemList = addColumn($scope.entity.itemList,items[i].attributeName,items[i].attributeValue);
		}
	};
	
	addColumn = function(list,columnName,columnValues){
		// 定义一个集合用于保存生成的每行的数据:
		var newList = [];
		// 遍历该集合的数据:
		for(var i=0;i<list.length;i++){
			var oldRow = list[i];
			for(var j=0;j<columnValues.length;j++){
				// 对oldRow数据进行克隆:
				var newRow = JSON.parse( JSON.stringify(oldRow) );
				newRow.spec[columnName]=columnValues[j];
				// 将newRow存入到newList中
				newList.push(newRow);
			}
			
		}
		
		return newList;
	};
	
	$scope.searchEntity={};//定义搜索对象 
	$scope.list=[];
	//搜索   商品列表
	$scope.search=function(page,rows){			
		adminGoodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};
	
	$scope.status=['已下架','正常'];//商品状态
	
	$scope.itemCatList=[];//商品分类列表
	//加载商品分类列表
	$scope.findItemCatList=function(){		
		itemCatService.findAll().success(
				function(response){							
					for(var i=0;i<response.length;i++){
						$scope.itemCatList[response[i].id]=response[i].name;
					}
				}
		);
	};
	
	//查询实体 
	$scope.findOne=function(){			
		var id= $location.search()['id'];//获取参数值
		if(id==null){
			return ;
		}
		adminGoodsService.findOne(id).success(
			function(response){
				$scope.entity= response;
				//向富文本编辑器添加商品介绍
				editor.html($scope.entity.goodsDesc.introduction);
				//显示图片列表
				$scope.entity.goodsDesc.itemImages=  
				JSON.parse($scope.entity.goodsDesc.itemImages);
				//规格				
				$scope.entity.goodsDesc.specificationItems=JSON.parse($scope.entity.goodsDesc.specificationItems);
				//SKU列表规格列转换				
				for( var i=0;i<$scope.entity.itemList.length;i++ ){
					$scope.entity.itemList[i].spec = 
					JSON.parse( $scope.entity.itemList[i].spec);		
				}	
			}
		);				
	};
	
	//修改商品时回显该商品的具体规格
	$scope.checkAttributeValue = function(specName,optionName){
		var items = $scope.entity.goodsDesc.specificationItems;
		var object = $scope.searchObjectByKey(items,"attributeName",specName);
		if(object != null){
			if(object.attributeValue.indexOf(optionName)>=0){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	};
	
	//更改状态
	$scope.updateStatus=function(status,message){
		if(confirm(message)){
			adminGoodsService.updateStatus($scope.selectIds,status).success(
				function(response){
					if(response.success){//成功
						$scope.reloadList();//刷新列表
						$scope.selectIds=[];//清空ID集合
					}else{
						alert(response.message);
					}
				}
			);
		}
	};
	
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		adminGoodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.flag){
					$scope.reloadList();//刷新列表
					$scope.selectIds = [];
				}						
			}		
		);				
	};
	

});	
