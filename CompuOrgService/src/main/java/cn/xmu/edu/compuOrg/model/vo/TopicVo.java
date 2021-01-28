package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author snow create 2021/01/28 09:35
 */
@Data
public class TopicVo {

    @ApiModelProperty(value = "实验序号")
    @Min(value = 1, message = "实验序号不能小于1")
    @Max(value = 5, message = "实验序号不能大于5")
    @NotNull(message = "实验序号不能为空")
    private Long experimentId;

    @ApiModelProperty(value = "类型")
    @NotNull
    private Byte type;

    @ApiModelProperty(value = "分值")
    @NotNull
    private Byte score;

    @ApiModelProperty(value = "内容")
    @NotNull
    private String content;

    @ApiModelProperty(value = "图片URL")
    private String imgUrl;

}
