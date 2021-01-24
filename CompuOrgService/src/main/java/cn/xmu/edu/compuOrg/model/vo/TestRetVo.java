package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;

/**
 * @author snow create 2021/01/24 14:56
 */
@Data
public class TestRetVo {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "题目数量")
    private Integer size;

    @ApiModelProperty(value = "实验序号")
    private Integer experimentId;

    @ApiModelProperty(value = "题目列表")
    private ArrayList<TopicRetVo> topics;

}
