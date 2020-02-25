package com.manicure.mapper;

import com.manicure.entity.TbService;
import com.manicure.entity.TbServiceExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbServiceMapper {
    int countByExample(TbServiceExample example);

    int deleteByExample(TbServiceExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbService record);

    int insertSelective(TbService record);

    List<TbService> selectByExample(TbServiceExample example);

    TbService selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbService record, @Param("example") TbServiceExample example);

    int updateByExample(@Param("record") TbService record, @Param("example") TbServiceExample example);

    int updateByPrimaryKeySelective(TbService record);

    int updateByPrimaryKey(TbService record);
}