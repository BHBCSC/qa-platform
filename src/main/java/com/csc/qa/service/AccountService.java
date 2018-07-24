package com.csc.qa.service;

import com.csc.qa.dao.AccountDAO;
import com.csc.qa.dao.TeacherDAO;
import com.csc.qa.entity.Account;
import com.csc.qa.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11 0011.
 */
@Service
public class AccountService implements UserDetailsService {
    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private TeacherDAO teacherDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountDAO.selectByUserName(username);
        if(account != null){
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(account.getRole()));

            return new User(account.getUsername(), "{noop}" + account.getPassword(), authorities);
        }
        throw new UsernameNotFoundException("没有用户名为 " + username + "的用户");
    }

    public int register(Account account){
        try{
            return accountDAO.insertSelective(account);
        } catch (DuplicateKeyException e){
            return 0;
        }
    }
}
