package com.apple.tpo.e_commerce;

import com.apple.tpo.e_commerce.dto.auth.LoginRequest;
import com.apple.tpo.e_commerce.model.Role;
import com.apple.tpo.e_commerce.model.Usuario;
import com.apple.tpo.e_commerce.respository.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MYSQL",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.sql.init.mode=never"
})
class AuthJwtIntegrationTest {

	private MockMvc mockMvc;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private WebApplicationContext webApplicationContext;

	@BeforeEach
	void setUp() {
		// Importante: aplicar config de Spring Security para que el auth funcione en MockMvc
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
		usuarioRepository.deleteAll();

		Usuario u = new Usuario();
		u.setUsername("admin");
		u.setEmail("admin@apple-ar.com");
		u.setNombre("Admin");
		u.setApellido("Sistema");
		u.setPassword(passwordEncoder.encode("password"));
		u.setRole(Role.ROLE_ADMIN);
		usuarioRepository.save(u);
	}

	@Test
	void login_returnsToken_andProtectedEndpoint_acceptsBearerToken() throws Exception {
		LoginRequest req = new LoginRequest();
		req.setEmail("admin@apple-ar.com");
		req.setPassword("password");

		MvcResult loginResult = mockMvc.perform(
						post("/api/auth/login")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(req))
				)
				.andExpect(status().isOk())
				.andReturn();

		JsonNode json = objectMapper.readTree(loginResult.getResponse().getContentAsString());
		String token = json.get("token").asText();
		org.junit.jupiter.api.Assertions.assertFalse(token.isBlank(), "El token JWT debería venir en la respuesta");

		mockMvc.perform(
						get("/api/productos")
								.header("Authorization", "Bearer " + token)
				)
				.andExpect(status().isOk());
	}

	@Test
	void protectedEndpoint_withoutToken_returns401() throws Exception {
		mockMvc.perform(
						get("/api/productos")
				)
				.andExpect(status().isUnauthorized());
	}
}

