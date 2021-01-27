package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author snow create 2021/01/27 22:10
 */
@Data
public class TopicAnswerScoreVo {
    @ApiModelProperty(value = "题目答案id")
    @NotNull
    private Long topicAnswerId;

    @ApiModelProperty(value = "题目答案得分")
    @NotNull
    private Integer score;
}
