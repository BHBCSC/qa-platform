package com.csc.qa.dao;

import com.csc.qa.entity.Teacher;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by Administrator on 2018/4/17 0017.
 */
public interface TeacherESDAO extends ElasticsearchRepository<Teacher, Integer> {
}
