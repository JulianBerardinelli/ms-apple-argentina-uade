package com.apple.tpo.e_commerce;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MYSQL",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		// Evitamos ejecutar el seed SQL del proyecto en tests (usa MySQL)
		"spring.sql.init.mode=never"
})
class ECommerceApplicationTests {

	@Test
	void contextLoads() {
	}

}
