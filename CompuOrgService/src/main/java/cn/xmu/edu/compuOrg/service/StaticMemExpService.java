package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.dao.StaticMemExpDao;
import cn.xmu.edu.compuOrg.model.vo.exp.StaticMemReadVo;
import cn.xmu.edu.compuOrg.model.vo.exp.StaticMemWriteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaticMemExpService {

    @Autowired
    private StaticMemExpDao staticMemExpDao;

    public ReturnObject readData(String studentNo, StaticMemReadVo staticMemReadVo) {
        return new ReturnObject(staticMemExpDao.read(studentNo, staticMemReadVo));
    }

    public ReturnObject writeData(String studentNo, StaticMemWriteVo vo) {
        return new ReturnObject(staticMemExpDao.write(studentNo,vo));
    }
}
