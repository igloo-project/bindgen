package org.bindgen.processor.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nándor Előd Fekete
 */
public class ConfUtilTest {

	@Test
	public void testPathsToRoot() {
		String testPath = "a/b/dummy.txt";
		final List<String> result = ConfUtil.pathsToRoot(testPath)
			.collect(Collectors.toList());
		Assert.assertEquals(Arrays.asList("a/b/dummy.txt", "a/b", "a"), result);
	}

	@Test
	public void testPathsToRootWithLeadingSlash() {
		String testPath = "/a/b/dummy.txt";
		final List<String> result = ConfUtil.pathsToRoot(testPath)
			.collect(Collectors.toList());
		Assert.assertEquals(Arrays.asList("/a/b/dummy.txt", "/a/b", "/a"), result);
	}

}
