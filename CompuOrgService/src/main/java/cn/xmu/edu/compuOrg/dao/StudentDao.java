package cn.xmu.edu.compuOrg.dao;

import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.mapper.StudentPoMapper;
import cn.xmu.edu.compuOrg.model.bo.Student;
import cn.xmu.edu.compuOrg.model.po.StudentPo;
import cn.xmu.edu.compuOrg.model.po.StudentPoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class StudentDao extends UserDao {

    @Autowired
    private StudentPoMapper studentPoMapper;

    /**
     * 根据id查找学生
     * @author snow create 2021/01/18 00:04
     * @param studentId
     * @return
     */
    public ReturnObject<Student> findStudentById(Long studentId){
        if(studentId != null){
            try {
                StudentPo studentPo = studentPoMapper.selectByPrimaryKey(studentId);
                if(studentPo != null){
                    return new ReturnObject<>(new Student(studentPo));
                }
                else{
                    return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
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
     * 根据学号查找学生
     * @author snow create 2021/01/17/18:50
     * @param studentNo
     * @return
     */
    public ReturnObject<Student> findStudentBySno(String studentNo){
        if(studentNo != null){
            try {
                StudentPoExample example = new StudentPoExample();
                StudentPoExample.Criteria criteria = example.createCriteria();
                criteria.andStudentNoEqualTo(studentNo);
                List<StudentPo> studentPos = studentPoMapper.selectByExample(example);
                if(studentPos != null && studentPos.size() > 0){
                    return new ReturnObject<>(new Student(studentPos.get(0)));
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
     * 插入学生信息
     * @author snow create 2021/01/17 21:20
     * @param student
     * @return
     */
    public ReturnObject<Student> insertStudent(Student student){
        try {
            if(student.getUserNo() != null && isStudentNoAlreadyExist(student.getUserNo())){
                return new ReturnObject<>(ResponseCode.STUDENT_NO_REGISTERED);
            }
            if(student.getEmail() != null && isEmailAlreadyExist(student.getEmail())){
                return new ReturnObject<>(ResponseCode.EMAIL_REGISTERED);
            }
            if(student.getMobile() != null && isMobileAlreadyExist(student.getMobile())){
                return new ReturnObject<>(ResponseCode.MOBILE_REGISTERED);
            }
            StudentPo studentPo = student.createStudentPo();
            studentPo.setGmtCreate(LocalDateTime.now());
            int effectRows = studentPoMapper.insertSelective(studentPo);
            if(effectRows == 1){
                student.setId(studentPo.getId());
                return new ReturnObject<>(student);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }

        return new ReturnObject<>(student);
    }

    /**
     * 判断学号是否已存在
     * @author snow create 2021/01/17 21:15
     * @param studentNo
     * @return
     */
    public Boolean isStudentNoAlreadyExist(String studentNo){
        StudentPoExample example = new StudentPoExample();
        StudentPoExample.Criteria criteria = example.createCriteria();
        criteria.andStudentNoEqualTo(studentNo);
        List<StudentPo> studentPos = studentPoMapper.selectByExample(example);
        if(studentPos == null || studentPos.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 判断邮箱是否已存在
     * @author snow create 2021/01/17 20:10
     * @param email
     * @return
     */
    public Boolean isEmailAlreadyExist(String email){
        StudentPoExample example = new StudentPoExample();
        StudentPoExample.Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo(email);
        List<StudentPo> studentPos = studentPoMapper.selectByExample(example);
        if(studentPos == null || studentPos.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 判断电话号码是否已存在
     * @author snow create 2021/01/17 20:12
     * @param mobile
     * @return
     */
    public Boolean isMobileAlreadyExist(String mobile){
        StudentPoExample example = new StudentPoExample();
        StudentPoExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<StudentPo> studentPos = studentPoMapper.selectByExample(example);
        if(studentPos == null || studentPos.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 更新学生信息
     * @author snow create 2021/01/18 00:12
     *            modified 2021/01/23 16:55
     * @param student
     * @return
     */
    public ReturnObject updateStudentInformation(Student student){
        try {
            StudentPo studentPo = student.createStudentPo();
            studentPo.setGmtModified(LocalDateTime.now());
            int effectRows = studentPoMapper.updateByPrimaryKeySelective(studentPo);
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
