package com.csc.qa;

import com.csc.qa.dao.QuestionDAO;
import com.csc.qa.entity.Question;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by Administrator on 2018/4/13 0013.
 */

public class QuestionTest extends ApplicationTest{
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    ElasticsearchTemplate template;

    @Test
    public void testA(){
        Question question = new Question();
        question.setStatus("2");
        question.setStudentid(2);
        question.setContent("df");
        question.setKeyword("df");
        questionDAO.insertSelective(question);
        System.out.println(question.getId());
    }

    @Test
    public void test(){
        QueryBuilder queryBuilder = QueryBuilders
                .queryStringQuery("Java分布式Dubbo微服务");

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(0, 10))
                .withIndices("qa-teacher")
                .build()
                ;

        //搜索到未推送过该问题，且关键字符合的老师id
        List<String> queryResult = template.queryForIds(searchQuery);
        for(String s : queryResult)
            System.out.println(s);
    }

}
