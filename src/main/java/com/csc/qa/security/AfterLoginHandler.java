package com.csc.qa.security;

import com.csc.qa.dao.AccountDAO;
import com.csc.qa.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@Component
public class AfterLoginHandler implements AuthenticationSuccessHandler {
    @Autowired
    AccountDAO accountDAO;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String  redirectUrl = "/"; //缺省的登陆成功页面
        HttpSession session = httpServletRequest.getSession();
        Account account =  accountDAO.selectByUserName(authentication.getName());
        session.setAttribute("uid", account.getId());
        SavedRequest savedRequest = (SavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        if(savedRequest != null) {
            redirectUrl = savedRequest.getRedirectUrl();
            session.removeAttribute("SPRING_SECURITY_SAVED_REQUEST");
        }
        httpServletResponse.sendRedirect(redirectUrl);
    }
}

