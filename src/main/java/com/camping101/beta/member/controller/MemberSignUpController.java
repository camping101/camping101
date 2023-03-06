package com.camping101.beta.member.controller;

import com.camping101.beta.member.dto.MemberSignUpRequest;
import com.camping101.beta.member.exception.MemberException;
import com.camping101.beta.member.service.MemberSignUpService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signup")
@Api(tags = {"회원가입 API"})
public class MemberSignUpController {

    private final MemberSignUpService memberSignUpService;

    @PostMapping("/mail")
    public ResponseEntity<?> signUpByMail(@Valid MemberSignUpRequest memberSignUpRequest){

        memberSignUpService.signUpByEmail(memberSignUpRequest);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/mail/auth")
    public ResponseEntity<?> activateMemberByAuthCode(@RequestParam String email,
                                                      @RequestParam String authCode){

        memberSignUpService.activateMemberByAuthCode(email, authCode);

        return ResponseEntity.ok().build();
    }

}
