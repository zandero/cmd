package com.zandero.cmd;

import com.zandero.cmd.option.*;
import com.zandero.settings.Settings;
import com.zandero.utils.ResourceUtils;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class ConfigReaderTest {

	@Test
	public void readOptionsFromFile() throws CommandLineException, URISyntaxException {

		// build commands
		CommandOption all = new BoolOption("a")
			.invert()
			.longCommand("all");

		CommandOption file = new StringOption("f")
			.longCommand("file");

		CommandOption size = new IntOption("s")
			.longCommand("size");

		CommandOption config = new ConfigFileOption("c")
			.longCommand("config");

		// create builder
		CommandBuilder builder = new CommandBuilder();
		builder.add(all);
		builder.add(file);
		builder.add(size);
		builder.add(config);

		// create parser
		CommandLineParser parser = new CommandLineParser(builder);

		String configFile = this.getClass().getResource("/settings.cfg").getFile();
		String[] args = new String[]{"-c", configFile};

		Settings out = parser.parse(args);

		assertEquals(3, out.size());
		assertEquals(false, out.get("a"));
		assertEquals(10, out.get("s"));
		assertEquals("some.file", out.get("f"));
	}
}
