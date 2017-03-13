package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import com.zandero.utils.StringUtils;

/**
 * Integer option
 */
public class IntOption extends CommandOption<Integer> {

	private Integer min;

	private Integer max;

	/**
	 * Initializes command option
	 *
	 * @param shortName short command name, for instance: "a"
	 */
	public IntOption(String shortName) {

		super(shortName, Integer.class);
	}

	@Override
	public Integer parse(String argument) throws CommandLineException {

		if (StringUtils.isNullOrEmptyTrimmed(argument)) {
			throw new CommandLineException("Integer expected for " + getCommand() + ", but: '" + argument + "', was given!");
		}

		try {
			argument = argument.trim();
			int value = Integer.parseInt(argument);

			checkMinMax(value);

			return value;
		}
		catch (NumberFormatException e) {
			throw new CommandLineException("Integer expected for " + getCommand() + ", but: '" + argument + "', was given!");
		}


	}

	protected void checkMinMax(int value) throws CommandLineException {

		if (min != null && min > value) {
			throw new CommandLineException("Minimal allowed value for " + getCommand() + ", is " + getMin() + "!");
		}

		if (max != null && max < value) {
			throw new CommandLineException("Maximal allowed value for " + getCommand() + ", is " + getMax() + "!");
		}
	}

	/**
	 * @param minimum minimum allowed value (inclusive)
	 * @return build option
	 */
	public IntOption min(int minimum) {

		min = minimum;
		return this;
	}

	/**
	 * @param maximum allowed value (inclusive)
	 * @return build option
	 */
	public IntOption max(int maximum) {

		max = maximum;
		return this;
	}

	/**
	 * @return max allowed value for option or null if no maximum set
	 */
	public Integer getMin() {

		return min;
	}

	/**
	 * @return minimal allowed value for option of null if no minimum set
	 */
	public Integer getMax() {

		return max;
	}
}
