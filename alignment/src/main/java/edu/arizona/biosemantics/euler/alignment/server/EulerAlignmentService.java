package edu.arizona.biosemantics.euler.alignment.server;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.server.db.CollectionDAO;
import edu.arizona.biosemantics.euler.alignment.server.db.Query.QueryException;
import edu.arizona.biosemantics.euler.alignment.shared.Highlight;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulations;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomies;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation.Type;
import edu.arizona.biosemantics.euler.io.ArticulationsReader;

public class EulerAlignmentService extends RemoteServiceServlet implements IEulerAlignmentService {

	private CollectionDAO collectionDAO;

	@Inject
	public EulerAlignmentService(CollectionDAO collectionDAO) throws Exception {
		this.collectionDAO = collectionDAO;
		
		Collection collection = new Collection();
		collection.setSecret("test");
		
		edu.arizona.biosemantics.matrixreview.shared.model.Model model = unserializeMatrix("C:\\gitEtc3\\euler2\\alignment\\TaxonMatrix.ser");
		collection.setModel(model);
		this.createCollection(collection);
	}
	
	private edu.arizona.biosemantics.matrixreview.shared.model.Model unserializeMatrix(String file) throws FileNotFoundException, IOException, ClassNotFoundException {
		try(ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
			Object object = input.readObject();
			edu.arizona.biosemantics.matrixreview.shared.model.Model model = (edu.arizona.biosemantics.matrixreview.shared.model.Model)input.readObject();
			return model;
		}
	}
	
	@Override
	public Model getModel() {
		return createSampleModel();
	}
		
