package com.manicure.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.manicure.entity.TbGoods;
import com.manicure.entity.TbGoodsDesc;
import com.manicure.entity.TbGoodsExample;
import com.manicure.entity.TbItem;
import com.manicure.entity.TbItemCat;
import com.manicure.entity.TbItemExample;
import com.manicure.entity.TbItemExample.Criteria;
import com.manicure.mapper.TbGoodsDescMapper;
import com.manicure.mapper.TbGoodsMapper;
import com.manicure.mapper.TbItemCatMapper;
import com.manicure.mapper.TbItemMapper;
import com.manicure.service.GoodsService;

import entity.Goods;
import entity.PageResult;

@Service
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;

	@Autowired
	private TbGoodsDescMapper goodsDescMapper;

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public Map findByGoodsId(Long id) {
		Map map = new HashMap<>();
		// 1.获取spu商品
		TbGoods goods = goodsMapper.selectByPrimaryKey(id);
		map.put("goods", goods);
		// 2.获取商品spu Desc
		TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		map.put("goodsDesc", goodsDesc);
		// 3.获取商品分类
		String category1Name = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
		String category2Name = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
		String category3Name = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
		map.put("category1Name", category1Name);
		map.put("category2Name", category2Name);
		map.put("category3Name", category3Name);
		// 4.获取sku列表
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);// 指定SPUid
		criteria.andStatusEqualTo("1");// 状态为有效
		example.setOrderByClause("is_default desc");// 按照状态降序，保证第一个为默
		List<TbItem> itemList = itemMapper.selectByExample(example);
		map.put("itemList", itemList);
		return map;
	}

	@Override
	public void add(Goods goods) {
		goods.getGoods().setIsMarketable("1");//上架
		goodsMapper.insert(goods.getGoods());
		// System.out.println("id："+goods.getGoods());
		goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());// 设置ID
		goodsDescMapper.insert(goods.getGoodsDesc());// 插入商品扩展数据
		saveItemList(goods);
	}



	@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		TbGoodsExample example = new TbGoodsExample();
		com.manicure.entity.TbGoodsExample.Criteria criteria = example.createCriteria();
		criteria.andIsDeleteIsNull();
		if (goods != null) {
			if (goods.getGoodsName() != null && goods.getGoodsName().length() > 0) {
				criteria.andGoodsNameLike("%" + goods.getGoodsName() + "%");
			}
			if (goods.getIsMarketable() != null && goods.getIsMarketable().length() > 0) {
				criteria.andIsMarketableLike("%" + goods.getIsMarketable() + "%");
			}
			if (goods.getCaption() != null && goods.getCaption().length() > 0) {
				criteria.andCaptionLike("%" + goods.getCaption() + "%");
			}
		}
		Page<TbGoods> page = (Page<TbGoods>) goodsMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}
	
	@Override
	public Goods findOne(Long id) {
		Goods goods=new Goods();
		TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
		goods.setGoods(tbGoods);
		TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setGoodsDesc(tbGoodsDesc);
		//查询SKU商品列表
		TbItemExample example=new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(id);//查询条件：商品ID
		List<TbItem> itemList = itemMapper.selectByExample(example);		
		goods.setItemList(itemList);
		return goods;
	}

	@Override
	public void update(Goods goods) {
		goodsMapper.updateByPrimaryKey(goods.getGoods());//保存商品表
		goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());//保存商品扩展表
		//删除原有的sku列表数据		
		TbItemExample example=new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andGoodsIdEqualTo(goods.getGoods().getId());	
		itemMapper.deleteByExample(example);
		//添加新的sku列表数据
		saveItemList(goods);//插入商品SKU列表数据
		
	}
	
	private void saveItemList(Goods goods){	
		if ("1".equals(goods.getGoods().getIsEnableSpec())) {
			for (int i = 0; i < goods.getItemList().size(); i++) {
				TbItem item = goods.getItemList().get(i);
				// 标题
				String title = goods.getGoods().getGoodsName();
				Map<String, Object> specMap = JSON.parseObject(item.getSpec());
				for (String key : specMap.keySet()) {
					title += " " + specMap.get(key);
				}
				item.setTitle(title);
				setItemValus(goods,item,i);
				itemMapper.insert(item);
			}
		} else {
			TbItem item = new TbItem();
			item.setTitle(goods.getGoods().getGoodsName());// 商品SPU+规格描述串作为SKU名称
			item.setPrice(goods.getGoods().getPrice());// 价格
			item.setStatus("1");// 状态
			item.setIsDefault("1");// 是否默认
			item.setNum(99999);// 库存数量
			item.setSpec("{}");
			setItemValus(goods, item,0);
			itemMapper.insert(item);
		}
	}
	
	private void setItemValus(Goods goods, TbItem item,int i) {
		item.setGoodsId(goods.getGoods().getId());// 商品SPU编号
		item.setCategoryid(goods.getGoods().getCategory3Id());// 商品分类编号（3级）
		item.setCreateTime(new Date());// 创建日期
		item.setUpdateTime(new Date());// 修改日期
		// 分类名称
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
		item.setCategory(itemCat.getName());
		// 图片地址（取spu的第一个图片）
		List<Map> imageList = JSON.parseArray(goods.getGoodsDesc().getItemImages(), Map.class);
		if (imageList.size() > 0) {
			item.setImage((String) imageList.get(0).get("url"));
			if (imageList.get(i) != null) {
				item.setImage((String) imageList.get(i).get("url"));
			}
		}

	}
	
	public void updateStatus(Long[] ids, String status) {
		for(Long id:ids){
			TbGoods goods = goodsMapper.selectByPrimaryKey(id);
			goods.setIsMarketable(status);
			goodsMapper.updateByPrimaryKey(goods);
		}
	}

	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			TbGoods goods = goodsMapper.selectByPrimaryKey(id);
			goods.setIsDelete("0");
			goodsMapper.updateByPrimaryKey(goods);
		}
	}

	@Override
	public List<TbItem> findItemListByGoodsIdandStatus(Long[] goodsIds, String status) {
		TbItemExample example=new TbItemExample();
	    Criteria criteria = example.createCriteria();
		criteria.andGoodsIdIn(Arrays.asList(goodsIds));
		criteria.andStatusEqualTo(status);
		return itemMapper.selectByExample(example);
	}
}
