package com.github.rhllor.pc.library;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class LibraryApplicationTests {


	@Autowired
	private ConsumptionRepository _repository;

	@Test
	void contextLoads() {
		Assert.isTrue(true, ":P");
	}

	@SpringBootApplication
	static class TestConfiguration {

	}

}
