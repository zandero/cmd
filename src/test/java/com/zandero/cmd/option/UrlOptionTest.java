package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 */
public class UrlOptionTest {

    @Test
    public void urlOptionTest() throws CommandLineException {

        UrlOption option = new UrlOption("url");

        URI uri = option.parse("http://some.uri.com/");
        assertEquals("http", uri.getScheme());
        assertEquals("some.uri.com", uri.getHost());
        assertEquals("/", uri.getPath());
        assertEquals(-1, uri.getPort());

        uri = option.parse("http://some.uri.com:8080/to/be/tested");
        assertEquals("http", uri.getScheme());
        assertEquals("some.uri.com", uri.getHost());
        assertEquals("/to/be/tested", uri.getPath());
        assertEquals(8080, uri.getPort());
    }

    @Test
    public void invalidUrlOptionTest() throws CommandLineException {

        UrlOption option = new UrlOption("url");
        CommandLineException e = assertThrows(CommandLineException.class, () -> option.parse("//some.uri.com/"));
        assertEquals("URL expected for url, but: '//some.uri.com/', was given!", e.getMessage());
    }
}