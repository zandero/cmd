package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import com.zandero.utils.UrlUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * URL option
 */
public class UrlOption extends CommandOption<URI> {

	public UrlOption(String name) {

		super(name);
	}

	@Override
	public URI parse(String argument) throws CommandLineException {

		if (!UrlUtils.isUrl(argument)) {
			throw new CommandLineException("URL expected for " + getCommand() + ", but: '" + argument + "', was given!");
		}

		try {
			argument = argument.trim();
			return new URI(argument);
		}
		catch (URISyntaxException e) {
			throw new CommandLineException("URL expected for " + getCommand() + ", but: '" + argument + "', was given! " + e.getMessage());
		}
	}
}
