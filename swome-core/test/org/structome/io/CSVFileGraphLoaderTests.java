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
package org.structome.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.structome.core.ArtefactFactory;
import org.structome.core.Graph;
import org.structome.impl.SimpleArtefact;
import org.structome.impl.SimpleGraph;
import org.structome.impl.SimpleRelation;
import org.structome.impl.SimpleRelationFactory;

public class CSVFileGraphLoaderTests {
	public static final String[] graph = { "A,B", "A,C", "B,D", "D,E" };
	public static final String[] singleRecords = { "A", "A,C", "B,D", "E", ",D", ",J" };

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	@Test
	public void testLoader() throws IOException {
		File _file = tempFolder.newFile();

		PrintStream _printStream = new PrintStream(_file);

		for (String _line : graph) {
			_printStream.println(_line);
		}

		_printStream.close();

		CSVFileGraphLoader<SimpleArtefact, SimpleRelation> _graphLoader = new CSVFileGraphLoader<SimpleArtefact, SimpleRelation>(
				_file);

		ArtefactFactory<SimpleArtefact, String> _factory = new ArtefactFactory<SimpleArtefact, String>() {

			@Override
			public SimpleArtefact createArtefact(String _artefactRepresentation) {
				return new SimpleArtefact(_artefactRepresentation);
			}
		};

		Graph<SimpleArtefact, SimpleRelation> _graph = new SimpleGraph<SimpleArtefact, SimpleRelation>();

		_graphLoader.loadInto(_graph, _factory, new SimpleRelationFactory());

		assertEquals(5, _graph.artefacts().size());
	}

	@Test
	public void testSingleRecords() throws IOException {
		File _file = tempFolder.newFile();

		PrintStream _printStream = new PrintStream(_file);

		for (String _line : singleRecords) {
			_printStream.println(_line);
		}

		_printStream.close();

		CSVFileGraphLoader<SimpleArtefact, SimpleRelation> _graphLoader = new CSVFileGraphLoader<SimpleArtefact, SimpleRelation>(
				_file);

		ArtefactFactory<SimpleArtefact, String> _factory = new ArtefactFactory<SimpleArtefact, String>() {

			@Override
			public SimpleArtefact createArtefact(String _artefactRepresentation) {
				return new SimpleArtefact(_artefactRepresentation);
			}
		};

		Graph<SimpleArtefact, SimpleRelation> _graph = new SimpleGraph<SimpleArtefact, SimpleRelation>();

		_graphLoader.loadInto(_graph, _factory, new SimpleRelationFactory());

		assertEquals(7, _graph.artefacts().size());
	}
}
