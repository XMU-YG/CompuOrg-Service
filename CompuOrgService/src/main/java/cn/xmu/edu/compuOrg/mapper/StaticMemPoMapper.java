package cn.xmu.edu.compuOrg.mapper;

import cn.xmu.edu.compuOrg.model.po.StaticMemPo;
import cn.xmu.edu.compuOrg.model.po.StaticMemPoExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface StaticMemPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table static_memory
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table static_memory
     *
     * @mbg.generated
     */
    int insert(StaticMemPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table static_memory
     *
     * @mbg.generated
     */
    int insertSelective(StaticMemPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table static_memory
     *
     * @mbg.generated
     */
    List<StaticMemPo> selectByExample(StaticMemPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table static_memory
     *
     * @mbg.generated
     */
    StaticMemPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table static_memory
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") StaticMemPo record, @Param("example") StaticMemPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table static_memory
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") StaticMemPo record, @Param("example") StaticMemPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table static_memory
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(StaticMemPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table static_memory
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(StaticMemPo record);
}