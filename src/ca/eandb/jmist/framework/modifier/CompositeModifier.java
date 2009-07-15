/*
 * Copyright (c) 2008 Bradley W. Kimmel
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.eandb.jmist.framework.modifier;

import java.util.ArrayList;
import java.util.Collection;

import ca.eandb.jmist.framework.Modifier;
import ca.eandb.jmist.framework.ShadingContext;

/**
 * @author brad
 *
 */
public final class CompositeModifier implements Modifier {

	/**
	 * Serialization version ID.
	 */
	private static final long serialVersionUID = 3443065225852391116L;

	private final Collection<Modifier> modifiers;

	/**
	 * @param modifiers
	 */
	public CompositeModifier(Collection<Modifier> modifiers) {
		this.modifiers = modifiers;
	}

	public CompositeModifier() {
		this(new ArrayList<Modifier>());
	}

	/* (non-Javadoc)
	 * @see ca.eandb.jmist.framework.Modifier#modify(ca.eandb.jmist.framework.ShadingContext)
	 */
	public void modify(ShadingContext context) {
		for (Modifier modifier : modifiers) {
			modifier.modify(context);
		}
	}

	public final CompositeModifier addModifier(Modifier modifier) {
		modifiers.add(modifier);
		return this;
	}

}
