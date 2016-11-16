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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

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
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.AttributeExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.swome.core.ArtefactRegistry;
import org.swome.impl.java.ClassReference;
import org.swome.impl.java.MethodCallContext;
import org.swome.impl.java.MethodCallReference;
import org.swome.impl.java.MethodDefinition;
import org.swome.impl.java.MethodSignature;
import org.swome.impl.java.TypeDeclaration;

public class PluginBasedGroovyCodeVisitor extends GroovyCodeVisitor {

	private static final String THIS = "this";
	private static final String SUPER = "super";

	private class Context {
		private Map<String, TypeDeclaration> locals = new HashMap<String, TypeDeclaration>();

		public void add(String _varName, TypeDeclaration _typeDeclaration) {
			locals.put(_varName, _typeDeclaration);
		}

		public TypeDeclaration get(String _varName) {
			return locals.get(_varName);
		}
	}

	private Stack<Context> contextStack = new Stack<PluginBasedGroovyCodeVisitor.Context>();
	private Stack<MethodCallContext> methodCallContextStack = new Stack<MethodCallContext>();
	private ArtefactRegistry<GroovyClassArtefact> artefactRegistry;

	public PluginBasedGroovyCodeVisitor(
			ArtefactRegistry<GroovyClassArtefact> registry) {
		artefactRegistry = registry;
	}

	private List<String> flattenType(ClassNode node) {
		List<String> typesList = new ArrayList<String>();

		typesList.add(node.getText());

		if (node.getGenericsTypes() != null) {
			for (GenericsType gType : node.getGenericsTypes()) {
				typesList.addAll(this.flattenType(gType.getType()));
			}
		}

		return typesList;
	}

	private void pushContext() {
		contextStack.push(new Context());
	}

	private Context popContext() {
		return contextStack.pop();
	}

	private Context getContext() {
		return contextStack.peek();
	}

	private MethodSignature buildMethodSignature(MethodNode _enclosingMethod) {
		MethodSignature _methodSignature = new MethodSignature();

		StringBuilder _builder = new StringBuilder();

		if (_enclosingMethod.getParameters() != null) {
			for (int i = 0; i < _enclosingMethod.getParameters().length - 1; i++) {
				Parameter _p = _enclosingMethod.getParameters()[i];
				_builder.append(_p.getType().getText());
				_builder.append(" ");
				_builder.append(_p.getName());
				_builder.append(",");
			}

			if (_enclosingMethod.getParameters().length > 0) {
				Parameter _p = _enclosingMethod.getParameters()[_enclosingMethod
						.getParameters().length - 1];
				_builder.append(_p.getType().getText());
				_builder.append(" ");
				_builder.append(_p.getName());
			}
		}

		_methodSignature.setName(_enclosingMethod.getName());
		_methodSignature.setReturnExpression(_enclosingMethod.getReturnType()
				.getName());
		_methodSignature.setParametersExpression(_builder.toString());

		return _methodSignature;
	}

	private TypeDeclaration findVariableInScope(String _varName) {
		ListIterator<Context> _iter = contextStack.listIterator(contextStack
				.size());

		while (_iter.hasPrevious()) {
			Context _c = _iter.previous();

			TypeDeclaration _t = _c.get(_varName);

			if (_t != null) {
				return _t;
			}
		}

		return null;
	}

