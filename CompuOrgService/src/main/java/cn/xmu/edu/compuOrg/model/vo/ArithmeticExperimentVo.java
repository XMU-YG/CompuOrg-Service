package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 实验一开始实验输入参数
 * @author yg
 */
@Data
public class ArithmeticExperimentVo {

    @ApiModelProperty(value = "S3-S0指令")
    private String commandOfS3_0;
    @ApiModelProperty(value = "暂存器A")
    private int []memA;
    @ApiModelProperty(value = "暂存器B")
    private int []memB;
    @ApiModelProperty(value = "控制单元Cn位")
    private int Cn;

}
