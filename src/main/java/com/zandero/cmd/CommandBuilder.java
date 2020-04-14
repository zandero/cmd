package com.zandero.cmd;

import com.zandero.cmd.option.CommandOption;
import com.zandero.cmd.option.ConfigFileOption;
import com.zandero.settings.Settings;
import com.zandero.utils.Assert;
import com.zandero.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Collects options and checks there is no overlapping
 */
public class CommandBuilder {

	List<CommandOption<?>> options = new ArrayList<>();

	private String helpAppVersion;

	private String helpAppExample;

	/**
	 * Creates empty builder
	 */
	public CommandBuilder() {

	}

	/**
	 * Creates builder with list of options
	 *
	 * @param commands options to add
	 */
	public CommandBuilder(List<CommandOption<?>> commands) {

		Assert.notNull(commands, "Missing command option!");
		commands.forEach(this::add);
	}

	/**
	 * Adds new option and checks if option overlaps with existing options
	 *
	 * @param option to be added
	 * @throws IllegalArgumentException in case option overlaps with existing options by name, long name or setting
	 */
	public void add(CommandOption<?> option) {

		Assert.notNull(option, "Missing command line option");

		CommandOption<?> found = find(option);
		Assert.isNull(found, "Given option: " + option + " overlaps with: " + found);

		options.add(option);
	}

	/**
	 * Finds similar option by name or short name
	 *
	 * @param option to provide info
	 * @return found option or null if none found
	 */
	private CommandOption<?> find(CommandOption<?> option) {

		Assert.notNull(option, "Missing option!");

		Optional<CommandOption<?>> found = options.stream().filter(o -> o.is(option)).findFirst();
		return found.orElse(null);
	}

	/**
	 * Find option by short name
	 *
	 * @param argument short name
	 * @return found option or null if none found
	 */
	public CommandOption<?> findShort(String argument) {

		Assert.notNullOrEmptyTrimmed(argument, "Missing short name!");

		Optional<CommandOption<?>> found = options.stream().filter(o -> o.isShort(argument)).findFirst();
		return found.orElse(null);
	}

	/**
	 * Finds option by long name
	 *
	 * @param argument long name
	 * @return found option or null if none found
	 */
	public CommandOption<?> findLong(String argument) {

		Assert.notNullOrEmptyTrimmed(argument, "Missing long name!");

		Optional<CommandOption<?>> found = options.stream().filter(o -> o.isLong(argument)).findFirst();
		return found.orElse(null);
	}

	/**
	 * Finds option by setting
	 *
	 * @param key setting name
	 * @return found option or null if none found
	 */
	private CommandOption<?> findBySetting(String key) {

		Assert.notNullOrEmptyTrimmed(key, "Missing setting key!");

		Optional<CommandOption<?>> found = options.stream().filter(o -> o.getSetting().equals(key)).findFirst();
		return found.orElse(null);
	}

	/**
	 * Finds option by short, long or setting name
	 *
	 * @param name to search for
	 * @return found option or null if none found
	 */
	public CommandOption<?> findOption(String name) {

		Assert.notNullOrEmptyTrimmed(name, "Missing name!");

		Optional<CommandOption<?>> found = options.stream().filter(o ->
			o.getSetting().equals(name) ||
				o.isShort(name) ||
				o.isLong(name)).findFirst();

		return found.orElse(null);
	}

	/**
	 * @return list of stored options
	 */
	List<CommandOption<?>> get() {

		return options;
	}

	/**
	 * Sets default settings if listed in options
	 *
	 * @param defaultSettings to be set
	 */
	void setDefaults(Settings defaultSettings) {

		Assert.notNull(defaultSettings, "Missing default settings!");

		for (String key : defaultSettings.keySet()) {

			CommandOption<?> option = findBySetting(key);

			if (option == null) {
				// option not present ... check next
				continue;
			}

			Object value = defaultSettings.get(key);
			option.defaultsTo(value);
		}

	}

	/**
	 * Gets config file option if present in list
	 *
	 * @return config file option or null if not configured
	 */
	CommandOption<?> getConfigFileOption() {

		for (CommandOption<?> option : options) {
			if (option instanceof ConfigFileOption) {
				return option;
			}
		}

		return null;
	}

	/**
	 * Sets app version and example to be show in help screen
	 * @param appVersion application name and version
	 * @param usageExample example or additional data
	 */
	public void setHelp(String appVersion, String usageExample) {

		helpAppVersion = StringUtils.trimToNull(appVersion);
		helpAppExample = StringUtils.trimToNull(usageExample);
	}

	/**
	 * Outputs command line options for System.out display
	 * @return list of options as strings
	 */
	public List<String> getHelp() {

		List<String> out = new ArrayList<>();

		if (helpAppVersion != null) {
			out.add(helpAppVersion);
		}

		if (helpAppExample != null) {
			out.add(helpAppExample);
		}

		if (helpAppVersion != null || helpAppExample != null) {
	 		out.add(""); // new line
 		}

		// find longest option to format all other options by this one
		int max = 0;
		for (CommandOption<?> option: options) {
			String command = option.toCommandString();
			max = Math.max(max, command.length());
		}

		for (CommandOption<?> option: options) {
			String info = option.toCommandString();
			int spaces = max - info.length() + 1;

			String delimiter = new String(new char[spaces]).replace("\0", " "); // returns 'spaces' long empty string

			info = info + delimiter + option.getDescription();
			out.add(info);
		}

		return out;
	}

	/**
	 * @param command argument / parameter
	 * @return help for specific command, or empty list if none found
	 */
	public List<String> getHelpFor(String command) {
		command = StringUtils.trimAll(command, "-");
		for (CommandOption<?> option: options) {
			if (option.isLong(command) || option.isShort(command)) {
				return option.getHelp();
			}
		}

		return new ArrayList<>();
	}
}
