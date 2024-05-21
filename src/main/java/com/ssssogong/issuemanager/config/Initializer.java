package com.ssssogong.issuemanager.config;

import com.ssssogong.issuemanager.domain.account.Account;
import com.ssssogong.issuemanager.domain.account.Admin;
import com.ssssogong.issuemanager.domain.account.User;
import com.ssssogong.issuemanager.domain.role.*;
import com.ssssogong.issuemanager.repository.AdminRepository;
import com.ssssogong.issuemanager.repository.RoleRepository;
import com.ssssogong.issuemanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.swing.text.html.StyleSheet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class Initializer implements ApplicationRunner {
    private final int PL_COUNT = 2;
    private final int DEV_COUNT = 10;
    private final int TESTER_COUNT = 5;
    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void saveInitialRoles(){
        System.out.println("TestAccountInitializer: saving roles");
        final List<Role> roles = roleRepository.findAll();
        final List<Role> saves = List.of(new Administrator(), new Tester(), new Developer(), new ProjectLeader());
        //todo: 새로운 Role이 추가되어도 알잘딱하게 추가해주기
        roleRepository.saveAll(saves);
        System.out.println("TestAccountInitializer: saved roles");
    }

    public void saveInitialAccounts(){
        System.out.println("TestAccountInitializer: saving accounts");
        Admin admin;
        List<User> pl = new ArrayList<>();
        List<User> dev = new ArrayList<>();
        List<User> tester = new ArrayList<>();
        Role administrator = roleRepository.findByName("Administrator").get();
        Role developer = roleRepository.findByName("Developer").get();
        Role projectLeader = roleRepository.findByName("ProjectLeader").get();
        Role testerRole = roleRepository.findByName("Tester").get();

        // admin 추가
        admin = Admin.builder()
                .accountId("adminID")
                .username("Hong Gildong")
                .password("qwe1234")
                .role(administrator)
                .build();
        // PL 추가
        for(int i = 1; i <= PL_COUNT; i++){
            pl.add(
                    User.builder()
                            .accountId("pl" + i)
                            .username("pl" + i)
                            .password("zxc123")
                            .role(projectLeader)
                            .build()
            );
        }
        // dev 추가
        for(int i = 1; i <= DEV_COUNT; i++){
            dev.add(User.builder()
                    .accountId("dev" + i)
                    .username("dev" + i)
                    .password("asd1234")
                    .role(developer)
                    .build()
            );
        }
        // tester 추가
        for(int i = 1; i <= TESTER_COUNT; i++){
            tester.add(User.builder()
                    .accountId("tester" + i)
                    .username("tester" + i)
                    .password("1111")
                    .role(testerRole)
                    .build()
            );
        }
        // DB에 저장
        adminRepository.save((Admin)admin);
        userRepository.saveAll(pl);
        userRepository.saveAll(dev);
        userRepository.saveAll(tester);
        System.out.println("TestAccountInitializer: saved initial accounts to db");
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        saveInitialRoles();
        saveInitialAccounts();
    }
}
