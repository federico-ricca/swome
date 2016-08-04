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
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.structome.core.ArtefactStringRepresentationFactory;
import org.structome.impl.SimpleArtefact;
import org.structome.impl.SimpleGraph;
import org.structome.impl.SimpleRelation;

public class CSVFileGraphWriterTests {
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void testWriteSimpleGraph() throws IOException {
		SimpleGraph<SimpleArtefact, SimpleRelation> _graph = new SimpleGraph<SimpleArtefact, SimpleRelation>();

		SimpleArtefact _a = new SimpleArtefact("A");
		SimpleArtefact _b = new SimpleArtefact("B");
		SimpleArtefact _c = new SimpleArtefact("C");
		SimpleArtefact _d = new SimpleArtefact("D");

		_graph.addArtefact(_a);
		_graph.addArtefact(_b);
		_graph.addArtefact(_c);
		_graph.addArtefact(_d);

		_graph.addRelation(new SimpleRelation(), _a, _b);
		_graph.addRelation(new SimpleRelation(), _a, _c);
		_graph.addRelation(new SimpleRelation(), _c, _d);

		ArtefactStringRepresentationFactory<SimpleArtefact> _simpleRepresentationFactory = new ArtefactStringRepresentationFactory<SimpleArtefact>() {

			@Override
			public String createStringRepresentationFor(SimpleArtefact _anArtefact) {
				return _anArtefact.getId();
			}
		};

		CSVGraphWriter<SimpleArtefact, SimpleRelation> _graphWriter = new CSVGraphWriter<SimpleArtefact, SimpleRelation>();

		folder.create();
		File _file = folder.newFile();

		_graphWriter.write(_graph, _file, _simpleRepresentationFactory, null);

		BufferedReader _reader = new BufferedReader(new FileReader(_file));

		String _string = _reader.readLine();
		Set<String> _testGraph = new HashSet<String>();

		while (_string != null) {
			_testGraph.add(_string);
			_string = _reader.readLine();
		}

		_reader.close();

		assertTrue(_testGraph.contains("A,B"));
		assertTrue(_testGraph.contains("A,C"));
		assertTrue(_testGraph.contains("C,D"));
		assertEquals(3, _testGraph.size());
	}
}
