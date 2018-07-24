package com.csc.qa.controller;

import com.csc.qa.entity.Question;
import com.csc.qa.entity.Teacher;
import com.csc.qa.service.QuestionService;
import com.csc.qa.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@Controller
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    QuestionService questionService;

    @Autowired
    TeacherService teacherService;

    //老师首页（旧问题解决情况）
    @RequestMapping({"/","/index"})
    public String index(HttpSession session, Model model){
        List<Question> list = questionService.getQuestionByTeacherId((Integer) session.getAttribute("uid"));
        model.addAttribute("questionList", list);

        return "/teacher/index";
    }

    //设置、查看关键字页面
    @RequestMapping("/keyword")
    public String keyword(HttpSession session, Teacher teacher, Model model){
        teacher.setTeacherid((Integer) session.getAttribute("uid"));
        String originKeyWord = teacherService.getKeyWord(teacher.getTeacherid());
        if(teacher.getKeyword() != null && !teacher.getKeyword().equals("")) {
            teacherService.setKeyWord(teacher);
            model.addAttribute("message", "成功");
            model.addAttribute("keyword", teacher.getKeyword());
        } else {
            model.addAttribute("keyword", originKeyWord);
        }

        return "/teacher/keyword";
    }

    @RequestMapping("search")
    public String search(HttpSession session, String text, Model model){
        int teacherId = (Integer) session.getAttribute("uid");
        model.addAttribute("questionList", teacherService.search(teacherId, text));
        model.addAttribute("text", text);

        return "/teacher/search";
    }
}
