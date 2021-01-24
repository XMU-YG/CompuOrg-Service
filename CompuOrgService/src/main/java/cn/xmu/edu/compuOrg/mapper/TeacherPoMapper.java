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
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher
     *
     * @mbg.generated
     */
    int insert(TeacherPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher
     *
     * @mbg.generated
     */
    int insertSelective(TeacherPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher
     *
     * @mbg.generated
     */
    List<TeacherPo> selectByExample(TeacherPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher
     *
     * @mbg.generated
     */
    TeacherPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") TeacherPo record, @Param("example") TeacherPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") TeacherPo record, @Param("example") TeacherPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(TeacherPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table teacher
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(TeacherPo record);
}