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

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.junit.Test;
import org.swome.core.Artefact;
import org.swome.core.ArtefactStringRepresentationFactory;
import org.swome.core.DefaultArtefactProcessor;
import org.swome.core.FileArtefact;
import org.swome.core.Relation;
import org.swome.core.RelationStringRepresentationFactory;
import org.swome.core.Structome;
import org.swome.core.SystemRepresentation;
import org.swome.extraction.SystemProcessor;
import org.swome.impl.java.MethodDefinition;
import org.swome.impl.java.groovyparser.GroovyClassArtefact;
import org.swome.impl.java.javaparser.JavaParserClassArtefactsProcessor;
import org.swome.impl.jung2.Jung2GraphFactory;
import org.swome.io.CSVGraphWriter;

public class ClassDependencyExtraction {

	@Test
	public void extractClassDependencyGraph() throws IOException {
		String _path = "";
		_path = "/Users/federico.ricca/b-projects/swome/swome-core/src";
		// _path =
		// "/Users/federico.ricca/work/portal-splitting/portal-web/modules/portal-core/src/groovy/de/kaufda/brochure/campaign";

		SystemProcessor _systemProcessor = new SystemProcessor();
		Structome _structome = new Structome(new Jung2GraphFactory());
		SystemRepresentation _sysRep = new SystemRepresentation();

//		DefaultArtefactProcessor<FileArtefact, GroovyClassArtefact> _artefactProcessor = new GroovyClassArtefactsProcessor();
		DefaultArtefactProcessor<FileArtefact, GroovyClassArtefact> _artefactProcessor = new JavaParserClassArtefactsProcessor();

		_systemProcessor.registerProcesor("class-dependency", ".*\\.java",
				_artefactProcessor);

		_artefactProcessor
				.addStep((_artefact, _graph) -> {
					GroovyClassArtefact _groovyClassArtefact = (GroovyClassArtefact) _artefact;

					for (MethodDefinition _methodDef : _groovyClassArtefact
							.getMethods()) {
						Artefact _methodArtefact = new Artefact();

						_methodArtefact.setId(_groovyClassArtefact.getId()
								+ ":" + _methodDef.getSignature());
System.out.println(">> " + _groovyClassArtefact.getId() +":"+_methodDef.getSignature());
						_graph.addArtefact(_methodArtefact);
						_graph.addDirectedRelation(new Relation("member-method"), _artefact,
								_methodArtefact);

					}
					return Collections.emptyList();
				});

		_systemProcessor.processFolder(_path, _sysRep, _structome);

		CSVGraphWriter _graphWriter = new CSVGraphWriter();

		File _file = new File("dependency-graph.csv");

		ArtefactStringRepresentationFactory _simpleRepresentationFactory = new ArtefactStringRepresentationFactory() {

			@Override
			public String createStringRepresentationFor(Artefact _anArtefact) {
				return _anArtefact.getId();
			}
		};

		RelationStringRepresentationFactory _relationRepresentationFactory = new RelationStringRepresentationFactory() {
			@Override
			public String createStringRepresentationFor(Relation _relation) {
				return _relation.toString();
			}

		};

		_graphWriter.write(_structome.getGraph(), _file,
				_simpleRepresentationFactory, _relationRepresentationFactory);

	}
}
