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

		assertEquals("java.lang.Integer", option.getType().getTypeName());

		assertEquals("", option.getDescription());
		assertTrue(option.hasArguments());

		option.defautlTo(1);

		assertEquals(1, option.getDefault());

		assertEquals(14, option.parse("14"));
	}

	@Test(expected = CommandLineException.class)
	public void parseMinimumFailTest() throws CommandLineException {

		CommandOption option = new IntOption("n")
			.min(10)
			.max(20)
			.longCommand("name")
			.setting("user_name");

		try {
			option.parse("9");
		}
		catch (CommandLineException e) {
			assertEquals("Minimal allowed value for n, is 10!", e.getMessage());
			throw e;
		}
	}

	@Test(expected = CommandLineException.class)
	public void parseMaximumFailTest() throws CommandLineException {

		CommandOption option = new IntOption("n")
			.min(10)
			.max(20)
			.longCommand("name")
			.setting("user_name");

		try {
			option.parse("21");
		}
		catch (CommandLineException e) {
			assertEquals("Maximal allowed value for n, is 20!", e.getMessage());
			throw e;
		}
	}

	@Test(expected = CommandLineException.class)
	public void parseTest() throws CommandLineException {

		IntOption option = new IntOption("test");
		try {
			option.parse(null);
		}
		catch (CommandLineException e) {
			assertEquals("Integer expected for test, but: 'null', was given!", e.getMessage());
			throw e;
		}
	}

	@Test(expected = CommandLineException.class)
	public void parseTest_2() throws CommandLineException {

		IntOption option = new IntOption("test");
		try {
			option.parse("a");
		}
		catch (CommandLineException e) {
			assertEquals("Integer expected for test, but: 'a', was given!", e.getMessage());
			throw e;
		}
	}
}