package cn.xmu.edu.compuOrg.model.vo;

import cn.xmu.edu.compuOrg.model.bo.Tests;
import cn.xmu.edu.compuOrg.model.bo.Topic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;

/**
 * @author snow create 2021/01/24 14:56
 */
@Data
public class TestRetVo {

    @ApiModelProperty(value = "题目数量")
    private Long size;

    @ApiModelProperty(value = "实验序号")
    private Long experimentId;

    @ApiModelProperty(value = "题目列表")
    private ArrayList<TopicRetVo> topics;

    public TestRetVo(Tests test){
        this.size = test.getSize();
        this.experimentId = test.getExperimentId();
        this.topics = new ArrayList<>();
        for(Topic topic : test.getTopics()){
            TopicRetVo topicRetVo = new TopicRetVo(topic);
            this.topics.add(topicRetVo);
        }
    }

}
