package com.manicure.service;

import com.manicure.entity.TbService;
import entity.PageResult;

import java.util.List;

public interface BookService {

    /**
     * 预约服务
     * @param service
     */
    public void add(TbService service);

    /**
     * 获取用胡预约记录
     * @param username
     * @return
     */
    public List<TbService> getByUsername(String username);

    /**
     * 后台获取预约记录/帅选
     * @param service
     * @return
     */
    public PageResult findPage(TbService service,int pageNum, int pageSize);

    /**
     * 管理员更新预约状态
     * @param ids
     * @param status
     */
    public void updateStatus(Long[] ids, String status);


    /**
     * 查看订单详情
     * @param id
     * @return
     */
    public TbService finfOne(Long id);

}
