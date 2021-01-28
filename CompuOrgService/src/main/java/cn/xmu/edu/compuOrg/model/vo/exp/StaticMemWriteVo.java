package cn.xmu.edu.compuOrg.model.vo.exp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class StaticMemWriteVo {

    @ApiModelProperty("待写入数据地址")
    private ArrayList<Integer> address;

    @ApiModelProperty("待写入数据")
    private ArrayList<Integer> data;

}
