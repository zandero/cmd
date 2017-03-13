package com.zandero.cmd.option;

/**
 *
 */
public class VoidOption extends CommandOption<Void> {

	public VoidOption(String name) {

		super(name, Void.class);
	}

	@Override
	public Void parse(String argument) {

		return null;
	}
}
