package ca.eandb.jmist.framework.loader.renderman;

interface State extends Cloneable {
	
	State clone();

	Object get(RtToken name, RtToken param);

	void set(RtToken name, RtToken param, Object value);

}