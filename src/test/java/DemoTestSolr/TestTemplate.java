package DemoTestSolr;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import entity.SolrItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext-solr.xml")
public class TestTemplate {

	@Autowired
	private SolrTemplate solrTemplate;
	
	@Test
	public void testAdd(){
		SolrItem item=new SolrItem();
		item.setId(1L);
		item.setCategory("家教有");
		item.setGoodsId(1L);
		item.setTitle("华为Mate9");
		item.setPrice(new BigDecimal(20));		
		solrTemplate.saveBean(item);
		solrTemplate.commit();
	}
	
	@Test
	public void testFindOne(){
		SolrItem item = solrTemplate.getById(1, SolrItem.class);
		System.out.println(item.getTitle());
	}
	
	@Test
	public void testDelete(){
		solrTemplate.deleteById("1");
		solrTemplate.commit();
	}
	
	@Test
	public void testAddList(){
		List<SolrItem> list=new ArrayList();
		
		for(int i=0;i<100;i++){
			SolrItem item=new SolrItem();
			item.setId(i+1L);
			item.setCategory("手机");
			item.setGoodsId(1L);
			item.setTitle("华为Mate"+i);
			item.setPrice(new BigDecimal(2000+i));	
			list.add(item);
		}
		
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}
	

    /*编写分页查询测试代码：*/
	
	@Test
	public void testPageQuery(){
		Query query=new SimpleQuery("*:*");
		query.setOffset(20);//开始索引（默认0）
		query.setRows(20);//每页记录数(默认10)
		ScoredPage<SolrItem> page = solrTemplate.queryForPage(query, SolrItem.class);
		System.out.println("总记录数："+page.getTotalElements());
		List<SolrItem> list = page.getContent();
		showList(list);
	}	
	//显示记录数据
	private void showList(List<SolrItem> list){		
		for(SolrItem item:list){
			System.out.println(item.getTitle() +item.getPrice());
		}		
	}
	
	@Test
	public void testPageQueryMutil(){	
		Query query=new SimpleQuery("*:*");
		Criteria criteria=new Criteria("item_title");
		criteria=criteria.and("item_keywords").is("美甲饰品");		
		query.addCriteria(criteria);
		//query.setOffset(20);//开始索引（默认0）
		//query.setRows(20);//每页记录数(默认10)
		ScoredPage<SolrItem> page = solrTemplate.queryForPage(query, SolrItem.class);
		System.out.println("总记录数："+page.getTotalElements());
		List<SolrItem> list = page.getContent();
		showList(list);
	}
	
	@Test
	public void testDeleteAll(){
		Query query=new SimpleQuery("*:*");
		solrTemplate.delete(query);
		solrTemplate.commit();
	}
}
