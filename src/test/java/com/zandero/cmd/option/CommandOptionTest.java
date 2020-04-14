package com.zandero.cmd.option;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class CommandOptionTest {

    @Test
    public void isShortOptionTest() {

        CommandOption<Void> option = new VoidOption("s").longCommand("long");
        assertTrue(option.isShort("-s"));
        assertTrue(option.isShort("s"));

        assertFalse(option.isShort("--s"));
        assertFalse(option.isShort(" "));
        assertFalse(option.isShort(null));
    }

    @Test
    public void isLongOptionTest() {

        CommandOption<Void> option = new VoidOption("s").longCommand("long");
        assertTrue(option.isLong("--long"));
        assertTrue(option.isLong("long"));

        assertFalse(option.isLong("-long"));

        assertFalse(option.isLong(" "));
        assertFalse(option.isLong(null));
    }

    @Test
    public void isOptionTest() {

        CommandOption<Void> option = new VoidOption("s").longCommand("long").setting("LONG");

        CommandOption<Void> other = new VoidOption("s").longCommand("other");
        CommandOption<Void> other2 = new VoidOption("sh").longCommand("long");
        CommandOption<Void> other3 = new VoidOption("sh").longCommand("longer").setting("LONG");

        CommandOption<Void> compare = new VoidOption("short").longCommand("longer").setting("LONGER");

        assertTrue(option.is(other));
        assertTrue(option.is(other2));
        assertTrue(option.is(other3));

        assertFalse(option.is(compare));
        assertFalse(option.is(null));
    }

    @Test
    public void setDefaultTest() {

        CommandOption<String> option = new StringOption("v");
        option.defaultsTo("helo");

        assertEquals("helo", option.getDefault());

        option.defaultsTo(" helo ");
        assertEquals(" helo ", option.getDefault());

        option.defaultsTo(null);
        assertNull(option.getDefault());
    }

    @Test
    public void invalidDefaultTest() {

        CommandOption<String> option = new StringOption("v");
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> option.defaultsTo(1));
        assertEquals("Expected default setting of type: java.lang.String, but was provided: java.lang.Integer", e.getMessage());
    }

    @Test
    public void toStringTest() {

        CommandOption<String> option = new StringOption("v");
        assertEquals("-v", option.toString());

        option.longCommand("valid");
        assertEquals("-v [ --valid ]", option.toString());

        option.description("This is a test option");
        assertEquals("-v [ --valid ] This is a test option", option.toString());
    }
}