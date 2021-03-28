package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author snow create 2021/01/27 22:09
 */
@Data
public class TestResultScoreVo {
    @ApiModelProperty(value = "测试结果id")
    @NotNull(message = "测试结果id不能为空")
    private Long testResultId;

    @ApiModelProperty(value = "各题目答案得分")
    @Valid
    private List<TopicAnswerScoreVo> topicAnswerScores;
}
