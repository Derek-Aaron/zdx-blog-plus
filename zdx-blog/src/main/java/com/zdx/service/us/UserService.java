package com.zdx.service.us;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.model.dto.RegisterDto;
import com.zdx.model.dto.ResetPwd;
import com.zdx.model.dto.UserStatus;
import com.zdx.entity.us.User;

import java.util.List;
import java.util.Map;

/**
* @author zhaodengxuan
* @description 针对表【us_user】的数据库操作Service
* @createDate 2023-07-05 17:30:41
*/
public interface UserService extends IService<User> {


    /**
     * 重置密码
     * @param resetPwd 密码实体
     * @return 返回
     */
    Boolean resetPwd(ResetPwd resetPwd);

    /**
     * 更改用户章台
     * @param userStatus 状态实体
     * @return 返回
     */
    Boolean updateUserStatus(UserStatus userStatus);

    /**
     * 获取所有的用户
     * @param words 关键词
     * @return 返回
     */
    List<Map<String, Object>> listUserAll(String words);

    /**
     * 通过用户名获取用户
     * @param username 用户名
     * @return 返回
     */
    User getUserByUserName(String username);

    /**
     * 注册用户
     * @param register 注册实体
     * @return 返回
     */
    boolean register(RegisterDto register);
}
