package com.csc.qa.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Document(indexName = "qa-question", type = "question", shards = 1, replicas = 0,refreshInterval = "-1")
public class Question {
    @Id
    private Integer id;

    @Field(type= FieldType.text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content;

    private Integer studentid;

    private Integer teacherid;

    private String answer;

    private Integer studentscore;

    private Integer teacherscore;

    private String status;

    @Field(type= FieldType.text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String keyword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getStudentid() {
        return studentid;
    }

    public void setStudentid(Integer studentid) {
        this.studentid = studentid;
    }

    public Integer getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(Integer teacherid) {
        this.teacherid = teacherid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }

    public Integer getStudentscore() {
        return studentscore;
    }

    public void setStudentscore(Integer studentscore) {
        this.studentscore = studentscore;
    }

    public Integer getTeacherscore() {
        return teacherscore;
    }

    public void setTeacherscore(Integer teacherscore) {
        this.teacherscore = teacherscore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", studentid=" + studentid +
                ", teacherid=" + teacherid +
                ", answer='" + answer + '\'' +
                ", studentscore=" + studentscore +
                ", teacherscore=" + teacherscore +
                ", status='" + status + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}