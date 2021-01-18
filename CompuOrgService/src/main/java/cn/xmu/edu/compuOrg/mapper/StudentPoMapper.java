package cn.xmu.edu.compuOrg.mapper;

import cn.xmu.edu.compuOrg.model.po.StudentPo;
import cn.xmu.edu.compuOrg.model.po.StudentPoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface StudentPoMapper {
    long countByExample(StudentPoExample example);

    int deleteByExample(StudentPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(StudentPo record);

    int insertSelective(StudentPo record);

    List<StudentPo> selectByExample(StudentPoExample example);

    StudentPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") StudentPo record, @Param("example") StudentPoExample example);

    int updateByExample(@Param("record") StudentPo record, @Param("example") StudentPoExample example);

    int updateByPrimaryKeySelective(StudentPo record);

    int updateByPrimaryKey(StudentPo record);
}