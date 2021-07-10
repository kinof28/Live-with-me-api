package com.Abdelwahab.Live.With.ME;

import com.Abdelwahab.Live.With.ME.requests.ClientRegisterRequest;
import com.Abdelwahab.Live.With.ME.services.AdminService;
import com.Abdelwahab.Live.With.ME.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class LiveWithMeApplication {
//	@Autowired
//	AdminService adminService;

	public static void main(String[] args) {
		SpringApplication.run(LiveWithMeApplication.class, args);
	}
//	@PostConstruct
//	public void initAdmin(){
//		this.adminService.initAdmin("kinof@gmail.com","00000");
//	}

}
