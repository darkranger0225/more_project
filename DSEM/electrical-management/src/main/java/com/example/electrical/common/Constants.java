package com.example.electrical.common;

public class Constants {
    
    // 角色常量
    public static final int ROLE_STUDENT = 0;
    public static final int ROLE_ADMIN = 1;  // 管理员（合并公寓管理员和系统管理员）
    
    // 用户状态
    public static final int USER_STATUS_DISABLED = 0;
    public static final int USER_STATUS_ENABLED = 1;
    
    // 用电记录状态
    public static final int ELECTRICITY_STATUS_UNPAID = 0;
    public static final int ELECTRICITY_STATUS_PAID = 1;
    
    // 缴费单状态
    public static final int PAYMENT_STATUS_PENDING = 0;
    public static final int PAYMENT_STATUS_PAID = 1;
    
    // 报修状态
    public static final int REPAIR_STATUS_PENDING = 0;
    public static final int REPAIR_STATUS_PROCESSING = 1;
    public static final int REPAIR_STATUS_COMPLETED = 2;
    
    // 公告状态
    public static final int ANNOUNCEMENT_STATUS_DISABLED = 0;
    public static final int ANNOUNCEMENT_STATUS_ENABLED = 1;
    
    // JWT Token 前缀
    public static final String TOKEN_PREFIX = "Bearer ";
    
    // 请求头
    public static final String HEADER_AUTHORIZATION = "Authorization";

    // 消费类型
    public static final int CONSUMPTION_TYPE_ELECTRICITY = 0;  // 电费缴纳
    public static final int CONSUMPTION_TYPE_LOGIN = 1;        // 登录扣费
}
