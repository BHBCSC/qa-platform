package com.csc.qa.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Document(indexName = "qa-teacher", type = "teacher", shards = 1, replicas = 0,refreshInterval = "-1")
public class Teacher {
    @Transient
    private Integer id;

    @Id
    private Integer teacherid;

    @Field(type= FieldType.text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String keyword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(Integer teacherid) {
        this.teacherid = teacherid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }
}