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

package org.swome.usecases;

import java.io.IOException;

import org.junit.Test;
import org.swome.core.DefaultArtefactProcessor;
import org.swome.core.FileArtefact;
import org.swome.core.Structome;
import org.swome.core.SystemRepresentation;
import org.swome.extraction.SystemProcessor;
import org.swome.impl.groovy.GroovyClassArtefact;
import org.swome.impl.groovy.GroovyClassArtefactsProcessor;
import org.swome.impl.titandb.InMemoryTitanGraphFactory;

public class ClassDependencyExtraction {

	@Test
	public void extractClassDependencyGraph() throws IOException {
		String _path = "";
		_path = "/Users/federico.ricca/b-projects/swome/swome-core/src/main/java";
		//_path = "/Users/federico.ricca/work/portal-splitting/portal-web/modules/portal-core/src/groovy/de/kaufda/brochure/campaign";

		SystemProcessor _systemProcessor = new SystemProcessor();
		Structome _structome = new Structome(new InMemoryTitanGraphFactory());
		SystemRepresentation _sysRep = new SystemRepresentation();
		
		DefaultArtefactProcessor<FileArtefact, GroovyClassArtefact> _artefactProcessor = new GroovyClassArtefactsProcessor();
		
		_systemProcessor.registerProcesor("class-dependency", ".*\\.java", _artefactProcessor);

		_artefactProcessor.addStep((artefact, graph) -> {
			System.out.println(artefact);
			return artefact;
		});
		
		_systemProcessor.processFolder(_path, _sysRep, _structome);
/*
		CSVGraphWriter<GroovyClassArtefact, ClassReferenceRelation> _graphWriter = new CSVGraphWriter<GroovyClassArtefact, ClassReferenceRelation>();

		File _file = new File("dependency-graph.csv");

		ArtefactStringRepresentationFactory<GroovyClassArtefact> _simpleRepresentationFactory = new ArtefactStringRepresentationFactory<GroovyClassArtefact>() {

			@Override
			public String createStringRepresentationFor(GroovyClassArtefact _anArtefact) {
				return _anArtefact.getId();
			}
		};

		RelationStringRepresentationFactory<ClassReferenceRelation> _relationRepresentationFactory = new RelationStringRepresentationFactory<ClassReferenceRelation>() {

			@Override
			public String createStringRepresentationFor(ClassReferenceRelation _relation) {
				return _relation.toString();
			}

		};

		_graphWriter.write(
				(Graph<GroovyClassArtefact, ClassReferenceRelation>) _structome.getGraph("class-dependency"),
				_file, _simpleRepresentationFactory, _relationRepresentationFactory);
				*/

	}
}
