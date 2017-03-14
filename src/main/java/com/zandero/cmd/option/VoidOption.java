package com.zandero.cmd.option;

/**
 * Placeholder option
 */
public class VoidOption extends CommandOption<Void> {

	public VoidOption(String name) {

		super(name);
	}

	@Override
	public Void parse(String argument) {

		return null;
	}
}
