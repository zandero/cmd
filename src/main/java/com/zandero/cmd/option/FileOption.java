package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import com.zandero.utils.Assert;

import java.io.File;

/**
 * Reads configured options from a given file
 */
public class FileOption extends CommandOption<String> {

	/**
	 * Initializes file option
	 *
	 * @param shortName short command name, for instance: "a"
	 */
	public FileOption(String shortName) {

		super(shortName);
	}

	/**
	 * Expects file that is available and readable
	 *
	 * @param argument given argument in command line
	 * @return absolute file path
	 *
	 * @throws CommandLineException in case file name does not exists are can't be read
	 */
	@Override
	public String parse(String argument) throws CommandLineException {

		File file = new File(argument);

		try {
			Assert.isTrue(file.exists(), "File '" + file + "' does not exist");
			Assert.isFalse(file.isDirectory(), "File '" + file + "' is a directory");
			Assert.isTrue(file.canRead(), "File '" + file + "' cannot be read");
		}
		catch (IllegalArgumentException e) {
			throw new CommandLineException(e.getMessage());
		}

		return file.getAbsolutePath();
	}
}
