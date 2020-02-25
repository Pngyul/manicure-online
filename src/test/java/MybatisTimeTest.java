import com.manicure.entity.TbUser;
import com.manicure.entity.TbUserExample;
import com.manicure.mapper.TbUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath*:spring/*.xml")
public class MybatisTimeTest {

    @Autowired
    private TbUserMapper userMapper;

    @Test
    public void test(){
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //获取七天前的date
        Calendar c = Calendar.getInstance();
        /*c.set(Calendar.YEAR,2016);
        c.set(Calendar.MONTH,1);*/
        c.set(2016,0,1);
        Date time2016 = c.getTime();
        System.out.println("2016年时间："+time2016);
        c.add(Calendar.YEAR,1);
        Date time2017 = c.getTime();
        System.out.println("2017年时间："+time2017);
        //数据库筛选加入时间控制在7天内
        criteria.andCreatedBetween(time2016,time2017);
        List<TbUser> list = userMapper.selectByExample(example);
        if(list.size()>0){
            for(TbUser  TbUser:list){
                System.out.println(TbUser);
            }
        }
    }

    @Test
    public void test1(){
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //获取七天前的date
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE,-30);
        //数据库筛选加入时间控制在7天内
        System.out.println(new Date());
        System.out.println(c.getTime());

        criteria.andCreatedLessThanOrEqualTo(c.getTime());
        List<TbUser> list = userMapper.selectByExample(example);
        if(list.size()>0){
            for(TbUser  TbUser:list){
                System.out.println(TbUser);
            }
        }
    }

    @Test
     public void test2() {
           Calendar calendar = Calendar.getInstance();
        // 获取年
              int year =  calendar.get(Calendar.YEAR);
              // 获取月，这里需要需要月份的范围为0~11，因此获取月份的时候需要+1才是当前月份值
              int month = calendar.get(Calendar.MONTH) + 1;
              // 获取日
              int day = calendar.get(Calendar.DAY_OF_MONTH);
              // 获取时
              int hour = calendar.get(Calendar.HOUR);
              // int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24小时表示
              // 获取分
              int minute = calendar.get(Calendar.MINUTE);
              // 获取秒
              int second = calendar.get(Calendar.SECOND);
              // 星期，英语国家星期从星期日开始计算
              int weekday = calendar.get(Calendar.DAY_OF_WEEK);
              System.out.println("现在是" + year + "年" + month + "月" + day + "日" + hour+ "时" + minute + "分" + second + "秒" + "星期" + weekday);
          }
}
