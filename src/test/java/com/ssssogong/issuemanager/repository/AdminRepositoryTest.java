package com.ssssogong.issuemanager.repository;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//import static org.junit.Assert.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@Sql(value = "/truncate.sql")
public class AdminRepositoryTest {

/*    private static Admin admin1;
    private static Admin admin2;
    @Autowired
    AdminRepository adminRepository;

    @BeforeEach
    public void setUp() {
        admin1 = Admin.builder()
                .username("Jin")
                .accountId("1")
                .password("1")
                .build();

        admin2 = Admin.builder()
                .username("Hyun")
                .accountId("2")
                .password("2")
                .build();

    }

    @DisplayName("계정 추가")
    @Test
    public void 계정_추가() {
        // given
        adminRepository.save(admin1);
        System.out.println(admin1.getId());

        // when
        Admin findAdmin = adminRepository.findById(1L).orElseThrow(RuntimeException::new);

        // then
        assertThat(admin1.getUsername()).isEqualTo(findAdmin.getUsername());

    }

    @DisplayName("계정 찾기")
    @Test
    public void 계정_찾기() {
        // given
        adminRepository.save(admin2);
        adminRepository.save(admin1);

        // when
        List<Admin> adminList = adminRepository.findAll();

        // then
        assertThat(adminRepository.count()).isEqualTo(adminList.size());
    }

    @DisplayName("계정 삭제")
    @Test
    public void 계정_삭제() {
        // given
        adminRepository.save(admin2);
        adminRepository.save(admin1);

        // when
        long size = adminRepository.count();
        adminRepository.delete(admin2);

        // then
        assertThat(size).isNotEqualTo(adminRepository.count());
    }*/


}
