package DemoTestSolr;


import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.manicure.entity.TbItem;
import com.manicure.entity.TbItemExample;
import com.manicure.entity.TbItemExample.Criteria;
import com.manicure.mapper.TbItemMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:spring/*.xml")
public class SolrUtil {
	@Autowired
	private TbItemMapper itemMapper;
	
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
	}	

	@Test
	public void testAdd(){
		importItemData();
		
		}
	}
