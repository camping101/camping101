package com.camping101.beta;

import javax.persistence.EntityListeners;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@SpringBootApplication
@EntityListeners(value = {AuditingEntityListener.class})
public class BetaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BetaApplication.class, args);
    }

}

