package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class BoolOptionTest {

	@Test
	public void booleanOptionTest() {

		CommandOption option = new BoolOption("b");
		assertFalse(option.hasArguments());
		assertFalse((Boolean) option.getDefault());
	}

	@Test
	public void invertBooleanOptionTest() {

		CommandOption option = new BoolOption("b")
			.invert();

		assertFalse(option.hasArguments());
		assertTrue((Boolean) option.getDefault());
	}

	@Test
	public void parseTest() throws CommandLineException {

		CommandOption option = new BoolOption("b").longCommand("bool");

		assertTrue((boolean) option.parse("true"));
		assertTrue((boolean) option.parse("T"));
		assertTrue((boolean) option.parse("yes"));
		assertTrue((boolean) option.parse("Yes"));
		assertTrue((boolean) option.parse("on"));
		assertTrue((boolean) option.parse("ON"));
		assertTrue((boolean) option.parse("1"));

		assertFalse((boolean) option.parse("FALSE"));
		assertFalse((boolean) option.parse("F"));
		assertFalse((boolean) option.parse("no"));
		assertFalse((boolean) option.parse("NO"));
		assertFalse((boolean) option.parse("off"));
		assertFalse((boolean) option.parse("off"));
		assertFalse((boolean) option.parse("0"));

		assertNull(option.parse(""));
		assertNull(option.parse(" "));
		assertNull(option.parse(null));
		assertNull(option.parse("null"));
		assertNull(option.parse("NULL"));
		assertNull(option.parse("nil"));
		assertNull(option.parse("nul"));
	}
}