package com.camping101.beta.security;

import com.camping101.beta.member.entity.Member;
import com.camping101.beta.member.entity.status.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class MemberDetails implements UserDetails {

    private final Member member;

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> member.getMemberType().name());
    }

    @Override
    public boolean isAccountNonExpired() {
        return MemberStatus.NOT_ACTIVATED.equals(member.getMemberStatus()) ||
                MemberStatus.IN_USE.equals(member.getMemberStatus()) ||
                MemberStatus.WITHDRAW.equals(member.getMemberStatus())
                        && member.getDeletedAt().plusYears(1)
                                 .isBefore(LocalDateTime.now());
    }

    @Override
    public boolean isAccountNonLocked() {
        return MemberStatus.STOPPED.equals(member.getMemberStatus()) == false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return MemberStatus.IN_USE.equals(member.getMemberStatus());
    }

}
