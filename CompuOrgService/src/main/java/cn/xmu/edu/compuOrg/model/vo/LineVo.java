package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LineVo {

    @ApiModelProperty(value = "连线A端")
    @NotNull
    private String endA;

    @ApiModelProperty(value = "连线B端")
    @NotNull
    private String endB;
}
