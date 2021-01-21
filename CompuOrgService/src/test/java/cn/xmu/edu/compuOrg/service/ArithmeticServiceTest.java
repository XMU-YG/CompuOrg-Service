package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.compuOrg.CompuOrgServiceApplication;
import cn.xmu.edu.compuOrg.model.vo.exp.ArithmeticExperimentRetVo;
import cn.xmu.edu.compuOrg.model.vo.exp.ArithmeticExperimentVo;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest(classes = CompuOrgServiceApplication.class)
@AutoConfigureMockMvc
@Slf4j
public class ArithmeticServiceTest {
    @Autowired
    private ArithmeticExpService arithmeticExpService;

    private ArithmeticExperimentVo initArithmeticVo(String s3_0,int cn){
        ArithmeticExperimentVo arithmeticExperimentVo=new ArithmeticExperimentVo();
        arithmeticExperimentVo.setCn(cn);
        arithmeticExperimentVo.setCommandOfS3_0(s3_0);
        ArrayList<Integer> a=new ArrayList<>(8){
            {
                add(1);add(0);add(0);add(0);add(0);add(0);add(0);add(1);
            }
        };
        arithmeticExperimentVo.setMemA(a);
        ArrayList<Integer> b=new ArrayList<>(8){
            {
                add(0);add(0);add(1);add(1);add(0);add(0);add(1);add(0);
            }
        };
        arithmeticExperimentVo.setMemB(b);
        return arithmeticExperimentVo;
    }