	private Model createSampleModel() {
		
		Taxon t1 = new Taxon(Rank.FAMILY, "rosacea", "author1", "1979", "this is the description about t1");
		Taxon t2 = new Taxon(Rank.GENUS, "rosa", "author2", "1985",  "this is the description about t2");
		Taxon t3 = new Taxon(Rank.SPECIES,
				"example", "author3", "2002", 
				"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. "
						+ "Sed metus nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. "
						+ "Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, "
						+ "tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis "
						+ "vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a, "
						+ "fringilla vel, urna.<br/><br/>Aliquam commodo ullamcorper erat. Nullam vel justo in neque "
						+ "porttitor laoreet. Aenean lacus dui, consequat eu, adipiscing eget, nonummy non, nisi. "
						+ "Morbi nunc est, dignissim non, ornare sed, luctus eu, massa. Vivamus eget quam. Vivamus "
						+ "tincidunt diam nec urna. Curabitur velit.");
		Taxon t4 = new Taxon(Rank.VARIETY,
				"prototype", "author4", "2014", //"leaf length");
				"Lorem leaf size = 4 ipsum dolor sit amet, consectetuer adipiscing elit. "
						+ "Sed metus length oblong nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. "
						+ "Maecenas tortor turpis, green interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, "
						+ "tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis "
						+ "vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a, "
						+ "fringilla vel, urna.<br/><br/>Aliquam commodo ullamcorper erat. Nullam vel justo in neque "
						+ "porttitor laoreet. Aenean lacus dui, consequat eu, adipiscing eget, nonummy non, nisi. "
						+ "Morbi nunc est, dignissim non, ornare sed, luctus eu, massa. Vivamus eget quam. Vivamus "
						+ "tincidunt diam nec urna. Curabitur velit.");
		t1.addChild(t2);
		t2.addChild(t3);
		t2.addChild(t4); 
		List<Taxon> hierarchyTaxa1 = new LinkedList<Taxon>();
		hierarchyTaxa1.add(t1);
		
		Taxon t21 = new Taxon(Rank.FAMILY, "rosacea", "x", "1979", "this is the description about t1");
		Taxon t22 = new Taxon(Rank.GENUS, "rosa", "x", "1985",  "this is the description about t2");
		Taxon t23 = new Taxon(Rank.SPECIES,
				"example", "author3", "2002", 
				"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. "
						+ "Sed metus nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. "
						+ "Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, "
						+ "tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis "
						+ "vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a, "
						+ "fringilla vel, urna.<br/><br/>Aliquam commodo ullamcorper erat. Nullam vel justo in neque "
						+ "porttitor laoreet. Aenean lacus dui, consequat eu, adipiscing eget, nonummy non, nisi. "
						+ "Morbi nunc est, dignissim non, ornare sed, luctus eu, massa. Vivamus eget quam. Vivamus "
						+ "tincidunt diam nec urna. Curabitur velit.");
		Taxon t24 = new Taxon(Rank.VARIETY,
				"prototype", "x", "2014", 
				"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. "
						+ "Sed metus nibh, sodales pubescence www a, porta at, leaf length 4 vulputate eget, dui. Pellentesque ut nisl. "
						+ "Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, "
						+ "tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis "
						+ "vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a leaf shape long short, "
						+ "fringilla vel, urna.<br/><br/>Aliquam commodo ullamcorper erat. Nullam architecture xyz vel justo in neque "
						+ "porttitor laoreet. Aenean lacus dui, consequat eu, adipiscing eget, nonummy non, nisi. "
						+ "Morbi nunc est, dignissim non, ornare sed, luctus eu, massa. Vivamus eget quam. Vivamus "
						+ "tincidunt diam nec urna. Curabitur velit.");
		t21.addChild(t24);
		t21.addChild(t23);
		t21.addChild(t22);
		
		List<Taxon> hierarchyTaxa2 = new LinkedList<Taxon>();
		hierarchyTaxa2.add(t21);
		
		Taxonomy taxonomy1 = new Taxonomy("2005", "Groves_MSW3", hierarchyTaxa1);		
		Taxonomy taxonomy2 = new Taxonomy("1993", "Groves_MSW3", hierarchyTaxa2);
		
		Taxonomies taxonomies = new Taxonomies();
		taxonomies.add(taxonomy1);
		taxonomies.add(taxonomy2);
		
		Map<Taxon, Map<Taxon, List<Evidence>>> evidenceMap = new HashMap<Taxon, Map<Taxon, List<Evidence>>>();
		for(Taxon taxon1 : taxonomy1.getTaxaDFS()) {
			evidenceMap.put(taxon1, new HashMap<Taxon, List<Evidence>>());
			for(Taxon taxon2 : taxonomy2.getTaxaDFS()) {
				evidenceMap.get(taxon1).put(taxon2, createEvidence(taxon1, taxon2));
			}
		}
		
		evidenceMap.get(t4).get(t24).add(new Evidence("leaf length = 4", "leaf length = 4", 1.0, 0, DiagnosticValue.MEDIUM, Rank.SPECIES));
		evidenceMap.get(t4).get(t24).add(new Evidence("leaf shape = oblong", "leaf schape = lancelote", 0.66, 0, DiagnosticValue.MEDIUM, Rank.SPECIES));
		evidenceMap.get(t4).get(t24).add(new Evidence("size = long", "size = short to long", 0.5, 0, DiagnosticValue.MEDIUM, Rank.SPECIES));
		evidenceMap.get(t4).get(t24).add(new Evidence("color = green", "", -1, 1, DiagnosticValue.MEDIUM, Rank.SPECIES));
		evidenceMap.get(t4).get(t24).add(new Evidence("", "architecture = xyz", -1, 1, DiagnosticValue.MEDIUM, Rank.SPECIES));
		evidenceMap.get(t4).get(t24).add(new Evidence("", "pubescence = www", -1, 1, DiagnosticValue.MEDIUM, Rank.SPECIES));
		
		Articulations machineArticulations = new Articulations();
		machineArticulations.add(new Articulation(t4, t24, Relation.CONGRUENT, 1.0, Type.MACHINE));
		machineArticulations.add(new Articulation(t4, t24, Relation.A_INCLUDES_B, 1.0, Type.MACHINE));
		Model model = new Model(taxonomies, evidenceMap);
		model.addArticulations(machineArticulations);
		return model;
		
		/*Taxon t2 = new Taxon(Rank.FAMILY, "rosacea", "author2", "1980", "thi sis another description");
		List<Taxon> hierarchyTaxa1 = new LinkedList<Taxon>();
		hierarchyTaxa1.add(t1);
		for(int i=0; i<100; i++) {
			Taxon t = new Taxon(Rank.FAMILY, "rosacea1", "author1", "1979", "this is the description about t1");
			hierarchyTaxa1.add(t);
		}*/
			
		/*Taxonomy taxonomy1 = new Taxonomy("2005", "Groves_MSW3", hierarchyTaxa1);		
		Taxonomy taxonomy2 = new Taxonomy("1993", "Groves_MSW3", hierarchyTaxa2);
		
		Taxonomies taxonomies = new Taxonomies();
		taxonomies.add(taxonomy1);
		taxonomies.add(taxonomy2);
		
		/*
		try {
			taxonomies = new Taxonomies();
			FileTaxonomyReader reader = new FileTaxonomyReader("C:/Users/rodenhausen/etcsite/users/1068/euler/1.txt");
			Taxonomy taxA = reader.read();
			reader = new FileTaxonomyReader("C:/Users/rodenhausen/etcsite/users/1068/euler/2.txt");
			Taxonomy taxB = reader.read();
			taxonomies.add(taxA);
			taxonomies.add(taxB);
		} catch(Exception e) {
			e.printStackTrace();
		}*/
		
		/*TaxonomyFileReader reader1 = new TaxonomyFileReader("C:/Users/rodenhausen/Desktop/trash/1.txt");
		TaxonomyFileReader reader2 = new TaxonomyFileReader("C:/Users/rodenhausen/Desktop/trash/2.txt");
		Taxonomies taxonomies = new Taxonomies();
		Map<Taxon, Map<Taxon, List<Evidence>>> evidenceMap = new HashMap<Taxon, Map<Taxon, List<Evidence>>>();
		try {
			Taxonomy taxonomy1 = reader1.read();
			Taxonomy taxonomy2 = reader2.read();
			taxonomies.add(taxonomy1);
			taxonomies.add(taxonomy2);
			
			for(Taxon taxon1 : taxonomy1.getTaxaDFS()) {
				evidenceMap.put(taxon1, new HashMap<Taxon, List<Evidence>>());
				for(Taxon taxon2 : taxonomy2.getTaxaDFS()) {
					evidenceMap.get(taxon1).put(taxon2, createEvidence(taxon1, taxon2));
				}
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return new Model(taxonomies, evidenceMap);*/
		
		
	}



