package com.zandero.cmd;

import com.zandero.cmd.option.CommandOption;
import com.zandero.settings.Settings;
import com.zandero.utils.Assert;
import com.zandero.utils.Pair;
import com.zandero.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Loads options from a config file
 */
public class ConfigReader {

	private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);

	/**
	 * Loads name value pairs directly from given file ... adding them as settings
	 *
	 * @param file to get settings from
	 * @return list of settings
	 */
	public Settings load(String file, CommandBuilder builder) throws CommandLineException {

		Scanner scanner = null;
		try {

			File config = new File(file);
			Assert.isTrue(config.exists(), "File '" + file + "' does not exist");
			Assert.isFalse(config.isDirectory(), "File '" + file + "' is a directory");
			Assert.isTrue(config.canRead(), "File '" + file + "' cannot be read");

			scanner = new Scanner(config);

			ArrayList<String> list = new ArrayList<>();
			while (scanner.hasNextLine()) {
				list.add(scanner.nextLine());
			}

			return parse(list, builder);
		}
		catch (FileNotFoundException e) {

			log.error("File not found: " + e.getMessage());
			throw new CommandLineException("File: '" + file + "', not found!");
		}
		finally {

			if (scanner != null) {
				scanner.close();
			}
		}
	}

	/**
	 * Expects name = value in each line
	 * ignores lines starting with '#' or '//'
	 *
	 * @param list of strings
	 */
	private Settings parse(ArrayList<String> list, CommandBuilder builder) throws CommandLineException {

		Settings settings = new Settings();

		if (list != null && list.size() > 0) {

			for (String line : list) {

				line = StringUtils.trimToNull(line);
				if (line != null && !isComment(line)) {
					String[] items = line.split("=");

					if (items.length == 2) {
						String name = items[0];
						String value = items[1];

						Pair<String, Object> found = parseAndAdd(builder, name, value);

						if (found != null) {
							settings.put(found.getKey(), found.getValue());
						}
					}
				}
			}
		}

		return settings;
	}

	private Pair<String, Object> parseAndAdd(CommandBuilder cmdBuilder, String name, String value) throws CommandLineException {

		name = StringUtils.trim(name);

		CommandOption found = cmdBuilder.findOption(name);

		// OK setting exits ... check and return
		if (found != null) {

			value = StringUtils.trim(value);
			Object optionValue = found.parse(value);

			return new Pair<>(found.getSetting(), optionValue);
		}

		return null;
	}

	private boolean isComment(String line) {

		return line != null && (line.startsWith("#") || line.startsWith("//"));
	}
}
