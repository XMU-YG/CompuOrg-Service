package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.CompuOrgServiceApplication;
import cn.xmu.edu.compuOrg.model.bo.Tests;
import cn.xmu.edu.compuOrg.model.bo.Topic;
import cn.xmu.edu.compuOrg.model.vo.TopicVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.ArrayList;

/**
 * @author snow create 2021/05/24 15:00
 */
@SpringBootTest(classes = CompuOrgServiceApplication.class)
@AutoConfigureMockMvc
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CompuOrgServiceTest {
    @Autowired
    private CompuOrgService service;

    /**
     * 非学生用户访问
     */
    @Test
    @Order(1)
    public void generateTest1(){
        ReturnObject retObj = service.generateTest(1L, 0L, 1L, 5L);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 学生已测试
     */
    @Test
    @Order(2)
    public void generateTest2(){
        ReturnObject retObj = service.generateTest(1L, 2L, 1L, 5L);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 无测试题目
     */
    @Test
    @Order(3)
    public void generateTest3(){
        ReturnObject retObj = service.generateTest(1L, 2L, 5L, 5L);
        Assert.assertEquals(ResponseCode.NO_MORE_TOPIC, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(4)
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
    @Order(5)
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
    @Order(6)
    public void appendTopic1(){
        ReturnObject retObj = service.appendTopic(2L, null);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(7)
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
    @Order(8)
    public void removeTopic1(){
        ReturnObject retObj = service.removeTopic(2L, 11L);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 因外键删除失败
     */
    @Test
    @Order(9)
    public void removeTopic2(){
        ReturnObject retObj = service.removeTopic(1L, 3L);
        Assert.assertEquals(ResponseCode.DELETE_TOPIC_FAILED, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(10)
    public void removeTopic3(){
        ReturnObject retObj = service.removeTopic(1L, 11L);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 学生访问
     */
    @Test
    @Order(11)
    public void alterTopic1(){
        ReturnObject retObj = service.modifyTopic(2L, 1L, null);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 题目不存在
     */
    @Test
    @Order(12)
    public void alterTopic2(){
        TopicVo topicVo = new TopicVo();
        topicVo.setContent("content");
        ReturnObject retObj = service.modifyTopic(1L, 0L, topicVo);
        Assert.assertEquals(ResponseCode.RESOURCE_ID_NOTEXIST, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(13)
    public void alterTopic3(){
        TopicVo topicVo = new TopicVo();
        topicVo.setContent("content");
        ReturnObject retObj = service.modifyTopic(1L, 1L, topicVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        retObj = service.getTopicList(1L, 1L, 1, 1);
        if (retObj.getData() instanceof PageInfo) {
            PageInfo pageInfo = (PageInfo)retObj.getData();
            List list = pageInfo.getList();
            Topic actualTopic = (Topic) list.get(0);
            Assert.assertEquals(topicVo.getContent(), actualTopic.getContent());
        }
    }

    /**
     * 学生访问
     */
    @Test
    @Order(14)
    public void getTopics1(){
        ReturnObject retObj = service.getTopicList(2L, 1L, 1, 5);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 无题目
     */
    @Test
    @Order(15)
    public void getTopics2(){
        ReturnObject retObj = service.getTopicList(1L, 5L, 1, 5);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        if (retObj.getData() instanceof PageInfo) {
            PageInfo pageInfo = (PageInfo)retObj.getData();
            Assert.assertEquals(1, pageInfo.getPageNum());
            Assert.assertEquals(5, pageInfo.getPageSize());
            Assert.assertEquals(0, pageInfo.getTotal());
            Assert.assertEquals(0, pageInfo.getPages());
            Assert.assertEquals(new ArrayList<>(), pageInfo.getList());
        }
    }

    /**
     * 成功
     */
    @Test
    @Order(16)
    public void getTopics3(){
        ReturnObject retObj = service.getTopicList(1L, null, 1, 5);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        if (retObj.getData() instanceof PageInfo) {
            PageInfo pageInfo = (PageInfo)retObj.getData();
            Assert.assertEquals(1, pageInfo.getPageNum());
            Assert.assertEquals(5, pageInfo.getPageSize());
            Assert.assertEquals(15, pageInfo.getTotal());
            Assert.assertEquals(3, pageInfo.getPages());
            Assert.assertNotNull(pageInfo.getList());
        }
    }



}