	@Override
	public void visitClass(ClassNode node) {
		getGroovyClassArtefact().setClassName(node.getNameWithoutPackage());

		ClassReference _selfRef = ClassReference.resolved(
				node.getPackageName(), node.getNameWithoutPackage());
		getGroovyClassArtefact().addMember(THIS, new TypeDeclaration(_selfRef));
		artefactRegistry.put(_selfRef.getFQCN(), this.getGroovyClassArtefact());

		if (node.getGenericsTypes() != null) {
			for (GenericsType _generics : node.getGenericsTypes()) {
				getGroovyClassArtefact().addGenerics(
						_generics.getType().getNameWithoutPackage());
			}
		}

		ClassNode _superclass = node.getSuperClass();
		ClassReference _superRef = this.getGroovyClassArtefact()
				.resolveClassReference(_superclass.getNameWithoutPackage());
		getGroovyClassArtefact().addMember(SUPER,
				new TypeDeclaration(_superRef));

		this.pushContext();

		ClassNode _outerNode = node.getOuterClass();

		if (_outerNode != null) {
			// inner class
			ClassReference _outerClassReference = ClassReference.resolved(
					_outerNode.getPackageName(),
					_outerNode.getNameWithoutPackage());

			GroovyClassArtefact _parent = artefactRegistry
					.findById(_outerClassReference.getFQCN());

			Set<Entry<String, TypeDeclaration>> _members = _parent.getMembers();

			// pass all members from parent to actual context, so the
			// inner class have field visibility
			for (Entry<String, TypeDeclaration> _m : _members) {
				this.getContext().add(_m.getKey(), _m.getValue());
			}

			MethodNode _enclosingMethod = node.getEnclosingMethod();

			if (_enclosingMethod != null) {
				String _methodSignature = this.buildMethodSignature(
						_enclosingMethod).toString();

				MethodDefinition _methodDef = _parent
						.getMethodBySignature(_methodSignature);
				System.out.println(_methodSignature);
				_members = _methodDef.getParameters();

				// pass all members from parent to actual context, so the
				// inner class have field visibility
				for (Entry<String, TypeDeclaration> _m : _members) {
					this.getContext().add(_m.getKey(), _m.getValue());
				}
			}
		}

		methodCallContextStack.push(this.getGroovyClassArtefact()
				.getMethodCallContext());

		super.visitClass(node);

		methodCallContextStack.pop();

		this.popContext();

		ClassNode _superType = node.getSuperClass();

		String _superClassName = getGroovyClassArtefact().resolveClassName(
				_superType.getName());

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
				getGroovyClassArtefact().addSuperClassGenerics(
						_generics.getType().getNameWithoutPackage());
			}
		}
	}

	@Override
	public void visitImports(ModuleNode node) {
		for (ImportNode importNode : node.getImports()) {
			ClassReference _classReference = ClassReference.fromFQCN(importNode
					.getClassName());
			getGroovyClassArtefact().addImport(_classReference);
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

		String _varName = expression.getVariableExpression().getName();
		ClassReference _classReference = getGroovyClassArtefact()
				.resolveClassReference(
						expression.getVariableExpression().getType().getName());
		TypeDeclaration _typeDeclaration = new TypeDeclaration(_classReference);

		this.getContext().add(_varName, _typeDeclaration);

		super.visitDeclarationExpression(expression);
	}

	@Override
	public void visitCatchStatement(CatchStatement statement) {
		String _varName = statement.getVariable().getName();
		ClassReference _classReference = getGroovyClassArtefact()
				.resolveClassReference(
						statement.getVariable().getType().getName());
		TypeDeclaration _typeDeclaration = new TypeDeclaration(_classReference);

		this.getContext().add(_varName, _typeDeclaration);

		super.visitCatchStatement(statement);
	}

	@Override
	public void visitForLoop(ForStatement forLoop) {
		String _varName = forLoop.getVariable().getName();
		ClassReference _classReference = getGroovyClassArtefact()
				.resolveClassReference(
						forLoop.getVariable().getType().getName());
		TypeDeclaration _typeDeclaration = new TypeDeclaration(_classReference);

		this.getContext().add(_varName, _typeDeclaration);

		super.visitForLoop(forLoop);
	}

	@Override
	public void visitConstructor(ConstructorNode node) {
		MethodSignature _methodSignature = new MethodSignature();
		_methodSignature.setName("<init>");
		MethodDefinition _methodDef = new MethodDefinition(_methodSignature);

		for (Parameter _param : node.getParameters()) {
			String _varName = _param.getName();
			ClassReference _classReference = getGroovyClassArtefact()
					.resolveClassReference(_param.getType().getName());
			TypeDeclaration _typeDeclaration = new TypeDeclaration(
					_classReference);

			this.getContext().add(_varName, _typeDeclaration);
			_methodDef.addParameter(_varName, _typeDeclaration);
		}

		this.getGroovyClassArtefact().addMethod(_methodDef);

		methodCallContextStack.push(_methodDef.getMethodCallContext());

		super.visitConstructor(node);

		methodCallContextStack.pop();
	}

	@Override
	public void visitMethod(MethodNode node) {
		MethodDefinition _methodDef = new MethodDefinition(
				this.buildMethodSignature(node));

		for (Parameter _param : node.getParameters()) {
			String _varName = _param.getName();
			ClassReference _classReference = getGroovyClassArtefact()
					.resolveClassReference(_param.getType().getName());
			TypeDeclaration _typeDeclaration = new TypeDeclaration(
					_classReference);

			this.getContext().add(_varName, _typeDeclaration);
			_methodDef.addParameter(_varName, _typeDeclaration);
		}

		this.getGroovyClassArtefact().addMethod(_methodDef);

		methodCallContextStack.push(_methodDef.getMethodCallContext());

		super.visitMethod(node);

		methodCallContextStack.pop();
	}

	@Override
	public void visitBlockStatement(BlockStatement block) {
		this.pushContext();

		super.visitBlockStatement(block);

		this.popContext();
	}

	@Override
	public void visitField(FieldNode _fieldNode) {
		List<ClassReference> _resolvedTypes = new ArrayList<ClassReference>();

		for (String _type : this.flattenType(_fieldNode.getType())) {
			ClassReference _classReference = getGroovyClassArtefact()
					.resolveClassReference(_type);

			_resolvedTypes.add(_classReference);
		}

		String _memberName = _fieldNode.getName();
		TypeDeclaration _typeDeclaration = new TypeDeclaration(_resolvedTypes);

		getGroovyClassArtefact().addMember(_memberName, _typeDeclaration);
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
	public void visitMethodCallExpression(
			MethodCallExpression _methodCallExpression) {

		if (_methodCallExpression.getObjectExpression() instanceof VariableExpression) {
			// receiver is a variable / class
			VariableExpression _varExp = (VariableExpression) _methodCallExpression
					.getObjectExpression();

			if (!_varExp.equals("this")) {
				if (_varExp.getAccessedVariable() == null) { // no local
																// variable as
																// receiver =>
																// static call
																// or
																// call on class
																// member
					String _varName = _varExp.getText();
					ClassReference _memberClassReference = null;

					// check for class names (first letter is upper case)
					if (ClassReference.isClassName(_varName)) {
						_memberClassReference = getGroovyClassArtefact()
								.resolveClassReference(_varName);
					} else {
						/*
						 * TYPE ERASURE HERE, CAUSED BY
						 * '.getMemberType(_varName).getTypes().get(0)'
						 */
						TypeDeclaration _varType = getGroovyClassArtefact()
								.getMemberType(_varName);

						if (_varType != null) {
							_memberClassReference = _varType.getTypes().get(0);
						} else {
							// local variable
							_varType = this.findVariableInScope(_varName);
							if (_varType == null) {
								System.out.println(this
										.getGroovyClassArtefact().getId()
										+ "; " + _varName);
							}
							_memberClassReference = _varType.getTypes().get(0);
						}
					}

					MethodNode _method = _methodCallExpression
							.getMethodTarget();

					if (_method != null) {
						MethodCallReference _methodCallReference = new MethodCallReference(
								_memberClassReference,
								this.buildMethodSignature(_method));
						methodCallContextStack.peek().addMethodCallReference(
								_methodCallReference);
					}
				}
			}
		}

		super.visitMethodCallExpression(_methodCallExpression);
	}

	@Override
	public void visitConstructorCallExpression(
			ConstructorCallExpression _constructorCallExpression) {
		MethodSignature _methodSignature = new MethodSignature();
		_methodSignature.setName("<init>");
		MethodCallReference _methodCallReference = new MethodCallReference(
				getGroovyClassArtefact().resolveClassReference(
						_constructorCallExpression.getType().getName()),
				_methodSignature);

		methodCallContextStack.peek().addMethodCallReference(
				_methodCallReference);

		super.visitConstructorCallExpression(_constructorCallExpression);
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
	public void visitPackage(PackageNode _node) {
		super.visitPackage(_node);

		if (_node != null) {
			String _packageName = _node.getName();

			if (_packageName.endsWith(".")) {
				_packageName = _packageName.substring(0,
						_packageName.length() - 1);
			}

			getGroovyClassArtefact().setPackageName(_packageName);

			getGroovyClassArtefact().getMemberType(THIS).getTypes().get(0)
					.setPackageName(_packageName);
		}
	}

}
