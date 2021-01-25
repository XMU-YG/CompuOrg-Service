package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.compuOrg.model.po.TestResultPo;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author snow create 2021/01/25 22:48
 */
@Data
public class TestResult implements VoObject, Serializable {
    private Long id;
    private Long studentId;
    private Long experimentId;
    private Integer score;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private List<TopicAnswer> topicAnswers;

    public TestResult(){};

    public TestResult(TestResultPo testResultPo){
        this.id = testResultPo.getId();
        this.studentId = testResultPo.getStudentId();
        this.experimentId = testResultPo.getExperimentId();
        this.score = testResultPo.getScore();
        this.gmtCreate = testResultPo.getGmtCreate();
        this.gmtModified = testResultPo.getGmtModified();
    }

    @Override
    public Object createVo() {
        return this;
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
