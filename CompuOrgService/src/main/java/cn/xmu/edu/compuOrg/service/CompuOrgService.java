package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.compuOrg.dao.ExperimentLinesDao;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.model.vo.LinesVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompuOrgService {

    @Autowired
    private ExperimentLinesDao experimentLinesDao;

    /**
     * @author snow create 2021/01/12 20:26
     * @param experimentId
     * @param linesVo
     * @return
     */
    public ReturnObject connectLines(Integer experimentId, LinesVo linesVo){
        if(experimentLinesDao.validSingleConnection(experimentId, linesVo)){
            return new ReturnObject(ResponseCode.OK);
        }
        else{
            return new ReturnObject(ResponseCode.LINE_ENDS_NOT_VALID);
        }
    }

    public ReturnObject validAllLines(Integer experimentId, List<LinesVo> linesVos){
        if(experimentLinesDao.validAllLines(experimentId, linesVos)){
            return new ReturnObject(ResponseCode.OK);
        }
        else {
            return new ReturnObject(ResponseCode.LINE_CONNECT_ERROR);
        }
    }
}
