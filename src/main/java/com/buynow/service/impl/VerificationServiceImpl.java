package com.buynow.service.impl;

import org.springframework.stereotype.Service;

import com.buynow.model.VerificationCode;
import com.buynow.repository.VerificationCodeRepository;
import com.buynow.service.VerificationService;

@Service
public class VerificationServiceImpl implements VerificationService {

    private final VerificationCodeRepository verificationCodeRepository;

    VerificationServiceImpl(VerificationCodeRepository verificationCodeRepository){

        this.verificationCodeRepository = verificationCodeRepository;
    }

    @Override
    public VerificationCode createVerificationCode(String otp,String email) {
    	
        VerificationCode isExist=verificationCodeRepository.findByEmail(email);

        if(isExist!=null) {
            verificationCodeRepository.delete(isExist);
        }

        VerificationCode verificationCode=new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);

        return verificationCodeRepository.save(verificationCode);

    }
}
