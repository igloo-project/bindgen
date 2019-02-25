package org.bindgen.processor.util;

import org.junit.Assert;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nándor Előd Fekete
 */
public class ConfUtilTest {

	@Test
	public void testRelativePathParents() {
		final Path testPath = Paths.get("a", "b", "dummy.txt");
		final List<Path> result = ConfUtil.pathWithParents(testPath).collect(Collectors.toList());
		Assert.assertEquals(
			Arrays.asList(
				testPath,
				testPath.getParent(),
				testPath.getParent().getParent()
			),
			result
		);
	}

	@Test
	public void testAbsolutePathParents() {
		final Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
		/*
		 * The test is going to fail if the security manager denies access to
		 * all filesystem roots or there are no filesystem roots in the running
		 * system (not sure if that's possible).
		 */
		final Path rootDirectory = rootDirectories.iterator().next();
		final Path testPath = rootDirectory.resolve(Paths.get("a", "b", "dummy.txt"));
		final List<Path> result = ConfUtil.pathWithParents(testPath).collect(Collectors.toList());
		Assert.assertEquals(
			Arrays.asList(
				rootDirectory.resolve(Paths.get("a", "b", "dummy.txt")),
				rootDirectory.resolve(Paths.get("a", "b")),
				rootDirectory.resolve(Paths.get("a")),
				rootDirectory
			),
			result
		);
	}

}
