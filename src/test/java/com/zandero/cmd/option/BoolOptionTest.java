package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 */
public class BoolOptionTest {

	@Test
	public void booleanOptionTest() {

		CommandOption<Boolean> option = new BoolOption("b");
		assertFalse(option.hasArguments());
		assertFalse(option.getDefault());
	}

	@Test
	public void invertBooleanOptionTest() {

		CommandOption<Boolean> option = new BoolOption("b")
			.invert();

		assertFalse(option.hasArguments());
		assertTrue(option.getDefault());
	}

	@Test
	public void parseTest() throws CommandLineException {

		CommandOption<Boolean> option = new BoolOption("b").longCommand("bool");

		assertTrue(option.parse("true"));
		assertTrue(option.parse("T"));
		assertTrue(option.parse("yes"));
		assertTrue(option.parse("Yes"));
		assertTrue(option.parse("on"));
		assertTrue(option.parse("ON"));
		assertTrue(option.parse("1"));

		assertFalse(option.parse("FALSE"));
		assertFalse(option.parse("F"));
		assertFalse(option.parse("no"));
		assertFalse(option.parse("NO"));
		assertFalse(option.parse("off"));
		assertFalse(option.parse("off"));
		assertFalse(option.parse("0"));

		assertNull(option.parse(""));
		assertNull(option.parse(" "));
		assertNull(option.parse(null));
		assertNull(option.parse("null"));
		assertNull(option.parse("NULL"));
		assertNull(option.parse("nil"));
		assertNull(option.parse("nul"));
	}
}