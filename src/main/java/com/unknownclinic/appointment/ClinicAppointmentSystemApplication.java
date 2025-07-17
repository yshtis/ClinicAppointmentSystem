package com.unknownclinic.appointment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.unknownclinic.appointment.repository")
public class ClinicAppointmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicAppointmentSystemApplication.class, args);
	}

}
