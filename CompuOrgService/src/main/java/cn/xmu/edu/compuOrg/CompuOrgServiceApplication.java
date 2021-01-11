package cn.xmu.edu.compuOrg;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDubbo
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.compuOrg"})
//@MapperScan("cn.edu.xmu.compuOrg.mapper")
public class CompuOrgServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CompuOrgServiceApplication.class);
    }
}
