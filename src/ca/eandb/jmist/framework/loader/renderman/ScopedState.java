/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Brad
 *
 */
class ScopedState implements State {
	
	private static final class Key {
		
		public final RtToken name;
		public final RtToken parameter;
		
		public Key(RtToken name, RtToken parameter) {
			if (name == null) {
				throw new IllegalArgumentException("name must be non-null");
			}
			if (parameter == null) {
				throw new IllegalArgumentException("parameter must be non-null");
			}
			this.name = name;
			this.parameter = parameter;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return name.hashCode() ^ parameter.hashCode();
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Key) {
				Key key = (Key) obj;
				return (name == key.name && parameter == key.parameter);
			}
			return false;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#clone()
		 */
		@Override
		protected Object clone() {
			return this;
		}
		
	};
	
	private final ScopedState outer;

	private final Map<Key, Object> parameters = new HashMap<Key, Object>();
	
	public ScopedState() {
		this.outer = null;
	}
	
	private ScopedState(ScopedState outer) {
		this.outer = outer;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public ScopedState clone() {
		return new ScopedState(this);
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.State#get(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken)
	 */
	@Override
	public Object get(RtToken name, RtToken param) {
		Key key = new Key(name, param);
		for (ScopedState scope = this; scope != null; scope = scope.outer) {
			Object value = scope.parameters.get(key);
			if (value != null) {
				return value;
			}
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.loader.renderman.State#set(ca.eandb.jmist.framework.loader.renderman.RtToken, ca.eandb.jmist.framework.loader.renderman.RtToken, java.lang.Object)
	 */
	@Override
	public void set(RtToken name, RtToken param, Object value) {
		Key key = new Key(name, param);
		parameters.put(key, value);
	}
	
}
