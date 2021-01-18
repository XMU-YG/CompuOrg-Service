package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.compuOrg.CompuOrgServiceApplication;
import cn.xmu.edu.compuOrg.model.vo.ArithmeticExperimentRetVo;
import cn.xmu.edu.compuOrg.model.vo.ArithmeticExperimentVo;
import cn.xmu.edu.compuOrg.service.ArithmeticExpService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

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
        int []a={1,0,0,0,0,0,0,1};
        arithmeticExperimentVo.setMemA(a);
        int []b={0,0,1,1,0,0,1,0};
        arithmeticExperimentVo.setMemB(b);
        return arithmeticExperimentVo;
    }

    @Test
    public void _0000_X() throws JSONException {
        ArithmeticExperimentVo arithmeticExperimentVo=initArithmeticVo("0000",1);
        ArithmeticExperimentRetVo arithmeticExperimentRetVo= (ArithmeticExperimentRetVo) arithmeticExpService.operateResult(arithmeticExperimentVo).getData();
        JSONAssert.assertEquals(arithmeticExperimentVo.getMemA().toString(),arithmeticExperimentRetVo.getF().toString(),true);
    }

}
