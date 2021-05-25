package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.compuOrg.model.po.TopicPo;
import cn.xmu.edu.compuOrg.model.vo.TopicVo;
import cn.xmu.edu.compuOrg.model.vo.TopicRetVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author snow create 2021/01/28 09:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Topic implements VoObject, Serializable {
    private Long id;
    private Byte type;
    private Byte score;
    private String content;
    private String imgUrl;
    private Long experimentId;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

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
        this.imgUrl = topicPo.getImgUrl();
        this.content = topicPo.getContent();
        this.experimentId = topicPo.getExperimentId();
        this.gmtCreate = topicPo.getGmtCreate();
        this.gmtModified = topicPo.getGmtModified();
    }

    public TopicPo createTopicPo(){
        TopicPo topicPo = new TopicPo();
        topicPo.setId(this.id);
        topicPo.setType(this.type);
        topicPo.setScore(this.score);
        topicPo.setImgUrl(this.imgUrl);
        topicPo.setContent(this.content);
        topicPo.setExperimentId(this.experimentId);
        topicPo.setGmtCreate(this.gmtCreate);
        topicPo.setGmtModified(this.gmtModified);
        return topicPo;
    }

    public Boolean authentic(){
        return true;
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
