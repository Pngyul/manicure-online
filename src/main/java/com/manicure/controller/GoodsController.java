package com.manicure.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.manicure.entity.TbGoods;
import com.manicure.entity.TbItem;
import com.manicure.service.GoodsService;
import com.manicure.service.ItemSearchService;

import entity.Goods;
import entity.PageResult;
import entity.Result;

@RestController
@RequestMapping("/goods")
public class GoodsController {
	
	@Autowired
	private GoodsService goodsService;
	
	@Autowired
	private ItemSearchService itemSearchService;
	
	@RequestMapping("/findByGoodsId")
	public Map findByGoodsId(Long id){
		return goodsService.findByGoodsId(id);
		
	}
	
	@RequestMapping("/add")
	public Result add(@RequestBody Goods goods){
		try {
			goodsService.add(goods);
			
			//更新solr索引库
			Long[] a= new Long[1];
			a[0]=goods.getGoods().getId();
			List<TbItem> itemList = goodsService.findItemListByGoodsIdandStatus(a,"1");
			if(itemList.size()>0){				
				itemSearchService.importList(itemList);
			}else{
				System.out.println("没有明细数据");
			}
			
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}
	
	@RequestMapping("/findOne")
	public Goods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	@RequestMapping("/update")
	public Result update(@RequestBody Goods goods){
		try {
			goodsService.update(goods);
			//先删除solr索引库
			/*List list = new ArrayList<>();
			list.add(goods.getGoods().getId());*/
			Long[] a= new Long[1];
			a[0]=goods.getGoods().getId();
			itemSearchService.deleteByGoodsIds(Arrays.asList(a));
			//后再加入索引库
			List<TbItem> itemList = goodsService.findItemListByGoodsIdandStatus(a,"1");
			if(itemList.size()>0){				
				itemSearchService.importList(itemList);
			}else{
				System.out.println("没有明细数据");
			}
			
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}
	
	@RequestMapping("/updateStatus")
	public Result updateStatus(Long[] ids, String status){
		try {
			goodsService.updateStatus(ids, status);
			//上架，更新solr索引库
			if(status.equals("1")){
				List<TbItem> itemList = goodsService.findItemListByGoodsIdandStatus(ids,"1");
				if(itemList.size()>0){				
					itemSearchService.importList(itemList);
				}else{
					System.out.println("没有明细数据");
				}
			}else{//下架，更新solr索引库
				itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			}
			return new Result(true, "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "操作失败");
		}
	}
	
	@RequestMapping("/delete")
	public Result delete(Long[] ids){
		try {
			goodsService.delete(ids);
			//更新索引库
			itemSearchService.deleteByGoodsIds(Arrays.asList(ids));
			return new Result(true, "操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "操作失败");
		}
	}

}
