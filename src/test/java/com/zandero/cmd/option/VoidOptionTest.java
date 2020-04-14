package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *
 */
public class VoidOptionTest {

	@Test
	public void VoidOption() throws CommandLineException {

		CommandOption<Void> option = new VoidOption("v");
		assertFalse(option.hasArguments());
		assertNull(option.getDefault());

		assertNull(option.parse("anything"));
	}
}