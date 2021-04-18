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
    @NotNull(message = "题目答案不能为空")
    private Long topicAnswerId;

    @ApiModelProperty(value = "题目答案得分")
    @NotNull(message = "答案得分不能为空")
    private Integer score;

    @ApiModelProperty(value = "题目答案评语")
    private String comment;
}
