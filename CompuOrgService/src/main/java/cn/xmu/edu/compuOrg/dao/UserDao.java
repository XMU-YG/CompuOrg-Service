package cn.xmu.edu.compuOrg.dao;

import cn.xmu.edu.Core.util.ResponseCode;
import cn.xmu.edu.Core.util.ReturnObject;
import cn.xmu.edu.compuOrg.mapper.UserPoMapper;
import cn.xmu.edu.compuOrg.model.bo.User;
import cn.xmu.edu.compuOrg.model.po.UserPo;
import cn.xmu.edu.compuOrg.model.po.UserPoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Repository
public class UserDao {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    private UserPoMapper userPoMapper;

    /**
     * 根据id查找用户
     * @author snow create 2021/03/27 20:12
     * @param userId
     * @return
     */
    public ReturnObject<User> findUserById(Long userId){
        if(userId != null){
            try {
                UserPo userPo = userPoMapper.selectByPrimaryKey(userId);
                if(userPo != null){
                    return new ReturnObject<>(new User(userPo));
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
     * 根据用户名查找用户
     * @author snow create 2021/03/27 20:14
     * @param userName
     * @return
     */
    public ReturnObject<User> findUserByUserName(String userName){
        try {
            UserPoExample example = new UserPoExample();
            UserPoExample.Criteria criteria = example.createCriteria();
            criteria.andUserNameEqualTo(userName);
            List<UserPo> userPos = userPoMapper.selectByExample(example);
            if(userPos != null && userPos.size() > 0){
                return new ReturnObject<>(new User(userPos.get(0)));
            }
            else{
                return new ReturnObject<>(ResponseCode.AUTH_INVALID_ACCOUNT);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 插入用户信息
     * @author snow create 2021/03/27 20:24
     * @param user
     * @return
     */
    public ReturnObject<User> insertUser(User user){
        try {
            if(user.getUserName() != null && isUserNameAlreadyExist(user.getUserName())){
                return new ReturnObject<>(ResponseCode.USER_NAME_REGISTERED);
            }
            if(user.getEmail() != null && isEmailAlreadyExist(user.getEmail())){
                return new ReturnObject<>(ResponseCode.EMAIL_REGISTERED);
            }
            if(user.getMobile() != null && isMobileAlreadyExist(user.getMobile())){
                return new ReturnObject<>(ResponseCode.MOBILE_REGISTERED);
            }
            UserPo userPo = user.createUserPo();
            userPo.setGmtCreate(LocalDateTime.now());
            int effectRows = userPoMapper.insertSelective(userPo);
            if(effectRows == 1){
                user.setId(userPo.getId());
                return new ReturnObject<>(user);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 判断用户名是否已存在
     * @author snow create 2021/03/27 20:17
     * @param userName
     * @return
     */
    public Boolean isUserNameAlreadyExist(String userName){
        UserPoExample example = new UserPoExample();
        UserPoExample.Criteria criteria = example.createCriteria();
        criteria.andUserNameEqualTo(userName);
        List<UserPo> userPos = userPoMapper.selectByExample(example);
        return userPos != null && userPos.size() != 0;
    }

    /**
     * 判断邮箱是否已存在
     * @author snow create 2021/03/27 20:18
     * @param email
     * @return
     */
    public Boolean isEmailAlreadyExist(String email){
        UserPoExample example = new UserPoExample();
        UserPoExample.Criteria criteria = example.createCriteria();
        criteria.andEmailEqualTo(email);
        List<UserPo> userPos = userPoMapper.selectByExample(example);
        return userPos != null && userPos.size() != 0;
    }

    /**
     * 判断电话号码是否已存在
     * @author snow create 2021/03/27 20:19
     * @param mobile
     * @return
     */
    public Boolean isMobileAlreadyExist(String mobile){
        UserPoExample example = new UserPoExample();
        UserPoExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(mobile);
        List<UserPo> userPos = userPoMapper.selectByExample(example);
        return userPos != null && userPos.size() != 0;
    }

    /**
     * 更新用户信息
     * @author snow create 2021/03/27 20:22
     * @param user
     * @return
     */
    public ReturnObject updateUserInformation(User user){
        try {
            UserPo userPo = user.createUserPo();
            userPo.setGmtModified(LocalDateTime.now());
            int effectRows = userPoMapper.updateByPrimaryKeySelective(userPo);
            if(effectRows == 1){
                return new ReturnObject(ResponseCode.OK);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
    }

    /**
     * 判断是否重复请求验证码
     * @author snow create 2021/01/17 23:06
     * @param ipAddress
     * @return
     */
    public Boolean isAllowRequestForVerifyCode(String ipAddress){
        String key = "ip_" + ipAddress;
        if(redisTemplate.hasKey(key)){
            return false;
        }
        redisTemplate.opsForValue().set(key, ipAddress);
        redisTemplate.expire(key, 1, TimeUnit.MINUTES);
        return true;
    }

    /**
     * 将验证码放入Redis
     * @author snow create 2021/01/17 23:17
     * @param verifyCode
     * @param studentId
     */
    public void putVerifyCodeIntoRedis(String verifyCode, String studentId){
        String key = "cp_" + verifyCode;
        redisTemplate.opsForValue().set(key, studentId);
        redisTemplate.expire(key, 5, TimeUnit.MINUTES);
    }

    /**
     * 从验证码中取出id
     * @author snow create 2021/01/17 23:58
     * @param verifyCode
     * @return
     */
    public Long getUserIdByVerifyCode(String verifyCode){
        String key = "cp_" + verifyCode;
        if(!redisTemplate.hasKey(key)){
            return null;
        }
        return Long.valueOf(redisTemplate.opsForValue().get(key).toString());
    }

    /**
     * 修改密码成功之后让验证码失效
     * @author snow create 2021/01/18 10:32
     * @param verifyCode
     */
    public void disableVerifyCodeAfterSuccessfullyModifyPassword(String verifyCode){
        String key = "cp_" + verifyCode;
        if(redisTemplate.hasKey(key)){
            redisTemplate.expire(key, 1, TimeUnit.MILLISECONDS);
        }
    }
}
