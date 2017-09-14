package com.zbin.drools.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.zbin.drools.entity.CoachInfo;
import com.zbin.drools.entity.CoachInfoExample;

public interface CoachInfoMapper {
    int countByExample(CoachInfoExample example);

    int deleteByExample(CoachInfoExample example);

    int deleteByPrimaryKey(String phonenum);

    int insert(CoachInfo record);

    int insertSelective(CoachInfo record);

    List<CoachInfo> selectByExample(CoachInfoExample example);

    CoachInfo selectByPrimaryKey(String phonenum);

    int updateByExampleSelective(@Param("record") CoachInfo record, @Param("example") CoachInfoExample example);

    int updateByExample(@Param("record") CoachInfo record, @Param("example") CoachInfoExample example);

    int updateByPrimaryKeySelective(CoachInfo record);

    int updateByPrimaryKey(CoachInfo record);
}