package com.zandero.cmd;

import com.zandero.cmd.option.CommandOption;
import com.zandero.cmd.option.ConfigFileOption;
import com.zandero.settings.Settings;
import com.zandero.utils.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Collects options and checks there is no overlapping
 */
public class CommandBuilder {

	List<CommandOption> options = new ArrayList<>();

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
	public CommandBuilder(List<CommandOption> commands) {

		for (CommandOption option : commands) {
			add(option);
		}
	}

	/**
	 * Adds new option and checks if option overlaps with existing options
	 *
	 * @param option to be added
	 * @throws IllegalArgumentException in case option overlaps with existing options by name, long name or setting
	 */
	public void add(CommandOption option) {

		Assert.notNull(option, "Missing command line option");

		CommandOption found = find(option);
		Assert.isNull(found, "Given option: " + option + " overlaps with: " + found);

		options.add(option);
	}

	/**
	 * Finds similar option by name or short name
	 *
	 * @param option to provide info
	 * @return found option or null if none found
	 */
	private CommandOption find(CommandOption option) {

		Assert.notNull(option, "Missing option!");

		Optional<CommandOption> found = options.stream().filter(o -> o.is(option)).findFirst();
		return found.orElse(null);
	}

	/**
	 * Find option by short name
	 *
	 * @param argument short name
	 * @return found option or null if none found
	 */
	public CommandOption findShort(String argument) {

		Assert.notNullOrEmptyTrimmed(argument, "Missing short name!");

		Optional<CommandOption> found = options.stream().filter(o -> o.isShort(argument)).findFirst();
		return found.orElse(null);
	}

	/**
	 * Finds option by long name
	 *
	 * @param argument long name
	 * @return found option or null if none found
	 */
	public CommandOption findLong(String argument) {

		Assert.notNullOrEmptyTrimmed(argument, "Missing long name!");

		Optional<CommandOption> found = options.stream().filter(o -> o.isLong(argument)).findFirst();
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

		Optional<CommandOption> found = options.stream().filter(o -> o.getSetting().equals(key)).findFirst();
		return found.orElse(null);
	}

	/**
	 * Finds option by short, long or setting name
	 *
	 * @param name to search for
	 * @return found option or null if none found
	 */
	public CommandOption findOption(String name) {

		Assert.notNullOrEmptyTrimmed(name, "Missing name!");

		Optional<CommandOption> found = options.stream().filter(o ->
			o.getSetting().equals(name) ||
				o.isShort(name) ||
				o.isLong(name)).findFirst();

		return found.orElse(null);
	}

	/**
	 * @return list of stored options
	 */
	List<CommandOption> get() {

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
			option.setDefault(value);
		}

	}

	/**
	 * Gets config file option if present in list
	 *
	 * @return config file option or null if not configured
	 */
	CommandOption getConfigFileOption() {

		for (CommandOption option : options) {
			if (option instanceof ConfigFileOption) {
				return option;
			}
		}

		return null;
	}
}
