package cn.xmu.edu.compuOrg.dao;

import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.controller.CompuOrgController;
import cn.xmu.edu.compuOrg.mapper.TestPoMapper;
import cn.xmu.edu.compuOrg.mapper.TestResultPoMapper;
import cn.xmu.edu.compuOrg.model.po.TestPo;
import cn.xmu.edu.compuOrg.model.po.TestPoExample;
import cn.xmu.edu.compuOrg.model.vo.TopicVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author snow create 2021/01/24 17:00
 */
@Repository
public class TestDao {

    private  static  final Logger logger = LoggerFactory.getLogger(TestDao.class);

    @Autowired
    private TestPoMapper testPoMapper;

    @Autowired
    private TestResultPoMapper testResultPoMapper;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    /**
     * 根据从Redis中实验序号获取题目
     * @author snow create 2021/01/24 17:34
     * @param experimentId
     * @param size
     * @return
     */
    public ReturnObject getTest(Long experimentId, Long size){
        try {
            String key = "ex_" + experimentId;
            if(!redisTemplate.hasKey(key)){
                ArrayList<TopicVo> topicVos = selectTopicByExperimentId(experimentId);
                if(topicVos != null){
                    redisTemplate.opsForSet().add(key, topicVos);
                }
                else {
                    return new ReturnObject(ResponseCode.NO_MORE_TOPIC);
                }
            }
            Set topics = redisTemplate.opsForSet().distinctRandomMembers(key, size);
            for (Object topic : topics){
                logger.debug(topic.toString());
            }
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
            TestPoExample example = new TestPoExample();
            TestPoExample.Criteria criteria = example.createCriteria();
            criteria.andExperimentIdEqualTo(experimentId);
            List<TestPo> testPos = testPoMapper.selectByExample(example);
            if(testPos != null && testPos.size() != 0){
                ArrayList<TopicVo> topicVos = new ArrayList<>();
                for (TestPo testPo : testPos){
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



}
