package com.zandero.cmd.option;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class MultiArgOptionTest {

	@Test
	public void multiOptionTest() {

		CommandOption option = new MultiArgOption("m")
			.longCommand("multi");

		assertEquals("java.util.List<java.lang.String>", option.getType().getTypeName());
		assertTrue(option.hasArguments());

		List<String> defaults = Arrays.asList("a", "b", "c");
		option.defautlTo(defaults);

		Object compare = option.getDefault();
		assertTrue(compare instanceof List);
		List<String> items = (List<String>) compare;
		assertEquals(3, items.size());
		assertEquals("a", items.get(0));
		assertEquals("b", items.get(1));
		assertEquals("c", items.get(2));
	}
}