package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class FileOptionTest {

	@Test
	public void parse() throws CommandLineException {

		CommandOption option = new FileOption("f");

		String file = this.getClass().getResource("/settings.cfg").getFile();
		assertEquals(file, option.parse(file));
	}

	@Test(expected = CommandLineException.class)
	public void parseInvalidFile() throws CommandLineException {


		try {
			CommandOption option = new FileOption("f");
			option.parse("/fail.file");
		}
		catch (CommandLineException e) {
			assertEquals("File '/fail.file' does not exist", e.getMessage());
			throw e;
		}
	}

}