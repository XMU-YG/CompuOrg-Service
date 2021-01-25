package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.compuOrg.model.po.TopicAnswerPo;
import cn.xmu.edu.compuOrg.model.vo.TopicAnswerVo;
import lombok.Data;

/**
 * @author snow create 2021/01/25 21:49
 */
@Data
public class TopicAnswer {
    private Long id;
    private Long topicId;
    private Long testResultId;
    private String answer;

    public TopicAnswer(TopicAnswerVo topicAnswerVo){
        this.topicId = topicAnswerVo.getTopicId();
        this.answer = topicAnswerVo.getAnswer();
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
}
