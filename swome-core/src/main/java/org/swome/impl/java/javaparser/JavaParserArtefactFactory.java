package org.swome.impl.java.javaparser;

import java.io.IOException;

import org.swome.core.ArtefactFactory;
import org.swome.core.FileArtefact;
import org.swome.core.SingleArtefactProcessor;
import org.swome.impl.java.groovyparser.GroovyClassArtefact;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

public class JavaParserArtefactFactory implements
		ArtefactFactory<FileArtefact, GroovyClassArtefact> {

	@Override
	public GroovyClassArtefact createArtefact(
			FileArtefact _artefactRepresentation,
			SingleArtefactProcessor<GroovyClassArtefact> _processor) {
		CompilationUnit cu;

		// parse the file
		try {
			cu = JavaParser.parse(_artefactRepresentation.getSourceFile());

			new JavaClassVisitor().visit(cu, null);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
