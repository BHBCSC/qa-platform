package com.csc.qa.controller;

import com.csc.qa.entity.Question;
import com.csc.qa.service.AccountService;
import com.csc.qa.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2018/4/12 0012.
 */
@Controller
@RequestMapping("/student")
public class StudentController {
    @Autowired
    QuestionService questionService;

    @RequestMapping({"/","/index"})
    public String index(HttpSession session, Model model){
        List<Question> list = questionService.getQuestionByStudentId((Integer) session.getAttribute("uid"));
        model.addAttribute("questionList", list);

        return "/student/index";
    }

    @RequestMapping(value = "/ask" ,method = RequestMethod.GET)
    public String ask(){
        return "/student/ask";
    }

    @RequestMapping(value = "/ask", method = RequestMethod.POST)
    public String doAsk(HttpSession session, Question question){
        if(question.getKeyword() != null && !question.getKeyword().equals("")
                && question.getContent() != null && !question.getContent().equals("")){
            question.setStudentid((Integer) session.getAttribute("uid"));
            questionService.addQuestion(question);
            return "redirect:/student/index";
        } else {
            return "/student/ask";
        }
    }
}
