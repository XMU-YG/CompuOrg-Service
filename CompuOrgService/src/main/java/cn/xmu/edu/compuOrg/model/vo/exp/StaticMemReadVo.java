package cn.xmu.edu.compuOrg.model.vo.exp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class StaticMemReadVo {
    @ApiModelProperty(value = "地址")
    private ArrayList<Integer> address;
}
