package org.swome.impl.java.javaparser;

import java.util.List;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.swome.core.Artefact;
import org.swome.core.ArtefactFactory;
import org.swome.core.DefaultArtefactProcessor;
import org.swome.core.FileArtefact;
import org.swome.impl.java.groovyparser.GroovyClassArtefact;

public class JavaParserClassArtefactsProcessor extends
		DefaultArtefactProcessor<FileArtefact, GroovyClassArtefact> {

	@Override
	public List<Artefact> mainStep(GroovyClassArtefact _artefact, Graph _graph) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArtefactFactory<FileArtefact, GroovyClassArtefact> getArtefactFactory() {
		return new JavaParserArtefactFactory();
	}

	@Override
	public void traverseGraph(Graph _graph) {
		// TODO Auto-generated method stub

	}

}
