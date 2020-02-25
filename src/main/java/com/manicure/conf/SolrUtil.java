package com.manicure.conf;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import com.manicure.entity.TbItem;
import com.manicure.entity.TbItemExample;
import com.manicure.entity.TbItemExample.Criteria;
import com.manicure.mapper.TbItemMapper;

@Component
public class SolrUtil {
	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private SolrTemplate solrTemplate;
	
	/**
	 * 导入商品数据
	 */
	public void importItemData(){
		TbItemExample example=new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andStatusEqualTo("1");
		List<TbItem> itemList = itemMapper.selectByExample(example);
		System.out.println("===商品列表===");
		for(TbItem item:itemList){
			System.out.println(item.getTitle());			
		}		
		System.out.println("===结束===");	
		
		solrTemplate.saveBeans(itemList);
		solrTemplate.commit();
	}	

}
