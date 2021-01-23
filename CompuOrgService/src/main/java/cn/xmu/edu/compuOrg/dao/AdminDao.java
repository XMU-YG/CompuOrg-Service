package cn.xmu.edu.compuOrg.dao;

import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.mapper.AdminPoMapper;
import cn.xmu.edu.compuOrg.model.bo.Admin;
import cn.xmu.edu.compuOrg.model.po.AdminPo;
import cn.xmu.edu.compuOrg.model.po.AdminPoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author snow create 2021/01/18 23:35
 */
@Repository
public class AdminDao extends UserDao{
    
    @Autowired
    private AdminPoMapper adminPoMapper;

    /**
     * 根据id查找管理员
     * @author snow create 2021/01/18 23:50
     * @param adminId
     * @return
     */
    public ReturnObject<Admin> findAdminById(Long adminId){
        if(adminId != null){
            try {
                AdminPo adminPo = adminPoMapper.selectByPrimaryKey(adminId);
                if(adminPo != null){
                    return new ReturnObject<>(new Admin(adminPo));
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
     * 根据管理员号查找管理员
     * @author snow create 2021/01/18 23:53
     * @param adminNo
     * @return
     */
    public ReturnObject<Admin> findAdminBySno(String adminNo){
        if(adminNo != null){
            try {
                AdminPoExample example = new AdminPoExample();
                AdminPoExample.Criteria criteria = example.createCriteria();
                criteria.andAdminNoEqualTo(adminNo);
                List<AdminPo> adminPos = adminPoMapper.selectByExample(example);
                if(adminPos != null && adminPos.size() > 0){
                    return new ReturnObject<>(new Admin(adminPos.get(0)));
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
     * 插入管理员信息
     * @author snow create 2021/01/19 00:03
     * @param admin
     * @return
     */
    public ReturnObject<Admin> insertAdmin(Admin admin){
        try {
            if(admin.getUserNo() != null && isAdminNoAlreadyExist(admin.getUserNo())){
                return new ReturnObject<>(ResponseCode.ADMIN_NO_REGISTERED);
            }
            if(admin.getEmail() != null && isEmailAlreadyExist(admin.getEmail())){
                return new ReturnObject<>(ResponseCode.EMAIL_REGISTERED);
            }
            if(admin.getMobile() != null && isMobileAlreadyExist(admin.getMobile())){
                return new ReturnObject<>(ResponseCode.MOBILE_REGISTERED);
            }
            AdminPo studentPo = admin.createAdminPo();
            studentPo.setGmtCreate(LocalDateTime.now());
            int effectRows = adminPoMapper.insertSelective(studentPo);
            if(effectRows == 1){
                admin.setId(studentPo.getId());
                return new ReturnObject<>(admin);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }

        return new ReturnObject<>(admin);
    }

    /**
     * 判断管理员号是否已存在
     * @author snow create 2021/01/18 23:59
     * @param adminNo
     * @return
     */
    public Boolean isAdminNoAlreadyExist(String adminNo){
        AdminPoExample example = new AdminPoExample();
        AdminPoExample.Criteria criteria = example.createCriteria();
        criteria.andAdminNoEqualTo(adminNo);
        List<AdminPo> adminPos = adminPoMapper.selectByExample(example);
        if(adminPos == null || adminPos.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 判断邮箱是否已存在
     * @author snow create 2021/01/19 00:00
     * @param email
     * @return
     */
    public Boolean isEmailAlreadyExist(String email){
        AdminPoExample example = new AdminPoExample();
        AdminPoExample.Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo(email);
        List<AdminPo> adminPos = adminPoMapper.selectByExample(example);
        if(adminPos == null || adminPos.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 判断电话号码是否已存在
     * @author snow create 2021/01/19 00:00
     * @param mobile
     * @return
     */
    public Boolean isMobileAlreadyExist(String mobile){
        AdminPoExample example = new AdminPoExample();
        AdminPoExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<AdminPo> adminPos = adminPoMapper.selectByExample(example);
        if(adminPos == null || adminPos.size() == 0){
            return false;
        }
        return true;
    }

    /**
     * 更新管理员信息
     * @author snow create 2021/01/19 00:02
     *            modified 2021/01/23 19:10
     * @param admin
     * @return
     */
    public ReturnObject updateAdminInformation(Admin admin){
        try {
            AdminPo adminPo = admin.createAdminPo();
            adminPo.setGmtModified(LocalDateTime.now());
            int effectRows = adminPoMapper.updateByPrimaryKeySelective(adminPo);
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
