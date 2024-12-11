package com.syuto.hwid;

import com.syuto.hwid.utils.EncryptionUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Hwid {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Hwid.class, args);
        //System.out.println(EncryptionUtil.generateHWID());
    }
}