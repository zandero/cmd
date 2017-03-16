package com.zandero.cmd;

import com.zandero.cmd.option.BoolOption;
import com.zandero.cmd.option.CommandOption;
import com.zandero.cmd.option.IntOption;
import com.zandero.cmd.option.StringOption;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class CommandBuilderTest {
	
	@Test
	public void getHelpTest() {

		// build commands
		CommandOption all = new BoolOption("a")
			.invert()
			.longCommand("all")
			.description("show all files");

		CommandOption file = new StringOption("f")
			.longCommand("files")
			.description("show single file");

		CommandOption size = new IntOption("s")
			.description("display file sizes");

		// create builder
		CommandBuilder builder = new CommandBuilder();
		builder.add(all);
		builder.add(file);
		builder.add(size);

		List<String> out = builder.getHelp();
		assertEquals("-a [ --all ]   show all files", out.get(0));
		assertEquals("-f [ --files ] show single file", out.get(1));
		assertEquals("-s             display file sizes", out.get(2));
	}
}