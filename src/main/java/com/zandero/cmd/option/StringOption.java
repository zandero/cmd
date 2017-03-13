package com.zandero.cmd.option;

import com.zandero.utils.StringUtils;

/**
 * String based options
 */
public class StringOption extends CommandOption<String> {

	public StringOption(String name) {

		super(name, String.class);
	}

	@Override
	public String parse(String argument) {

		return StringUtils.trim(argument);
	}
}
