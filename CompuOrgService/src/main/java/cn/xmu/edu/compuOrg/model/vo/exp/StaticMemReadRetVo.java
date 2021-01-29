package cn.xmu.edu.compuOrg.model.vo.exp;

import cn.xmu.edu.Core.util.JacksonUtil;
import cn.xmu.edu.compuOrg.model.po.StaticMemPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class StaticMemReadRetVo implements Serializable {
    @ApiModelProperty(value = "数据")
    private String data;

    public StaticMemReadRetVo(StaticMemPo staticMemPo){
        this.data= staticMemPo.getData();
    }

    public StaticMemReadRetVo() {

    }
}
