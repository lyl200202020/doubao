package com.company.admin.common.util;

import java.nio.charset.StandardCharsets;

public class PasswordGenerator {
    
    public static void main(String[] args) {
        String[] passwords = {
            "admin123",
            "ywgl01123",
            "ywgl02123",
            "khgl03123",
            "khgl04123",
            "khgl05123",
            "khgl06123",
            "zhgl01123",
            "zhgl02123"
        };
        
        System.out.println("-- 用户密码SM3哈希值");
        System.out.println("-- 密码规则：用户名 + '123'");
        System.out.println();
        
        for (String password : passwords) {
            String hash = SM3Util.hash(password);
            System.out.println("-- " + password + " -> " + hash);
        }
        
        System.out.println();
        System.out.println("-- UPDATE语句");
        System.out.println("UPDATE sys_user SET password = '" + SM3Util.hash("admin123") + "' WHERE username = 'admin';");
        System.out.println("UPDATE sys_user SET password = '" + SM3Util.hash("ywgl01123") + "' WHERE username = 'ywgl01';");
        System.out.println("UPDATE sys_user SET password = '" + SM3Util.hash("ywgl02123") + "' WHERE username = 'ywgl02';");
        System.out.println("UPDATE sys_user SET password = '" + SM3Util.hash("khgl03123") + "' WHERE username = 'khgl03';");
        System.out.println("UPDATE sys_user SET password = '" + SM3Util.hash("khgl04123") + "' WHERE username = 'khgl04';");
        System.out.println("UPDATE sys_user SET password = '" + SM3Util.hash("khgl05123") + "' WHERE username = 'khgl05';");
        System.out.println("UPDATE sys_user SET password = '" + SM3Util.hash("khgl06123") + "' WHERE username = 'khgl06';");
        System.out.println("UPDATE sys_user SET password = '" + SM3Util.hash("zhgl01123") + "' WHERE username = 'zhgl01';");
        System.out.println("UPDATE sys_user SET password = '" + SM3Util.hash("zhgl02123") + "' WHERE username = 'zhgl02';");
    }
}
