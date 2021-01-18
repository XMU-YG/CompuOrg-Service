package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.model.vo.ArithmeticExperimentRetVo;
import cn.xmu.edu.compuOrg.model.vo.ArithmeticExperimentVo;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 运算器
 * @author yg
 */
@Service
public class ArithmeticExpService {
    public ReturnObject operateResult(ArithmeticExperimentVo arithmeticExperimentVo) {
        ReturnObject ret=null;
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=null;
        switch(arithmeticExperimentVo.getCommandOfS3_0()){
            case "0000": arithmeticExperimentRetVo=this._0000_X(arithmeticExperimentVo);break;
            case "0001": arithmeticExperimentRetVo=this._0001_X(arithmeticExperimentVo);break;
            case "0010": arithmeticExperimentRetVo=this._0010_X(arithmeticExperimentVo);break;
            case "0011": arithmeticExperimentRetVo=this._0011_X(arithmeticExperimentVo);break;
            case "0100": arithmeticExperimentRetVo=this._0100_X(arithmeticExperimentVo);break;
            case "0101": arithmeticExperimentRetVo=this._0101_X(arithmeticExperimentVo);break;
            case "0110": {
                if (arithmeticExperimentVo.getCn()==0)
                    arithmeticExperimentRetVo=this._0110_0(arithmeticExperimentVo);
                else
                    arithmeticExperimentRetVo=this._0110_1(arithmeticExperimentVo);
                break;
            }
            case "0111": {
                if (arithmeticExperimentVo.getCn()==0)
                    arithmeticExperimentRetVo=this._0111_0(arithmeticExperimentVo);
                else
                    arithmeticExperimentRetVo=this._0111_1(arithmeticExperimentVo);
                break;
            }
            case "1000": arithmeticExperimentRetVo=this._1000_X(arithmeticExperimentVo);break;
            case "1001": arithmeticExperimentRetVo=this._1001_X(arithmeticExperimentVo);break;
            case "1010": arithmeticExperimentRetVo=this._1010_X(arithmeticExperimentVo);break;
            case "1011": arithmeticExperimentRetVo=this._1011_X(arithmeticExperimentVo);break;
            case "1100": arithmeticExperimentRetVo=this._1100_X(arithmeticExperimentVo);break;
            case "1101": arithmeticExperimentRetVo=this._1101_X(arithmeticExperimentVo);break;
        }
        ret=new ReturnObject(arithmeticExperimentRetVo);

        return ret;
    }

