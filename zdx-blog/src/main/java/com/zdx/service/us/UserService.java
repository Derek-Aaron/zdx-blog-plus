package com.zdx.service.us;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zdx.controller.dto.ResetPwd;
import com.zdx.controller.dto.UserStatus;
import com.zdx.controller.vo.HomeUserInfo;
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
     * 获取前台用户信息
     * @return 返回
     */
    HomeUserInfo getHomeInfo();
}
