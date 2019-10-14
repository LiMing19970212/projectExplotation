package com.yinhebairong.projectexplotation;

public class Config {

    public static int RUN_MODE = 0; // 0 测试 1 正式
    public static final String TEST_IP = "";
    public static final String BASE_IP = "";
    public static String USE_URL = (RUN_MODE == 0 ? TEST_IP : BASE_IP);

}
