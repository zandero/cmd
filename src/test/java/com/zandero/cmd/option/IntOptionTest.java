package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class IntOptionTest {

	@Test
	public void IntegerOptionTest() throws CommandLineException {

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
	}
}