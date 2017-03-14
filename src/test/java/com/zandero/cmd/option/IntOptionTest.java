package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class IntOptionTest {

	@Test
	public void intOptionTest() {
		CommandOption option = new IntOption("i");

		assertTrue(option.hasArguments());
		assertNull(option.getDefault());
	}

	@Test
	public void intOptionMinMaxTest() throws CommandLineException {

		CommandOption option = new IntOption("n")
			.min(10)
			.max(20)
			.longCommand("name")
			.setting("user_name");

		assertEquals("n", option.getCommand());
		assertEquals("name", option.getLongCommand());
		assertEquals("user_name", option.getSetting());

		assertEquals("java.lang.Integer", option.getClazz().getName());

		assertNull(option.getDescription());
		assertTrue(option.hasArguments());

		option.setDefault(1);

		assertEquals(1, option.getDefault());

		assertEquals(14, option.parse("14"));

		try {
			option.parse("21");
			assertFalse("Should not come here!", true);
		}
		catch (CommandLineException e) {
			assertEquals("Maximal allowed value for n, is 20!", e.getMessage());
		}

		try {
			option.parse("9");
			assertFalse("Should not come here!", true);
		}
		catch (CommandLineException e) {
			assertEquals("Minimal allowed value for n, is 10!", e.getMessage());
		}
	}
}