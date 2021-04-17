package com.github.rhllor.pc.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

@SpringBootTest
@AutoConfigureMockMvc
class ServiceApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
		Assert.isTrue(true, ":P");
	}
}
