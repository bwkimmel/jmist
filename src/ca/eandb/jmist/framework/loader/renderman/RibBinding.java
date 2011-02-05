/**
 * 
 */
package ca.eandb.jmist.framework.loader.renderman;

/**
 * @author Brad
 *
 */
interface RibBinding {

	void processBinding(RibTokenizer tokenizer, RenderManContext context) throws RibException;
	
}
