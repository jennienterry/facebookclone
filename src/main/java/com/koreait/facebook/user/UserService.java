package com.koreait.facebook.user;

import com.koreait.facebook.common.EmailService;
import com.koreait.facebook.common.EmailServiceImpl;
import com.koreait.facebook.common.MyFileUtils;
import com.koreait.facebook.common.MySecurityUtils;
import com.koreait.facebook.security.IAuthenticationFacade;
import com.koreait.facebook.user.model.UserEntity;
import com.koreait.facebook.user.model.UserProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {
    @Autowired
    private EmailService email;

    @Autowired
    private MySecurityUtils secUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IAuthenticationFacade auth;

    @Autowired
    private MyFileUtils myFileUtils;


    @Autowired
    private UserMapper mapper;

    @Autowired
    private UserProfileMapper profileMapper;

    public int join(UserEntity param) {
        String authCd = secUtils.getRandomDigit(5);
        //비밀번호 암호화
//        String hashedPw = BCrypt.hashpw(param.getPw(), BCrypt.gensalt());
        String hashedPw = passwordEncoder.encode(param.getPw());
        param.setPw(hashedPw);
        param.setAuthCd(authCd);
        int result = mapper.join(param);

        if (result == 1) { //메일쏘기 !!( id, authcd값을 메일로 쓴다.)
            String subject = "[얼굴책] 인증메일입니다.";
            String txt = String.format("<a href=\"http://localhost:8090/user/auth?email=%s&authCd=%s\">인증하기</a>"
                    , param.getEmail(), authCd);
            email.sendSimpleMessage(param.getEmail(), subject, txt);
        }
        return result;
    }

    //이메일 인증 처리
    public int auth(UserEntity param) {
        return mapper.auth(param);
    }

//    //로그인
//    public String login(UserEntity param) {
//        UserEntity loginUser = mapper.selUser(param);
//        return "";
//    }

    public void profileImg(MultipartFile[] imgArr) {
        UserEntity loginUser = auth.getLoginUser();
        int iuser = loginUser.getIuser();
        System.out.println("iuser : " + iuser);
        String target = "profile/" + iuser;

        UserProfileEntity param = new UserProfileEntity();
        param.setIuser(iuser);

        for (MultipartFile img : imgArr) {
            String saveFileNm = myFileUtils.transferTo(img, target);
            //saveFileNm이 null이 아니라면 t_user_profile 테이블에 insert
            if (saveFileNm != null) {
                param.setImg(saveFileNm);
                int result = profileMapper.insUserProfile(param);

                //param2(메인프로필)는 param(유저)과 상관없음 !
                if (loginUser.getMainProfile() == null && result == 1) {
                    UserEntity param2 = new UserEntity();
                    param2.setIuser(loginUser.getIuser());
                    param2.setMainProfile(saveFileNm);

                    if (mapper.updUser(param2) == 1) {
                        loginUser.setMainProfile(saveFileNm);
                    }
                }
            }
        }
    }
}
