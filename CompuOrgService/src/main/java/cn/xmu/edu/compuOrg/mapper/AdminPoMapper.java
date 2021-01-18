package cn.xmu.edu.compuOrg.mapper;

import cn.xmu.edu.compuOrg.model.po.AdminPo;
import cn.xmu.edu.compuOrg.model.po.AdminPoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface AdminPoMapper {
    long countByExample(AdminPoExample example);

    int deleteByExample(AdminPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AdminPo record);

    int insertSelective(AdminPo record);

    List<AdminPo> selectByExample(AdminPoExample example);

    AdminPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AdminPo record, @Param("example") AdminPoExample example);

    int updateByExample(@Param("record") AdminPo record, @Param("example") AdminPoExample example);

    int updateByPrimaryKeySelective(AdminPo record);

    int updateByPrimaryKey(AdminPo record);
}