package com.zandero.cmd;

import com.zandero.cmd.option.CommandOption;
import com.zandero.settings.Settings;
import com.zandero.utils.Assert;

import java.util.*;

/**
 * Collects options and checks there is no overlapping
 */
public class CommandBuilder {

	List<CommandOption> options = new ArrayList<>();

	public CommandBuilder() {

	}

	public CommandBuilder(List<CommandOption> commands) {

		for (CommandOption option: commands) {
			add(option);
		}
	}

	public void add(CommandOption option) {

		Assert.notNull(option, "Missing command line option");

		CommandOption found = find(option);
		Assert.isNull(found, "Given option: " + option + " overlaps with: " + found);

		options.add(option);
	}

	/**
	 * Finds similar option by name or short name
	 * @param option to provide info
	 * @return found option or null if none found
	 */
	private CommandOption find(CommandOption option) {

		Optional<CommandOption> found = options.stream().filter(o -> o.is(option)).findFirst();
		return found.orElse(null);
	}


	public CommandOption findShort(String argument) {

		Optional<CommandOption> found = options.stream().filter(o -> o.isShort(argument)).findFirst();
		return found.orElse(null);
	}

	public CommandOption findLong(String argument) {

		Optional<CommandOption> found = options.stream().filter(o -> o.isLong(argument)).findFirst();
		return found.orElse(null);
	}

	private CommandOption<?> findBySetting(String key) {

		Assert.notNullOrEmptyTrimmed(key, "Missing setting key!");

		Optional<CommandOption> found = options.stream().filter(o -> o.getSetting().equals(key)).findFirst();
		return found.orElse(null);
	}

	List<CommandOption> get() {

		return options;
	}

	/**
	 * Sets default settings if listed in options
	 * @param defaultSettings to be set
	 */
	public void setDefaults(Settings defaultSettings) {

		Assert.notNull(defaultSettings, "Missing default settings!");
		for (String key: defaultSettings.keySet()) {

			CommandOption<?> option = findBySetting(key);

			if (option == null) {
				// option not present ... check next
				continue;
			}

			Object value = defaultSettings.get(key);
			option.setDefault(value);
		}

	}
}
