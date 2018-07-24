package com.csc.qa.controller;

import com.csc.qa.entity.Question;
import com.csc.qa.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@Controller
@RequestMapping("/question")
public class QuestionController {
    @Autowired
    QuestionService questionService;

    //查看问题详情
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String detail(HttpSession session, @PathVariable(value = "id") Integer id, Model model) {
        Integer uid = (Integer) session.getAttribute("uid");
        Question question = questionService.getQuestion(id);
        model.addAttribute("question", question);
        model.addAttribute("id", id);
        model.addAttribute("uid", uid);

        if (getRoles().equals("ROLE_STUDENT") && uid.equals(question.getStudentid())) {
            return "/student/detail";
        }

        if (getRoles().equals("ROLE_TEACHER")) {
            return "/teacher/detail";
        }

        return "forward:/student/index";
    }

    //更新问题（无论是学生还是教师更新，凡是POST请求均进入此方法）
    @RequestMapping(value = "{id}", method = RequestMethod.POST)
    public String update(HttpSession session, @PathVariable(value = "id") Integer id, Question question, Model model) {
        Integer uid = (Integer) session.getAttribute("uid");
        question.setId(id);
        model.addAttribute("uid", uid);

        questionService.updateQuestionByIdSelective(question);

        return "redirect:/question/" + id;
    }

    //向老师推送的问题
    @RequestMapping("/push")
    public String push(HttpSession session, Model model){
        Integer uid = (Integer) session.getAttribute("uid");
        List<Question> pushedQuestion = questionService.getPushedQuestionByTeacherId(uid);
        model.addAttribute("questionList", pushedQuestion);

        return "/teacher/push";
    }

    //老师认领问题
    @RequestMapping("/confirm")
    public String confirm(HttpSession session, int id) {
        Integer uid = (Integer) session.getAttribute("uid");
        questionService.confirmQuestion(id, uid);
        return "redirect:/question/" + id;
    }

    private String getRoles() {
        String role = "";
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            role = authority.getAuthority();
        }
        return role;
    }
}
