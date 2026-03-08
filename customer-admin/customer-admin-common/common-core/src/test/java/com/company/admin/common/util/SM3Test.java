package com.company.admin.common.util;

public class SM3Test {
    public static void main(String[] args) {
        String password = "123456";
        String hash = SM3Util.hash(password);
        System.out.println("SM3 hash of '123456': " + hash);
        
        String storedHash = "207cf410532f92a47db2452543548c221b9e540243b1312128cd8dae2877ea72";
        boolean verified = SM3Util.verify(password, storedHash);
        System.out.println("Verify result: " + verified);
    }
}
