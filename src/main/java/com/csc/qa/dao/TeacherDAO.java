package com.csc.qa.dao;

import com.csc.qa.entity.Teacher;

public interface TeacherDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(Teacher record);

    int insertSelective(Teacher record);

    Teacher selectByPrimaryKey(Integer id);

    Teacher selectByTeacherId(Integer teacherid);

    int updateByPrimaryKeySelective(Teacher record);

    int updateByPrimaryKey(Teacher record);

    int updateByTeacherId(Teacher record);
}