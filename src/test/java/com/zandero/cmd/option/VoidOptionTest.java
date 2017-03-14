package com.zandero.cmd.option;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 */
public class VoidOptionTest {

	@Test
	public void VoidOptionTest() {

		CommandOption option = new VoidOption("v");
		assertFalse(option.hasArguments());
		assertNull(option.getDefault());
	}
}