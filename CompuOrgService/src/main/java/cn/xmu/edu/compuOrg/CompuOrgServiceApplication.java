package cn.xmu.edu.compuOrg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cn.xmu.edu.compuOrg"})
//@MapperScan("cn.edu.xmu.compuOrg.mapper")
public class CompuOrgServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompuOrgServiceApplication.class);
    }
}
