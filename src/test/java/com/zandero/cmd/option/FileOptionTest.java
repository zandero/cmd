package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 */
public class FileOptionTest {

    @Test
    public void parse() throws CommandLineException {
        CommandOption<String> option = new FileOption("f");
        String file = this.getClass().getResource("/settings.cfg").getFile();
        assertEquals(file, option.parse(file));
    }

    @Test
    public void parseInvalidFile() {
    	CommandOption<String> option = new FileOption("f");
		CommandLineException e = assertThrows(CommandLineException.class, () -> option.parse("/fail.file"));
        assertEquals("File '/fail.file' does not exist", e.getMessage());
    }
}