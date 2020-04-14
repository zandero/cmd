package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class IntOptionTest {

    @Test
    public void intOptionTest() {

        CommandOption<Integer> option = new IntOption("i");

        assertTrue(option.hasArguments());
        assertNull(option.getDefault());
    }

    @Test
    public void intOptionMinMaxTest() throws CommandLineException {

        CommandOption<Integer> option = new IntOption("n")
                                            .min(10)
                                            .max(20)
                                            .longCommand("name")
                                            .setting("user_name");

        assertEquals("n", option.getCommand());
        assertEquals("name", option.getLongCommand());
        assertEquals("user_name", option.getSetting());

        assertEquals("java.lang.Integer", option.getType().getTypeName());

        assertEquals("", option.getDescription());
        assertTrue(option.hasArguments());

        option.defaultsTo(1);

        assertEquals(1, option.getDefault());

        assertEquals(14, option.parse("14"));
    }

    @Test
    public void parseMinimumFailTest() {

        CommandOption<Integer> option = new IntOption("n")
                                            .min(10)
                                            .max(20)
                                            .longCommand("name")
                                            .setting("user_name");

        CommandLineException e = assertThrows(CommandLineException.class, () -> option.parse("9"));
        assertEquals("Minimal allowed value for n, is 10!", e.getMessage());

    }

    @Test
    public void parseMaximumFailTest() throws CommandLineException {

        CommandOption<Integer> option = new IntOption("n")
                                            .min(10)
                                            .max(20)
                                            .longCommand("name")
                                            .setting("user_name");

        CommandLineException e = assertThrows(CommandLineException.class, () -> option.parse("21"));
        assertEquals("Maximal allowed value for n, is 20!", e.getMessage());
    }

    @Test
    public void parseTest() {

        IntOption option = new IntOption("test");

        CommandLineException e = assertThrows(CommandLineException.class, () -> option.parse(null));
        assertEquals("Integer expected for test, but: 'null', was given!", e.getMessage());
    }

    @Test
    public void parseTest_2() throws CommandLineException {

        IntOption option = new IntOption("test");
        CommandLineException e = assertThrows(CommandLineException.class, () -> option.parse("a"));
        assertEquals("Integer expected for test, but: 'a', was given!", e.getMessage());
    }
}