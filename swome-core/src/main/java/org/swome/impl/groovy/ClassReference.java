package org.swome.impl.groovy;

public class ClassReference {
	private String packageName;
	private boolean isResolved;
	private String referencedFrom;
	private String className;

	public ClassReference() {
		this.setResolved(false);
	}

	public void resolveAs(String _packageName, String _className) {
		this.setResolved(true);
		this.setClassName(_className);
		this.setPackageName(_packageName);
	}

	public String getFQCN() {
		if (this.isResolved()) {
			return this.getPackageName() + "." + this.getClassName();
		}

		return null;
	}

	public boolean isResolved() {
		return isResolved;
	}

	public void setResolved(boolean isResolved) {
		this.isResolved = isResolved;
	}

	public String getReferencedFrom() {
		return referencedFrom;
	}

	public void setReferencedFrom(String referencedFrom) {
		this.referencedFrom = referencedFrom;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public static boolean classNameContainsPackage(String _className) {
		return _className.lastIndexOf('.') != -1;
	}

	public static boolean isClassName(String _varName) {
		return _varName.matches("[A-Z].*");
	}

	public static ClassReference resolved(String _packageName, String _className) {
		ClassReference _classReference = new ClassReference();
		_classReference.resolveAs(_packageName, _className);

		return _classReference;
	}

	public static ClassReference fromFQCN(final String _fqcn) {
		ClassReference _classReference = new ClassReference();

		int _i = _fqcn.lastIndexOf(".");

		String _packageName = "";
		String _className = _fqcn;

		if (_i != -1) {
			_packageName = _fqcn.substring(0, _i);
			_className = _fqcn.substring(_i + 1, _fqcn.length());
		}
		_classReference.resolveAs(_packageName, _className);

		return _classReference;
	}

	public static ClassReference unresolved(String _className) {
		ClassReference _classReference = new ClassReference();
		_classReference.setClassName(_className);

		return _classReference;
	}
}
