package com.zandero.cmd;

import com.zandero.cmd.option.CommandOption;
import com.zandero.cmd.option.ConfigFileOption;
import com.zandero.settings.Settings;
import com.zandero.utils.Assert;
import com.zandero.utils.StringUtils;

import java.util.List;

/**
 * Parses given arguments to a map of settings if possible
 */
public class CommandLineParser {

	private final CommandBuilder builder;

	/**
	 * Sets up command line parser
	 *
	 * @param options definition of provided command line arguments
	 */
	public CommandLineParser(List<CommandOption<?>> options) {

		this(new CommandBuilder(options));
	}

	/**
	 * Sets up command line parser
	 *
	 * @param cmdBuilder command option builder
	 */
	public CommandLineParser(CommandBuilder cmdBuilder) {

		builder = cmdBuilder;
	}

	/**
	 * Sets default settings to be returned in case given setting is not provided with arguments
	 *
	 * @param settings map of default settings
	 */
	public void setDefaults(Settings settings) {

		Assert.notNull(settings, "Missing default settings!");

		builder.setDefaults(settings);
	}

	/**
	 * Returns HashMap of read out settings
	 *
	 * @param arguments to be parsed and checked
	 * @return IllegalArgumentException in case parsing failed
	 */
	public Settings parse(String[] arguments) throws CommandLineException {

		Settings out = new Settings();

		if (arguments != null) {

			// each argument is either a option or a option value
			CommandOption<?> option = null;

			for (String argument : arguments) {

				if (isOption(argument)) { // is this an option or an argument ... ?

					option = findOption(argument);

					if (option != null && !option.hasArguments()) {
						// this is a no arg option ... add it to list (we might override this in the next step)
						out.put(option.getSetting(), option.parse(argument));
					}
					continue;
				}

				if (option == null) {
					throw new CommandLineException("Unknown command line option: " + argument);
				}

				Object value = option.parse(argument);
				out.put(option.getSetting(), value);
			}
		}

		// check if ConfigFileOption is provided and given
		CommandOption<?> config = builder.getConfigFileOption();

		// create copy of builder to override defaults if needed
		CommandBuilder defaults = new CommandBuilder(builder.get());

		if (config != null) {

			String file = (String) out.get(config.getSetting()); // is provided?
			if (file != null) {
				ConfigFileReader reader = new ConfigFileReader();
				Settings configSettings = reader.load(file, builder);

				// override default with read config
				defaults.setDefaults(configSettings);

				// remove config setting from output
				out.remove(config.getSetting());
			}
		}

		// add default options if any
		for (CommandOption<?> option : defaults.get()) {

			if (!(option instanceof ConfigFileOption) &&
				!out.containsKey(option.getSetting())) {
				out.put(option.getSetting(), option.getDefault());
			}
		}

		// check if required options are present
		for (CommandOption<?> option : builder.options) {
			if (option.isRequired() &&  out.get(option.getSetting()) == null) {
				throw new CommandLineException("Missing required: " + option.toCommandString());
			}
		}

		return out;
	}

	private boolean isOption(String argument) {

		if (StringUtils.isNullOrEmptyTrimmed(argument)) {
			return false;
		}

		return argument.startsWith("-") || argument.startsWith("--");
	}

	/**
	 * Extracts argument and returns setting
	 *
	 * @param argument to extract setting from
	 * @return name, value pair (name = setting name)
	 */
	private CommandOption<?> findOption(String argument) {

		if ("--".equals(argument) || argument == null) {
			return null;
		}

		if (argument.startsWith("--")) {
			return builder.findLong(argument);
		}

		if (argument.startsWith("-")) {
			return builder.findShort(argument);
		}

		// ok find short or long
		CommandOption<?> found = builder.findShort(argument);
		if (found == null) {
			found = builder.findLong(argument);
		}

		return found;
	}
}
