package com.github.rhllor.pc.library;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class LibraryApplicationTests {

	@Test
	void contextLoads() {
		Assert.isTrue(true, ":P");
	}

	@SpringBootApplication
	static class TestConfiguration {

	}

}
