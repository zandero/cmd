package com.zandero.cmd;

import com.zandero.cmd.option.BoolOption;
import com.zandero.cmd.option.CommandOption;
import com.zandero.cmd.option.IntOption;
import com.zandero.cmd.option.StringOption;
import com.zandero.settings.Settings;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class CommandLineParserTest {

	@Test
	public void boolTest() throws CommandLineException {

		// build commands
		CommandOption option = new BoolOption("a").longCommand("all");

		// create builder
		CommandBuilder builder = new CommandBuilder();
		builder.add(option);

		// create parser
		CommandLineParser parser = new CommandLineParser(builder);

		// 0. empty parse
		Settings output = parser.parse(null);
		assertFalse(output.getBool("a"));

		// 1. parse (short option)
		String[] args = new String[]{"-a"};
		output = parser.parse(args);

		assertTrue(output.getBool("a"));

		// 2. parse (long option)
		args = new String[]{"--all"};
		output = parser.parse(args);

		assertTrue(output.getBool("a"));

		// nothing given ... should return default
		output = parser.parse(null);
		assertFalse(output.getBool("a"));

		// 3. parse with arguments given
		args = new String[]{"--all", "false"};
		output = parser.parse(args);

		assertFalse(output.getBool("a"));
	}

	@Test
	public void invertBoolParseTest() throws CommandLineException {

		// build commands
		CommandOption option = new BoolOption("a")
			.invert()
			.longCommand("all");

		// create builder
		CommandBuilder builder = new CommandBuilder();
		builder.add(option);

		// create parser
		CommandLineParser parser = new CommandLineParser(builder);

		// 0. empty parse (default to true)
		Settings output = parser.parse(null);
		assertTrue(output.getBool("a"));

		// 1. parse (short option)
		String[] args = new String[]{"-a"};
		output = parser.parse(args);

		assertTrue(output.getBool("a"));

		// 2. parse (short option)
		args = new String[]{"-a", "on"};
		output = parser.parse(args);

		assertTrue(output.getBool("a"));

		// 3. parse (short option)
		args = new String[]{"-a", "off"};
		output = parser.parse(args);

		assertFalse(output.getBool("a"));
	}

	@Test
	public void parseVariousOptions() throws CommandLineException {


		// build commands
		CommandOption all = new BoolOption("a")
			.invert()
			.longCommand("all");

		CommandOption file = new StringOption("f")
			.longCommand("file");

		CommandOption size = new IntOption("s")
			.longCommand("size");

		// create builder
		CommandBuilder builder = new CommandBuilder();
		builder.add(all);
		builder.add(file);
		builder.add(size);

		// create parser
		CommandLineParser parser = new CommandLineParser(builder);

		String[] args = new String[]{"-a", "false", "--size", "100", "-f", "some.file"};
		Settings out = parser.parse(args);

		assertEquals(3, out.size());
		assertEquals(false, out.get("a"));
		assertEquals(100, out.get("s"));
		assertEquals("some.file", out.get("f"));
	}
}