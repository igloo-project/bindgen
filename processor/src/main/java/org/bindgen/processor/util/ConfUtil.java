package org.bindgen.processor.util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.JavaFileManager.Location;
import javax.tools.StandardLocation;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

public class ConfUtil {

	/** Attempts to load {@code fileName} and return its properties. */
	public static Map<String, String> loadProperties(ProcessingEnvironment env, String fileName) {
		Map<String, String> properties = new LinkedHashMap<String, String>();

		// Eclipse, ant, and maven all act a little differently here, so try both source and class output
		InputStream inputStream = null;
		for (Location location : new Location[] { StandardLocation.SOURCE_OUTPUT, StandardLocation.CLASS_OUTPUT }) {
			inputStream = resolveBindgenPropertiesIfExists(location, env, fileName);
			if (inputStream != null) {
				break;
			}
		}

		if (inputStream != null) {
			Properties p = new Properties();
			try (InputStream is = inputStream) {
				p.load(is);
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (Map.Entry<Object, Object> entry : p.entrySet()) {
				properties.put((String) entry.getKey(), (String) entry.getValue());
			}
		}

		return properties;
	}

	/** Finds a file by starting by <code>location</code> and walkig up.
	 *
	 * This uses a heuristic because in Eclipse we will not know what our
	 * working directory is (it is wherever Eclipse was started from), so
	 * project/workspace-relative paths will not work.
	 *
	 * As far as passing in a the properties location as a {@code -Afile=path}
	 * setting, Eclipse also lacks any {@code ${basepath}}-type interpolation
	 * in its APT key/value pairs (like Ant would be able to do). So only fixed
	 * values are accepted, meaning an absolute path, which would be too tied
	 * to any one developer's particular machine.
	 *
	 * The one thing the APT API gives us is the CLASS_OUTPUT (e.g. bin/apt).
	 * So we start there and walk up parent directories looking for
	 * {@code bindgen.properties} files.
	 */
	private static InputStream resolveBindgenPropertiesIfExists(Location location, ProcessingEnvironment env, String fileName) {
		final FileObject fileObject;
		try {
			fileObject = env.getFiler().getResource(location, "", fileName);
		} catch (IOException e1) {
			return null;
		}

		final Path path = Paths.get(fileObject.toUri());
		final Optional<InputStream> bindingPropertiesInAncestorPaths = pathWithParents(path)
			//skip the path that still contains the filename - we're gonna append that to every parent path anyway
			.skip(1)
			.map(newPath -> {
				try {
					final Path resolve = newPath.resolve(fileName);
					InputStream inputStream = resolve.toUri().toURL().openStream();
					return Optional.of(inputStream);
				} catch (IOException e) {
					return Optional.<InputStream> empty();
				}
			})
			.filter(Optional::isPresent)
			.map(Optional::get)
			.findFirst();

		return bindingPropertiesInAncestorPaths.orElseGet(() -> {
			// Before giving up, try just grabbing it from the current directory
			File possible = new File(fileName);
			if (possible.exists()) {
				try {
					return new FileInputStream(possible);
				} catch (FileNotFoundException e) {
					return null;
				}
			}
			// No file found
			return null;
		});

	}

    /**
     * <p>
     *     Returns a {@link Stream} of {@link Path}s containing the given path and all it's parent paths.
     *     If the path is absolute path, it will return all paths starting with the current path,
     *     the current path's parent, etc., with the last element of the stream being the filesystem root.
     * </p>
     * <p>
     *     If this is a relative path, it will return all paths starting with the current path,
     *     the current path's parent, etc., up to the topmost path component of the the given path.
     * </p>
     *
     * @param path a {@link Path}
     * @return a {@link Stream} of {@link Path} objects corresponding to the above
     */
    static Stream<Path> pathWithParents(Path path) {
		final Stream.Builder<Path> builder = Stream.builder();
		while (path != null) {
			builder.add(path);
			path = path.getParent();
		}
		return builder.build();
	}

}
