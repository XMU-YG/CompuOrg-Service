package cn.xmu.edu.compuOrg.dao;

import cn.xmu.edu.compuOrg.model.vo.LinesVo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author snow 2021/01/12 21:50
 */
@Repository
public class ExperimentLinesDao {
    private Map<String, String> experimentOne;
    private Map<String, String> experimentTwo;
    private Map<String, String> experimentThree;
    private Map<String, String> experimentFour;
    private Map<String, String> experimentFive;
    private Map<Integer, Map<String, String>> experimentLines;

    ExperimentLinesDao(){
        experimentOne = new HashMap();
        experimentTwo = new HashMap();
        experimentThree = new HashMap();
        experimentFour = new HashMap();
        experimentFive = new HashMap();
        experimentLines = new HashMap<>();
        initializeOne();
        initializeTwo();
        initializeThree();
        initializeFour();
        initializeFive();
        initializeAll();
    }

    private void initializeOne(){
        experimentOne.put("aluALU_B", "conALU_B");
        experimentOne.put("aluS3-S0", "conS3-S0");
        experimentOne.put("aluCn", "conCn");
        experimentOne.put("aluLDA-LDB", "conLDA-LDB");
        experimentOne.put("aluIN7-IN0", "SD27-SD20");
        experimentOne.put("outD7-D0", "cpuD7-D0");
        experimentOne.put("aluD7-D0", "cpuD7-D0");
        experimentOne.put("clk0", "30HZ");
        experimentOne.put("time-con-T4-T1", "con-bus-T4-T1");
    }
    private void initializeTwo(){
        experimentTwo.put("clk0", "30HZ");
        experimentTwo.put("time-con-T4-T1", "con-bus-T4-T1");
        experimentTwo.put("con-bus-XD7-XD0", "MEM-unit-D7-D0");
        experimentTwo.put("con-bus-XA7-XA0", "MEM-unit-A7-A0");
        experimentTwo.put("con-bus-XMWR-XMRD", "MEM-unit-WR-RD");
        experimentTwo.put("con-bus-WR-RD-IOM", "con-unit-WR-RD-IOM");
        experimentTwo.put("con-unit-IOR", "in-unit-IN_B-RD");
        experimentTwo.put("con-unit-LDAR", "pc-ar-LDAR");
        experimentTwo.put("pc-ar-D7-D0", "cpuD7-D0");
        experimentTwo.put("in-unit-D7-D0", "cpuD7-D0");
        experimentTwo.put("pc-ar-D7-D0", "in-unit-D7-D0");
    }
    private void initializeThree(){
        experimentThree.put("clk0", "30HZ");
        experimentThree.put("time-con-T4-T1", "con-bus-T4-T1");
        experimentThree.put("con-bus-XIOW-XIOR", "expansion-E3-E2");
        experimentThree.put("con-bus-XMWR-XMRD", "expansion-E1-E0");
        experimentThree.put("con-bus-WR-RD-IOM", "con-unit-WR-RD-IOM");
    }
    private void initializeFour(){
        experimentFour.put("clk0", "30HZ");
        experimentFour.put("time-con-T4-T1", "con-bus-T4-T1");
        experimentFour.put("in-unit-D7-D0", "con-bus-XD7-XD0");
        experimentFour.put("out-unit-D7-D0", "con-bus-XD7-XD0");
        experimentFour.put("con-bus-WR-RD-IOM", "mc-unit-WR-RD-IOM");
        experimentFour.put("con-bus-XIOR", "in-unit-IN_B-RD");
        experimentFour.put("con-bus-XIOW", "out-unit-WR");
        experimentFour.put("out-unit-LED_B", "expansion-GND");
        experimentFour.put("alu-reg-unit-IN7-IN0", "cpuD7-D0");
        experimentFour.put("alu-reg-unit-OUT7-OUT0", "cpuD7-D0");
        experimentFour.put("alu-reg-unit-D7-D0", "cpuD7-D0");
        experimentFour.put("mc-unit-ALU_B", "alu-reg-unit-ALU_B");
        experimentFour.put("mc-unit-LDA-LDB", "alu-reg-unit-LDA-LDB");
        experimentFour.put("mc-unit-S3-S0", "alu-reg-unit-S3-S0");
        experimentFour.put("mc-unit-RS_B", "alu-reg-unit-RO_B");
        experimentFour.put("mc-unit-LDRi", "alu-reg-unit-LDRO");
        experimentFour.put("mc-unit-LDIR", "ir-unit-LDIR");
        experimentFour.put("mc-unit-F1", "ir-unit-F1");
        experimentFour.put("mc-unit-SE5-SE0", "ir-unit-SE5-SE0");
        experimentFour.put("con-unit-SD27-SD20", "ir-unit-D7-D0");
    }
    private void initializeFive(){

    }
    private void initializeAll(){
        experimentLines.put(1, experimentOne);
        experimentLines.put(2, experimentTwo);
        experimentLines.put(3, experimentThree);
        experimentLines.put(4, experimentFour);
        experimentLines.put(5, experimentFive);
    }

    public Boolean validAllLines(Integer experimentId, List<LinesVo> linesVos){
        for(LinesVo linesVo : linesVos){
            if(!validSingleConnection(experimentId, linesVo))
                return false;
        }
        return true;
    }

    public Boolean validSingleConnection(Integer experimentId, LinesVo linesVo){
        Map<String, String> experimentLine = this.experimentLines.get(experimentId);
        if(experimentLine != null){
            if(experimentLine.get(linesVo.getEndA()) == null){
                if(experimentLine.get(linesVo.getEndB()) != null &&
                        experimentLine.get(linesVo.getEndB()).equals(linesVo.getEndA()))
                    return true;
            }
            else{
                if(experimentLine.get(linesVo.getEndA()).equals(linesVo.getEndB()))
                    return true;
            }
        }
        return false;
    }
}
