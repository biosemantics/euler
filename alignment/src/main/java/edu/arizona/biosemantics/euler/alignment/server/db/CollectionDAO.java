package edu.arizona.biosemantics.euler.alignment.server.db;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;

import au.com.bytecode.opencsv.CSVReader;
import edu.arizona.biosemantics.common.biology.TaxonGroup;
import edu.arizona.biosemantics.common.ling.know.IGlossary;
import edu.arizona.biosemantics.common.ling.know.SingularPluralProvider;
import edu.arizona.biosemantics.common.ling.know.Term;
import edu.arizona.biosemantics.common.ling.know.lib.InMemoryGlossary;
import edu.arizona.biosemantics.common.ling.know.lib.WordNetPOSKnowledgeBase;
import edu.arizona.biosemantics.common.ling.transform.IInflector;
import edu.arizona.biosemantics.common.ling.transform.lib.SomeInflector;
import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.euler.alignment.server.Configuration;
import edu.arizona.biosemantics.euler.alignment.server.db.Query.QueryException;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.AnnotationProperty;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.PartOfModel;
import edu.arizona.biosemantics.euler.alignment.shared.model.Collection;
import edu.arizona.biosemantics.euler.alignment.shared.model.Model;
import edu.arizona.biosemantics.oto.client.oto.OTOClient;
import edu.arizona.biosemantics.oto.model.GlossaryDownload;
import edu.arizona.biosemantics.oto.model.TermCategory;
import edu.arizona.biosemantics.oto.model.TermSynonym;
import edu.arizona.biosemantics.oto.model.lite.Decision;
import edu.arizona.biosemantics.oto.model.lite.Download;
import edu.arizona.biosemantics.oto.model.lite.Synonym;

public class CollectionDAO {
		
	public CollectionDAO() {} 
	
	public boolean isValidSecret(int id, String secret) throws QueryException  {
		try(Query query = new Query("SELECT secret FROM alignment_collection WHERE id = ?")) {
			query.setParameter(1, id);
			ResultSet result = query.execute();
			while(result.next()) {
				String validSecret = result.getString(1);
				return validSecret.equals(secret);
			}
		} catch(QueryException | SQLException e) {
			log(LogLevel.ERROR, "Query Exception", e);
			throw new QueryException(e);
		}
		return false;
	}
	
	public Collection get(int id) throws Exception  {
		Collection collection = null;
		try(Query query = new Query("SELECT * FROM alignment_collection WHERE id = ?")) {
			query.setParameter(1, id);
			ResultSet result = query.execute();
			while(result.next()) {
				collection = createCollection(result);
			}
		} catch(QueryException | SQLException e) {
			log(LogLevel.ERROR, "Query Exception", e);
			throw new QueryException(e);
		}
		
		try(Query query = new Query("UPDATE alignment_collection SET lastretrieved = ? WHERE id = ?")) {
			query.setParameter(2, id);
			Date date = new Date();
			query.setParameter(1, new Timestamp(date.getTime()));
			query.execute();
		} catch(QueryException e) {
			log(LogLevel.ERROR, "Query Exception", e);
			throw e;
		}
		
		return collection;
	}
	
	private Collection createCollection(ResultSet result) throws Exception {
		int id = result.getInt("id");
		String secret = result.getString("secret");
		String glossaryPath = result.getString("glossary_path");
		String ontologyPath = result.getString("ontology_path");
		TaxonGroup taxonGroup = TaxonGroup.valueOf(result.getString("taxon_group"));
		return new Collection(id, secret, taxonGroup, unserializeModel(id), glossaryPath, ontologyPath);
	}
	
