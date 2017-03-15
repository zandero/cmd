package com.zandero.cmd.option;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class CommandOptionTest {

	@Test
	public void isShortOptionTest() {

		CommandOption option = new VoidOption("s").longCommand("long");
		assertTrue(option.isShort("-s"));
		assertTrue(option.isShort("s"));

		assertFalse(option.isShort("--s"));
		assertFalse(option.isShort(" "));
		assertFalse(option.isShort(null));
	}

	@Test
	public void isLongOptionTest() {

		CommandOption option = new VoidOption("s").longCommand("long");
		assertTrue(option.isLong("--long"));
		assertTrue(option.isLong("long"));

		assertFalse(option.isLong("-long"));

		assertFalse(option.isLong(" "));
		assertFalse(option.isLong(null));
	}

	@Test
	public void isOptionTest() {

		CommandOption option = new VoidOption("s").longCommand("long").setting("LONG");

		CommandOption other = new VoidOption("s").longCommand("other");
		CommandOption other2 = new VoidOption("sh").longCommand("long");
		CommandOption other3 = new VoidOption("sh").longCommand("longer").setting("LONG");

		CommandOption compare = new VoidOption("short").longCommand("longer").setting("LONGER");

		assertTrue(option.is(other));
		assertTrue(option.is(other2));
		assertTrue(option.is(other3));

		assertFalse(option.is(compare));
		assertFalse(option.is(null));
	}

	@Test
	public void setDefaultTest() {

		CommandOption option = new StringOption("v");
		option.setDefault("helo");

		assertEquals("helo", option.getDefault());

		option.setDefault(" helo ");
		assertEquals(" helo ", option.getDefault());

		option.setDefault(null);
		assertNull(option.getDefault());
	}

	@Test(expected = IllegalArgumentException.class)
	public void invalidDefaultTest() {

		try {
			CommandOption option = new StringOption("v");
			option.setDefault(1);
		}
		catch (IllegalArgumentException e) {
			assertEquals("Expected default setting of type: java.lang.String, but was provided: java.lang.Integer", e.getMessage());
			throw e;
		}
	}

	@Test
	public void toStringTest() {

		CommandOption option = new StringOption("v");
		assertEquals("[-v]", option.toString());

		option.longCommand("valid");
		assertEquals("[-v valid]", option.toString());

		option.description("This is a test option");
		assertEquals("[-v valid] This is a test option", option.toString());
	}
}