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
package org.swome.impl.java.groovyparser;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.swome.core.Artefact;
import org.swome.impl.java.ClassReference;
import org.swome.impl.java.JavaLangDef;
import org.swome.impl.java.MethodCallContext;
import org.swome.impl.java.MethodDefinition;
import org.swome.impl.java.TypeDeclaration;
import org.swome.util.ClassNameUtils;

public class GroovyClassArtefact extends Artefact {
	private static Map<String, String> javaLangImports = JavaLangDef
			.getImports();
	private String className;
	private String packageName;
	private String superClassName;
	private String superClassPackageName;
	private Set<TypeDeclaration> referencedTypeDeclarations = new HashSet<TypeDeclaration>();
	private Map<String, TypeDeclaration> classMemberTypes = new HashMap<String, TypeDeclaration>();
	private Map<String, ClassReference> imports = new HashMap<String, ClassReference>();
	private Map<String, MethodDefinition> methods = new HashMap<String, MethodDefinition>();
	private MethodCallContext methodCallContext;

	public GroovyClassArtefact() {
		this.setMethodCallContext(new MethodCallContext());
	}

	@Override
	public final String getId() {
		if (packageName != null) {
			return packageName + "." + className;
		}

		return className;
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

	public final void addImport(ClassReference _import) {
		imports.put(_import.getClassName(), _import);
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

	public final String fqcnByClassName(String _className) {
		// lookup java.lang package
		String _id = javaLangImports.get(_className);

		if (_id == null) {
			ClassReference _classReference = imports.get(_className);

			if (_classReference != null) {
				_id = _classReference.getFQCN();
			}
		}

		return _id;
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

	public final void addMember(String _memberName,
			TypeDeclaration _typeDeclaration) {
		classMemberTypes.put(_memberName, _typeDeclaration);
		referencedTypeDeclarations.add(_typeDeclaration);
	}

	public final TypeDeclaration getMemberType(String _varName) {
		return classMemberTypes.get(_varName);
	}

	public final String resolveClassName(String _identifier) {
		String _fqcn = null;

		if (ClassReference.classNameContainsPackage(_identifier)) {
			// fqcn
			_fqcn = _identifier;
		} else if (ClassReference.isClassName(_identifier)) {
			_fqcn = this.fqcnByClassName(_identifier);
		}

		return _fqcn;
	}

	/*
	 * public final String resolveIdentifierClassname(String _identifier) { if
	 * (ClassNameUtils.containsPackage(_identifier)) { // fqcn return
	 * _identifier; } else if (ClassNameUtils.isClassName(_identifier)) { String
	 * _fqcn = this.findImportByClass(_identifier);
	 * 
	 * return _fqcn; }
	 * 
	 * return this.getMemberType(_identifier).get; }
	 */
	public ClassReference resolveClassReference(String _className) {
		ClassReference _classReference = null;

		if (ClassReference.classNameContainsPackage(_className)) {
			_classReference = ClassReference.fromFQCN(_className);
		} else {
			String _fqcn = this.fqcnByClassName(_className);

			if (_fqcn != null) {
				_classReference = ClassReference.fromFQCN(_fqcn);
			} else {
				_classReference = ClassReference.unresolved(_className);
				_classReference.setReferencedFrom(this.getPackageName());
			}
		}

		return _classReference;
	}

	public Set<Entry<String, TypeDeclaration>> getMembers() {
		return classMemberTypes.entrySet();
	}

	public Collection<MethodDefinition> getMethods() {
		return methods.values();
	}

	public MethodDefinition getMethodBySignature(String _methodSignature) {
		return methods.get(_methodSignature);
	}

	public void addMethod(MethodDefinition _methodDef) {
		methods.put(_methodDef.getSignature().toString(), _methodDef);
	}

	public MethodCallContext getMethodCallContext() {
		return methodCallContext;
	}

	public void setMethodCallContext(MethodCallContext methodCallContext) {
		this.methodCallContext = methodCallContext;
	}
}