	private List<Evidence> createEvidence(Taxon taxon1, Taxon taxon2) {
		List<Evidence> result = new LinkedList<Evidence>();
		//result.add(new Evidence(taxon1, taxon2, "metus", "nibh", 0.5, 0, DiagnosticValue.MEDIUM, Rank.SPECIES));
		return result;
	}

	@Override
	public Articulations getArticulations(String text, Model model) throws Exception {
		ArticulationsReader articulationsReader = new ArticulationsReader();
		return articulationsReader.read(text, model);
	}

	@Override
	public SafeHtml getHighlighted(String content, Set<Highlight> highlights) {
		for(Highlight highlight : highlights) {
			Document document = Jsoup.parseBodyFragment(content);
			List<Node> result = new ArrayList<Node>();
			for(Node node : document.body().childNodes()) {
				if(node instanceof TextNode) {
					TextNode textNode = ((TextNode)node);
					String regex = createRegex(highlight);
					if(regex == null) {
						result.add(node);
					} else {
						List<Node> newNodes = createHighlightedNodes(textNode, regex, highlight.getColorHex());
						result.addAll(newNodes);
					}
				} else {
					result.add(node);
				}
			}
			Element body = new Element(Tag.valueOf("body"), "");
			for(Node newNode : result)
				body.appendChild(newNode);
			content = body.toString();
		}
		return SafeHtmlUtils.fromTrustedString(content);
	}

	private String createRegex(Highlight highlight) {
		String parts = "";
		for(String part : highlight.getText().trim().split(" ")) {
			if(!part.isEmpty())
				parts += Pattern.quote(part) + "|";
		}
		if(!parts.isEmpty())
			parts = parts.substring(0, parts.length() - 1);
		if(!parts.isEmpty())
			return "\\b(" + parts + ")\\b";
		return null;
	}

	private List<Node> createHighlightedNodes(TextNode textNode, String regex, String colorHex) {
		List<Node> result = new ArrayList<Node>();
		String text = textNode.text();
		StringBuilder textBuilder = new StringBuilder(text);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		if(!matcher.find()) {
			result.add(textNode);
			return result;
		}
		StringBuilder beforeReplaceText = new StringBuilder(textBuilder.substring(0, matcher.start(1)));
		String afterReplaceText = textBuilder.substring(matcher.end(1), textBuilder.length());
		result.add(new TextNode(beforeReplaceText.toString(), ""));
		Element fontElement = new Element(Tag.valueOf("font"), "");
		fontElement.attr("color", "#" + colorHex);
		fontElement.text(text.substring(matcher.start(1), matcher.end(1)));
		result.add(fontElement);
		result.addAll(createHighlightedNodes(new TextNode(afterReplaceText, ""), regex, colorHex));
		return result;
	}

	@Override
	public Collection createCollection(Collection collection) throws QueryException, IOException {
		collection = collectionDAO.insert(collection);
		return collection;
	}
}
