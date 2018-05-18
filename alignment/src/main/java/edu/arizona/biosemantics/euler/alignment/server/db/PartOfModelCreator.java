package edu.arizona.biosemantics.euler.alignment.server.db;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.apibinding.OWLManager;
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

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.AnnotationProperty;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.PartOfModel;

public class PartOfModelCreator {

	public PartOfModel create(String ontologyPath) {
		PartOfModel partOfModel = new PartOfModel();
		if(ontologyPath == null || (!new File(ontologyPath).exists()))
			return partOfModel;
		
		OWLOntologyManager owlOntologyManager = OWLManager.createOWLOntologyManager();
		try {
			OWLOntology owlOntology = owlOntologyManager.loadOntologyFromOntologyDocument(new File(ontologyPath));
			OWLAnnotationProperty labelProperty = owlOntologyManager.getOWLDataFactory().getOWLAnnotationProperty(
					OWLRDFVocabulary.RDFS_LABEL.getIRI());
			
			//assumption for passed ontologies: labels are unique identifiers		
			Map<String, OWLClass> iriClassMap = new HashMap<String, OWLClass>();
			for(OWLClass owlClass : owlOntology.getClassesInSignature())
				iriClassMap.put(owlClass.getIRI().toString(), owlClass);
			
			for(OWLClass owlClass : owlOntology.getClassesInSignature()) {
				Set<OWLAxiom> axioms = EntitySearcher.getReferencingAxioms(owlClass, owlOntology).collect(Collectors.toSet());
				for(OWLAxiom axiom : axioms ) {
					if(axiom instanceof OWLSubClassOfAxiom) {
						OWLSubClassOfAxiom owlSubClassOfAxiom = (OWLSubClassOfAxiom)axiom;
						OWLClassExpression owlClassExpression = owlSubClassOfAxiom.getSuperClass();
						if(owlClassExpression instanceof OWLObjectSomeValuesFrom) {
							OWLObjectSomeValuesFrom owlObjectSomeValuesFrom = (OWLObjectSomeValuesFrom)owlClassExpression;
							
							OWLObjectPropertyExpression property = owlObjectSomeValuesFrom.getProperty();
							if(property instanceof OWLObjectProperty) {
								OWLObjectProperty owlObjectProperty = (OWLObjectProperty)property;
								if(owlObjectProperty.getIRI().toString().equals(AnnotationProperty.PART_OF.getIRI())) {
									OWLClassExpression filler = owlObjectSomeValuesFrom.getFiller();
									if(filler instanceof OWLClass) {
										OWLClass fillerClass = (OWLClass)filler;
										if(!fillerClass.getIRI().equals(owlClass.getIRI())) {
											OWLClass bearerClass = iriClassMap.get(fillerClass.getIRI().toString());
											String bearerLabel = getLabel(bearerClass, owlOntology, labelProperty);
											String partLabel = getLabel(owlClass, owlOntology, labelProperty);
											
											partOfModel.addPartOfRelation(partLabel, bearerLabel);
										}
									}
								}
							}						
						}
					}
				}
			}
		} catch(OWLOntologyCreationException e) {
			log(LogLevel.ERROR, "Could not read ontology.", e);
		}
			
		return partOfModel;
	}
	
	private static String getLabel(OWLClass owlClass, OWLOntology owlOntology, OWLAnnotationProperty labelProperty) {
		Set<OWLAnnotation> annotations = EntitySearcher.getAnnotations(owlClass, owlOntology, labelProperty).collect(Collectors.toSet());
		for (OWLAnnotation annotation : annotations) {
			if (annotation.getValue() instanceof OWLLiteral) {
				OWLLiteral val = (OWLLiteral) annotation.getValue();
				//if (val.hasLang("en")) {
				return val.getLiteral();
				//}
			}
		}
		return null;
	}
	
}
