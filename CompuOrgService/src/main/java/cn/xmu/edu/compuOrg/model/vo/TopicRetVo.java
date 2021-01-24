package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author snow create 2021/01/24 14:52
 */
@Data
public class TopicRetVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "类型")
    private Byte type;

    @ApiModelProperty(value = "分值")
    private Byte score;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "图片URL")
    private String imgUrl;

}
