package com.csc.qa;

import com.csc.qa.entity.Question;
import com.csc.qa.entity.Teacher;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/4/1 0001.
 */

@SpringBootApplication
@MapperScan("com.csc.qa")
public class Application implements ApplicationRunner{
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        //elasticsearchTemplate.putMapping(Teacher.class);
        //elasticsearchTemplate.putMapping(Question.class);
    }
}
