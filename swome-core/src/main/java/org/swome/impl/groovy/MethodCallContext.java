package org.swome.impl.groovy;

import java.util.ArrayList;
import java.util.Collection;

public class MethodCallContext {
	private Collection<MethodCallReference> methodCallReferences = new ArrayList<MethodCallReference>();

	public final void addMethodCallReference(
			MethodCallReference _methodCallReference) {
		methodCallReferences.add(_methodCallReference);
	}

	public final Collection<MethodCallReference> getMethodCallReferences() {
		return methodCallReferences;
	}

}
