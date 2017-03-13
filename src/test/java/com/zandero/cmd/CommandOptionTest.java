package com.zandero.cmd;

import com.zandero.cmd.option.BoolOption;
import com.zandero.cmd.option.CommandOption;
import com.zandero.cmd.option.StringOption;
import com.zandero.cmd.option.VoidOption;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class CommandOptionTest {

	@Test
	public void StringOptionTest() {

		CommandOption option = new StringOption("n")
			.longCommand("name")
			.setting("user_name");

		assertEquals("n", option.getCommand());
		assertEquals("name", option.getLongCommand());
		assertEquals("user_name", option.getSetting());

		assertEquals("java.lang.String", option.getClazz().getName());

		assertNull(option.getDescription());
		assertTrue(option.hasArguments());

		option.setDefault("Test");

		assertEquals("Test", option.getDefault());
	}

	@Test
	public void EmptyOptionTest() {

		CommandOption option = new VoidOption("n");
		assertFalse(option.hasArguments());
	}

	@Test
	public void BooleanOptionTest() {

		CommandOption option = new BoolOption("n");
		assertFalse(option.hasArguments());
	}
}