package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class ClassMiscTest {


    @ParameterizedTest
    @EnumSource
    public void testDatatypeIsClass(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        Resource c = m.createResource();
        c.addProperty(RDF.type, RDFS.Datatype);
        Assertions.assertTrue(c.canAs(OntClass.class));
    }
}
