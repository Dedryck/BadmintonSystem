//package com.coding24.badmintonsystem_1.config;
//
//import com.coding24.badmintonsystem_1.entity.User;
//import com.coding24.badmintonsystem_1.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class DataInitializer implements CommandLineRunner {
//
//    private final UserService userService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public DataInitializer(@Lazy UserService userService, PasswordEncoder passwordEncoder) {
//        this.userService = userService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (userService.countUsers() == 0) {
//            User admin = new User();
//            admin.setUsername("admin");
//            admin.setPassword(passwordEncoder.encode("123"));
//            admin.setEmail("admin@example.com");
//            admin.setPhone("1234567890");
//            admin.setRole("admin");
//            userService.insertUser(admin);
//
//            User member = new User();
//            member.setUsername("member");
//            member.setPassword(passwordEncoder.encode("123"));
//            member.setEmail("member@example.com");
//            member.setPhone("0987654321");
//            member.setRole("member");
//            userService.insertUser(member);
//        }
//    }
//}
