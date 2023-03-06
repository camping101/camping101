package com.camping101.beta.member.entity.type;

import org.springframework.security.core.GrantedAuthority;

public enum MemberType implements GrantedAuthority {

    CUSTOMER, OWNER, ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
