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
package org.structome;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.structome.core.ArtefactStringRepresentationFactory;
import org.structome.core.Graph;
import org.structome.core.RelationStringRepresentationFactory;
import org.structome.core.Structome;
import org.structome.extraction.StructomeExtractor;
import org.structome.impl.groovy.ClassReferenceRelation;
import org.structome.impl.groovy.GroovyClassArtefact;
import org.structome.impl.groovy.GroovyClassArtefactFactory;
import org.structome.impl.groovy.GroovyClassDependencyGraphBuilder;
import org.structome.impl.groovy.PluginBasedGroovyCodeVisitor;
import org.structome.io.CSVGraphWriter;

public class ClassDependencyExtraction {

	@Test
	public void extractClassDependencyGraph() throws IOException {
		String _path = "";
		_path = "/Users/federico.ricca/work/portal-splitting/portal-web/modules/portal-core";
		//_path = "/Users/federico.ricca/work/portal-splitting/portal-web/modules/portal-core/src/groovy/de/kaufda/brochure/campaign";

		StructomeExtractor _extractor = new StructomeExtractor();

		GroovyClassDependencyGraphBuilder _builder = new GroovyClassDependencyGraphBuilder();

		GroovyClassArtefactFactory _factory = new GroovyClassArtefactFactory(
				new PluginBasedGroovyCodeVisitor());

		_extractor.registerGraph("class-dependency", ".*\\.groovy", _builder, _factory);

		Structome _structome = _extractor.processFolder(_path);

		assertNotNull(_structome);

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

	}
}
