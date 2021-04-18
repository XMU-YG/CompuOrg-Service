package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * @author snow create 2021/01/27 22:09
 */
@Data
public class TestResultScoreVo {
    @ApiModelProperty(value = "各题目答案得分与评语")
    @Valid
    private List<TopicAnswerScoreVo> topicAnswerScores;
}
