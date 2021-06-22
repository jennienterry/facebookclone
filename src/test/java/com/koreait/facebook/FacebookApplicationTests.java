package com.koreait.facebook;

import com.koreait.facebook.common.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacebookApplicationTests {

    @Autowired
    private EmailService email;

    @Test
    void sendEmail() {
        String to = "hello1913@naver.com"; //받는사람 메일
        String subject = "제목입니다.";
        String txt = "내용입니다.";
        email.sendSimpleMessage(to,subject,txt);
    }

}
