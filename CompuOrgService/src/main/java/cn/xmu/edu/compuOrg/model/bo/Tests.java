package cn.xmu.edu.compuOrg.model.bo;

import cn.xmu.edu.Core.model.VoObject;
import cn.xmu.edu.compuOrg.model.vo.TestRetVo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author snow create 2021/01/24 18:45
 */
@Data
public class Tests implements VoObject, Serializable {

    private Long size;
    private Long experimentId;
    private ArrayList<Topic> topics;

    @Override
    public Object createVo() {
        return new TestRetVo(this);
    }

    @Override
    public Object createSimpleVo() {
        return null;
    }
}
