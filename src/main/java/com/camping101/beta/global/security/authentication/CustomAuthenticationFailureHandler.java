package com.camping101.beta.global.security.authentication;

import com.camping101.beta.util.FilterResponseHandler;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        FilterResponseHandler.sendFilterExceptionResponse(response, getExceptionMessage(exception), 401);

        response.sendRedirect("/api/signin/fail");
    }

    private String getExceptionMessage(AuthenticationException exception){

        if (exception instanceof UsernameNotFoundException) {
            return "해당 회원이 존재하지 않습니다.";
        } else if (exception instanceof BadCredentialsException) {
            return "비밀번호가 일치하지 않습니다.";
        } else if (exception instanceof DisabledException) {
            return "비활성화된 회원입니다.";
        } else if (exception instanceof LockedException) {
            return "정지된 회원입니다.";
        } else if (exception instanceof AccountExpiredException) {
            return "탈퇴한 회원입니다.";
        } else {
            return "확인된 에러가 없습니다.";
        }

    }

}
