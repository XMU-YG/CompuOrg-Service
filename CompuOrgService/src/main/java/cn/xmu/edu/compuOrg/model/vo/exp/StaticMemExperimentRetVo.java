package cn.xmu.edu.compuOrg.model.vo.exp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class StaticMemExperimentRetVo {
    @ApiModelProperty(value = "数据")
    private ArrayList<Integer> data;
}
