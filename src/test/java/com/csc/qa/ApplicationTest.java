package com.csc.qa;

import com.csc.qa.dao.AccountDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Administrator on 2018/4/1 0001.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {
    @Autowired
    AccountDAO accountDAO;

    @Test
    public void index() throws Exception {
        //System.out.println("tostring!" + accountDAO.selectByUserName("acc"));
    }
}