package cn.xmu.edu.compuOrg.model.vo;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author snow create 2021/01/25 22:05
 */
@Data
public class TestVo {
    @Min(value = 1, message = "实验序号不能小于1")
    @Max(value = 5, message = "实验序号不能大于5")
    @NotNull(message = "实验序号不能为空")
    private Long experimentId;
    private List<TopicAnswerVo> topicAnswerVos;
}
