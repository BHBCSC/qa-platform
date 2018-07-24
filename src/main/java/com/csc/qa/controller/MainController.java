package com.csc.qa.controller;

import com.csc.qa.dao.AccountDAO;
import com.csc.qa.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/4/12 0012.
 */
@Controller
public class MainController {
    @Autowired
    AccountService accountService;

    @RequestMapping({"/","index"})
    public String index(Model model){
        String role = getRoles();
        if(role.equals("ROLE_STUDENT")){
            return "forward:/student/index";
        } else {
            return "forward:/teacher/index";
        }
    }

    private String getUserName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private String getRoles(){
        String role ="";
        for(GrantedAuthority authority :SecurityContextHolder.getContext().getAuthentication().getAuthorities()){
            role = authority.getAuthority();
        }
        return role;
    }
}
