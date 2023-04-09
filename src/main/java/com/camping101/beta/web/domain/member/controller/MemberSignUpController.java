package com.camping101.beta.web.domain.member.controller;

import com.camping101.beta.web.domain.member.dto.signup.MemberSignUpRequest;
import com.camping101.beta.web.domain.member.service.singup.MemberSignUpService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signup")
@Api(tags = {"캠핑 101 - 회원가입 API"})
public class MemberSignUpController {

    private final MemberSignUpService memberSignUpService;

    @PostMapping("/mail")
    public ResponseEntity<?> signUpByMail(@Valid MemberSignUpRequest memberSignUpRequest){

        memberSignUpService.signUpByEmail(memberSignUpRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/mail/auth")
    public ResponseEntity<?> activateMemberByAuthCode(@RequestParam String email,
                                                      @RequestParam String mailAuthCode){

        memberSignUpService.activateMemberByMailAuthCode(email, mailAuthCode);

        return ResponseEntity.ok().build();
    }

}
