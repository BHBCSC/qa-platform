package com.csc.qa.dao;

import com.csc.qa.entity.Account;

public interface AccountDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(Account record);

    int insertSelective(Account record);

    Account selectByPrimaryKey(Integer id);

    Account selectByUserName(String username);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);
}