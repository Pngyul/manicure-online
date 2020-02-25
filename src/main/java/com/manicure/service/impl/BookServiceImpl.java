package com.manicure.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.manicure.entity.TbGoods;
import com.manicure.entity.TbService;
import com.manicure.entity.TbServiceExample;
import com.manicure.mapper.TbServiceMapper;
import com.manicure.service.BookService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private TbServiceMapper serviceMapper;

    @Override
    public void add(TbService service) {
        //判断预约类型
        if("1".equals(service.getType())){//用户网上预约
            service.setStatus("0");//未审核
        }else{
            service.setStatus("1");//已接单
        }

        service.setCreateTime(new Date());//提交预约时间
        serviceMapper.insert(service);
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        for(Long id:ids){
            TbService service = serviceMapper.selectByPrimaryKey(id);
            service.setStatus(status);
            serviceMapper.updateByPrimaryKey(service);
        }

    }

    @Override
    public PageResult findPage(TbService service, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TbServiceExample example = new TbServiceExample();
        TbServiceExample.Criteria criteria = example.createCriteria();
        if (service != null) {

            if (service.getuId() != null && service.getuId().length() > 0) {
                criteria.andUIdLike("%" + service.getuId() + "%");
            }
            if (service.getStatus()!= null && service.getStatus().length() > 0) {
                criteria.andStatusLike("%" + service.getStatus() + "%");
            }
            if (service.getType()!= null && service.getType().length() > 0) {
                criteria.andTypeLike("%" + service.getType() + "%");
            }
        }
        Page<TbService> page = (Page<TbService>) serviceMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<TbService> getByUsername(String username) {
        TbServiceExample example = new TbServiceExample();
        example.createCriteria().andUIdEqualTo(username);
        List<TbService> list = serviceMapper.selectByExample(example);
        return list;
    }

    @Override
    public TbService finfOne(Long id) {
        return serviceMapper.selectByPrimaryKey(id);
    }
}
