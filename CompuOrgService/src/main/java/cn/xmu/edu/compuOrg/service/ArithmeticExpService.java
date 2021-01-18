package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.model.vo.ArithmeticExperimentRetVo;
import cn.xmu.edu.compuOrg.model.vo.ArithmeticExperimentVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        ArrayList<Integer> temp=new ArrayList<>(8);
        for(;index<8;index++) {
            if(index==0)
                temp.set(7 - index, arithmeticExperimentVo.getMemA().get(7 - index) + 1 + flag);
            else temp.set(7 - index, arithmeticExperimentVo.getMemA().get(7 - index) + flag);
            if (temp.get(7 - index) >=2){
                temp.set(7 - index, 2);
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
        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
        return arithmeticExperimentRetVo;
    }

    /**
     * 判断FZ位
     * @param f 运算结果
     * @return 1；0
     * @author yg
     */
    private int isFZ(ArrayList<Integer> f) {
        for (Integer integer : f) {
            if (integer!=0){
                return 0;
            }
        }
        return 1;
    }

    private ArithmeticExperimentRetVo _1100_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 减 1
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int flag=0;
        int index = 0;
        ArrayList<Integer> temp=new ArrayList<>(8);
        for(;index<8;index++) {
            if (index==0)
                temp.set(7 - index, arithmeticExperimentVo.getMemA().get(7 - index) - 1 - flag);
            else
                temp.set(7 - index, arithmeticExperimentVo.getMemA().get(7 - index) - flag);
            if (temp.get(7 - index) <0){
                temp.set(7 - index, 2);
                if (index==7){
                    arithmeticExperimentRetVo.setFC(1);
                }
                flag=1;  //有借位
            }
            else flag=0;
        }
        arithmeticExperimentRetVo.setF(temp);
        int []zero = {0,0,0,0,0,0,0,0};
        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _1011_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 减 B
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int flag=0;
        int index = 0;
        ArrayList<Integer> temp=new ArrayList<>(8);
        for(;index<8;index++) {
            temp.set(7 - index, arithmeticExperimentVo.getMemA().get(7 - index) - arithmeticExperimentVo.getMemB().get(7 - index) - flag);
            if (temp.get(7 - index) <0){
                temp.set(7 - index, 2);
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
        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _1010_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 加 B 加 FC
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=this._1001_X(arithmeticExperimentVo);
        int flag=0;
        int index = 0;
        ArrayList<Integer> temp=new ArrayList<>(8);
        for(;index<8;index++) {
            if(index==0)
            {
                temp.set(7 - index, arithmeticExperimentRetVo.getF().get(7 - index) + arithmeticExperimentRetVo.getFC());
            }
            else {
                temp.set(7 - index, arithmeticExperimentRetVo.getF().get(7 - index) + flag);
            }
            if (temp.get(7 - index) >=2){
                temp.set(7 - index, 2);
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
        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _1001_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 加 B
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int flag=0;
        int index = 0;
        ArrayList<Integer> temp=new ArrayList<>(8);
        for(;index<8;index++) {
            temp.set(7 - index, arithmeticExperimentVo.getMemA().get(7 - index) + arithmeticExperimentVo.getMemB().get(7 - index) + flag);
            if (temp.get(7 - index) >=2){
                Integer a= temp.get(7-index)-2;
                temp.set(7 - index, a);
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

        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
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
        ArrayList<Integer> temp=new ArrayList<>(8);
        int index = 0;
        for(;index<8;index++) {
            if (index==7){
                arithmeticExperimentRetVo.setFC(arithmeticExperimentVo.getMemA().get(0));
                temp.set(7, arithmeticExperimentRetVo.getFC());
            }
            else{
                temp.set(index, arithmeticExperimentVo.getMemA().get(index + 1));
            }
        }
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setMemA(temp);
        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0111_0(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 逻辑左移一位
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        ArrayList<Integer> temp=new ArrayList<>(8);
        int index = 0;
        for(;index<8;index++) {
            if (index==7){
                temp.set(7, 0);
            }
            else{
                temp.set(index, arithmeticExperimentVo.getMemA().get(index + 1));
            }
        }
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setMemA(temp);
        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0110_1(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 带进位右移一位
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        ArrayList<Integer> temp=new ArrayList<>(8);
        int index = 0;
        for(;index<8;index++) {
            if (index==0){
                arithmeticExperimentRetVo.setFC(arithmeticExperimentVo.getMemA().get(7));
                temp.set(0, arithmeticExperimentRetVo.getFC());
            }
            else{
                temp.set(index, arithmeticExperimentVo.getMemA().get(index - 1));
            }
        }
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setMemA(temp);
        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0110_0(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 逻辑右移一位
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        ArrayList<Integer> temp=new ArrayList<>(8);
        int index = 0;
        for(;index<8;index++) {
            if (index==0){
                temp.set(0, 0);
            }
            else{
                temp.set(index, arithmeticExperimentVo.getMemA().get(index - 1));
            }
        }
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setMemA(temp);
        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0101_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=A 不带进位循环右移 B（取低 3 位）位
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        int count= arithmeticExperimentVo.getMemB().get(7) + arithmeticExperimentVo.getMemB().get(6) *2+ arithmeticExperimentVo.getMemB().get(5) *4;
        ArrayList<Integer> temp=new ArrayList<>(8);
        int index = 0;
        for(;index<8;index++) {
            int v= arithmeticExperimentVo.getMemA().get((index - count + 8) % 8);
            temp.set(index, v);
        }
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setMemA(temp);
        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
        return arithmeticExperimentRetVo;
    }

    private ArithmeticExperimentRetVo _0100_X(ArithmeticExperimentVo arithmeticExperimentVo) {
        //F=/A
        ArrayList<Integer> temp=new ArrayList<>(8);
        int i=0;
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        for (int v:arithmeticExperimentVo.getMemA()
        ) {
            temp.set(i, v == 0 ? 1 : 0);
            i++;
        }
        arithmeticExperimentRetVo.setMemA(temp);
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
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
        ArrayList<Integer> temp=new ArrayList<>(8);
        int i=0;
        ArrayList<Integer> memB=arithmeticExperimentVo.getMemB();
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        for (int v:arithmeticExperimentVo.getMemA()
             ) {
            temp.set(i, v & memB.get(i));
            i++;
        }
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
        return arithmeticExperimentRetVo;
    }
    //F=A|B
    private ArithmeticExperimentRetVo _0011_X(ArithmeticExperimentVo arithmeticExperimentVo) {

        ArrayList<Integer> temp=new ArrayList<>(8);
        int i=0;
        ArrayList<Integer> memB=arithmeticExperimentVo.getMemB();
        ArithmeticExperimentRetVo arithmeticExperimentRetVo=new ArithmeticExperimentRetVo(arithmeticExperimentVo);
        for (int v:arithmeticExperimentVo.getMemA()
        ) {
            temp.set(i, v | memB.get(i));
            i++;
        }
        arithmeticExperimentRetVo.setF(temp);
        arithmeticExperimentRetVo.setFZ(isFZ(arithmeticExperimentRetVo.getF()));
        return arithmeticExperimentRetVo;
    }


}
