package com.manicure.service;

import java.util.List;
import java.util.Map;

import com.manicure.entity.TbGoods;
import com.manicure.entity.TbItem;

import entity.Goods;
import entity.PageResult;

public interface GoodsService {

	
	/**
	 * 用户查看商品
	 * @param id
	 * @return
	 */
	public Map findByGoodsId(Long id);
	
	/**
	 * 管理员添加商品
	 * @param goods
	 */
	public void add(Goods goods);
	
	
	/**
	 *管理员查看商品列表分页
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int page, int rows);

	/**
	 * 根据id修改商品
	 * @param id
	 * @return
	 */
	public Goods findOne(Long id);
	
	/**
	 * 更新商品
	 * @param goods
	 */
	public void update(Goods goods);
	
	/**
	 * 批量修改状态
	 * @param ids
	 * @param status
	 */
	public void updateStatus(Long []ids,String status);

	/**
	 * 删除该商品，并不是真正意义的删除
	 * @param ids
	 */
	public void delete(Long[] ids);
	
	/**
	 * 根据商品ID和状态查询Item表信息  
	 * @param goodsId
	 * @param status
	 * @return
	 */
	public List<TbItem> findItemListByGoodsIdandStatus(Long[] goodsIds, String status );
}
