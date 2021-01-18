package cn.xmu.edu.compuOrg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class LinesVo {

    @ApiModelProperty(value = "连线列表")
    @NotNull
    @Valid
    private List<LineVo> lines;
}
