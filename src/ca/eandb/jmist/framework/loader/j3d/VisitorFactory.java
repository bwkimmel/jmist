/**
 * 
 */
package ca.eandb.jmist.framework.loader.j3d;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.j3d.SceneGraphObject;

import ca.eandb.jmist.framework.SceneElement;
import ca.eandb.util.UnexpectedException;

/**
 * @author Brad
 *
 */
final class VisitorFactory {
	
	private static final Logger logger = Logger.getLogger(VisitorFactory.class.getName());
	
	private static final String PACKAGE_NAME = VisitorFactory.class.getPackage().getName();
	private static final String SUFFIX = "Visitor";

	static Visitor createVisitor(SceneGraphObject obj) {
		Class<?> clazz = obj.getClass();
		for (clazz = obj.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
			String targetClassName = clazz.getSimpleName();
			String visitorClassName = String.format("%s.%s%s", PACKAGE_NAME, targetClassName, SUFFIX);

			try {
				Class<?> visitorClass = Class.forName(visitorClassName);
				return (Visitor) visitorClass.newInstance();
			} catch (ClassNotFoundException e) {
				/* try superclass */
			} catch (InstantiationException e) {
				logger.log(Level.SEVERE, "Visitor implementation must have public default constructor.", e);
				throw new UnexpectedException(e);
			} catch (IllegalAccessException e) {
				/* This should not happen. */
				logger.log(Level.SEVERE, "Visitor implementation must have public default constructor.", e);
				throw new UnexpectedException(e);
			}
		}
		
		return DefaultVisitor.INSTANCE;
	}
	
	static SceneElement createSceneElement(SceneGraphObject obj) {
		Visitor v = createVisitor(obj);
		return v.createSceneElement(obj);
	}
	
}
