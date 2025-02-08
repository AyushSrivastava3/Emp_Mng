//package com.dq.empportal.service;
//
//
//import com.dq.empportal.model.User;
//import com.dq.empportal.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//
//@Component
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//    @Autowired
//    UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user=userRepository.findByUsername(username);
//        if (user!=null){
//            UserDetails userDetails=org.springframework.security.core.userdetails.User.builder()
//                    .username(user.getUsername())
//                    .password(user.getPassword())
//                    .build();
//            return userDetails;
//        }
//        throw new UsernameNotFoundException("User not found with username :"+username);
//    }
//}

//
//package com.dq.empportal.service;
//
//import com.dq.empportal.model.User;
//import com.dq.empportal.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if (user != null) {
//            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
//
//            return org.springframework.security.core.userdetails.User.builder()
//                    .username(user.getUsername())
//                    .password(user.getPassword())
//                    .authorities(Collections.singleton(authority)) // Assigning roles
//                    .build();
//        }
//        throw new UsernameNotFoundException("User not found with username: " + username);
//    }
//}


//
//package com.dq.empportal.service;
//
//import com.dq.empportal.model.User;
//import com.dq.empportal.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//        if (user != null) {
//            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
//
//            // Returning custom UserDetails that includes the userId
//            return org.springframework.security.core.userdetails.User.builder()
//                    .username(user.getUsername())
//                    .password(user.getPassword())
//                    .authorities(Collections.singleton(authority)) // Assigning roles
//                    .build();
//        }
//        throw new UsernameNotFoundException("User not found with username: " + username);
//    }
//}



package com.dq.empportal.service;

import com.dq.empportal.model.User;
import com.dq.empportal.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // Constructor Injection
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Handling Null Role Case
        String role = (user.getRole() != null) ? user.getRole().name() : "USER";
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);

        // Returning custom UserDetails with userId
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(Collections.singleton(authority))
                .build();
    }
}
