package com.company.admin.platform.sys.domain.service;

import com.company.admin.common.exception.BusinessException;
import com.company.admin.common.enums.ErrorCode;
import com.company.admin.common.util.SM3Util;
import com.company.admin.platform.sys.domain.entity.SysUser;
import com.company.admin.platform.sys.domain.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthDomainService {
    
    private final SysUserRepository sysUserRepository;
    
    public SysUser login(String userCode, String password) {
        SysUser user = sysUserRepository.findByUserCode(userCode);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        if (user.getUserStatus() == 0) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }
        
        String inputHash = SM3Util.hash(password);
        log.info("Login attempt: userCode={}, inputPassword={}, inputHash={}, storedHash={}", 
                userCode, password, inputHash, user.getPassword());
        
        if (!SM3Util.verify(password, user.getPassword())) {
            throw new BusinessException(ErrorCode.USER_PASSWORD_ERROR);
        }
        
        log.info("User login success: userCode={}", userCode);
        return user;
    }
    
    public void validateUser(String userId) {
        SysUser user = sysUserRepository.findById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        if (user.getUserStatus() == 0) {
            throw new BusinessException(ErrorCode.USER_DISABLED);
        }
    }
}
