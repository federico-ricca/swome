package org.swome.impl.java;

import java.util.ArrayList;
import java.util.List;

public class TypeDeclaration {
	private List<ClassReference> types;

	public TypeDeclaration(List<ClassReference> types) {
		this.setTypes(types);
	}

	public TypeDeclaration(ClassReference _classReference) {
		List<ClassReference> _types = new ArrayList<ClassReference>();
		_types.add(_classReference);

		this.setTypes(_types);
	}

	public List<ClassReference> getTypes() {
		return types;
	}

	public void setTypes(List<ClassReference> types) {
		this.types = types;
	}

}
