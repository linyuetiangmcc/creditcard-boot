package com.cmcc.creditcard.dao;

import com.cmcc.creditcard.entity.UserInfo;
import java.util.List;

public interface UserInfoDao {
    List<UserInfo> queryUserInfoList();
    UserInfo queryUserInfoByUserId(String userId);
    UserInfo queryUserInfoByOpenId(String openId);
    int insertUserInfo(UserInfo userInfo);
    int updateUserInfo(UserInfo userInfo);
    int deleteUserInfoByUserId(String userId);
    int deleteUserInfoByOpenId(String openId);
}
