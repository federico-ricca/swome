package org.swome.impl.jung2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.process.computer.GraphComputer;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.swome.core.Artefact;
import org.swome.core.Relation;
import org.swome.core.RelationArtefactPair;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

public class Jung2GraphAdapter implements Graph {
	private DirectedSparseMultigraph<Artefact, Relation> graphDelegate;
	private HashMap<String, Artefact> artefactMap;

	public Jung2GraphAdapter() {
		graphDelegate = new DirectedSparseMultigraph<Artefact, Relation>();
		artefactMap = new HashMap<String, Artefact>();
	}
/*
	@Override
	public Collection<Artefact> artefacts() {
		return graphDelegate.getVertices();
	}

	@Override
	public void addRelation(Relation _relation, Artefact _source, Artefact _dest) {
		graphDelegate.addEdge(_relation, _source, _dest, EdgeType.UNDIRECTED);
	}

	@Override
	public void addDirectedRelation(Relation _relation, Artefact _source,
			Artefact _dest) {
		graphDelegate.addEdge(_relation, _source, _dest, EdgeType.DIRECTED);
	}

	@Override
	public void addArtefact(Artefact _artefact) {
		graphDelegate.addVertex(_artefact);
		artefactMap.put(_artefact.getId(), _artefact);
	}

	@Override
	public Collection<RelationArtefactPair> getRelationsFor(String _id) {
		Collection<RelationArtefactPair> _relations = new ArrayList<RelationArtefactPair>();

		Artefact _targetArtefact = artefactMap.get(_id);

		Collection<Relation> _edges = graphDelegate
				.getIncidentEdges(_targetArtefact);

		for (Relation _edge : _edges) {
			Pair<Artefact> _pair = graphDelegate.getEndpoints(_edge);

			if (_targetArtefact.equals(_pair.getFirst())) {
				_relations.add(new RelationArtefactPair(_edge, _pair
						.getSecond()));
			} 
			 // else if (_targetArtefact.equals(_pair.getSecond())) {
			 // _relations.add(new RelationArtefactPair<ClassReferenceRelation,
			 // GroovyClassArtefact>(_edge, _pair.getFirst())); }
			 //
		}

		return _relations;
	}

	@Override
	public boolean hasRelations(String _id) {
		return !graphDelegate.getIncidentEdges(artefactMap.get(_id)).isEmpty();
	}

	@Override
	public Artefact getArtefact(String _id) {
		return artefactMap.get(_id);
	}

	@Override
	public void addArtefacts(List<Artefact> _artefacts) {
		for (Artefact _artefact : _artefacts) {
			this.addArtefact(_artefact);
		}
	}
*/
	@Override
	public Vertex addVertex(Object... keyValues) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <C extends GraphComputer> C compute(Class<C> graphComputerClass)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GraphComputer compute() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Vertex> vertices(Object... vertexIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Edge> edges(Object... edgeIds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Transaction tx() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Variables variables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Configuration configuration() {
		// TODO Auto-generated method stub
		return null;
	}

}
