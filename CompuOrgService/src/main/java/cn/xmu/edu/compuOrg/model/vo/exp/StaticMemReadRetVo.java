package cn.xmu.edu.compuOrg.model.vo.exp;

import cn.xmu.edu.Core.util.JacksonUtil;
import cn.xmu.edu.compuOrg.model.po.StaticMemPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class StaticMemReadRetVo {
    @ApiModelProperty(value = "数据")
    private ArrayList<Integer> data;

    public StaticMemReadRetVo(StaticMemPo staticMemPo){
        this.data= (ArrayList<Integer>) JacksonUtil.parseIntegerList(staticMemPo.getData(),"");
    }

    public StaticMemReadRetVo() {

    }
}
