package cn.xmu.edu.compuOrg.dao;

import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.mapper.TeacherPoMapper;
import cn.xmu.edu.compuOrg.model.bo.Teacher;
import cn.xmu.edu.compuOrg.model.po.TeacherPo;
import cn.xmu.edu.compuOrg.model.po.TeacherPoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class TeacherDao extends UserDao {

    @Autowired
    private TeacherPoMapper teacherPoMapper;

    /**
     * 根据id查找老师
     * @author snow create 2021/01/18 10:54
     * @param teacherId
     * @return
     */
    public ReturnObject<Teacher> findTeacherById(Long teacherId){
        if(teacherId != null){
            try {
                TeacherPo teacherPo = teacherPoMapper.selectByPrimaryKey(teacherId);
                if(teacherPo != null){
                    return new ReturnObject<>(new Teacher(teacherPo));
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
            }
        }
        return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
    }

    /**
     * 根据工号查找老师
     * @author snow create 2021/01/18/11:54
     * @param teacherNo
     * @return
     */
    public ReturnObject<Teacher> findTeacherBySno(String teacherNo){
        if(teacherNo != null){
            try {
                TeacherPoExample example = new TeacherPoExample();
                TeacherPoExample.Criteria criteria = example.createCriteria();
                criteria.andTeacherNoEqualTo(teacherNo);
                List<TeacherPo> teacherPos = teacherPoMapper.selectByExample(example);
                if(teacherPos != null && teacherPos.size() > 0){
                    return new ReturnObject<>(new Teacher(teacherPos.get(0)));
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
            }
        }
        return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
    }

    /**
     * 插入老师信息
     * @author snow create 2021/01/18 11:59
     * @param teacher
     * @return
     */
    public ReturnObject<Teacher> insertTeacher(Teacher teacher){
        try {
            if(teacher.getUserNo() != null && isTeacherNoAlreadyExist(teacher.getUserNo())){
                return new ReturnObject<>(ResponseCode.TEACHER_NO_REGISTERED);
            }
            if(teacher.getEmail() != null && isEmailAlreadyExist(teacher.getEmail())){
                return new ReturnObject<>(ResponseCode.EMAIL_REGISTERED);
            }
            if(teacher.getMobile() != null && isMobileAlreadyExist(teacher.getMobile())){
                return new ReturnObject<>(ResponseCode.MOBILE_REGISTERED);
            }
            TeacherPo teacherPo = teacher.createTeacherPo();
            teacherPo.setGmtCreate(LocalDateTime.now());
            int effectRows = teacherPoMapper.insertSelective(teacherPo);
            if(effectRows == 1){
                teacher.setId(teacherPo.getId());
                return new ReturnObject<>(teacher);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 判断工号是否已存在
     * @author snow create 2021/01/18 11:57
     * @param teacherNo
     * @return
     */
    public Boolean isTeacherNoAlreadyExist(String teacherNo){
        TeacherPoExample example = new TeacherPoExample();
        TeacherPoExample.Criteria criteria = example.createCriteria();
        criteria.andTeacherNoEqualTo(teacherNo);
        List<TeacherPo> teacherPos = teacherPoMapper.selectByExample(example);
        if(teacherPos == null || teacherPos.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 判断邮箱是否已存在
     * @author snow create 2021/01/18 12:02
     * @param email
     * @return
     */
    public Boolean isEmailAlreadyExist(String email){
        TeacherPoExample example = new TeacherPoExample();
        TeacherPoExample.Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo(email);
        List<TeacherPo> teacherPos = teacherPoMapper.selectByExample(example);
        if(teacherPos == null || teacherPos.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 判断电话号码是否已存在
     * @author snow create 2021/01/18 12:03
     * @param mobile
     * @return
     */
    public Boolean isMobileAlreadyExist(String mobile){
        TeacherPoExample example = new TeacherPoExample();
        TeacherPoExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<TeacherPo> teacherPos = teacherPoMapper.selectByExample(example);
        if(teacherPos == null || teacherPos.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 更新老师密码
     * @author snow create 2021/01/18 12:04
     *            modified 2021/01/23 17:45
     * @param teacher
     * @return
     */
    public ReturnObject updateTeacherInformation(Teacher teacher){
        try {
            TeacherPo teacherPo = teacher.createTeacherPo();
            teacherPo.setGmtModified(LocalDateTime.now());
            int effectRows = teacherPoMapper.updateByPrimaryKeySelective(teacherPo);
            if(effectRows == 1){
                return new ReturnObject(ResponseCode.OK);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }
}
