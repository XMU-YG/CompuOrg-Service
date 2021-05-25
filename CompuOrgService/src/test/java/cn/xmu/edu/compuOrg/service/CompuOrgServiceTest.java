package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.CompuOrgServiceApplication;
import cn.xmu.edu.compuOrg.model.bo.Tests;
import cn.xmu.edu.compuOrg.model.bo.Topic;
import cn.xmu.edu.compuOrg.model.vo.TopicVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author snow create 2021/05/24 15:00
 */
@SpringBootTest(classes = CompuOrgServiceApplication.class)
@AutoConfigureMockMvc
@Slf4j
public class CompuOrgServiceTest {
    @Autowired
    private CompuOrgService service;

    /**
     * 非学生用户访问
     */
    @Test
    public void generateTest1(){
        ReturnObject retObj = service.generateTest(1L, 0L, 1L, 5L);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 学生已测试
     */
    @Test
    public void generateTest2(){
        ReturnObject retObj = service.generateTest(1L, 2L, 1L, 5L);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 无测试题目
     */
    @Test
    public void generateTest3(){
        ReturnObject retObj = service.generateTest(1L, 2L, 5L, 5L);
        Assert.assertEquals(ResponseCode.NO_MORE_TOPIC, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    public void generateTest4(){
        Long size = 5L, experimentId = 3L;
        ReturnObject retObj = service.generateTest(1L, 2L, experimentId, size);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        Tests getTest = (Tests) retObj.getData();
        Assert.assertEquals(size, getTest.getSize());
        Assert.assertEquals(experimentId, getTest.getExperimentId());
    }

    /**
     * 成功
     */
    @Test
    public void generateTest5(){
        Long size = 5L, experimentId = 3L;
        ReturnObject retObj = service.generateTest(1L, 2L, experimentId, size*2);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        Tests getTest = (Tests) retObj.getData();
        System.out.println(getTest.getTopics());
        Assert.assertEquals(size, getTest.getSize());
        Assert.assertEquals(experimentId, getTest.getExperimentId());
    }

    /**
     * 学生访问
     */
    @Test
    public void appendTopic1(){
        ReturnObject retObj = service.appendTopic(2L, null);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    public void appendTopic2(){
        TopicVo topicVo = new TopicVo();
        topicVo.setType((byte)0);
        topicVo.setScore((byte)5);
        topicVo.setContent("content");
        topicVo.setExperimentId(4L);
        ReturnObject retObj = service.appendTopic(1L, topicVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        Topic actualTopic = (Topic) retObj.getData();
        Topic expectTopic = new Topic(topicVo);
        expectTopic.setId(actualTopic.getId());
        Assert.assertEquals(expectTopic, actualTopic);
    }

    /**
     * 学生访问
     */
    @Test
    public void removeTopic1(){
        ReturnObject retObj = service.removeTopic(2L, 11L);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 因外键删除失败
     */
    @Test
    public void removeTopic2(){
        ReturnObject retObj = service.removeTopic(1L, 3L);
        Assert.assertEquals(ResponseCode.DELETE_TOPIC_FAILED, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    public void removeTopic3(){
        ReturnObject retObj = service.removeTopic(1L, 11L);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

}
