package org.softome.web.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.softome.web.ui.core.GraphData;
import org.softome.web.ui.core.Link;
import org.softome.web.ui.core.Node;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class GraphController {

	@RequestMapping(method = RequestMethod.GET, value = "/v1/graphData", produces = { "application/json" })
	public ResponseEntity<GraphData> index(Model model) throws IOException {
		GraphData graphData = new GraphData();

		List<Node> nodes = new ArrayList<Node>();
		List<Link> links = new ArrayList<Link>();

		nodes.add(new Node("one", "1"));
		nodes.add(new Node("two", "2"));
		nodes.add(new Node("three", "3"));
		nodes.add(new Node("four", "4"));

		links.add(new Link(0, 1, 1));
		links.add(new Link(0, 2, 1));
		links.add(new Link(2, 3, 1));

		graphData.setNodes(nodes);
		graphData.setLinks(links);

		return new ResponseEntity<GraphData>(graphData, HttpStatus.OK);
	}
}
