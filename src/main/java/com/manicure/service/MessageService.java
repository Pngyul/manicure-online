package com.manicure.service;

import com.manicure.entity.TbMessage;
import entity.PageResult;

public interface MessageService {


    /**
     * 提交留言
     * @param message
     */
    public void add(TbMessage message);


    /**
     * 管理员获取留言
     * @param message
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult findPage(TbMessage message, int pageNum, int pageSize);

    /**
     * 管理员更新留言状态
     * @param ids
     * @param status
     */
    public void updateStatus(Long[] ids, String status);


    /**
     * 查看留言详情
     * @param id
     * @return
     */
    public TbMessage findOne(Long id);


    /**
     * 删除留言，并不是真正的删除
     * @param ids
     */
    public void dele(Long[] ids);

}
