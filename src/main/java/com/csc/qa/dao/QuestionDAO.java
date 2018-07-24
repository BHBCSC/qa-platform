package com.csc.qa.dao;

import com.csc.qa.entity.Question;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QuestionDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKey(Question record);

    List<Question> selectByStudentId(Integer id);

    List<Question> selectByTeacherId(Integer id);

    int increaseStatusByPrimaryKeyAndTeacherId(@Param("id") Integer id, @Param("teacherid") Integer teacherid);
}