    private ArithmeticExperimentRetVo _1101_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 加 1
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int flag=0;
        int index = 0;
        int []temp = {0,0,0,0,0,0,0,0};
        for(;index<8;index++) {
            if(index==0)
                temp[7-index] = arithmeticExperimentVo.getMemA()[7-index]+1+flag;
            else temp[7-index] = arithmeticExperimentVo.getMemA()[7-index]+flag;
            if (temp[7-index]>=2){
                temp[7-index]-=2;
                if (index==7){
                    arithmeticExperimentRetVo.setFC(1);
                }
                flag=1;  //有进位1
            }
            else{
                flag=0;
            }
        }
        arithmeticExperimentRetVo.setF(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _1100_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 减 1
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int flag=0;
        int index = 0;
        int []temp = {0,0,0,0,0,0,0,0};
        for(;index<8;index++) {
            if (index==0)
                temp[7-index] = arithmeticExperimentVo.getMemA()[7-index] -1 - flag;
            else
                temp[7-index] = arithmeticExperimentVo.getMemA()[7-index] - flag;
            if (temp[7-index]<0){
                temp[7-index]+=2;
                if (index==7){
                    arithmeticExperimentRetVo.setFC(1);
                }
                flag=1;  //有借位
            }
            else flag=0;
        }
        arithmeticExperimentRetVo.setF(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _1011_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 减 B
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int flag=0;
        int index = 0;
        int []temp = {0,0,0,0,0,0,0,0};
        for(;index<8;index++) {
            temp[7-index]=arithmeticExperimentVo.getMemA()[7-index]-arithmeticExperimentVo.getMemB()[7-index]-flag;
            if (temp[7-index]<0){
                temp[7-index]+=2;
                if (index==7){
                    arithmeticExperimentRetVo.setFC(1);
                }
                flag=1;  //有进位1
            }
            else{
                flag=0;
            }
        }
        arithmeticExperimentRetVo.setF(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _1010_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 加 B 加 FC
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=this._1001_X(arithmeticExperimentVo);
        int flag=0;
        int index = 0;
        int []temp = {0,0,0,0,0,0,0,0};
        for(;index<8;index++) {
            if(index==0)
            {
                temp[7-index] = arithmeticExperimentRetVo.getF()[7-index] +arithmeticExperimentRetVo.getFC();
            }
            else {
                temp[7-index] = arithmeticExperimentRetVo.getF()[7-index] +flag;
            }
            if (temp[7-index]>=2){
                temp[7-index]-=2;
                if (index==7){
                    arithmeticExperimentRetVo.setFC(1);
                }
                flag=1;  //有进位1
            }
            else{
                flag=0;
            }
        }
        arithmeticExperimentRetVo.setF(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _1001_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 加 B
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int flag=0;
        int index = 0;
        int []temp = {0,0,0,0,0,0,0,0};
        for(;index<8;index++) {
            temp[7-index]=arithmeticExperimentVo.getMemA()[7-index]+arithmeticExperimentVo.getMemB()[7-index]+flag;
            if (temp[7-index]>=2){
                temp[7-index]-=2;
                if (index==7){
                    arithmeticExperimentRetVo.setFC(1);
                }
                flag=1;  //有进位1
            }
            else{
                flag=0;
            }
        }
        arithmeticExperimentRetVo.setF(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _1000_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //置 FC=CN
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        arithmeticExperimentRetVo.setFC(arithmeticExperimentVo.getCn());
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0111_1(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 带进位左移一位
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int []temp = {0,0,0,0,0,0,0,0};
        int index = 0;
        for(;index<8;index++) {
            if (index==7){
                arithmeticExperimentRetVo.setFC(arithmeticExperimentVo.getMemA()[0]);
                temp[7]=arithmeticExperimentRetVo.getFC();
            }
            else{
                temp[index]=arithmeticExperimentVo.getMemA()[index+1];
            }
        }
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setMemA(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0111_0(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 逻辑左移一位
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int []temp = {0,0,0,0,0,0,0,0};
        int index = 0;
        for(;index<8;index++) {
            if (index==7){
                temp[7] = 0;
            }
            else{
                temp[index]=arithmeticExperimentVo.getMemA()[index+1];
            }
        }
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setMemA(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0110_1(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 带进位右移一位
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int []temp = {0,0,0,0,0,0,0,0};
        int index = 0;
        for(;index<8;index++) {
            if (index==0){
                arithmeticExperimentRetVo.setFC(arithmeticExperimentVo.getMemA()[7]);
                temp[0] = arithmeticExperimentRetVo.getFC();
            }
            else{
                temp[index]=arithmeticExperimentVo.getMemA()[index-1];
            }
        }
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setMemA(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0110_0(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 逻辑右移一位
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int []temp = {0,0,0,0,0,0,0,0};
        int index = 0;
        for(;index<8;index++) {
            if (index==0){
                temp[0] = 0;
            }
            else{
                temp[index]=arithmeticExperimentVo.getMemA()[index-1];
            }
        }
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setMemA(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0101_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 不带进位循环右移 B（取低 3 位）位
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int count=arithmeticExperimentVo.getMemB()[7]+arithmeticExperimentVo.getMemB()[6]*2+arithmeticExperimentVo.getMemB()[5]*4;
        int []temp = {0,0,0,0,0,0,0,0};
        int index = 0;
        for(;index<8;index++) {
            int v=arithmeticExperimentVo.getMemA()[(index-count+8)%8];
            temp[index] = v;
        }
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setMemA(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0100_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=/A
        int []temp=new int[10];
        int i=0;
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        for (int v:arithmeticExperimentVo.getMemA()
        ) {
            temp[i]=v==0?1:0;
            i++;
        }
        arithmeticExperimentRetVo.setMemA(temp);
        arithmeticExperimentRetVo.setF(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0000_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        arithmeticExperimentRetVo.setF(arithmeticExperimentVo.getMemA());
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0001_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=B
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        arithmeticExperimentRetVo.setF(arithmeticExperimentVo.getMemB());
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0010_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A与B
        int []temp=new int[8];
        int i=0;
        int []memB=arithmeticExperimentVo.getMemB();
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        for (int v:arithmeticExperimentVo.getMemA()
             ) {
            temp[i]=v&memB[i];
            i++;
        }
        arithmeticExperimentRetVo.setF(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }
    //F=A|B
    private ArithmeticExperimentRetVo _0011_X(ArithmeticExperimentVo arithmeticExperimentVo) {

        int []temp=new int[10];
        int i=0;
        int []memB=arithmeticExperimentVo.getMemB();
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        for (int v:arithmeticExperimentVo.getMemA()
        ) {
            temp[i]=v|memB[i];
            i++;
        }
        arithmeticExperimentRetVo.setF(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(Arrays.equals(temp, zero) ?1:0);
        return arithmeticExperimentRetVo;
    }


}
