package com.zdx.service.us;

import com.zdx.controller.dto.ResetPwd;
import com.zdx.controller.dto.UserStatus;
import com.zdx.entity.us.User;
import com.baomidou.mybatisplus.extension.service.IService;

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

    Boolean updateUserStatus(UserStatus userStatus);
}
