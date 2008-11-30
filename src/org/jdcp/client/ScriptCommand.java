/**
 *
 */
package org.jdcp.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Queue;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.selfip.bkimmel.util.args.AbstractCommand;
import org.selfip.bkimmel.util.args.ArgumentProcessor;
import org.selfip.bkimmel.util.args.Command;
import org.selfip.bkimmel.util.args.FileFieldOption;
import org.selfip.bkimmel.util.args.StringFieldOption;

/**
 * @author brad
 *
 */
public final class ScriptCommand implements Command<Configuration> {

	private static final String DEFAULT_LANGUAGE = "JavaScript";

	public static class Options {

		public File file;

		public String language = null;

	}

	public void process(Queue<String> argq, final Configuration conf) {

		ArgumentProcessor<Options> argProcessor = new ArgumentProcessor<Options>();

		argProcessor.addOption("file", 'f', new FileFieldOption<Options>("file", true));
		argProcessor.addOption("language", 'l', new StringFieldOption<Options>("language"));

		argProcessor.setDefaultCommand(new AbstractCommand<Options>() {

			@Override
			protected void run(String[] args, Options options) {

				try {
					ScriptEngineManager factory = new ScriptEngineManager();
					ScriptEngine engine = getScriptEngine(factory, options);
					if (engine == null) {
						System.err.println("Unrecognized language");
						System.exit(1);
					}
					InputStream in = (options.file != null)
							? new FileInputStream(options.file)
							: System.in;
					Reader reader = new InputStreamReader(in);

					engine.put("jdcp", new ScriptFacade(conf));
					engine.put("args", args);
					engine.eval(reader);
				} catch (ScriptException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		});

		argProcessor.process(argq, new Options());

	}

	private ScriptEngine getScriptEngine(ScriptEngineManager factory, Options options) {
		if (options.language != null) {
			return factory.getEngineByName(options.language);
		}
		if (options.file != null) {
			String fileName = options.file.getName();
			int separator = fileName.lastIndexOf('.');
			if (separator < 0) {
				String extension = fileName.substring(separator + 1);
				return factory.getEngineByExtension(extension);
			}
		}
		return factory.getEngineByName(DEFAULT_LANGUAGE);
	}

}
