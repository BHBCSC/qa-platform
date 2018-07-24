package com.csc.qa.service;

import com.csc.qa.dao.QuestionDAO;
import com.csc.qa.dao.QuestionESDAO;
import com.csc.qa.entity.Question;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    QuestionESDAO questionESDAO;

    @Autowired
    ThreadPoolTaskScheduler threadPoolTaskScheduler;

    //推送间隔时间（单位：毫秒），两天为1000 * 60 * 60 * 24 * 2
    final static int pushTime = 1000 * 30;

    public Question getQuestion(int questionId){
        return questionDAO.selectByPrimaryKey(questionId);
    }

    //学生新增问题
    public void addQuestion(Question question){
        questionDAO.insertSelective(question);
        questionESDAO.save(question);//在ES中保存该问题，便于老师搜索
        Date date = new Date();
        date.setTime(date.getTime() + pushTime);//两天后时间


        threadPoolTaskScheduler.schedule(new Thread(new Runnable() {
            @Override
            public void run() {
                Question checkStatus = questionDAO.selectByPrimaryKey(question.getId());
                //如果在推送前发现已经被领取，不进行继续推送
                if(Integer.valueOf(checkStatus.getStatus()) != 0){
                    Set<String> teacherRecord = redisTemplate.opsForSet().members("question_" + question.getId());
                    if(teacherRecord != null){
                        for(String pushedId : teacherRecord){
                            redisTemplate.opsForSet().remove("teacher_"+pushedId, String.valueOf(question.getId()));
                        }
                    }

                    //删除问题的推送老师记录
                    redisTemplate.delete("question_" + question.getId());

                } else {//否则继续推送
                    //已被推送过过该问题的老师集合
                    Set<String> pushedteachers = redisTemplate.opsForSet().members("question_"+question.getId());
                    String[] pushedTeachersString = pushedteachers == null || pushedteachers.size() == 0
                            ? new String[]{"0"}
                            : pushedteachers.toArray(new String[pushedteachers.size()]);

                    //在qa-teacher根据关键字搜索老师
                    QueryBuilder queryBuilder = QueryBuilders
                            .boolQuery()
                            .mustNot(QueryBuilders.idsQuery().addIds(pushedTeachersString))//排除推送过的老师id
                            .must(
                                    QueryBuilders.queryStringQuery(question.getKeyword()).field("keyword")//根据关键字查找
                            );

                    SearchQuery searchQuery = new NativeSearchQueryBuilder()
                            .withQuery(queryBuilder)
                            .withPageable(PageRequest.of(0, 10))
                            .withIndices("qa-teacher")
                            .build()
                            ;

                    //搜索到未推送过该问题，且关键字符合的老师id
                    List<String> queryResult = elasticsearchTemplate.queryForIds(searchQuery);
                    if(queryResult != null && queryResult.size() > 0){
                        for(String teacherid : queryResult){//遍历id放入
                            //TODO 保证状态一致性，即若推送和领取并发执行，需要保证不推送
                            //在所推送的老师下记录，以便老师拉取问题
                            redisTemplate.opsForSet().add("teacher_" + teacherid, String.valueOf(question.getId()));
                            //记录所推送过的老师，以保证不重复推送
                            redisTemplate.opsForSet().add("question_" + question.getId(), teacherid);
                        }
                    }

                    //重复推送
                    Date date = new Date();
                    date.setTime(date.getTime() + pushTime);//两天后时间
                    threadPoolTaskScheduler.schedule(this, date);
                }
            }
        }),date);
    }

    //老师确认问题
    public void confirmQuestion(Integer questionid, Integer teacherid){
        //数据库更新状态
        questionDAO.increaseStatusByPrimaryKeyAndTeacherId(questionid, teacherid);

        //从推送过该问题的老师列表中，逐个删除该问题记录
        Set<String> teacherRecord = redisTemplate.opsForSet().members("question_" + questionid);
        if(teacherRecord != null){
            for(String pushedId : teacherRecord){
                redisTemplate.opsForSet().remove("teacher_"+pushedId, String.valueOf(questionid));
            }
        }

        //删除问题的推送老师记录
        redisTemplate.delete("question_" + questionid);

        //ES根据id删除问题记录，不允许搜索
        questionESDAO.deleteById(questionid);
    }

    public int updateQuestionByIdSelective(Question question){
        Question prev = questionDAO.selectByPrimaryKey(question.getId());
        question.setStatus(String.valueOf(Integer.valueOf(prev.getStatus()) + 1));
        return questionDAO.updateByPrimaryKeySelective(question);
    }

    //学生获取所有发表过的问题
    public List<Question> getQuestionByStudentId(int studentId){
        List<Question> list = questionDAO.selectByStudentId(studentId);
        for(Question q:list){
            String cont = q.getContent();
            q.setContent(cont.length() > 50 ? cont.substring(0, 50) : cont);
        }
        return list;
    }

    //老师获取已认领的问题
    public List<Question> getQuestionByTeacherId(int teacherId){
        List<Question> list = questionDAO.selectByTeacherId(teacherId);
        for(Question q:list){
            String cont = q.getContent();
            q.setContent(cont.length() > 50 ? cont.substring(0, 50) : cont);
        }
        return list;
    }

    public List<Question> getPushedQuestionByTeacherId(int teacherId){
        List<Question> questions = new ArrayList<>();
        //从老师集合中搜取推送记录
        Set<String> questionids = redisTemplate.opsForSet().members("teacher_"+teacherId);

        //根据推送的问题id找到问题
        if(questionids != null && questionids.size() > 0){
            for(String questionid : questionids){
                int id = Integer.valueOf(questionid);
                Question question = questionDAO.selectByPrimaryKey(id);
                //接受推送时发现已经被认领，不展示且删除所有有关数据
                if(Integer.valueOf(question.getStatus()) != 0){
                    Set<String> teacherRecord = redisTemplate.opsForSet().members("question_" + question.getId());
                    if(teacherRecord != null){
                        for(String pushedId : teacherRecord){
                            redisTemplate.opsForSet().remove("teacher_"+pushedId, String.valueOf(question.getId()));
                        }
                    }

                    //删除问题的推送老师记录
                    redisTemplate.delete("question_" + question.getId());
                } else {
                    //展示以领取
                    questions.add(question);
                }

            }
        }

        return questions;
    }
}
