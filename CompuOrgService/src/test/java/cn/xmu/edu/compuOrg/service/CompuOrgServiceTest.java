package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.CompuOrgServiceApplication;
import cn.xmu.edu.compuOrg.model.bo.TestResult;
import cn.xmu.edu.compuOrg.model.bo.Tests;
import cn.xmu.edu.compuOrg.model.bo.Topic;
import cn.xmu.edu.compuOrg.model.bo.User;
import cn.xmu.edu.compuOrg.model.vo.*;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

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

    /**
     * 非学生访问
     */
    @Test
    @Order(17)
    public void commitTestResult1(){
        TestVo testVo = new TestVo();
        testVo.setExperimentId(1L);
        ReturnObject retObj = service.commitTestResult(1L, 1L, testVo);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 已测试
     */
    @Test
    @Order(18)
    public void commitTestResult2(){
        TestVo testVo = new TestVo();
        testVo.setExperimentId(1L);
        ReturnObject retObj = service.commitTestResult(1L, 2L, testVo);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 学生不存在
     */
    @Test
    @Order(19)
    public void commitTestResult3(){
        TestVo testVo = new TestVo();
        testVo.setExperimentId(3L);
        ReturnObject retObj = service.commitTestResult(20L, 2L, testVo);
        Assert.assertEquals(ResponseCode.INTERNAL_SERVER_ERR, retObj.getCode());
    }

    /**
     * 答案对应的题目不存在
     */
    @Test
    @Order(20)
    public void commitTestResult4(){
        TestVo testVo = new TestVo();
        testVo.setExperimentId(3L);
        TopicAnswerVo topicAnswerVo = new TopicAnswerVo();
        topicAnswerVo.setTopicId(0L);
        topicAnswerVo.setAnswer("answer");
        List<TopicAnswerVo> topicAnswerVos = new ArrayList<>(1);
        topicAnswerVos.add(topicAnswerVo);
        testVo.setTopicAnswerVos(topicAnswerVos);
        ReturnObject retObj = service.commitTestResult(1L, 2L, testVo);
        Assert.assertEquals(ResponseCode.INTERNAL_SERVER_ERR, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(21)
    public void commitTestResult5(){
        TestVo testVo = new TestVo();
        testVo.setExperimentId(4L);
        TopicAnswerVo topicAnswerVo = new TopicAnswerVo();
        topicAnswerVo.setTopicId(12L);
        topicAnswerVo.setAnswer("answer");
        List<TopicAnswerVo> topicAnswerVos = new ArrayList<>(1);
        topicAnswerVos.add(topicAnswerVo);
        testVo.setTopicAnswerVos(topicAnswerVos);
        ReturnObject retObj = service.commitTestResult(1L, 2L, testVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        TestResult actualTestResult = (TestResult) retObj.getData();
        Assert.assertEquals(1, actualTestResult.getStudentId().intValue());
        Assert.assertEquals(4, actualTestResult.getExperimentId().intValue());
        Assert.assertEquals(1, actualTestResult.getTopicAnswers().size());
    }

    /**
     * 成功
     */
    @Test
    @Order(22)
    public void getTestResultList1(){
        ReturnObject<PageInfo<VoObject>> pageInfoReturnObject =
                service.getTestResultList(2L, 1L, null, null, null, 1, 5);
        PageInfo<VoObject> pageInfo = pageInfoReturnObject.getData();
        Assert.assertEquals(1, pageInfo.getPageNum());
        Assert.assertEquals(5, pageInfo.getPageSize());
        Assert.assertEquals(4, pageInfo.getTotal());
        Assert.assertEquals(1, pageInfo.getPages());
        Assert.assertEquals(4, pageInfo.getList().size());
    }

    /**
     * 成功
     */
    @Test
    @Order(23)
    public void getTestResultList2(){
        ReturnObject<PageInfo<VoObject>> pageInfoReturnObject =
                service.getTestResultList(1L, 1L, 2L, 1L, true, 1, 5);
        PageInfo<VoObject> pageInfo = pageInfoReturnObject.getData();
        Assert.assertEquals(1, pageInfo.getPageNum());
        Assert.assertEquals(5, pageInfo.getPageSize());
        Assert.assertEquals(1, pageInfo.getTotal());
        Assert.assertEquals(1, pageInfo.getPages());
        Assert.assertEquals(1, pageInfo.getList().size());
    }

    /**
     * 资源不存在
     */
    @Test
    @Order(24)
    public void getTestResultDetailByTestResultId1(){
        ReturnObject retObj = service.getTestResultDetailByTestResultId(2L, 2L, 0L);
        Assert.assertEquals(ResponseCode.RESOURCE_ID_NOTEXIST, retObj.getCode());
    }

    /**
     * 不是自己的测试结果
     */
    @Test
    @Order(25)
    public void getTestResultDetailByTestResultId2(){
        ReturnObject retObj = service.getTestResultDetailByTestResultId(2L, 2L, 1L);
        Assert.assertEquals(ResponseCode.RESOURCE_ID_OUTSCOPE, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(26)
    public void getTestResultDetailByTestResultId3(){
        ReturnObject retObj = service.getTestResultDetailByTestResultId(1L, 2L, 1L);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        TestResult actualTestResult = (TestResult) retObj.getData();
        Assert.assertEquals(1, actualTestResult.getExperimentId().intValue());
        Assert.assertEquals(5, actualTestResult.getTopicAnswers().size());
    }

    /**
     * 非学生用户访问
     */
    @Test
    @Order(27)
    public void getTestResultDetailByExperimentId1(){
        ReturnObject retObj = service.getTestResultDetailByExperimentId(1L, 1L, 1L);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 无测试结果
     */
    @Test
    @Order(28)
    public void getTestResultDetailByExperimentId2(){
        ReturnObject retObj = service.getTestResultDetailByExperimentId(1L, 2L, 5L);
        Assert.assertEquals(ResponseCode.NO_MORE_TEST_RESULT, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(29)
    public void getTestResultDetailByExperimentId3(){
        ReturnObject retObj = service.getTestResultDetailByExperimentId(1L, 2L, 1L);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        TestResult actualTestResult = (TestResult) retObj.getData();
        Assert.assertEquals(1, actualTestResult.getExperimentId().intValue());
        Assert.assertEquals(5, actualTestResult.getTopicAnswers().size());
    }

    /**
     * 无权限访问
     */
    @Test
    @Order(30)
    public void commitTestResultScore1(){
        TestResultScoreVo testResultVo = new TestResultScoreVo();
        ReturnObject retObj = service.commitTestResultScore(2L, 2L, testResultVo);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 测试题目答案不存在
     */
    @Test
    @Order(31)
    public void commitTestResultScore2(){
        TestResultScoreVo testResultVo = new TestResultScoreVo();
        TopicAnswerScoreVo topicAnswerVo = new TopicAnswerScoreVo();
        topicAnswerVo.setScore(5);
        topicAnswerVo.setTopicAnswerId(13L);
        List<TopicAnswerScoreVo> topicAnswerScoreVos = new ArrayList<>(1);
        topicAnswerScoreVos.add(topicAnswerVo);
        testResultVo.setTopicAnswerScores(topicAnswerScoreVos);
        ReturnObject retObj = service.commitTestResultScore(1L, 2L, testResultVo);
        Assert.assertEquals(ResponseCode.RESOURCE_ID_NOTEXIST, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(32)
    public void commitTestResultScore3(){
        TestResultScoreVo testResultVo = new TestResultScoreVo();
        TopicAnswerScoreVo topicAnswerVo = new TopicAnswerScoreVo();
        topicAnswerVo.setScore(5);
        topicAnswerVo.setTopicAnswerId(12L);
        List<TopicAnswerScoreVo> topicAnswerScoreVos = new ArrayList<>(1);
        topicAnswerScoreVos.add(topicAnswerVo);
        testResultVo.setTopicAnswerScores(topicAnswerScoreVos);
        ReturnObject retObj = service.commitTestResultScore(1L, 2L, testResultVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 用户名不存在
     */
    @Test
    @Order(33)
    public void userLogin1(){
        UserLoginVo userVo = new UserLoginVo();
        userVo.setUserName("123");
        userVo.setPassword("123456");
        ReturnObject retObj = service.userLogin(userVo);
        Assert.assertEquals(ResponseCode.AUTH_INVALID_ACCOUNT, retObj.getCode());
    }

    /**
     * 用户信息被篡改
     */
    @Test
    @Order(34)
    public void userLogin2(){
        UserLoginVo userVo = new UserLoginVo();
        userVo.setUserName("snow3");
        userVo.setPassword("123456");
        ReturnObject retObj = service.userLogin(userVo);
        Assert.assertEquals(ResponseCode.RESOURCE_FALSIFY, retObj.getCode());
    }

    /**
     * 用户预留邮箱未确认
     */
    @Test
    @Order(35)
    public void userLogin3(){
        UserLoginVo userVo = new UserLoginVo();
        userVo.setUserName("snow4");
        userVo.setPassword("123456");
        ReturnObject retObj = service.userLogin(userVo);
        Assert.assertEquals(ResponseCode.EMAIL_NOT_VERIFIED, retObj.getCode());
    }

    /**
     * 密码错误
     */
    @Test
    @Order(36)
    public void userLogin4(){
        UserLoginVo userVo = new UserLoginVo();
        userVo.setUserName("snow");
        userVo.setPassword("123");
        ReturnObject retObj = service.userLogin(userVo);
        Assert.assertEquals(ResponseCode.AUTH_INVALID_ACCOUNT, retObj.getCode());
    }

    /**
     * 管理员登录成功
     */
    @Test
    @Order(37)
    public void userLogin5(){
        UserLoginVo userVo = new UserLoginVo();
        userVo.setUserName("snow");
        userVo.setPassword("123456");
        ReturnObject retObj = service.userLogin(userVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        String actualStr = (String) retObj.getData();
        Assert.assertEquals("adm", actualStr.substring(0, 3));
    }

    /**
     * 教师登录成功
     */
    @Test
    @Order(38)
    public void userLogin6(){
        UserLoginVo userVo = new UserLoginVo();
        userVo.setUserName("snow1");
        userVo.setPassword("123456");
        ReturnObject retObj = service.userLogin(userVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        String actualStr = (String) retObj.getData();
        Assert.assertEquals("tea", actualStr.substring(0, 3));
    }

    /**
     * 学生登录成功
     */
    @Test
    @Order(39)
    public void userLogin7(){
        UserLoginVo userVo = new UserLoginVo();
        userVo.setUserName("snow2");
        userVo.setPassword("123456");
        ReturnObject retObj = service.userLogin(userVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        String actualStr = (String) retObj.getData();
        Assert.assertEquals("stu", actualStr.substring(0, 3));
    }

    /**
     * 其他角色登录成功
     */
    @Test
    @Order(40)
    public void userLogin8(){
        UserLoginVo userVo = new UserLoginVo();
        userVo.setUserName("snow5");
        userVo.setPassword("123456");
        ReturnObject retObj = service.userLogin(userVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        String actualStr = (String) retObj.getData();
        Assert.assertEquals("unk", actualStr.substring(0, 3));
    }

    /**
     * 验证码不正确或已过期
     */
    @Test
    @Order(41)
    public void studentSignUp1(){
        UserVo userVo = new UserVo();
        userVo.setVerifyCode("202189473298");
        ReturnObject retObj = service.studentSignUp(userVo);
        Assert.assertEquals(ResponseCode.VERIFY_CODE_EXPIRE, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(42)
    public void studentSignUp2(){
        UserVo userVo = new UserVo();
        userVo.setUserName("snow2021");
        userVo.setPassword("123456");
        userVo.setEmail("127@qq.com");
        userVo.setVerifyCode("1");
        ReturnObject retObj = service.studentSignUp(userVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 无权限访问
     */
    @Test
    @Order(43)
    public void teacherSignUp1(){
        UserVo userVo = new UserVo();
        userVo.setVerifyCode("202189473298");
        ReturnObject retObj = service.teacherSignUp(2L, userVo);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(44)
    public void teacherSignUp2(){
        UserVo userVo = new UserVo();
        userVo.setUserName("teacher2021");
        userVo.setPassword("123456");
        userVo.setEmail("128@qq.com");
        userVo.setVerifyCode("1");
        ReturnObject retObj = service.teacherSignUp(1L, userVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 无权限访问
     */
    @Test
    @Order(45)
    public void appendAdmin1(){
        UserVo userVo = new UserVo();
        userVo.setVerifyCode("202189473298");
        ReturnObject retObj = service.appendAdmin(1L, userVo);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(46)
    public void appendAdmin2(){
        UserVo userVo = new UserVo();
        userVo.setUserName("admin2021");
        userVo.setPassword("123456");
        userVo.setEmail("129@qq.com");
        userVo.setVerifyCode("1");
        ReturnObject retObj = service.appendAdmin(0L, userVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 用户名不存在
     */
    @Test
    @Order(47)
    public void userResetPassword1(){
        UserPasswordVo userVo = new UserPasswordVo();
        userVo.setUserName("202189473298");
        userVo.setEmail("123@qq.com");
        ReturnObject retObj = service.userResetPassword(userVo, "127.0.0.1");
        Assert.assertEquals(ResponseCode.AUTH_INVALID_ACCOUNT, retObj.getCode());
    }

    /**
     * 与预留邮箱不一致
     */
    @Test
    @Order(48)
    public void userResetPassword2(){
        UserPasswordVo userVo = new UserPasswordVo();
        userVo.setUserName("snow");
        userVo.setEmail("snow0220@163.com");
        ReturnObject retObj = service.userResetPassword(userVo, "127.0.0.1");
        Assert.assertEquals(ResponseCode.EMAIL_WRONG, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(49)
    public void userResetPassword3(){
        UserPasswordVo userVo = new UserPasswordVo();
        userVo.setUserName("snow");
        userVo.setEmail("snow02203835@163.com");
        ReturnObject retObj = service.userResetPassword(userVo, "127.0.0.1");
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 一分钟内重复请求验证码
     */
    @Test
    @Order(50)
    public void userResetPassword4(){
        UserPasswordVo userVo = new UserPasswordVo();
        userVo.setUserName("snow");
        userVo.setEmail("snow02203835@163.com");
        redisTemplate.opsForValue().set("ip_127.0.0.2", "127.0.0.2");
        ReturnObject retObj = service.userResetPassword(userVo, "127.0.0.2");
        Assert.assertEquals(ResponseCode.AUTH_USER_FORBIDDEN, retObj.getCode());
    }

    /**
     * 用户不存在
     */
    @Test
    @Order(51)
    public void userVerifyPassword1(){
        ReturnObject retObj = service.userVerifyPassword(0L, "123456");
        Assert.assertEquals(ResponseCode.RESOURCE_ID_NOTEXIST, retObj.getCode());
    }

    /**
     * 密码不正确
     */
    @Test
    @Order(52)
    public void userVerifyPassword2(){
        ReturnObject retObj = service.userVerifyPassword(1L, "123");
        Assert.assertEquals(ResponseCode.AUTH_INVALID_ACCOUNT, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(53)
    public void userVerifyPassword3(){
        ReturnObject retObj = service.userVerifyPassword(1L, "123456");
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        Assert.assertNotNull(retObj.getData());
    }

    /**
     * 验证码不正确或已过期
     */
    @Test
    @Order(54)
    public void userVerifyCode1(){
        VerifyCodeVo codeVo = new VerifyCodeVo();
        codeVo.setVerifyCode("123878787");
        ReturnObject retObj = service.userVerifyCode(codeVo);
        Assert.assertEquals(ResponseCode.VERIFY_CODE_EXPIRE, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(55)
    public void userVerifyCode2(){
        redisTemplate.opsForValue().set("cp_123456", "1");
        VerifyCodeVo codeVo = new VerifyCodeVo();
        codeVo.setVerifyCode("123456");
        ReturnObject retObj = service.userVerifyCode(codeVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        Assert.assertNotNull(retObj.getData());
    }

    /**
     * 验证码不正确或已过期
     */
    @Test
    @Order(56)
    public void userModifyPassword1(){
        UserModifyPasswordVo passwordVo = new UserModifyPasswordVo();
        passwordVo.setPassword("1234");
        passwordVo.setVerifyCode("123878787");
        ReturnObject retObj = service.userModifyPassword(passwordVo);
        Assert.assertEquals(ResponseCode.VERIFY_CODE_EXPIRE, retObj.getCode());
    }

    /**
     * 用户不存在
     */
    @Test
    @Order(57)
    public void userModifyPassword2(){
        redisTemplate.opsForValue().set("cp_123456", "0");
        UserModifyPasswordVo passwordVo = new UserModifyPasswordVo();
        passwordVo.setPassword("1234");
        passwordVo.setVerifyCode("123456");
        ReturnObject retObj = service.userModifyPassword(passwordVo);
        Assert.assertEquals(ResponseCode.RESOURCE_ID_NOTEXIST, retObj.getCode());
    }

    /**
     * 新密码与旧密码相同
     */
    @Test
    @Order(58)
    public void userModifyPassword3(){
        redisTemplate.opsForValue().set("cp_123456", "1");
        UserModifyPasswordVo passwordVo = new UserModifyPasswordVo();
        passwordVo.setPassword("123456");
        passwordVo.setVerifyCode("123456");
        ReturnObject retObj = service.userModifyPassword(passwordVo);
        Assert.assertEquals(ResponseCode.PASSWORD_SAME, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(59)
    public void userModifyPassword4(){
        redisTemplate.opsForValue().set("cp_123456", "1");
        UserModifyPasswordVo passwordVo = new UserModifyPasswordVo();
        passwordVo.setPassword("123");
        passwordVo.setVerifyCode("123456");
        ReturnObject retObj = service.userModifyPassword(passwordVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(60)
    public void userGetBasicInformation1(){
        ReturnObject retObj = service.userGetBasicInformation(1L);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
        Assert.assertNotNull(retObj.getData());
    }

    /**
     * 无权限访问
     */
    @Test
    @Order(61)
    public void adminGetUserInformation1(){
        ReturnObject retObj = service.adminGetUserInformation(1L, null, null, 1, 5);
        Assert.assertEquals(ResponseCode.AUTH_NOT_ALLOW, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(62)
    public void adminGetUserInformation2(){
        ReturnObject retObj = service.adminGetUserInformation(0L, null, null, 1, 5);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 用户不存在
     */
    @Test
    @Order(63)
    public void userModifyBasicInformation1(){
        ReturnObject retObj = service.userModifyBasicInformation(0L, null);
        Assert.assertEquals(ResponseCode.RESOURCE_ID_NOTEXIST, retObj.getCode());
    }

    /**
     * 新用户名与旧用户名相同
     */
    @Test
    @Order(64)
    public void userModifyBasicInformation2(){
        UserBasicInfoVo infoVo = new UserBasicInfoVo();
        infoVo.setUserName("snow");
        ReturnObject retObj = service.userModifyBasicInformation(1L, infoVo);
        Assert.assertEquals(ResponseCode.USER_NAME_SAME, retObj.getCode());
    }

    /**
     * 用户名已被注册
     */
    @Test
    @Order(65)
    public void userModifyBasicInformation3(){
        UserBasicInfoVo infoVo = new UserBasicInfoVo();
        infoVo.setUserName("snow1");
        ReturnObject retObj = service.userModifyBasicInformation(1L, infoVo);
        Assert.assertEquals(ResponseCode.USER_NAME_REGISTERED, retObj.getCode());
    }

    /**
     * 用户名已被注册
     */
    @Test
    @Order(66)
    public void userModifyBasicInformation4(){
        UserBasicInfoVo infoVo = new UserBasicInfoVo();
        infoVo.setUserName("snow-1111");
        infoVo.setMobile("18575118843");
        ReturnObject retObj = service.userModifyBasicInformation(1L, infoVo);
        Assert.assertEquals(ResponseCode.MOBILE_SAME, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(67)
    public void userModifyBasicInformation5(){
        UserBasicInfoVo infoVo = new UserBasicInfoVo();
        infoVo.setMobile("18050103835");
        ReturnObject retObj = service.userModifyBasicInformation(2L, infoVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 电话号码已被注册
     */
    @Test
    @Order(68)
    public void userModifyBasicInformation6(){
        UserBasicInfoVo infoVo = new UserBasicInfoVo();
        infoVo.setMobile("18050103835");
        ReturnObject retObj = service.userModifyBasicInformation(1L, infoVo);
        Assert.assertEquals(ResponseCode.MOBILE_REGISTERED, retObj.getCode());
    }

    /**
     * 用户不存在
     */
    @Test
    @Order(69)
    public void userVerifyEmail1(){
        ReturnObject retObj = service.userVerifyEmail(0L, null);
        Assert.assertEquals(ResponseCode.RESOURCE_ID_NOTEXIST, retObj.getCode());
    }

    /**
     * 用户预留的邮箱为空
     */
    @Test
    @Order(70)
    public void userVerifyEmail2(){
        ReturnObject retObj = service.userVerifyEmail(5L, null);
        Assert.assertEquals(ResponseCode.EMAIL_EMPTY, retObj.getCode());
    }

    /**
     * 1分钟内重复请求验证码
     */
    @Test
    @Order(71)
    public void userVerifyEmail3(){
        redisTemplate.opsForValue().set("ip_127.0.0.2", "127.0.0.2");
        ReturnObject retObj = service.userVerifyEmail(1L, "127.0.0.2");
        Assert.assertEquals(ResponseCode.AUTH_USER_FORBIDDEN, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(72)
    public void userVerifyEmail4(){
        ReturnObject retObj = service.userVerifyEmail(1L, "127.0.0.3");
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 1分钟内重复请求验证码
     */
    @Test
    @Order(73)
    public void userVerifyEmail5(){
        redisTemplate.opsForValue().set("ip_127.0.0.2", "127.0.0.2");
        ReturnObject retObj = service.userVerifyEmail("1", null, "127.0.0.2");
        Assert.assertEquals(ResponseCode.AUTH_USER_FORBIDDEN, retObj.getCode());
    }

    /**
     * 邮箱已被注册
     */
    @Test
    @Order(74)
    public void userVerifyEmail6(){
        ReturnObject retObj = service.userVerifyEmail("-3835", "snow02203835@163.com", "127.0.0.4");
        Assert.assertEquals(ResponseCode.EMAIL_REGISTERED, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(75)
    public void userVerifyEmail7(){
        ReturnObject retObj = service.userVerifyEmail("-3835", "snows@stu.xmu.edu.cn", "127.0.0.5");
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(75)
    public void userVerifyEmail8(){
        ReturnObject retObj = service.userVerifyEmail("1", "snows@stu.xmu.edu.cn", "127.0.0.6");
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 验证码不正确或已过期
     */
    @Test
    @Order(76)
    public void userModifyEmail1(){
        UserModifyEmailVo emailVo = new UserModifyEmailVo();
        emailVo.setEmail("snows@stu.xmu.edu.cn");
        emailVo.setVerifyCode("894377983205548");
        ReturnObject retObj = service.userModifyEmail(1L, emailVo);
        Assert.assertEquals(ResponseCode.VERIFY_CODE_EXPIRE, retObj.getCode());
    }

    /**
     * 用户不存在
     */
    @Test
    @Order(77)
    public void userModifyEmail2(){
        redisTemplate.opsForValue().set("cp_12345", "12345");
        UserModifyEmailVo emailVo = new UserModifyEmailVo();
        emailVo.setEmail("snows@stu.xmu.edu.cn");
        emailVo.setVerifyCode("12345");
        ReturnObject retObj = service.userModifyEmail(0L, emailVo);
        Assert.assertEquals(ResponseCode.RESOURCE_ID_NOTEXIST, retObj.getCode());
    }

    /**
     * 新邮箱与旧邮箱一致
     */
    @Test
    @Order(78)
    public void userModifyEmail3(){
        redisTemplate.opsForValue().set("cp_12346", "12346");
        UserModifyEmailVo emailVo = new UserModifyEmailVo();
        emailVo.setEmail("snow02203835@163.com");
        emailVo.setVerifyCode("12346");
        ReturnObject retObj = service.userModifyEmail(1L, emailVo);
        Assert.assertEquals(ResponseCode.EMAIL_SAME, retObj.getCode());
    }

    /**
     * 邮箱已被注册
     */
    @Test
    @Order(79)
    public void userModifyEmail4(){
        redisTemplate.opsForValue().set("cp_12347", "12347");
        UserModifyEmailVo emailVo = new UserModifyEmailVo();
        emailVo.setEmail("123@qq.com");
        emailVo.setVerifyCode("12347");
        ReturnObject retObj = service.userModifyEmail(1L, emailVo);
        Assert.assertEquals(ResponseCode.EMAIL_REGISTERED, retObj.getCode());
    }

    /**
     * 成功
     */
    @Test
    @Order(80)
    public void userModifyEmail5(){
        redisTemplate.opsForValue().set("cp_12348", "12348");
        UserModifyEmailVo emailVo = new UserModifyEmailVo();
        emailVo.setEmail("snows@stu.xmu.edu.cn");
        emailVo.setVerifyCode("12348");
        ReturnObject retObj = service.userModifyEmail(1L, emailVo);
        Assert.assertEquals(ResponseCode.OK, retObj.getCode());
    }

    /**
     * 无效邮箱地址
     */
    @Test
    @Order(81)
    public void sendEmail(){
        ReturnObject retObj = service.sendVerifyCode("title", "content", "123");
        Assert.assertEquals(ResponseCode.INTERNAL_SERVER_ERR, retObj.getCode());
    }

    public void createUser(){
        UserVo userVo = new UserVo();
        userVo.setPassword("123456");
        userVo.setEmail("126@qq.com");
        userVo.setUserName("snow5");
        userVo.setVerifyCode("1");
        User user = new User(userVo);
        user.setRole((byte)3);
        user.setSignature(user.createSignature());
        System.out.println(user);
    }

}
