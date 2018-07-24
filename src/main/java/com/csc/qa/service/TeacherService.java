package com.csc.qa.service;

import com.csc.qa.dao.TeacherDAO;
import com.csc.qa.dao.TeacherESDAO;
import com.csc.qa.entity.Question;
import com.csc.qa.entity.Teacher;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/4/15 0015.
 */
@Service
public class TeacherService {
    @Autowired
    private TeacherDAO teacherDAO;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private TeacherESDAO teacherESDAO;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public void setKeyWord(Teacher teacher){
        Teacher record = teacherDAO.selectByTeacherId(teacher.getTeacherid());
        if(record == null){//未设置过关键字
            teacherDAO.insertSelective(teacher);
        } else {//已设置过关键字
            teacher.setId(record.getId());
            teacherDAO.updateByPrimaryKeySelective(teacher);
        }
        teacherESDAO.save(teacher);//ES中保存老师，以便问题推送
    }

    public String getKeyWord(Integer teacherid){
        Teacher record = teacherDAO.selectByTeacherId(teacherid);
        if(record == null){
            return "";
        } else {
            return record.getKeyword() == null?"":record.getKeyword();
        }
    }

    public List<Question> search(Integer teacherid, String text){
        if(text == null || text.equals("") || text.length() == 0){
            return null;
        }

        Set<String> viewedQuestionIdSet = redisTemplate.opsForSet().members("teacher_" + teacherid);

        String[] viewdQuestionString = viewedQuestionIdSet == null || viewedQuestionIdSet.size() == 0
                ? new String[]{"0"}
                : viewedQuestionIdSet.toArray(new String[viewedQuestionIdSet.size()]);

        //在qa-question根据text搜索问题
        QueryBuilder queryBuilder = QueryBuilders
                .boolQuery()
                .mustNot(QueryBuilders.idsQuery().addIds(viewdQuestionString))//排除被推送过的问题
                .must(
                        QueryBuilders.queryStringQuery(text).field("content").field("keyword").boost(2.0f)//根据关键字查找
                );

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(0, 10))
                .withIndices("qa-question")
                .build()
                ;

        return elasticsearchTemplate.queryForList(searchQuery, Question.class);
    }
}
