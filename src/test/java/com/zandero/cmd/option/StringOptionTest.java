package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class StringOptionTest {

	@Test
	public void stringOptionTest() {

		CommandOption option = new StringOption("s");
		assertTrue(option.hasArguments());
		assertNull(option.getDefault());
		assertEquals("s", option.getSetting());
	}

	@Test
	public void stringOptionFullTest() {

		CommandOption option = new StringOption("s")
			.longCommand("name")
			.setting("user_name");

		assertEquals("s", option.getCommand());
		assertEquals("name", option.getLongCommand());
		assertEquals("user_name", option.getSetting());

		assertEquals("java.lang.String", option.getType().getTypeName());

		assertEquals("", option.getDescription());
		assertTrue(option.hasArguments());

		option.defaultsTo("Test");

		assertEquals("Test", option.getDefault());
	}

	@Test
	public void parseTest() throws CommandLineException {

		CommandOption option = new StringOption("s");

		assertNull(option.parse(null));
		assertEquals("tralala", option.parse("tralala"));
	}
}