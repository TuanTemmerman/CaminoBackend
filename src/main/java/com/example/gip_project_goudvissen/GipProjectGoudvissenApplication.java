package com.example.gip_project_goudvissen;
import com.example.gip_project_goudvissen.Service.AdminService;
import com.example.gip_project_goudvissen.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GipProjectGoudvissenApplication {

	@Autowired
	private RoleService roleService;

	@Autowired
	private AdminService adminService;

	public static void main(String[] args) {
		SpringApplication.run(GipProjectGoudvissenApplication.class, args);
	}

	@Bean
	public CommandLineRunner initializeRolesAndAdmin() {
		return args -> {
			roleService.initializeRoles();
			adminService.createAdminIfNotExists();
		};
	}

}
