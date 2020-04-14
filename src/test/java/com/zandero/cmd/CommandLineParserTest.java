package com.zandero.cmd;

import com.zandero.cmd.option.BoolOption;
import com.zandero.cmd.option.CommandOption;
import com.zandero.cmd.option.IntOption;
import com.zandero.cmd.option.StringOption;
import com.zandero.settings.Settings;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class CommandLineParserTest {

    @Test
    public void bool() throws CommandLineException {

        // build commands
        CommandOption<?> option = new BoolOption("a").longCommand("all");

        // create builder
        CommandBuilder builder = new CommandBuilder();
        builder.add(option);

        // create parser
        CommandLineParser parser = new CommandLineParser(builder);

        // 0. empty parse
        Settings output = parser.parse(null);
        assertFalse(output.getBool("a"));

        // 1. parse (short option)
        String[] args = new String[]{"-a"};
        output = parser.parse(args);

        assertTrue(output.getBool("a"));

        // 2. parse (long option)
        args = new String[]{"--all"};
        output = parser.parse(args);

        assertTrue(output.getBool("a"));

        // nothing given ... should return default
        output = parser.parse(null);
        assertFalse(output.getBool("a"));

        // 3. parse with arguments given
        args = new String[]{"--all", "false"};
        output = parser.parse(args);

        assertFalse(output.getBool("a"));
    }

    @Test
    public void invertBoolParse() throws CommandLineException {

        // build commands
        CommandOption<?> option = new BoolOption("a")
                                      .invert()
                                      .longCommand("all");

        // create builder
        CommandBuilder builder = new CommandBuilder();
        builder.add(option);

        // create parser
        CommandLineParser parser = new CommandLineParser(builder);

        // 0. empty parse (default to true)
        Settings output = parser.parse(null);
        assertTrue(output.getBool("a"));

        // 1. parse (short option)
        String[] args = new String[]{"-a"};
        output = parser.parse(args);

        assertTrue(output.getBool("a"));

        // 2. parse (short option)
        args = new String[]{"-a", "on"};
        output = parser.parse(args);

        assertTrue(output.getBool("a"));

        // 3. parse (short option)
        args = new String[]{"-a", "off"};
        output = parser.parse(args);

        assertFalse(output.getBool("a"));
    }

    @Test
    public void parseVariousOptions() throws CommandLineException {


        // build commands
        CommandOption<?> all = new BoolOption("a")
                                   .invert()
                                   .longCommand("all");

        CommandOption<?> file = new StringOption("f")
                                    .longCommand("file");

        CommandOption<?> size = new IntOption("s")
                                    .longCommand("size");

        // create builder
        CommandBuilder builder = new CommandBuilder();
        builder.add(all);
        builder.add(file);
        builder.add(size);

        // create parser
        CommandLineParser parser = new CommandLineParser(builder);

        String[] args = new String[]{"-a", "false", "--size", "100", "-f", "some.file"};
        Settings out = parser.parse(args);

        assertEquals(3, out.size());
        assertEquals(false, out.get("a"));
        assertEquals(100, out.get("s"));
        assertEquals("some.file", out.get("f"));
    }

    @Test
    public void commandLineParserFromOptions() throws CommandLineException {

        CommandOption<?> option = new BoolOption("a").longCommand("all");
        List<CommandOption<?>> list = new ArrayList<>();
        list.add(option);

        // create parser
        CommandLineParser parser = new CommandLineParser(list);

        Settings output = parser.parse(null);
        assertFalse(output.getBool("a"));

        output = parser.parse(new String[]{"-a"});
        assertTrue(output.getBool("a"));
    }

    @Test
    public void setDefaults() throws CommandLineException {

        CommandOption<?> option = new BoolOption("a")
                                      .longCommand("all");

        CommandBuilder builder = new CommandBuilder();
        builder.add(option);

        Settings settings = new Settings();
        settings.put("a", true);

        CommandLineParser parser = new CommandLineParser(builder);
        parser.setDefaults(settings);

        Settings out = parser.parse(null);
        assertTrue(out.getBool("a"));
    }

    @Test
    public void setDefaultsSettings() throws CommandLineException {

        CommandOption<?> option = new BoolOption("a")
                                      .longCommand("all")
                                      .setting("ALL");

        CommandBuilder builder = new CommandBuilder();
        builder.add(option);

        // 1st will this time not found as we have a setting defined
        Settings settings = new Settings();
        settings.put("a", true);

        CommandLineParser parser = new CommandLineParser(builder);
        parser.setDefaults(settings);

        Settings out = parser.parse(null);
        assertFalse(out.getBool("ALL"));

        // 2nd will find by setting name
        settings.put("ALL", true);
        parser.setDefaults(settings);

        out = parser.parse(null);
        assertTrue(out.getBool("ALL"));
    }

    @Test
    public void missingRequiredOption() {

        CommandOption<String> option = new StringOption("a")
                                            .longCommand("all")
                                            .required()
                                            .setting("ALL");

        CommandBuilder builder = new CommandBuilder();
        builder.add(option);

		CommandLineParser parser = new CommandLineParser(builder);

        CommandLineException e = assertThrows(CommandLineException.class, () -> parser.parse(null));
        assertEquals("Missing required: -a [ --all ]", e.getMessage());
    }
}