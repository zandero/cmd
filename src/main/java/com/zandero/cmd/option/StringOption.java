package com.zandero.cmd.option;

import com.zandero.utils.StringUtils;

/**
 * String option
 */
public class StringOption extends CommandOption<String> {

	public StringOption(String name) {

		super(name);
	}

	@Override
	public String parse(String argument) {

		return StringUtils.trim(argument);
	}
}
