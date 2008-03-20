/**
 *
 */
package org.selfip.bkimmel.jnlp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;

import javax.jnlp.FileContents;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;

import org.selfip.bkimmel.util.classloader.ClassLoaderStrategy;

/**
 * @author brad
 *
 */
public class PersistenceCacheClassLoaderStrategy implements ClassLoaderStrategy {

	private final URL codebase;
	private final PersistenceService service;
	private final ClassLoaderStrategy fallback;

	public PersistenceCacheClassLoaderStrategy(URL codebase, ClassLoaderStrategy fallback) throws UnavailableServiceException {
		this(codebase, (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService"), fallback);
	}

	public PersistenceCacheClassLoaderStrategy(URL codebase, PersistenceService service, ClassLoaderStrategy fallback) {
		this.codebase = codebase;
		this.service = service;
		this.fallback = fallback;
	}

	/* (non-Javadoc)
	 * @see org.selfip.bkimmel.util.classloader.ClassLoaderStrategy#getClassDefinition(java.lang.String)
	 */
	public ByteBuffer getClassDefinition(String name) {

		ByteBuffer def;
		URL classUrl = null;
		FileContents contents;
		String classPath = name.replaceAll("\\$.*$", "").replace('.', '/') + ".class";

		try {

			try {

				classUrl = new URL(codebase, classPath);
				contents = service.get(classUrl);

				InputStream stream = contents.getInputStream();
				byte[] data = new byte[(int) contents.getLength()];

				stream.read(data);

				return ByteBuffer.wrap(data);

			} catch (FileNotFoundException e) {

				assert(classUrl != null);

				def = fallback.getClassDefinition(name);

				if (def != null) {

					def.rewind();

					if (service.create(classUrl, def.remaining()) == def.remaining()) {
						contents = service.get(classUrl);
						OutputStream stream = contents.getOutputStream(true);
						byte[] data = new byte[def.remaining()];
						def.get(data);
						stream.write(data);
						stream.flush();
						stream.close();
					}

				}

				return def;

			}

		} catch (MalformedURLException e) {

			e.printStackTrace();
			throw new IllegalArgumentException("Invalid binary class name.");

		} catch (IOException e) {

			e.printStackTrace();

		}

		return null;

	}

}
