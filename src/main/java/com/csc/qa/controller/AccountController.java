package com.csc.qa.controller;

import com.csc.qa.entity.Account;
import com.csc.qa.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/4/10 0010.
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    AccountService accountService;

    @RequestMapping("/login")
    public String login() {
        return "account/login";
    }

    @RequestMapping("/register")
    public String register(Account account, Model model) {

        account.setRole("ROLE_" + account.getRole());
        if(account.getUsername() != null && account.getPassword() != null){
            if (accountService.register(account) == 0) {
                model.addAttribute("message", "用户名被注册");
            } else {
                model.addAttribute("message", "注册成功！");
            }
        }

        return "account/register";
    }
}