    /**
     * F=A测试
     * @throws JSONException
     */
    @Test
    public void _0000_X() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("0000",1);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        JSONAssert.assertEquals(arithmeticExperimentVo.getMemA().toString(),arithmeticExperimentRetVo.getF().toString(),true);
    }

    /**
     * F=B
     * @throws JSONException
     */

    @Test
    public void _0001_X() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("0001",1);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        JSONAssert.assertEquals(arithmeticExperimentVo.getMemB().toString(),arithmeticExperimentRetVo.getF().toString(),true);
    }

    /**
     * F=A&B
     * @throws JSONException
     */
    @Test
    public void _0010_X() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("0010",1);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        ArrayList<Integer> expect=new ArrayList<>(8){
            {
                add(0);add(0);add(0);add(0);add(0);add(0);add(0);add(0);
            }
        };
        JSONAssert.assertEquals(expect.toString(),arithmeticExperimentRetVo.getF().toString(),true);
    }

    /**
     * F=A|B
     * @throws JSONException
     */
    @Test
    public void _0011_X() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("0011",1);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        ArrayList<Integer> expect=new ArrayList<>(8){
            {
                add(1);add(0);add(1);add(1);add(0);add(0);add(1);add(1);
            }
        };
        JSONAssert.assertEquals(expect.toString(),arithmeticExperimentRetVo.getF().toString(),true);
    }

    /**
     * F=/A
     * @throws JSONException
     */
    @Test
    public void _0100_X() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("0100",1);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        ArrayList<Integer> expect=new ArrayList<>(8){
            {
                add(0);add(1);add(1);add(1);add(1);add(1);add(1);add(0);
            }
        };
        JSONAssert.assertEquals(expect.toString(),arithmeticExperimentRetVo.getF().toString(),true);
    }

    //移位运算
    /**
     * F=A 不带进位循环右移 B（取低 3 位）位
     * 移位运算
     * @throws JSONException
     */
    @Test
    public void _0101_X() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("0101",1);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        ArrayList<Integer> expect=new ArrayList<>(8){
            {
                add(0);add(1);add(1);add(0);add(0);add(0);add(0);add(0);
            }
        };
        JSONAssert.assertEquals(expect.toString(),arithmeticExperimentRetVo.getF().toString(),true);
        JSONAssert.assertEquals("0",String.valueOf(arithmeticExperimentRetVo.getFZ()),true);
    }

    /**
     * F=A 逻辑右移一位
     * 移位运算
     * @throws JSONException
     */
    @Test
    public void _0110_0() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("0110",0);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        ArrayList<Integer> expect=new ArrayList<>(8){
            {
                add(0);add(1);add(0);add(0);add(0);add(0);add(0);add(0);
            }
        };
        JSONAssert.assertEquals(expect.toString(),arithmeticExperimentRetVo.getF().toString(),true);
        JSONAssert.assertEquals("0",String.valueOf(arithmeticExperimentRetVo.getFZ()),true);
    }

    /**
     * F=A 带进位循环右移一位
     * 移位运算
     * @throws JSONException
     */
    @Test
    public void _0110_1() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("0110",1);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        ArrayList<Integer> expect=new ArrayList<>(8){
            {
                add(1);add(1);add(0);add(0);add(0);add(0);add(0);add(0);
            }
        };
        JSONAssert.assertEquals(expect.toString(),arithmeticExperimentRetVo.getF().toString(),true);
        JSONAssert.assertEquals("0",String.valueOf(arithmeticExperimentRetVo.getFZ()),true);
        JSONAssert.assertEquals("1",String.valueOf(arithmeticExperimentRetVo.getFC()),true);
    }

    /**
     * F=A 逻辑左移一位
     * 移位运算
     * @throws JSONException
     */
    @Test
    public void _0111_0() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("0111",0);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        ArrayList<Integer> expect=new ArrayList<>(8){
            {
                add(0);add(0);add(0);add(0);add(0);add(0);add(1);add(0);
            }
        };
        JSONAssert.assertEquals(expect.toString(),arithmeticExperimentRetVo.getF().toString(),true);
        JSONAssert.assertEquals("0",String.valueOf(arithmeticExperimentRetVo.getFZ()),true);
    }

    /**
     * F=A 带进位循环左移一位
     * 移位运算
     * @throws JSONException
     */
    @Test
    public void _0111_1() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("0111",1);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        ArrayList<Integer> expect=new ArrayList<>(8){
            {
                add(0);add(0);add(0);add(0);add(0);add(0);add(1);add(1);
            }
        };
        JSONAssert.assertEquals(expect.toString(),arithmeticExperimentRetVo.getF().toString(),true);
        JSONAssert.assertEquals("0",String.valueOf(arithmeticExperimentRetVo.getFZ()),true);
        JSONAssert.assertEquals("1",String.valueOf(arithmeticExperimentRetVo.getFC()),true);
    }

    /**
     * F=A 加 B
     * 算术运算
     * @throws JSONException
     */
    @Test
    public void _1001_X() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("1001",1);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        ArrayList<Integer> expect=new ArrayList<>(8){
            {
                add(1);add(0);add(1);add(1);add(0);add(0);add(1);add(1);
            }
        };
        JSONAssert.assertEquals(expect.toString(),arithmeticExperimentRetVo.getF().toString(),true);
        JSONAssert.assertEquals("0",String.valueOf(arithmeticExperimentRetVo.getFZ()),true);
        JSONAssert.assertEquals("0",String.valueOf(arithmeticExperimentRetVo.getFC()),true);
    }

    /**
     * F=A 加 B 加 FC
     * 算术运算
     * @throws JSONException
     */
    @Test
    public void _1010_X() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("1010",1);
        arithmeticExperimentVo.getMemB().set(0,1);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        ArrayList<Integer> expect=new ArrayList<>(8){
            {
                add(0);add(0);add(1);add(1);add(0);add(1);add(0);add(0);
            }
        };
        JSONAssert.assertEquals(expect.toString(),arithmeticExperimentRetVo.getF().toString(),true);
        JSONAssert.assertEquals("0",String.valueOf(arithmeticExperimentRetVo.getFZ()),true);
        JSONAssert.assertEquals("1",String.valueOf(arithmeticExperimentRetVo.getFC()),true);
    }

}
