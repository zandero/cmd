package com.zandero.cmd;

import com.zandero.cmd.option.BoolOption;
import com.zandero.cmd.option.CommandOption;
import com.zandero.cmd.option.IntOption;
import com.zandero.cmd.option.StringOption;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
public class CommandBuilderTest {

    @Test
    public void getHelpTest() {

        // build commands
        CommandOption<?> all = new BoolOption("a")
                                   .invert()
                                   .longCommand("all")
                                   .description("show all files")
                                   .required()
                                   .help(Collections.singletonList("Will display all files."));

        CommandOption<?> file = new StringOption("f")
                                    .longCommand("files")
                                    .description("show single file")
                                    .help(Collections.singletonList("Will display a single file."));

        CommandOption<?> size = new IntOption("s")
                                    .description("display file sizes");

        // create builder
        CommandBuilder builder = new CommandBuilder();
        builder.add(all);
        builder.add(file);
        builder.add(size);

        builder.setHelp("App Version 1.0", "usage.jar -option");

        List<String> out = builder.getHelp();
        int i = 0;
        assertEquals("App Version 1.0", out.get(i++));
        assertEquals("usage.jar -option", out.get(i++));
        assertEquals("", out.get(i++));
        assertEquals("-a [ --all ]   show all files", out.get(i++));
        assertEquals("-f [ --files ] show single file", out.get(i++));
        assertEquals("-s             display file sizes", out.get(i));

        for (String command : Arrays.asList("a", "all", "-a", "--all", "--a")) {
            List<String> outHelp = builder.getHelpFor(command);
            assertEquals(3, outHelp.size());
            assertEquals("-a [ --all ]", outHelp.get(0));
            assertEquals("", outHelp.get(1));
            assertEquals("   Will display all files.", outHelp.get(2));
        }
    }
}