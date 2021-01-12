package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LinesVo {

    @ApiModelProperty(value = "连线A端")
    private String endA;

    @ApiModelProperty(value = "连线B端")
    private String endB;
}
