package com.ruoyi.common.utils;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

/**
 * Jasypt加密解密工具类
 */
public class JasyptUtils {

    // 加密算法
    private static final String ALGORITHM = "PBEWithMD5AndDES";
    
    /**
     * 加密
     * @param content 待加密内容
     * @param password 加密密钥
     * @return 加密后内容
     */
    public static String encrypt(String content, String password) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm(ALGORITHM);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        encryptor.setConfig(config);
        return encryptor.encrypt(content);
    }
    
    /**
     * 解密
     * @param encryptedContent 加密后内容
     * @param password 解密密钥
     * @return 解密后内容
     */
    public static String decrypt(String encryptedContent, String password) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm(ALGORITHM);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        encryptor.setConfig(config);
        return encryptor.decrypt(encryptedContent);
    }
    
    public static void main(String[] args) {
        // 加密密钥，需与配置文件中的一致
        String password = "K3pR7sT9wQ2eB5vG";
        
        // 待加密内容（如数据库密码）
        String content = "redis@2025";
        
        // 加密
        String encrypted = encrypt(content, password);
        System.out.println("加密后: " + encrypted);
        
        // 解密
        String decrypted = decrypt(encrypted, password);
        System.out.println("解密后: " + decrypted);
    }
}
