package cn.xmu.edu.compuOrg.dao;

import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.mapper.TestResultPoMapper;
import cn.xmu.edu.compuOrg.mapper.TopicAnswerPoMapper;
import cn.xmu.edu.compuOrg.mapper.TopicPoMapper;
import cn.xmu.edu.compuOrg.model.bo.Tests;
import cn.xmu.edu.compuOrg.model.bo.TopicAnswer;
import cn.xmu.edu.compuOrg.model.po.TestResultPo;
import cn.xmu.edu.compuOrg.model.po.TopicAnswerPo;
import cn.xmu.edu.compuOrg.model.po.TopicPo;
import cn.xmu.edu.compuOrg.model.po.TopicPoExample;
import cn.xmu.edu.compuOrg.model.vo.TopicVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
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
     * @param experimentId
     * @param size
     * @return
     */
    public ReturnObject<Tests> getTest(Long experimentId, Long size){
        try {
            String key = "ex_" + experimentId;
            if(!redisTemplate.hasKey(key) || redisTemplate.opsForList().size(key) == 0){
                ArrayList<TopicVo> topicVos = selectTopicByExperimentId(experimentId);
                if(topicVos != null){
                    for (TopicVo topicVo:topicVos) {
                        redisTemplate.opsForList().rightPush(key, topicVo);
                    }
                }
                else {
                    return new ReturnObject(ResponseCode.NO_MORE_TOPIC);
                }
            }
            Tests test = new Tests();
            ArrayList<TopicVo> topicVos;
            if(redisTemplate.opsForList().size(key) <= size){
                List<Serializable> members = redisTemplate.opsForList().range(key, 0, -1);
                topicVos = (ArrayList)members;
            }
            else{
                int[] randomSet = randomArray(0, redisTemplate.opsForList().size(key).intValue(), size.intValue());
                topicVos = new ArrayList<>();
                for (int i : randomSet){
                    topicVos.add((TopicVo)redisTemplate.opsForList().index(key, i));
                }
            }
            test.setExperimentId(experimentId);
            test.setSize(size);
            test.setTopics(topicVos);
            return new ReturnObject(test);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 根据实验序号从数据库从获得题目
     * @author snow create 2021/01/24 17:30
     * @param experimentId
     * @return
     */
    protected ArrayList<TopicVo> selectTopicByExperimentId(Long experimentId){
        try {
            TopicPoExample example = new TopicPoExample();
            TopicPoExample.Criteria criteria = example.createCriteria();
            criteria.andExperimentIdEqualTo(experimentId);
            List<TopicPo> testPos = topicPoMapper.selectByExample(example);
            if(testPos != null && testPos.size() != 0){
                ArrayList<TopicVo> topicVos = new ArrayList<>();
                for (TopicPo testPo : testPos){
                    TopicVo topicVo = new TopicVo(testPo);
                    topicVos.add(topicVo);
                }
                return topicVos;
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
     * @param studentId
     * @param experimentId
     * @return
     */
    public Long insertTestResult(Long studentId, Long experimentId){
        try {
            TestResultPo testResultPo = new TestResultPo();
            testResultPo.setStudentId(studentId);
            testResultPo.setExperimentId(experimentId);
            testResultPo.setGmtCreate(LocalDateTime.now());
            int effectRows = testResultPoMapper.insertSelective(testResultPo);
            if(effectRows == 1){
                return testResultPo.getId();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 插入题目答案
     * @author snow create 2021/01/25 22:00
     * @param answer
     * @return
     */
    public Boolean insertTopicAnswer(TopicAnswer answer){
        try {
            TopicAnswerPo topicAnswerPo = answer.createTopicAnswerPo();
            topicAnswerPo.setGmtCreate(LocalDateTime.now());
            int effectRows = topicAnswerPoMapper.insertSelective(topicAnswerPo);
            if(effectRows == 1){
                answer.setId(topicAnswerPo.getId());
                return true;
            }
        }
        catch (Exception e){

        }
        return false;
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
