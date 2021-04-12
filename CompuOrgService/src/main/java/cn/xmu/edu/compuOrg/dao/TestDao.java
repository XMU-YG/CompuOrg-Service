package cn.xmu.edu.compuOrg.dao;

import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.mapper.TestResultPoMapper;
import cn.xmu.edu.compuOrg.mapper.TopicAnswerPoMapper;
import cn.xmu.edu.compuOrg.mapper.TopicPoMapper;
import cn.xmu.edu.compuOrg.model.bo.TestResult;
import cn.xmu.edu.compuOrg.model.bo.Tests;
import cn.xmu.edu.compuOrg.model.bo.Topic;
import cn.xmu.edu.compuOrg.model.bo.TopicAnswer;
import cn.xmu.edu.compuOrg.model.po.*;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author snow create 2021/01/24 17:00
 */
@Repository
public class TestDao {

    private  static  final Logger logger = LoggerFactory.getLogger(TestDao.class);

    @Autowired
    private TopicPoMapper topicPoMapper;

    @Autowired
    private TestResultPoMapper testResultPoMapper;

    @Autowired
    private TopicAnswerPoMapper topicAnswerPoMapper;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 根据从Redis中实验序号获取题目
     * @author snow create 2021/01/24 17:34
     *            modified 2021/01/28 10:00
     *            modified 2021/01/28 14:26
     * @param experimentId
     * @param size
     * @return
     */
    public ReturnObject<Tests> getTest(Long experimentId, Long size){
        try {
            String key = "ex_" + experimentId;
            if((!redisTemplate.hasKey(key) || redisTemplate.opsForList().size(key) == 0) && !loadTopicFromDataBase(experimentId)){
                return new ReturnObject<>(ResponseCode.NO_MORE_TOPIC);
            }
            Tests test = new Tests();
            ArrayList<Topic> topics;
            if(redisTemplate.opsForList().size(key) <= size){
                List<Serializable> members = redisTemplate.opsForList().range(key, 0, -1);
                topics = (ArrayList)members;
            }
            else{
                int[] randomSet = randomArray(0, redisTemplate.opsForList().size(key).intValue(), size.intValue());
                topics = new ArrayList<>();
                for (int i : randomSet){
                    topics.add((Topic)redisTemplate.opsForList().index(key, i));
                }
            }
            test.setExperimentId(experimentId);
            test.setSize(size);
            test.setTopics(topics);
            return new ReturnObject(test);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 把数据库中某个实验对应的题目load到缓存中
     * @author snow create 2021/01/28 14:23
     * @param experimentId
     * @return
     */
    public Boolean loadTopicFromDataBase(Long experimentId){
        ArrayList<Topic> topics = selectTopicByExperimentId(experimentId);
        if(topics != null){
            String key = "ex_" + experimentId;
            for (Topic topic : topics) {
                redisTemplate.opsForList().rightPush(key, topic);
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 从缓存中清空某一实验的题目
     * @author snow create 2021/01/28 10:18
     * @param experimentId
     * @return
     */
    public Boolean removeTestInRedisByExperimentId(Long experimentId){
        String key = "ex_" + experimentId;
        if(redisTemplate.hasKey(key)){
            return redisTemplate.delete(key);
        }
        return true;
    }

    /**
     * 根据实验序号从数据库从获得题目
     * @author snow create 2021/01/24 17:30
     *            modified 2021/01/28 09:55
     * @param experimentId
     * @return
     */
    protected ArrayList<Topic> selectTopicByExperimentId(Long experimentId){
        try {
            TopicPoExample example = new TopicPoExample();
            TopicPoExample.Criteria criteria = example.createCriteria();
            criteria.andExperimentIdEqualTo(experimentId);
            List<TopicPo> testPos = topicPoMapper.selectByExample(example);
            if(testPos != null && testPos.size() != 0){
                ArrayList<Topic> topics = new ArrayList<>();
                for (TopicPo testPo : testPos){
                    Topic topic = new Topic(testPo);
                    topics.add(topic);
                }
                return topics;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 插入测试结果
     * @author snow create 2021/01/25 21:47
     *            modified 2021/01/25 23:42
     * @param testResult
     * @return
     */
    public Boolean insertTestResult(TestResult testResult){
        try {
            TestResultPo testResultPo = new TestResultPo();
            testResultPo.setStudentId(testResult.getStudentId());
            testResultPo.setExperimentId(testResult.getExperimentId());
            testResultPo.setGmtCreate(LocalDateTime.now());
            int effectRows = testResultPoMapper.insertSelective(testResultPo);
            if(effectRows == 1){
                testResult.setGmtCreate(testResultPo.getGmtCreate());
                testResult.setId(testResultPo.getId());
                return true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 插入题目
     * @author snow create 2021/10:12
     * @param topic
     * @return
     */
    public ReturnObject insertTopic(Topic topic){
        try{
            TopicPo topicPo = topic.createTopicPo();
            topicPo.setGmtCreate(LocalDateTime.now());
            int effectRows = topicPoMapper.insert(topicPo);
            if (effectRows == 1){
                topic.setId(topicPo.getId());
                removeTestInRedisByExperimentId(topic.getExperimentId());
                return new ReturnObject(topic);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 删除题目
     * @author snow create 2021/01/28 13:42
     * @param topicId
     * @return
     */
    public ReturnObject deleteTopic(Long topicId){
        try {
            int effectRows = topicPoMapper.deleteByPrimaryKey(topicId);
            if (effectRows == 1){
                return new ReturnObject(ResponseCode.OK);
            }
            else {
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
        }
        catch (Exception e){
            Throwable cause = e.getCause();
            if(cause instanceof SQLIntegrityConstraintViolationException){
                return new ReturnObject(ResponseCode.DELETE_TOPIC_FAILED);
            }
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 修改题目
     * @author snow create 2021/01/28 13:46
     * @param topic
     * @return
     */
    public ReturnObject alterTopic(Topic topic){
        try {
            TopicPo topicPo = topic.createTopicPo();
            topicPo.setGmtModified(LocalDateTime.now());
            int effectRows = topicPoMapper.updateByPrimaryKeySelective(topicPo);
            if (effectRows == 1){
                return new ReturnObject(ResponseCode.OK);
            }
            else {
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 获取题目列表
     * @author snow create 2021/01/28 14:29
     * @param experimentId
     * @return
     */
    public PageInfo<TopicPo> findTopicList(Long experimentId){
        try {
            TopicPoExample example = new TopicPoExample();
            TopicPoExample.Criteria criteria = example.createCriteria();
            if(experimentId != null) {
                criteria.andExperimentIdEqualTo(experimentId);
            }
            List<TopicPo> topicPos = topicPoMapper.selectByExample(example);
            return new PageInfo<>(topicPos);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 插入题目答案
     * @author snow create 2021/01/25 22:00
     *            modified 2021/01/25 23:47
     * @param answer
     * @return
     */
    public Boolean insertTopicAnswer(TopicAnswer answer){
        try {
            TopicAnswerPo topicAnswerPo = answer.createTopicAnswerPo();
            topicAnswerPo.setGmtCreate(LocalDateTime.now());
            int effectRows = topicAnswerPoMapper.insertSelective(topicAnswerPo);
            if(effectRows == 1){
                answer.setGmtCreate(topicAnswerPo.getGmtCreate());
                answer.setId(topicAnswerPo.getId());
                return true;
            }
        }
        catch (Exception e){

        }
        return false;
    }

    /**
     * 更新测试结果总评分
     * @author snow create 2021/01/27 22:02
     * @param testResultId
     * @param score
     * @return
     */
    public ReturnObject updateTestResultScore(Long testResultId, Integer score){
        try {
            TestResultPo testResultPo = testResultPoMapper.selectByPrimaryKey(testResultId);
            if(testResultPo == null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            testResultPo.setScore(score);
            testResultPo.setGmtModified(LocalDateTime.now());
            int effectRows = testResultPoMapper.updateByPrimaryKeySelective(testResultPo);
            if (effectRows == 1){
                return new ReturnObject(ResponseCode.OK);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 更新题目答案评分
     * @author snow create 2021/01/27 22:04
     * @param topicAnswerId
     * @param score
     * @return
     */
    public ReturnObject updateTopicAnswerScore(Long topicAnswerId, Integer score){
        try {
            TopicAnswerPo topicAnswerPo = topicAnswerPoMapper.selectByPrimaryKey(topicAnswerId);
            if(topicAnswerPo == null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            topicAnswerPo.setScore(score);
            topicAnswerPo.setGmtModified(LocalDateTime.now());
            int effectRows = topicAnswerPoMapper.updateByPrimaryKeySelective(topicAnswerPo);
            if (effectRows == 1){
                return new ReturnObject(ResponseCode.OK);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 获取测试的结果列表
     * @author snow create 2021/01/25 22:53
     *            modified 2021/01/28 12:42
     *            modified 2021/03/25 10:49
     * @param experimentId
     * @param studentId
     * @return
     */
    public PageInfo<TestResultPo> findTestResult(Long experimentId, Long studentId, Boolean modified){
        try {
            TestResultPoExample example = new TestResultPoExample();
            TestResultPoExample.Criteria criteria = example.createCriteria();
            if(experimentId != null) {
                criteria.andExperimentIdEqualTo(experimentId);
            }
            if(studentId != null){
                criteria.andStudentIdEqualTo(studentId);
            }
            if(modified != null){
                if(modified){
                    criteria.andGmtModifiedIsNotNull();
                }
                else{
                    criteria.andGmtModifiedIsNull();
                }
            }
            List<TestResultPo> testResultPos = testResultPoMapper.selectByExample(example);
            return new PageInfo<>(testResultPos);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 学生根据实验序号查看测试结果
     * @author snow create 2021/03/25 10:54
     * @param studentId
     * @param experimentId
     * @return
     */
    public ReturnObject<TestResult> findTestResultByExperimentId(Long studentId, Long experimentId){
        try {
            TestResultPoExample example = new TestResultPoExample();
            TestResultPoExample.Criteria criteria = example.createCriteria();
            if(experimentId != null) {
                criteria.andExperimentIdEqualTo(experimentId);
            }
            if(studentId != null){
                criteria.andStudentIdEqualTo(studentId);
            }
            List<TestResultPo> testResultPos = testResultPoMapper.selectByExample(example);
            if(testResultPos != null && testResultPos.size() > 0){
                return new ReturnObject(new TestResult(testResultPos.get(0)));
            }
            else{
                return new ReturnObject(ResponseCode.NO_MORE_TEST_RESULT);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 根据测试结果id获得测试详情
     * @author snow create 2021/01/25 23:03
     * @param testResultId
     * @return
     */
    public ReturnObject<TestResult> findTestResultById(Long testResultId){
        try {
            TestResultPo testResultPo = testResultPoMapper.selectByPrimaryKey(testResultId);
            if(testResultPo != null){
                return new ReturnObject(new TestResult(testResultPo));
            }
            else{
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 根据测试结果id获得题目答案
     * @author snow create 2021/01/25 23:11
     * @param testResultId
     * @return
     */
    public List<TopicAnswer> findTopicAnswerByTestResultId(Long testResultId){
        try {
            TopicAnswerPoExample example = new TopicAnswerPoExample();
            TopicAnswerPoExample.Criteria criteria = example.createCriteria();
            criteria.andTestResultIdEqualTo(testResultId);
            List<TopicAnswerPo> topicAnswerPos = topicAnswerPoMapper.selectByExample(example);
            if(topicAnswerPos != null && topicAnswerPos.size() != 0){
                List<TopicAnswer> topicAnswers = new ArrayList<>();
                for (TopicAnswerPo topicAnswerPo : topicAnswerPos){
                    TopicAnswer topicAnswer = new TopicAnswer(topicAnswerPo);
                    TopicPo topicPo = topicPoMapper.selectByPrimaryKey(topicAnswerPo.getTopicId());
                    topicAnswer.addTopicInfo(topicPo);
                    topicAnswers.add(topicAnswer);
                }
                return topicAnswers;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 指定范围内随机生成n个不重复的数
     * @author snow create 2021/01/24 20:13
     * @param min
     * @param max
     * @param n
     * @return
     */
    public static int[] randomArray(int min, int max, int n) {
        int len = max - min + 1;
        if (max < min || n > len) {
            return null;
        }
        // 初始化给定范围的待选数组
        int[] source = new int[len];
        for (int i = min; i < min + len; i++) {
            source[i - min] = i;
        }
        int[] result = new int[n];
        Random rd = new Random();
        int index = 0;
        for (int i = 0; i < result.length; i++) {
            // 待选数组0到(len-2)随机一个下标
            index = Math.abs(rd.nextInt() % len--);
            // 将随机到的数放入结果集
            result[i] = source[index];
            // 将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换
            source[index] = source[len];
        }
        return result;
    }



}
