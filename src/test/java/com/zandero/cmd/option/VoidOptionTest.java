package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 *
 */
public class VoidOptionTest {

	@Test
	public void VoidOptionTest() throws CommandLineException {

		CommandOption option = new VoidOption("v");
		assertFalse(option.hasArguments());
		assertNull(option.getDefault());

		assertNull(option.parse("anything"));
	}
}