/*************************************************************************** 
   Copyright 2015 Federico Ricca

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 ***************************************************************************/
package org.structome.impl.groovy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.structome.core.Artefact;
import org.structome.util.ClassNameUtils;

public class GroovyClassArtefact implements Artefact {
	private String className;
	private String packageName;
	private String superClassName;
	private String superClassPackageName;
	private Map<String, String> classMemberTypes = new HashMap<String, String>();
	private Collection<MethodCallReference> methodCallReferences = new ArrayList<MethodCallReference>();
	private Map<String, String> imports = new HashMap<String, String>();

	@Override
	public final String getId() {
		return packageName + "." + className;
	}

	public final void setClassName(String _nameWithoutPackage) {
		className = _nameWithoutPackage;
	}

	public final void addGenerics(String nameWithoutPackage) {
		// TODO add class generic
	}

	public final void setSuperClassPackageName(final String _pkgName) {
		if (_pkgName.startsWith("java.lang")) {
			superClassName = null;
			superClassPackageName = null;

			return;
		}

		superClassPackageName = _pkgName;
	}

	public final void setSuperClassName(String _superClassName) {
		if (_superClassName == null || _superClassName.startsWith("java.lang")) {
			superClassName = null;
			superClassPackageName = null;

			return;
		}

		String[] _name = ClassNameUtils.split(_superClassName);

		this.setSuperClassPackageName(_name[0]);
		superClassName = _name[1];
	}

	public final void addSuperClassGenerics(String nameWithoutPackage) {
		// TODO add super class generics

	}

	public final void addImport(String _import) {
		final int _iIndex = _import.lastIndexOf(".");

		if (_iIndex == -1) {
			return;
		}

		final String _pkgName = _import.substring(0, _iIndex);
		final String _className = _import.substring(_iIndex + 1, _import.length());

		imports.put(_className, _pkgName + "." + _className);
	}

	public final void addAnnotation(String _annotation) {
		// TODO add annotation

	}

	public final void setPackageName(String name) {
		packageName = name;
	}

	public final String getSuperClassName() {
		return superClassName;
	}

	public final String getSuperClassPackageName() {
		return superClassPackageName;
	}

	public final String getPackageName() {
		return packageName;
	}

	public final String findImportByClass(String _className) {
		return imports.get(_className);
	}

	public final String getSuperClassId() {
		if (this.getSuperClassName() == null) {
			return null;
		}

		return this.getSuperClassPackageName() + "." + this.getSuperClassName();
	}

	@Override
	public String toString() {
		return this.getId();
	}

	public final void addMethodCallReference(MethodCallReference _methodCallReference) {
		methodCallReferences.add(_methodCallReference);
	}

	public final Collection<MethodCallReference> getMethodCallReferences() {
		return methodCallReferences;
	}

	public final void addMemberClassName(String _memberName, String _className) {
		classMemberTypes.put(_memberName, _className);
	}

	public final String getMemberClassName(String _varName) {
		return classMemberTypes.get(_varName);
	}

	public final String resolveIdentifierClassName(String _identifier) {
		if (ClassNameUtils.containsPackage(_identifier)) {
			// fqcn
			return _identifier;
		} else if (ClassNameUtils.isClassName(_identifier)) {
			String _fqcn = this.findImportByClass(_identifier);

			return _fqcn;
		}

		return this.getMemberClassName(_identifier);
	}
}
