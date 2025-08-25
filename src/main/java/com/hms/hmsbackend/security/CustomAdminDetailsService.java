package com.hms.hmsbackend.security;

import com.hms.hmsbackend.entity.Admin;
import com.hms.hmsbackend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomAdminDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
        return new org.springframework.security.core.userdetails.User(
                admin.getUsername(),
                admin.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(admin.getRole()))
        );
    }
}
