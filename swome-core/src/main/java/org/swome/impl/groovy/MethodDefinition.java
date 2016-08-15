package org.swome.impl.groovy;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MethodDefinition {
	private MethodSignature signature;
	private Map<String, TypeDeclaration> parameterTypes = new HashMap<String, TypeDeclaration>();
	private MethodCallContext methodCallContext;

	public MethodDefinition(MethodSignature _signature) {
		this.setSignature(_signature);
		this.setMethodCallContext(new MethodCallContext());
	}

	public Set<Entry<String, TypeDeclaration>> getParameters() {
		return parameterTypes.entrySet();
	}

	public void addParameter(String _paramName, TypeDeclaration _typeDeclaration) {
		parameterTypes.put(_paramName, _typeDeclaration);
	}

	public MethodCallContext getMethodCallContext() {
		return methodCallContext;
	}

	public void setMethodCallContext(MethodCallContext methodCallContext) {
		this.methodCallContext = methodCallContext;
	}

	public MethodSignature getSignature() {
		return signature;
	}

	public void setSignature(MethodSignature signature) {
		this.signature = signature;
	}

}
