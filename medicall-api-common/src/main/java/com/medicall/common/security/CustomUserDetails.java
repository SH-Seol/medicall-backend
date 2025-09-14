package com.medicall.common.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public record CustomUserDetails(
        Long userId,
        String serviceType
) implements UserDetails {

    /**
     * 권한 목록 반환
     * Medicall은 serviceType으로 구분하므로 Role을 반환하지 않음
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userId.toString();
    }
}
