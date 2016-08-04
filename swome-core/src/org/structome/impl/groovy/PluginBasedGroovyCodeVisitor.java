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

import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.ImportNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.ast.PackageNode;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.AttributeExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.structome.util.ClassNameUtils;

public class PluginBasedGroovyCodeVisitor extends GroovyCodeVisitor {

	@Override
	public void visitClass(ClassNode node) {
		getGroovyClassArtefact().setClassName(node.getNameWithoutPackage());

		if (node.getGenericsTypes() != null) {
			for (GenericsType _generics : node.getGenericsTypes()) {
				getGroovyClassArtefact().addGenerics(_generics.getType().getNameWithoutPackage());
			}
		}

		super.visitClass(node);

		ClassNode _superType = node.getSuperClass();

		String _superClassName = getGroovyClassArtefact().resolveIdentifierClassName(_superType.getName());
		// String _superClassName = _superType.getName();
		//
		// if (_superClassName.indexOf(".") == -1) {
		// // no package information, infer from import statements
		// _superClassName =
		// getGroovyClassArtefact().findImportByClass(_superClassName);
		//
		// if (_superClassName == null) {
		// // super class does not appear on imports:
		// // the class is declared either in the current package or in the
		// java.lang package
		// _superClassName = getGroovyClassArtefact().getPackageName() + "." +
		// _superType.getName();
		// }
		// }
		getGroovyClassArtefact().setSuperClassName(_superClassName);

		if (_superType.getGenericsTypes() != null) {
			for (GenericsType _generics : _superType.getGenericsTypes()) {
				getGroovyClassArtefact().addSuperClassGenerics(_generics.getType().getNameWithoutPackage());
			}
		}
	}

	@Override
	public void visitImports(ModuleNode node) {
		for (ImportNode importNode : node.getImports()) {
			getGroovyClassArtefact().addImport(importNode.getType().getName());
		}
		super.visitImports(node);
	}

	@Override
	public void visitAnnotations(AnnotatedNode node) {
		for (AnnotationNode annotationNode : node.getAnnotations()) {
			getGroovyClassArtefact().addAnnotation(annotationNode.getText());
		}
		super.visitAnnotations(node);
	}

	@Override
	public void visitDeclarationExpression(DeclarationExpression expression) {
		ClassNode _type = expression.getType();
		/*
		 * groovyClassArtefact.add(_type.getNameWithoutPackage());
		 * 
		 * if (_type.getGenericsTypes() != null) { for (GenericsType _generics :
		 * _type.getGenericsTypes()) {
		 * groovyClassArtefact.add(_generics.getType().getNameWithoutPackage());
		 * } }
		 */
		super.visitDeclarationExpression(expression);
	}

	@Override
	public void visitConstructor(ConstructorNode node) {
		// TODO Auto-generated method stub
		super.visitConstructor(node);
	}

	@Override
	public void visitMethod(MethodNode node) {
		/*
		 * for (Parameter _parameter : node.getParameters()) { ClassNode _type =
		 * _parameter.getType();
		 * 
		 * groovyClassArtefact.add(_type.getNameWithoutPackage());
		 * 
		 * if (_type.getGenericsTypes() != null) { for (GenericsType _generics :
		 * _type.getGenericsTypes()) {
		 * groovyClassArtefact.add(_generics.getType().getNameWithoutPackage());
		 * } } }
		 * 
		 * ClassNode _type = node.getReturnType();
		 * groovyClassArtefact.add(_type.getNameWithoutPackage());
		 * 
		 * if (_type.getGenericsTypes() != null) { for (GenericsType _generics :
		 * _type.getGenericsTypes()) {
		 * groovyClassArtefact.add(_generics.getType().getNameWithoutPackage());
		 * } }
		 */
		super.visitMethod(node);
	}

