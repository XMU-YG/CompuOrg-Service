package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.compuOrg.model.po.TopicAnswerPo;
import cn.xmu.edu.compuOrg.model.po.TopicPo;
import cn.xmu.edu.compuOrg.model.vo.TopicAnswerVo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author snow create 2021/01/25 21:49
 */
@Data
public class TopicAnswer implements VoObject, Serializable {
    private Long id;
    private Long topicId;
    private Long testResultId;
    private String answer;
    private Integer score;
    private String comment;
    private Byte type;
    private Byte totalScore;
    private String content;
    private String imgUrl;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;

    public TopicAnswer(TopicAnswerVo topicAnswerVo){
        this.topicId = topicAnswerVo.getTopicId();
        this.answer = topicAnswerVo.getAnswer();
    }

    public TopicAnswer(TopicAnswerPo topicAnswer){
        this.id = topicAnswer.getId();
        this.topicId = topicAnswer.getTopicId();
        this.testResultId = topicAnswer.getTestResultId();
        this.answer = topicAnswer.getAnswer();
        this.score = topicAnswer.getScore();
        this.comment = topicAnswer.getComment();
        this.gmtCreate = topicAnswer.getGmtCreate();
        this.gmtModified = topicAnswer.getGmtModified();
    }

    public void addTopicInfo(TopicPo topicPo){
        this.type = topicPo.getType();
        this.totalScore = topicPo.getScore();
        this.content = topicPo.getContent();
        this.imgUrl = topicPo.getImgUrl();
    }

    /**
     * 创建TopicAnswerPo
     * @author snow create 2021/01/25 21:55
     * @return
     */
    public TopicAnswerPo createTopicAnswerPo(){
        TopicAnswerPo topicAnswerPo = new TopicAnswerPo();
        topicAnswerPo.setTopicId(this.topicId);
        topicAnswerPo.setTestResultId(this.testResultId);
        topicAnswerPo.setAnswer(this.answer);
        return topicAnswerPo;
    }

    @Override
    public Object createVo() {
        return null;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
