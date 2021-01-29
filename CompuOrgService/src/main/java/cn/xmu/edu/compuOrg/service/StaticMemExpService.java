package cn.xmu.edu.compuOrg.service;

import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.dao.StaticMemExpDao;
import cn.xmu.edu.compuOrg.model.vo.exp.StaticMemWriteVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StaticMemExpService {

    @Autowired
    private StaticMemExpDao staticMemExpDao;

    public ReturnObject readData(String studentNo, String address) {
        return new ReturnObject(staticMemExpDao.read(studentNo, address));
    }

    public ReturnObject writeData(String studentNo, StaticMemWriteVo vo) {
        return new ReturnObject(staticMemExpDao.write(studentNo,vo));
    }
}
