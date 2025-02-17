package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class DemoApplicationTests {
	Calculator underTest = new Calculator();

	@Test
	void itShouldAddNumbers() {
		//Given
		int numberOne = 1;
		int numberTwo = 2;

		//When
		int result = underTest.add(numberOne, numberTwo);

		//Then
		int expected = 3;

		assertThat(result).isEqualTo(expected);
	}


	static class Calculator {
		int add(int a, int b) {
			return a + b;
		}
	}

}
