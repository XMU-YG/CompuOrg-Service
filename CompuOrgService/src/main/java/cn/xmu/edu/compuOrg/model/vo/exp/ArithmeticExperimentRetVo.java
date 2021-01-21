package cn.xmu.edu.compuOrg.model.vo.exp;

import cn.xmu.edu.compuOrg.model.vo.exp.ArithmeticExperimentVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;

/**
 * 运算器结果返回
 * @author yg
 */
@Data
public class ArithmeticExperimentRetVo {
    @ApiModelProperty(value = "S3-S0指令")
    private String commandOfS3_0;
    @ApiModelProperty(value = "暂存器A")
    private ArrayList<Integer> memA;
    @ApiModelProperty(value = "暂存器B")
    private ArrayList<Integer>memB;
    @ApiModelProperty(value = "控制单元Cn位")
    private int Cn;
    @ApiModelProperty(value = "零标识符")
    private int FZ=0;
    @ApiModelProperty(value = "进位标识符")
    private int FC=0;
    @ApiModelProperty(value = "结果暂存器")
    private ArrayList<Integer> F=new ArrayList<>(8){
        {
            add(0);add(0);add(0);add(0);add(0);add(0);add(0);add(0);
        }
    };

    public ArithmeticExperimentRetVo(ArithmeticExperimentVo arithmeticExperimentVo){
        this.Cn=arithmeticExperimentVo.getCn();
        this.commandOfS3_0=arithmeticExperimentVo.getCommandOfS3_0();
        this.memA=arithmeticExperimentVo.getMemA();
        this.memB=arithmeticExperimentVo.getMemB();
    }
}
