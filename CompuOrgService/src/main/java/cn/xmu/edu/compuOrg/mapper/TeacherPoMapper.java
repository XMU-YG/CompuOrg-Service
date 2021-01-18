package cn.xmu.edu.compuOrg.mapper;

import cn.xmu.edu.compuOrg.model.po.TeacherPo;
import cn.xmu.edu.compuOrg.model.po.TeacherPoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TeacherPoMapper {
    long countByExample(TeacherPoExample example);

    int deleteByExample(TeacherPoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TeacherPo record);

    int insertSelective(TeacherPo record);

    List<TeacherPo> selectByExample(TeacherPoExample example);

    TeacherPo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TeacherPo record, @Param("example") TeacherPoExample example);

    int updateByExample(@Param("record") TeacherPo record, @Param("example") TeacherPoExample example);

    int updateByPrimaryKeySelective(TeacherPo record);

    int updateByPrimaryKey(TeacherPo record);
}