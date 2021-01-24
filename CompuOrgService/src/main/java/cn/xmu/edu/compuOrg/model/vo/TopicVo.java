package cn.xmu.edu.compuOrg.model.vo;

import cn.xmu.edu.compuOrg.model.po.TestPo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author snow create 2021/01/24 14:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicVo implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "类型")
    private Byte type;

    @ApiModelProperty(value = "分值")
    private Byte score;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "图片URL")
    private String imgUrl;

    public TopicVo(TestPo testPo){
        this.id = testPo.getId();
        this.type = testPo.getType();
        this.score = testPo.getScore();
        this.content = testPo.getContent();
        this.imgUrl = testPo.getImgUrl();
    }

}
