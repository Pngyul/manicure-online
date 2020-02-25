package com.manicure.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.manicure.entity.TbMessage;
import com.manicure.entity.TbMessageExample;
import com.manicure.mapper.TbMessageMapper;
import com.manicure.service.MessageService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private TbMessageMapper messageMapper;

    @Override
    public void add(TbMessage message) {
        message.setStatus("0");//未读
        message.setCreateTime(new Date());//提交预约时间
        messageMapper.insert(message);
    }

    @Override
    public void updateStatus(Long[] ids, String status) {
        for(Long id:ids){
            TbMessage message = messageMapper.selectByPrimaryKey(id);
            message.setStatus(status);
            messageMapper.updateByPrimaryKey(message);
        }

    }
    @Override
    public TbMessage findOne(Long id) {
        //点击查看更改留言状态
        TbMessage message = messageMapper.selectByPrimaryKey(id);
        if("0".equals(message.getStatus())){
            message.setStatus("1");
            messageMapper.updateByPrimaryKey(message);
        }
        return message;
    }

    @Override
    public void dele(Long[] ids) {
        for(Long id:ids){
            TbMessage message = messageMapper.selectByPrimaryKey(id);
            message.setIsDelete("1");
            messageMapper.updateByPrimaryKey(message);
        }

    }

    @Override
    public PageResult findPage(TbMessage message, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        TbMessageExample example = new TbMessageExample();
        TbMessageExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteIsNull();
        if (message != null) {
            if (message.getuId() != null && message.getuId().length() > 0) {
                criteria.andUIdLike("%" + message.getuId() + "%");
            }
            if (message.getStatus()!= null && message.getStatus().length() > 0) {
                criteria.andStatusLike("%" + message.getStatus() + "%");
            }
        }
        Page<TbMessage> page = (Page<TbMessage>) messageMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }



}