	private void serializeModel(Collection collection) throws IOException {
		try(ObjectOutput output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(
				Configuration.collectionsPath + File.separator + collection.getId() + File.separator + "Model.ser")))) {
			output.writeObject(collection.getModel());
		}
	}
	
	private void createAndSerializeOntologyPartOfModel(Collection collection) throws IOException {
		PartOfModelCreator partOfModelCreator = new PartOfModelCreator();
		PartOfModel partOfModel = partOfModelCreator.create(collection.getOntologyPath());
		try(ObjectOutput output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(
				Configuration.collectionsPath + File.separator + collection.getId() + File.separator + "PartOf.ser")))) {
			output.writeObject(partOfModel);
		}
	}
	
	public InMemoryGlossary unserializeGlossary(int collectionId) throws IOException, ClassNotFoundException {
		try(ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(
				Configuration.collectionsPath + File.separator + collectionId + File.separator + "Glossary.ser")))) {
			InMemoryGlossary glossary = (InMemoryGlossary)input.readObject();
			return glossary;
		}
	}
	
	public PartOfModel unserializePartOfModel(int collectionId) throws IOException, ClassNotFoundException {
		try(ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(
				Configuration.collectionsPath + File.separator + collectionId + File.separator + "PartOf.ser")))) {
			PartOfModel partOfModel = (PartOfModel)input.readObject();
			return partOfModel;
		}
	}

	private void createAndSerializeGlossary(Collection collection) throws IOException {
		GlossaryCreator glossaryCreator = new GlossaryCreator();
		File dir = new File(collection.getGlossaryPath());
		if(dir.exists() && dir.isDirectory()) {
			File categoryTermFile = null;
			File synonymFile = null;
			for(File file : new File(collection.getGlossaryPath()).listFiles()) {
				if(file.getName().startsWith("category_term") && file.getName().endsWith(".csv")) {
					categoryTermFile = file;
				}
				if(file.getName().startsWith("category_mainterm_synonymterm") && file.getName().endsWith(".csv")) {
					synonymFile = file;
				}
			}
			InMemoryGlossary glossary = glossaryCreator.create(collection.getTaxonGroup(), categoryTermFile, synonymFile);
			try(ObjectOutput output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(
					Configuration.collectionsPath + File.separator + collection.getId() + File.separator + "Glossary.ser")))) {
				output.writeObject(glossary);
			}
		}
	}

	private Model unserializeModel(int collectionId) throws FileNotFoundException, IOException, ClassNotFoundException {
		try(ObjectInput input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(
				Configuration.collectionsPath + File.separator + collectionId + File.separator + "Model.ser")))) {
			Model model = (Model)input.readObject();
			return model;
		}
	}
	
	public Collection insert(Collection collection) throws QueryException, IOException  {
		if(collection.hasId()) 
			this.remove(collection);
		try(Query insert = new Query("INSERT INTO `alignment_collection` (`secret`, `taxon_group`, `glossary_path`, `ontology_path`) VALUES(?, ?, ?, ?)")) {
			insert.setParameter(1, collection.getSecret());
			insert.setParameter(2, collection.getTaxonGroup().toString());
			insert.setParameter(3, collection.getGlossaryPath());
			insert.setParameter(4, collection.getOntologyPath());
			insert.execute();
			ResultSet generatedKeys = insert.getGeneratedKeys();
			generatedKeys.next();
			int id = generatedKeys.getInt(1);
			collection.setId(id);
			
			File file = new File(Configuration.collectionsPath + File.separator + id);
			file.mkdirs();
			serializeModel(collection);
			createAndSerializeGlossary(collection);
			createAndSerializeOntologyPartOfModel(collection);
		} catch(QueryException | SQLException e) {
			log(LogLevel.ERROR, "Query Exception", e);
			throw new QueryException(e);
		}
		return collection;
	}

	public void update(Collection collection) throws QueryException, IOException  {		
		try(Query query = new Query("UPDATE alignment_collection SET secret = ?, taxon_group = ?, glossary_path = ?, ontology_path = ? WHERE id = ?")) {
			query.setParameter(1, collection.getSecret());
			query.setParameter(2, collection.getTaxonGroup().toString());
			query.setParameter(3, collection.getGlossaryPath());
			query.setParameter(4, collection.getOntologyPath());
			query.setParameter(5, collection.getId());
			query.execute();
		} catch(QueryException e) {
			log(LogLevel.ERROR, "Query Exception", e);
			throw e;
		}
	}
	
	public void remove(Collection collection) throws QueryException  {
		try(Query query = new Query("DELETE FROM alignment_collection WHERE id = ?")) {
			query.setParameter(1, collection.getId());
			query.execute();
		} catch(QueryException e) {
			log(LogLevel.ERROR, "Query Exception", e);
			throw e;
		}
	}
}