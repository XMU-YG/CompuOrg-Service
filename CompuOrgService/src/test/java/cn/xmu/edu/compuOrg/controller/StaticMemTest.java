package cn.xmu.edu.compuOrg.controller;

import cn.xmu.edu.compuOrg.CompuOrgServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = CompuOrgServiceApplication.class)
@AutoConfigureMockMvc
@Slf4j
public class StaticMemTest {
    @Autowired
    private MockMvc mvc;

    @Test
    public void writeTest(){

    }
}
