package edu.arizona.biosemantics.euler.alignment.server;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.shared.IEulerAlignmentService;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomies;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;

@SuppressWarnings("serial")
public class EulerAlignmentService extends RemoteServiceServlet implements IEulerAlignmentService {

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
				"prototype", "author4", "2014", 
				"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. "
						+ "Sed metus nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. "
						+ "Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, "
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
						+ "Sed metus nibh, sodales a, porta at, vulputate eget, dui. Pellentesque ut nisl. "
						+ "Maecenas tortor turpis, interdum non, sodales non, iaculis ac, lacus. Vestibulum auctor, "
						+ "tortor quis iaculis malesuada, libero lectus bibendum purus, sit amet tincidunt quam turpis "
						+ "vel lacus. In pellentesque nisl non sem. Suspendisse nunc sem, pretium eget, cursus a, "
						+ "fringilla vel, urna.<br/><br/>Aliquam commodo ullamcorper erat. Nullam vel justo in neque "
						+ "porttitor laoreet. Aenean lacus dui, consequat eu, adipiscing eget, nonummy non, nisi. "
						+ "Morbi nunc est, dignissim non, ornare sed, luctus eu, massa. Vivamus eget quam. Vivamus "
						+ "tincidunt diam nec urna. Curabitur velit.");
		t21.addChild(t24);
		t21.addChild(t23);
		t21.addChild(t22);
		
		List<Taxon> hierarchyTaxa2 = new LinkedList<Taxon>();
		hierarchyTaxa2.add(t21);
		
		
		/*Taxon t2 = new Taxon(Rank.FAMILY, "rosacea", "author2", "1980", "thi sis another description");
		List<Taxon> hierarchyTaxa1 = new LinkedList<Taxon>();
		hierarchyTaxa1.add(t1);
		for(int i=0; i<100; i++) {
			Taxon t = new Taxon(Rank.FAMILY, "rosacea1", "author1", "1979", "this is the description about t1");
			hierarchyTaxa1.add(t);
		}*/
			
		Taxonomy taxonomy1 = new Taxonomy("2005 Groves_MSW3", hierarchyTaxa1);		
		Taxonomy taxonomy2 = new Taxonomy("1993 Groves_MSW3", hierarchyTaxa2);
		
		Taxonomies taxonomies = new Taxonomies();
		taxonomies.add(taxonomy1);
		taxonomies.add(taxonomy2);
		return new Model(taxonomies);
	}

}
