package org.swome.impl.java.javaparser;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class JavaClassVisitor extends VoidVisitorAdapter<Object> {

	@Override
	public void visit(ConstructorDeclaration _constructorDeclaration, Object arg1) {
		System.out.println("<init> "+_constructorDeclaration.getName());
		super.visit(_constructorDeclaration, arg1);
	}

	@Override
	public void visit(ImportDeclaration _importDeclaration, Object arg) {
		System.out.println("imp " +_importDeclaration.getName());
		super.visit(_importDeclaration, arg);
	}

	@Override
	public void visit(PackageDeclaration _packageDeclaration, Object arg1) {
		System.out.println("pkgDecl " +_packageDeclaration.getPackageName());
		super.visit(_packageDeclaration, arg1);
	}

	@Override
	public void visit(ClassOrInterfaceDeclaration _classOrInterfaceDeclaration, Object arg1) {
		System.out.println("classDecl " + _classOrInterfaceDeclaration.getName());
		
		super.visit(_classOrInterfaceDeclaration, arg1);
	}

}