	@Override
	public void visitField(FieldNode _fieldNode) {
		String _className = _fieldNode.getType().getText();
		String _classId = "";

		if (ClassNameUtils.containsPackage(_className)) {
			_classId = _className;
		} else {
			_classId = getGroovyClassArtefact().findImportByClass(_className);
		}

		/*
		 * ClassNode _type = node.getType();
		 * groovyClassArtefact.add(_type.getNameWithoutPackage());
		 * 
		 * if (_type.getGenericsTypes() != null) { for (GenericsType _generics :
		 * _type.getGenericsTypes()) {
		 * groovyClassArtefact.add(_generics.getType().getNameWithoutPackage());
		 * } }
		 */
		super.visitField(_fieldNode);
	}

	@Override
	public void visitProperty(PropertyNode node) {
		// TODO Auto-generated method stub
		super.visitProperty(node);
	}

	@Override
	public void visitMethodCallExpression(MethodCallExpression _methodCallExpression) {

		if (_methodCallExpression.getObjectExpression() instanceof VariableExpression) {
			// receiver is a variable / class
			VariableExpression _varExp = (VariableExpression) _methodCallExpression.getObjectExpression();

			if (!_varExp.equals("this")) {
				if (_varExp.getAccessedVariable() == null) { // no local
																// variable as
																// receiver =>
																// static call
																// or
																// call on class
																// member
					String _varName = _varExp.getText();
					String _memberClassName = "";

					// check for class names (first letter is upper case)
					if (ClassNameUtils.isClassName(_varName)) {
						_memberClassName = getGroovyClassArtefact().findImportByClass(_varName);
					} else {
						_memberClassName = getGroovyClassArtefact().getMemberClassName(_varName);
					}

					MethodCallReference _methodCallReference = new MethodCallReference(_memberClassName,
							_methodCallExpression.getMethodAsString());
					getGroovyClassArtefact().addMethodCallReference(_methodCallReference);
				}
			}
		}

		super.visitMethodCallExpression(_methodCallExpression);
	}

	@Override
	public void visitStaticMethodCallExpression(StaticMethodCallExpression call) {
		super.visitStaticMethodCallExpression(call);
	}

	@Override
	public void visitCastExpression(CastExpression expression) {
		/*
		 * ClassNode _type = expression.getType();
		 * groovyClassArtefact.add(_type.getNameWithoutPackage());
		 * 
		 * if (_type.getGenericsTypes() != null) { for (GenericsType _generics :
		 * _type.getGenericsTypes()) {
		 * groovyClassArtefact.add(_generics.getType().getNameWithoutPackage());
		 * } }
		 */
		super.visitCastExpression(expression);
	}

	@Override
	public void visitAttributeExpression(AttributeExpression expression) {
		// TODO Auto-generated method stub
		super.visitAttributeExpression(expression);
	}

	@Override
	public void visitFieldExpression(FieldExpression expression) {
		super.visitFieldExpression(expression);
	}

	@Override
	public void visitArgumentlistExpression(ArgumentListExpression ale) {
		super.visitArgumentlistExpression(ale);
	}

	@Override
	public void visitVariableExpression(VariableExpression expression) {
		/*
		 * ClassNode _type = expression.getType();
		 * groovyClassArtefact.add(_type.getNameWithoutPackage());
		 * 
		 * if (_type.getGenericsTypes() != null) { for (GenericsType _generics :
		 * _type.getGenericsTypes()) {
		 * groovyClassArtefact.add(_generics.getType().getNameWithoutPackage());
		 * } }
		 */
		super.visitVariableExpression(expression);
	}

	@Override
	public void visitClassExpression(ClassExpression expression) {
		/*
		 * groovyClassArtefact.add(expression.getType().getNameWithoutPackage());
		 */
		super.visitClassExpression(expression);
	}

	@Override
	public void visitPackage(PackageNode _node) {
		super.visitPackage(_node);

		if (_node != null) {
			String _packageName = _node.getName();

			if (_packageName.endsWith(".")) {
				_packageName = _packageName.substring(0, _packageName.length() - 1);
			}

			getGroovyClassArtefact().setPackageName(_packageName);
		}
	}

}
