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
package org.structome.extraction;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.minitools.io.FileCollector;
import org.structome.core.Graph;
import org.structome.core.Structome;
import org.structome.impl.groovy.ClassReferenceRelation;
import org.structome.impl.groovy.GroovyClassArtefact;
import org.structome.impl.groovy.GroovyClassArtefactFactory;
import org.structome.impl.groovy.GroovyClassDependencyGraphBuilder;
import org.structome.impl.groovy.GroovyFileArtefact;

public class StructomeExtractor {
	class ExtractionConfiguration {
		public String filenameRegex;
		public GroovyClassDependencyGraphBuilder builder;
		public GroovyClassArtefactFactory factory;
	}

	private Map<String, ExtractionConfiguration> configurations = new HashMap<String, ExtractionConfiguration>();

	public Structome processFolder(String _path) {
		Structome _structome = new Structome();

		File _folder = new File(_path);
		FileCollector _fileCollector = new FileCollector(_folder);

		for (String _confKey : configurations.keySet()) {
			ExtractionConfiguration _configuration = configurations.get(_confKey);

			Collection<File> _files = _fileCollector.collect(_configuration.filenameRegex);

			ArrayList<GroovyFileArtefact> _fileArtefacts = new ArrayList<GroovyFileArtefact>();

			for (File _f : _files) {
				_fileArtefacts.add(new GroovyFileArtefact(_f));
			}

			Graph<GroovyClassArtefact, ClassReferenceRelation> _graph = _configuration.builder.buildFrom(
					_fileArtefacts, _configuration.factory);

			_structome.addGraph(_confKey, _graph);
		}

		return _structome;
	}

	public void registerGraph(String _graphId, String _filenameRegex,
			GroovyClassDependencyGraphBuilder _builder, GroovyClassArtefactFactory _factory) {
		ExtractionConfiguration _configuration = new ExtractionConfiguration();

		_configuration.filenameRegex = _filenameRegex;
		_configuration.builder = _builder;
		_configuration.factory = _factory;

		configurations.put(_graphId, _configuration);
	}
}
