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
package org.swome.core;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

public class GraphPublicInterfaceTests {

	/*
	 * This test is designed to drive the public interface of Graph, Artefact,
	 * Relation and GraphLoader.
	 * 
	 * Test a graph of the form:
	 * 
	 * A -> B A -> C B -> D D -> E
	 */
	@Test
	public void testLoadGraph() throws IOException {
		GraphLoader<Artefact, Relation, String> _graphLoader = new GraphLoader<Artefact, Relation, String>() {

			@Override
			public void loadInto(Graph graph,
					ArtefactFactory<Artefact, Artefact> _factory,
					RelationFactory<Relation, Artefact> _relationFactory)
					throws IOException {
				Artefact _A = _factory.createArtefact("A", null);
				_factory.createArtefact(_artefactRepresentation, _processor)
				graph.addArtefact(_A);

				Artefact _B = _factory.createArtefact("B");
				graph.addArtefact(_B);

				Artefact _C = _factory.createArtefact("C");
				graph.addArtefact(_C);

				Artefact _D = _factory.createArtefact("D");
				graph.addArtefact(_D);

				Artefact _E = _factory.createArtefact("E");
				graph.addArtefact(_E);

				graph.addRelation(_relationFactory.createRelation(_A, _B), _A,
						_B);
				graph.addRelation(_relationFactory.createRelation(_A, _C), _A,
						_C);
				graph.addRelation(_relationFactory.createRelation(_B, _D), _B,
						_D);
				graph.addRelation(_relationFactory.createRelation(_D, _E), _D,
						_E);
			}
		};

		ArtefactFactory<Artefact, String> _factory = new ArtefactFactory<Artefact, String>() {

			@Override
			public Artefact createArtefact(String _artefactRepresentation) {
				return new Artefact(_artefactRepresentation);
			}
		};

		Graph graph = new Graph();

		_graphLoader.loadInto(graph, _factory, new RelationFactory());

		assertEquals(5, graph.artefacts().size());
		assertEquals(2, graph.getRelationsFor("A").size());
	}
}
