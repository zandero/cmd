package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import com.zandero.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Supports options with multiple arguments 1..n
 * For instance: -a one two three ... would fill option "a" with a list of arguments holding (one, two, three)
 */
public class MultiArgOption extends CommandOption<List<String>> {

	/**
	 * Initializes command option
	 *
	 * @param shortName short command name, for instance: "a"
	 */
	public MultiArgOption(String shortName) {

		super(shortName);
	}

	@Override
	public List<String> parse(String argument) throws CommandLineException {

		if (StringUtils.isNullOrEmptyTrimmed(argument)) {
			return null;
		}

		String[] items = argument.split(" ");

		List<String> list = new ArrayList();
		for (String item : items) {
			list.add(StringUtils.trimToNull(item));
		}

		return list;
	}
}
