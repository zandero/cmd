package com.zandero.cmd.option;

import com.zandero.utils.StringUtils;

import java.util.HashMap;

/**
 * Boolean option with null value if desired
 */
public class BoolOption extends CommandOption<Boolean> {

	private static final HashMap<String, Boolean> valueMap;

	static {
		valueMap = new HashMap<>();
		valueMap.put("1", true);
		valueMap.put("t", true);
		valueMap.put("true", true);
		valueMap.put("yes", true);
		valueMap.put("y", true);
		valueMap.put("on", true);

		valueMap.put("0", false);
		valueMap.put("f", false);
		valueMap.put("false", false);
		valueMap.put("no", false);
		valueMap.put("n", false);
		valueMap.put("off", false);

		valueMap.put("null", null);
		valueMap.put("nil", null);
		valueMap.put("nul", null);
	}

	public BoolOption(String name) {

		super(name);
		super.setDefault(false); // by default boolean options are false
	}

	@Override
	public Boolean parse(String argument) {

		argument = StringUtils.trimToNull(argument);
		if (argument == null) {
			return null;
		}

		if (isLong(argument) || isShort(argument)) {
			return true;
		}

		argument = argument.toLowerCase();
		return valueMap.get(argument);
	}

	/**
	 * Will invert default to true, when option given value is false
	 * @return inverted BoolOption
	 */
	public BoolOption invert() {
		setDefault(!getDefault());
		return this;
	}
}
