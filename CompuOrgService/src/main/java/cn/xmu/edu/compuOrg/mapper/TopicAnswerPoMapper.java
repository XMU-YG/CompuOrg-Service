package cn.xmu.edu.compuOrg.mapper;

import cn.xmu.edu.compuOrg.model.po.TopicAnswerPo;
import cn.xmu.edu.compuOrg.model.po.TopicAnswerPoExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TopicAnswerPoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table topic_answer
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table topic_answer
     *
     * @mbg.generated
     */
    int insert(TopicAnswerPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table topic_answer
     *
     * @mbg.generated
     */
    int insertSelective(TopicAnswerPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table topic_answer
     *
     * @mbg.generated
     */
    List<TopicAnswerPo> selectByExample(TopicAnswerPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table topic_answer
     *
     * @mbg.generated
     */
    TopicAnswerPo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table topic_answer
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") TopicAnswerPo record, @Param("example") TopicAnswerPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table topic_answer
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") TopicAnswerPo record, @Param("example") TopicAnswerPoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table topic_answer
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(TopicAnswerPo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table topic_answer
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(TopicAnswerPo record);
}