package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import com.zandero.utils.Assert;
import com.zandero.utils.StringUtils;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

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
	 //* @param classType type of option value
	 */
	public CommandOption(String shortName) { //, Class<T> classType) {

		Assert.notNullOrEmptyTrimmed(shortName, "Missing option name!");
		shortName = StringUtils.trim(shortName);

		String compare = StringUtils.sanitizeWhitespace(shortName);
		Assert.isTrue(shortName.equals(compare), "Option name can not contain whitespace characters!");

		command = shortName;

		//
		final ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		Class<T> classType = (Class<T>) type.getActualTypeArguments()[0];

		if (classType == null ||
			classType.getName().equals(void.class.getName()) || // void to indicate a default option
			classType.getName().equals(Void.class.getName()) ||
			classType.getName().equals(boolean.class.getName()) || // boolean are optional by default ... if not given they default to false
			classType.getName().equals(Boolean.class.getName())) {
			classType = null; // has argument = false
		}

		clazz = classType;
	}

	public static <T> Class<T> getGenericClassParameter(final Class<?> parameterizedSubClass, final Class<?> genericSuperClass, final int pos) {
		// a mapping from type variables to actual values (classes)
		Map<TypeVariable<?>, Class<?>> mapping = new HashMap<>();

		Class<?> klass = parameterizedSubClass;
		while (klass != null) {
			Type type = klass.getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType parType = (ParameterizedType) type;
				Type rawType = parType.getRawType();
				if (rawType == genericSuperClass) {
					// found
					Type t = parType.getActualTypeArguments()[pos];
					if (t instanceof Class<?>) {
						return (Class<T>) t;
					} else {
						return (Class<T>) mapping.get((TypeVariable<?>)t);
					}
				}
				// resolve
				Type[] vars = ((GenericDeclaration)(parType.getRawType())).getTypeParameters();
				Type[] args = parType.getActualTypeArguments();
				for (int i = 0; i < vars.length; i++) {
					if (args[i] instanceof Class<?>) {
						mapping.put((TypeVariable)vars[i], (Class<?>)args[i]);
					} else {
						mapping.put((TypeVariable)vars[i], mapping.get((TypeVariable<?>)(args[i])));
					}
				}
				klass = (Class<?>) rawType;
			} else {
				klass = klass.getSuperclass();
			}
		}
		throw new IllegalArgumentException("no generic supertype for " + parameterizedSubClass + " of type " + genericSuperClass);
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

		return clazz != null && !clazz.isInstance(Void.class);
	}

	/**
	 * @return setting name option is associated with
	 */
	public String getSetting() {

		return setting == null ? command : setting;
	}

	/**
	 * Compares against other option
	 * @param option to compare agains
	 * @return true if options have same short name, long name or setting associated
	 */
	public boolean is(CommandOption option) {

		if (option == null) {
			return false;
		}

		return StringUtils.equals(command, option.command)
			|| StringUtils.equals(longCommand, option.longCommand)
			|| StringUtils.equals(setting, option.setting);
	}

	/**
	 * @param argument to compare against
	 * @return true if short command name equals argument, otherwise false
	 */
	public boolean isShort(String argument) {

		if (StringUtils.isNullOrEmptyTrimmed(argument)) {
			return false;
		}

		return argument.equals("-" + command) || argument.equals(command); // compare both "-a" and "a"
	}

	/**
	 * @param argument to compare against
	 * @return true if long command name equals argument, otherwise false
	 */
	public boolean isLong(String argument) {

		if (StringUtils.isNullOrEmptyTrimmed(argument)) {
			return false;
		}

		return argument.equals("--" + longCommand) || argument.equals(longCommand); // compare both "--long" and "long"
	}

	@Override
	public String toString() {

		String out = "[-" + command + " " + (longCommand == null ? "" : longCommand) + "]";

		if (description != null) {
			out = out + " " + description;
		}

		return out;
	}

	/**
	 * To be implemented by custom option
	 * @param argument given argument in command line
	 * @return parsed value of argument in option type, or throws CommandLineException if can't be parsed into given type
	 */
	public abstract T parse(String argument) throws CommandLineException;
}
