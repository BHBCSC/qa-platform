package com.csc.qa;

import com.csc.qa.dao.TeacherDAO;
import com.csc.qa.entity.Teacher;
import com.csc.qa.service.TeacherService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2018/4/15 0015.
 */
public class TeacherTest extends ApplicationTest {
    @Autowired
    TeacherService teacherService;

    @Autowired
    TeacherDAO teacherDAO;

    @Test
    public void testKeyword(){
        Teacher teacher = new Teacher();
        teacher.setId(1);
        teacher.setTeacherid(2);
        teacher.setKeyword("Java SQL 啊啊啊");
        teacherDAO.updateByPrimaryKeySelective(teacher);
    }
}
