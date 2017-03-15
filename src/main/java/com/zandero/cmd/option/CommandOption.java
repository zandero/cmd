package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import com.zandero.utils.Assert;
import com.zandero.utils.StringUtils;

import java.lang.reflect.ParameterizedType;

/**
 * Build up a single command line option
 */
public abstract class CommandOption<T> {

	/**
	 * Short command version: -a
	 */
	private final String command;

	/**
	 * Long command version: all
	 */
	private String longCommand;

	/**
	 * Expected value type of option
	 * Void for optional types
	 */
	private Class<T> clazz;

	/**
	 * Description of option
	 */
	private String description;

	/**
	 * Name of underlying setting
	 */
	private String setting = null;

	/**
	 * Default option value if any
	 */
	private T defaultValue = null;

	/**
	 * Initializes command option
	 *
	 * @param shortName short command name, for instance: "a"
	 */
	public CommandOption(String shortName) {

		Assert.notNullOrEmptyTrimmed(shortName, "Missing option name!");
		shortName = StringUtils.trim(shortName);

		String compare = StringUtils.sanitizeWhitespace(shortName);
		Assert.isTrue(shortName.equals(compare), "Option name can not contain whitespace characters!");

		command = shortName;

		// find out generic class type given in derived class
		clazz = getParametrizedType(this.getClass());
	}

	private Class<T> getParametrizedType(Class<?> clazz) {

		try {

			ParameterizedType type = (ParameterizedType) clazz.getGenericSuperclass();
			return (Class<T>) type.getActualTypeArguments()[0];
		}
		catch (ClassCastException e) {

			clazz = clazz.getSuperclass();
			if (clazz != null) {
				return getParametrizedType(clazz);
			}
		}

		return null;
	}

	/**
	 * Sets long command name
	 *
	 * @param longName long name, for instance: "all"
	 * @return command option
	 */
	public CommandOption<T> longCommand(String longName) {

		Assert.notNullOrEmptyTrimmed(longName, "Missing long name!");

		longName = StringUtils.trim(longName);
		Assert.isTrue(command.length() <= longName.length(), "Long name can't be shorter than command name!");

		String compare = StringUtils.sanitizeWhitespace(longName);
		Assert.isTrue(longName.equals(compare), "Option long name can not contain whitespace characters!");

		longCommand = longName;
		return this;
	}

	/**
	 * Sets command option setting key to be output when set
	 * if not set short command option name is used instead
	 *
	 * @param name of setting key
	 * @return command option
	 */
	public CommandOption<T> setting(String name) {

		Assert.notNullOrEmptyTrimmed(name, "Missing setting name");

		name = StringUtils.trim(name);
		String compare = StringUtils.sanitizeWhitespace(name);
		Assert.isTrue(name.equals(compare), "Setting name can not contain whitespace characters!");

		setting = name;
		return this;
	}

	/**
	 * Sets command option description to be shown in help screen
	 *
	 * @param text description
	 * @return command option
	 */
	public CommandOption<T> description(String text) {

		description = StringUtils.trimToNull(text);
		return this;
	}

	/**
	 * Sets command option default value
	 *
	 * @param aDefault value
	 */
	public void setDefault(Object aDefault) {

		if (aDefault == null) {
			defaultValue = null;
			return;
		}

		if (!getClazz().isInstance(aDefault)) {
			throw new IllegalArgumentException("Expected default setting of type: " + getClazz().getName() + ", but was provided: " + aDefault.getClass().getName());
		}

		defaultValue = (T) aDefault;
	}

	/**
	 * @return option default value used when option is not provided
	 */
	public T getDefault() {

		return defaultValue;
	}

	/**
	 * @return short command name
	 */
	public String getCommand() {

		return command;
	}

	/**
	 * @return long command name
	 */
	public String getLongCommand() {

		return longCommand;
	}

	/**
	 * @return command description
	 */
	public String getDescription() {

		if (description == null) {
			return "";
		}

		return description;
	}

	/**
	 * @return setting value type
	 */
	public Class<T> getClazz() {

		return clazz;
	}

	/**
	 * @return true in case command option expects an argument, false if it is a flag only option
	 */
	public boolean hasArguments() {

		return clazz != null && clazz != Void.class && clazz != Boolean.class;
	}

	/**
	 * @return setting name option is associated with
	 */
	public String getSetting() {

		return setting == null ? command : setting;
	}

	/**
	 * Compares against other option
	 *
	 * @param option to compare agains
	 * @return true if options have same short name, long name or setting associated
	 */
	public boolean is(CommandOption option) {

		if (option == null) {
			return false;
		}

		return StringUtils.equals(getCommand(), option.getCommand())
			|| StringUtils.equals(getLongCommand(), option.getLongCommand())
			|| StringUtils.equals(getSetting(), option.getSetting());
	}

	/**
	 * @param argument to compare against
	 * @return true if short command name equals argument, otherwise false
	 */
	public boolean isShort(String argument) {

		if (StringUtils.isNullOrEmptyTrimmed(argument)) {
			return false;
		}

		return argument.equals("-" + getCommand()) || argument.equals(getCommand()); // compare both "-a" and "a"
	}

	/**
	 * @param argument to compare against
	 * @return true if long command name equals argument, otherwise false
	 */
	public boolean isLong(String argument) {

		if (StringUtils.isNullOrEmptyTrimmed(argument)) {
			return false;
		}

		return argument.equals("--" + getLongCommand()) || argument.equals(getLongCommand()); // compare both "--long" and "long"
	}

	public String toCommandString() {

		String out = "-" + getCommand();
		if (longCommand == null) {
			return out;
		}

		return out + " [ --" + getLongCommand() + " ]";
	}

	@Override
	public String toString() {

		String out = toCommandString();

		if (description != null) {
			out = out + " " + getDescription();
		}

		return out;
	}

	/**
	 * To be implemented by custom option
	 *
	 * @param argument given argument in command line
	 * @return parsed value of argument in option type, or throws CommandLineException if can't be parsed into given type
	 */
	public abstract T parse(String argument) throws CommandLineException;
}
