/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.example.gomeAccountClient;

public class Constants {

    /**
     * Account type string.
     */
    public static final String ACCOUNT_TYPE = "com.gome.gomeaccountservice";
    /** The Intent extra to store username. */
    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    /**
     * Authtoken type string.
     */
    public static final String AUTHTOKEN_TYPE = "com.gome.gomeaccountservice";
    
    static final String KEY_AUTH_TOKEN_TYPE_GET_LOGIN_STATE = "key_auth_token_type_get_login_state";
    static final String KEY_AUTH_TOKEN_TYPE_GET_LOCAL_LOGIN_STATE = "key_auth_token_type_get_local_login_state";
	static final String KEY_AUTH_TOKEN_TYPE_LOGIN = "key_auth_token_type_login";
	static final String KEY_AUTH_TOKEN_TYPE_LOGIN_INFO = "key_auth_token_type_login_info";
	static final String KEY_AUTH_TOKEN_TYPE_VERIFY_PWD = "key_auth_token_type_verify_pwd";//验证密码
	static final String KEY_VERIFY_PWD_RESULT = "key_verify_pwd_result";//验证密码结果key
	static final String KEY_AUTH_TOKEN_TYPE_RESET_PWD = "key_auth_token_type_reset_pwd";
	static final String KEY_AUTH_TOKEN_TYPE_LOGIN_OUT = "key_auth_token_type_login_out";
	static final String KEY_AUTH_TOKEN_TYPE_REGISTER = "key_auth_token_type_register";
	static final String KEY_AUTH_TOKEN_TYPE_WEIBO_LOGIN = "key_auth_token_type_weibo_login";

    
    public static final String KEY_SERVER_AUTHEN_SUCCESS = "success";
    public static final String KEY_SERVER_AUTHEN_NAME_NOT_FOUND = "username not found";
    public static final String KEY_SERVER_AUTHEN_PASSWOR_ERROR = "password error";
    public static final String KEY_SERVER_AUTHEN_LOGIN_TIMEOUT = "login timout";
    
    //本地缓存信息key
    public static final String HEAD_PORTRAIT_DIR_PATH = "/sdcard/gomeaccount/";
    public static final String HEAD_PORTRAIT_SUFFIX = ".jpg";
    public static final String SHAREPREFEENCE_ACCOUNTS = "gome_accounts"; //临时保存账号信息
    public static final String SHAREPREFEENCE_ACCOUNT_INFO_PREFIX = "account_";//账号详细信息
    //server 账号信息key
    public static final String KEY_SERVER_TOKEN = "token";
    public static final String KEY_ACCOUNT_NAME = "nickName";
    public static final String KEY_ACCOUNT_PWD = "loginPwd";
    public static final String KEY_ACCOUNT_EMAIL = "mallAddress";
    public static final String KEY_ACCOUNT_PHONE_NUMBER = "phoneNo";
    public static final String KEY_SMS_CODE = "authCode";  //短信验证码
    public static final String KEY_ACCOUNT_REGISTER_TYPE = "registerType";  //注册类型0手机1邮箱3其他
    
    public static final String KEY_ACCOUNT_GOME_ID = "gomeId";
    public static final String KEY_ACCOUNT_CREATE_TIME = "createdTime";
    public static final String KEY_ACCOUNT_SEX = "sex";
    public static final String KEY_ACCOUNT_USER_LEVEL = "userLevel";
    
    public static final String KEY_LOCAL_AVATAR_PATH = "localAvatarPath";  //本地头像路径key
    
    //头像相关
    //public static final String ACCOUNT_SERVER_AVATAR_PREFIX = "http://192.168.1.129/";
    public static final String KEY_ACCOUNT_SERVER_AVATAR = "avatar";  //服务器头像路径key
    public static final String KEY_AVATARTYPE = "avatarType";
    //向server 请求key
    public static final String KEY_SERVER_REQUEST_SMS_MSG_TYPE = "msgType";
    public static final String SMS_MSG_TYPE_REGISTER = "1";
    public static final String SMS_MSG_TYPE_RETRIEVE = "2";
    public static final String REGISTER_TYPE_PHONE = "0";
    public static final String REGISTER_TYPE_EMAIL = "1";
    public static final String SERVER_TIMEOUT_RESULT_CODE = "-1"; //请求超时resultcode
    //server 返回状态key
    public static final String KEY_SERVER_RESULT_CODE = "resCode";
    public static final String KEY_SERVER_RESULT_MSG = "resMsg";
    public static final String SERVER_SUCCESS_RESULT_CODE = "000";
    //图片配置
    public static final float IMAGE_SCALE = 0.1f;
    //广播相关
    public static final String ACTION_GOME_ACCOUNT_LOGIN_OUT = "gome account login out";
}
