package com.mgmtp.jfunk.application.runner;

import com.beust.jcommander.internal.Maps;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.text.StrBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map.Entry;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Arrays.asList;
import static org.apache.commons.io.FilenameUtils.removeExtension;

/**
 * @author rnaegele
 */
public enum TestType {
	GROOVY {
		@Override
		public CommandLine createCommandLine(Path path, final String script, final TestParameters testParams) {
			try {
				Path scriptPath = path.resolve(script);

				CommandLine cmdl = new CommandLine("java");
				for (Entry<String, String> entry : testParams.getTestProps().entrySet()) {
					cmdl.addArgument("-D" + entry.getKey() + '=' + entry.getValue());
				}
				cmdl.addArgument("-Djfunk.props.file=" + testParams.getjFunkProps());
				cmdl.addArgument("-cp");

				StrBuilder sb = new StrBuilder();
				for (String dir : asList("classes", "test-classes")) {
					sb.append(File.pathSeparatorChar);
					sb.append("target");
					sb.append(File.separatorChar);
					sb.append(dir);
				}

				cmdl.addArgument(Files.toString(new File("target/classpath.txt"), UTF_8) + sb.toString());
				cmdl.addArgument("com.mgmtp.jfunk.core.JFunk");
				cmdl.addArgument(scriptPath.toString());
				for (String arg : testParams.getCommandLineArgs()) {
					cmdl.addArgument(arg);
				}

				return cmdl;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	},
	TESTNG {
		@Override
		public CommandLine createCommandLine(final Path path, final String method, final TestParameters testParams) {
			try {
				String test = removeExtension(path.toString()).replaceAll("[/\\\\]", ".");
				String xml = Resources.toString(Resources.getResource(getClass(), "testng_single_class.xml"), UTF_8);
				xml = xml.replace("${class}", test);
				File suiteFile = File.createTempFile("jfunk_testng", ".xml", new File("target"));
				Files.write(xml, suiteFile, UTF_8);

				CommandLine cmdl = new CommandLine("java");
				for (Entry<String, String> entry : testParams.getTestProps().entrySet()) {
					cmdl.addArgument("-D" + entry.getKey() + '=' + entry.getValue());
				}
				cmdl.addArgument("-Djfunk.props.file=" + testParams.getjFunkProps());
				cmdl.addArgument("-cp");

				StrBuilder sb = new StrBuilder();
				for (String dir : asList("classes", "test-classes")) {
					sb.append(File.pathSeparatorChar);
					sb.append(new File("target", dir).getAbsolutePath());
				}

				cmdl.addArgument(Files.toString(new File("target/classpath.txt"), UTF_8) + sb.toString());
				cmdl.addArgument("org.testng.TestNG");
				cmdl.addArgument("-d");
				cmdl.addArgument("${outputDir}");
				cmdl.addArgument("${suiteFile}");
				if (method != null) {
					cmdl.addArgument("-methods");
					cmdl.addArgument(test += '.' + method);
				}

				cmdl.setSubstitutionMap(Maps.newHashMap("suiteFile", suiteFile, "outputDir", new File("target/test-output")));
				return cmdl;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	},
	JUNIT {
		@Override
		public CommandLine createCommandLine(final Path path, final String method, final TestParameters testParams) {
			try {
				String test = removeExtension(path.toString()).replaceAll("[/\\\\]", ".");

				CommandLine cmdl = new CommandLine("java");
				for (Entry<String, String> entry : testParams.getTestProps().entrySet()) {
					cmdl.addArgument("-D" + entry.getKey() + '=' + entry.getValue());
				}
				cmdl.addArgument("-Djfunk.props.file=" + testParams.getjFunkProps());
				cmdl.addArgument("-cp");

				StrBuilder sb = new StrBuilder();
				for (String dir : asList("classes", "test-classes")) {
					sb.append(File.pathSeparatorChar);
					sb.append(new File("target", dir).getAbsolutePath());
				}


				cmdl.addArgument(Files.toString(new File("target/classpath.txt"), UTF_8) + sb.toString());

				cmdl.addArgument("com.mgmtp.jfunk.application.runner.util.JUnitRunner");
				cmdl.addArgument(test);
				if (method != null) {
					cmdl.addArgument(method);
				}
				return cmdl;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	};

	public abstract CommandLine createCommandLine(final Path path, final String methodOrScript, final TestParameters testParams);
}