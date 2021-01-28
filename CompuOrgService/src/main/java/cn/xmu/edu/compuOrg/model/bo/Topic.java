package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.compuOrg.model.po.TopicPo;
import cn.xmu.edu.compuOrg.model.vo.TopicVo;
import cn.xmu.edu.compuOrg.model.vo.TopicRetVo;
import lombok.Data;

import java.io.Serializable;

/**
 * @author snow create 2021/01/28 09:50
 */
@Data
public class Topic implements VoObject, Serializable {
    private Long id;
    private Byte type;
    private Byte score;
    private String content;
    private String imgUrl;
    private Long experimentId;

    public Topic(TopicVo topicVo){
        this.type = topicVo.getType();
        this.score = topicVo.getScore();
        this.imgUrl = topicVo.getImgUrl();
        this.content = topicVo.getContent();
        this.experimentId = topicVo.getExperimentId();
    }

    public Topic(TopicPo topicPo){
        this.id = topicPo.getId();
        this.type = topicPo.getType();
        this.score = topicPo.getScore();
        this.content = topicPo.getContent();
        this.imgUrl = topicPo.getImgUrl();
    }

    public TopicPo createTopicPo(){
        TopicPo topicPo = new TopicPo();
        topicPo.setId(this.id);
        topicPo.setType(this.type);
        topicPo.setScore(this.score);
        topicPo.setImgUrl(this.imgUrl);
        topicPo.setContent(this.content);
        topicPo.setExperimentId(this.experimentId);
        return topicPo;
    }

    @Override
    public Object createVo(){
        return new TopicRetVo(this);
    }

    @Override
    public Object createSimpleVo(){
        return null;
    }


}
