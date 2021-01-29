package cn.xmu.edu.compuOrg.dao;

import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.compuOrg.mapper.StaticMemPoMapper;
import cn.xmu.edu.compuOrg.model.po.StaticMemPo;
import cn.xmu.edu.compuOrg.model.po.StaticMemPoExample;
import cn.xmu.edu.compuOrg.model.vo.exp.StaticMemReadRetVo;
import cn.xmu.edu.compuOrg.model.vo.exp.StaticMemWriteVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StaticMemExpDao {

    @Autowired
    private StaticMemPoMapper staticMemPoMapper;

    private static final Logger logger = LoggerFactory.getLogger(StaticMemExpDao.class);

    public Object read(String studentNo,String address) {
        try{
            StaticMemPoExample example=new StaticMemPoExample();
            StaticMemPoExample.Criteria criteria=example.createCriteria();
            criteria.andStudentNoEqualTo(studentNo);
            criteria.andAddressEqualTo(address);

            List<StaticMemPo> staticMemPos=staticMemPoMapper.selectByExample(example);

            if (!staticMemPos.isEmpty()){
                StaticMemPo staticMemPo=staticMemPos.get(0);
                logger.debug("static memory experiment read data: "+staticMemPo.getData()+"   address"+staticMemPo.getAddress());
                return new StaticMemReadRetVo(staticMemPo);
            }else {
                logger.debug("static memory experiment : data is null address "+ address);
                return ResponseCode.RESOURCE_ID_NOTEXIST;
            }

        }catch (DataAccessException e){
            logger.error("数据库错误");
            return ResponseCode.INTERNAL_SERVER_ERR;
        }
    }

    public Object write(String studentNo, StaticMemWriteVo vo) {
        try {

            Object object=read(studentNo,vo.getAddress().toString());
            StaticMemPo staticMemPo=new StaticMemPo();
            staticMemPo.setData(vo.getData().toString());
            staticMemPo.setAddress(vo.getAddress().toString());
            staticMemPo.setStudentNo(studentNo);
            if (object!=ResponseCode.INTERNAL_SERVER_ERR&&object!=ResponseCode.RESOURCE_ID_NOTEXIST){
                staticMemPoMapper.updateByPrimaryKeySelective(staticMemPo);
            }else{
                staticMemPoMapper.insert(staticMemPo);
            }
            logger.debug("static memory experiment write data. address: "+vo.getAddress()+"   data: "+vo.getData());
            return new StaticMemReadRetVo(staticMemPo);
        }catch (DataAccessException e){
            logger.error("数据库错误： "+e.getMessage());
            return ResponseCode.INTERNAL_SERVER_ERR;
        }
    }
}
