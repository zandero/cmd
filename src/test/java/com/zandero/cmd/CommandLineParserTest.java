package com.zandero.cmd;

import com.zandero.cmd.option.BoolOption;
import com.zandero.cmd.option.CommandOption;
import com.zandero.settings.Settings;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class CommandLineParserTest {

	@Test
	public void simpleParseTest() throws CommandLineException {

		// build commands
		CommandOption option = new BoolOption("a").longCommand("all");

		// create builder
		CommandBuilder builder = new CommandBuilder();
		builder.add(option);

		// create parser
		CommandLineParser parser = new CommandLineParser(builder);

		// 1. parse (short option)
		String[] args = new String[]{"-a"};
		Settings output = parser.parse(args);

		assertTrue(output.getBool("a"));

		// 2. parse (long option)
		args = new String[]{"--all"};
		output = parser.parse(args);

		assertTrue(output.getBool("a"));

		// nothing given ... should return default
		output = parser.parse(null);
		assertFalse(output.getBool("a"));

	}

}