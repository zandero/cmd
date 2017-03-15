package com.zandero.cmd.option;

import com.zandero.cmd.CommandLineException;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

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

	@Test(expected = CommandLineException.class)
	public void invalidUrlOptionTest() throws CommandLineException {

		try {
			UrlOption option = new UrlOption("url");
			option.parse("//some.uri.com/");
		}
		catch (CommandLineException e) {
			assertEquals("URL expected for url, but: '//some.uri.com/', was given!", e.getMessage());
			throw e;
		}
	}
}