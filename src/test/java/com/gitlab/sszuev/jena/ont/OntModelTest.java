package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.common.CommonOntTestBase;
import com.gitlab.sszuev.jena.ont.testutils.IOTestUtils;
import com.gitlab.sszuev.jena.ont.testutils.JunitExtensions;
import com.gitlab.sszuev.jena.ont.testutils.ModelTestUtils;
import com.gitlab.sszuev.jena.ont.testutils.Spec;
import org.apache.jena.graph.Graph;
import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.DataRange;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.HasValueRestriction;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.ontology.impl.OWLDLProfile;
import org.apache.jena.ontology.impl.OWLLiteProfile;
import org.apache.jena.ontology.impl.OWLProfile;
import org.apache.jena.ontology.impl.OntModelImpl;
import org.apache.jena.ontology.impl.TestListSyntaxCategories;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class OntModelTest extends CommonOntTestBase {
    public static final String BASE = "http://www.hp.com/test";
    public static final String NS = BASE + "#";

    public static final String DOC = "<rdf:RDF" +
            "   xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"" +
            "   xmlns:owl=\"http://www.w3.org/2002/07/owl#\"" +
            "   xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\">" +
            "  <owl:Class rdf:about=\"http://www.hp.com/test#D\">" +
            "    <rdfs:subClassOf>" +
            "      <owl:Class rdf:about=\"http://www.hp.com/test#B\"/>" +
            "    </rdfs:subClassOf>" +
            "  </owl:Class>" +
            "  <owl:Class rdf:about=\"http://www.hp.com/test#B\">" +
            "    <rdfs:subClassOf rdf:resource=\"http://www.hp.com/test#A\"" +
            "       rdf:type=\"http://www.w3.org/2002/07/owl#Class\"/>" +
            "  </owl:Class>" +
            "  <owl:Class rdf:about=\"http://www.hp.com/test#C\">" +
            "    <rdfs:subClassOf rdf:resource=\"http://www.hp.com/test#B\"/>" +
            "  </owl:Class>" +
            "  <owl:ObjectProperty rdf:about=\"http://www.hp.com/test#p\">" +
            "    <rdfs:domain rdf:resource=\"http://www.hp.com/test#A\"/>" +
            "    <rdfs:range rdf:resource=\"http://www.hp.com/test#B\"/>" +
            "    <rdfs:range rdf:resource=\"http://www.hp.com/test#C\"/>" +
            "  </owl:ObjectProperty>" +
            "</rdf:RDF>";


    @ParameterizedTest
    @EnumSource(Spec.class)
    public void testListIndividuals(Spec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.spec);

        m.createResource("x", m.createResource("X"));
        m.createResource().addProperty(RDF.type, m.createResource("Y"));

        OntClass clazz = m.createClass("Q");
        clazz.createIndividual("q");
        clazz.createIndividual();

        m.write(System.out, "ttl");
        List<Individual> individuals = m.listIndividuals().toList();

        int expectedNumOfIndividuals = spec == Spec.RDFS_MEM_RDFS_INF ? 4 : 2;
        Assertions.assertEquals(expectedNumOfIndividuals, individuals.size());
    }

    /**
     * Test writing the base model to an output stream
     */
    @Test
    public void testWriteOutputStream() {
        OntModel m = ModelFactory.createOntologyModel();

        // set up the model
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");

        A.addSubClass(B);
        B.addSubClass(C);
        B.addSubClass(D);

        ObjectProperty p = m.createObjectProperty(NS + "p");

        p.addDomain(A);
        p.addRange(B);
        p.addRange(C);

        // write to a stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        m.write(out);

        String s = out.toString();
        ByteArrayInputStream in = new ByteArrayInputStream(s.getBytes());

        // read it back again
        Model mIn1 = ModelFactory.createDefaultModel();
        mIn1.read(in, BASE);

        Model mIn2 = ModelFactory.createDefaultModel();
        mIn2.read(new ByteArrayInputStream(DOC.getBytes()), BASE);

        // should be the same
        Assertions.assertTrue(mIn1.isIsomorphicWith(m.getBaseModel()), "InputStream write/read cycle failed (1)");
        Assertions.assertTrue(mIn2.isIsomorphicWith(m.getBaseModel()), "InputStream write/read cycle failed (2)");
    }

    @Test
    public void testGetBaseModelPrefixes() {
        OntModel om = ModelFactory.createOntologyModel();
        om.setNsPrefix("bill", "http://bill.and.ben/flowerpot#");
        om.setNsPrefix("grue", "ftp://grue.and.bleen/2000#");
        Assertions.assertEquals(om.getNsPrefixMap(), om.getBaseModel().getNsPrefixMap());
    }

    /**
     * The default namespace pefix of a non-base-model should not manifest as
     * the default namespace prefix of the base model or the Ont model.
     */
    @Test
    public void testPolyadicPrefixMapping() {
        final String IMPORTED_NAMESPACE = "http://imported#";
        final String LOCAL_NAMESPACE = "http://local#";
        Model importedModel = ModelFactory.createDefaultModel();
        importedModel.setNsPrefix("", IMPORTED_NAMESPACE);
        OntModel ontModel = ModelFactory.createOntologyModel();
        ontModel.setNsPrefix("", LOCAL_NAMESPACE);
        ontModel.addSubModel(importedModel);
        Assertions.assertNull(ontModel.getNsURIPrefix(IMPORTED_NAMESPACE));
    }

    @Test
    public void testWritesPrefixes() {
        OntModel om = ModelFactory.createOntologyModel();
        om.setNsPrefix("spoo", "http://spoo.spoo.com/spoo#");
        om.add(ModelTestUtils.statement(om, "ping http://spoo.spoo.com/spoo#pang pilly"));
        om.add(ModelTestUtils.statement(om, "gg " + OWL.getURI() + "hh ii"));
        StringWriter sw = new StringWriter();
        om.write(sw);
        String s = sw.getBuffer().toString();
        Assertions.assertTrue(s.indexOf("xmlns:spoo=\"http://spoo.spoo.com/spoo#\"") > 0);
        Assertions.assertTrue(s.indexOf("xmlns:owl=\"" + OWL.getURI() + "\"") > 0);
    }

    /**
     * Test writing the base model to an output stream
     */
    @Test
    public void testWriteWriter() {
        OntModel m = ModelFactory.createOntologyModel();

        // set up the model
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");

        A.addSubClass(B);
        B.addSubClass(C);
        B.addSubClass(D);

        ObjectProperty p = m.createObjectProperty(NS + "p");

        p.addDomain(A);
        p.addRange(B);
        p.addRange(C);

        // write to a stream
        StringWriter out = new StringWriter();
        m.write(out);

        String s = out.toString();

        // read it back again
        Model mIn1 = ModelFactory.createDefaultModel();
        mIn1.read(new StringReader(s), BASE);

        Model mIn2 = ModelFactory.createDefaultModel();
        mIn2.read(new StringReader(DOC), BASE);

        // should be the same
        Assertions.assertTrue(mIn1.isIsomorphicWith(m.getBaseModel()), "Writer write/read cycle failed (1)");
        Assertions.assertTrue(mIn2.isIsomorphicWith(m.getBaseModel()), "Writer write/read cycle failed (2)");
    }

    @Test
    public void testGetOntology() {
        OntModel m = ModelFactory.createOntologyModel();
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createOntology(NS + "s");
        Assertions.assertEquals(s, m.getOntology(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getOntology(NS + "q"), "result of get q");
        Assertions.assertNull(m.getOntology(NS + "r"), "result of get r");
    }


    @Test
    public void testGetIndividual() {
        OntModel m = ModelFactory.createOntologyModel();
        OntClass c = m.createClass(NS + "c");
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createIndividual(NS + "s", c);
        Assertions.assertEquals(s, m.getIndividual(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getIndividual(NS + "q"), "result of get q");
    }

    /**
     * User requested: allow null arguments when creating individuals
     */
    @Test
    public void testCreateIndividual() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        Resource i0 = m.createIndividual(OWL.Thing);
        Resource i1 = m.createIndividual(null);
        Resource i2 = m.createIndividual(NS + "i2", OWL.Thing);
        Resource i3 = m.createIndividual(NS + "i3", null);
        Resource i4 = m.createIndividual(null, OWL.Thing);
        Resource i5 = m.createIndividual(null, null);

        Assertions.assertNotNull(i0);
        Assertions.assertNotNull(i1);
        Assertions.assertNotNull(i2);
        Assertions.assertNotNull(i3);
        Assertions.assertNotNull(i4);
        Assertions.assertNotNull(i5);
    }

    @Test
    public void testGetOntProperty() {
        OntModel m = ModelFactory.createOntologyModel();
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createOntProperty(NS + "s");
        Assertions.assertEquals(s, m.getOntProperty(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getOntProperty(NS + "q"), "result of get q");
        Assertions.assertNull(m.getOntProperty(NS + "r"), "result of get r");
    }


    @Test
    public void testGetObjectProperty() {
        OntModel m = ModelFactory.createOntologyModel();
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createObjectProperty(NS + "s");
        Assertions.assertEquals(s, m.getObjectProperty(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getObjectProperty(NS + "q"), "result of get q");
        Assertions.assertNull(m.getObjectProperty(NS + "r"), "result of get r");
    }


    @Test
    public void testGetTransitiveProperty() {
        OntModel m = ModelFactory.createOntologyModel();
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createTransitiveProperty(NS + "s");
        Assertions.assertEquals(s, m.getTransitiveProperty(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getTransitiveProperty(NS + "q"), "result of get q");
        Assertions.assertNull(m.getTransitiveProperty(NS + "r"), "result of get r");
    }


    @Test
    public void testGetSymmetricProperty() {
        OntModel m = ModelFactory.createOntologyModel();
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createSymmetricProperty(NS + "s");
        Assertions.assertEquals(s, m.getSymmetricProperty(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getSymmetricProperty(NS + "q"), "result of get q");
        Assertions.assertNull(m.getSymmetricProperty(NS + "r"), "result of get r");
    }


    @Test
    public void testGetInverseFunctionalProperty() {
        OntModel m = ModelFactory.createOntologyModel();
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createInverseFunctionalProperty(NS + "s");
        Assertions.assertEquals(s, m.getInverseFunctionalProperty(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getInverseFunctionalProperty(NS + "q"), "result of get q");
        Assertions.assertNull(m.getInverseFunctionalProperty(NS + "r"), "result of get r");
    }


    @Test
    public void testGetDatatypeProperty() {
        OntModel m = ModelFactory.createOntologyModel();
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createDatatypeProperty(NS + "s");
        Assertions.assertEquals(s, m.getDatatypeProperty(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getDatatypeProperty(NS + "q"), "result of get q");
        Assertions.assertNull(m.getDatatypeProperty(NS + "r"), "result of get r");
    }


    @Test
    public void testGetAnnotationProperty() {
        OntModel m = ModelFactory.createOntologyModel();
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createAnnotationProperty(NS + "s");
        Assertions.assertEquals(s, m.getAnnotationProperty(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getAnnotationProperty(NS + "q"), "result of get q");
        Assertions.assertNull(m.getAnnotationProperty(NS + "r"), "result of get r");
    }

    @Test
    public void testGetOntResource() {
        OntModel m = ModelFactory.createOntologyModel();
        OntResource r0 = m.getOntResource(NS + "a");
        Assertions.assertNull(r0);
        OntResource r1 = m.createOntResource(NS + "aaa");
        Assertions.assertInstanceOf(OntResource.class, r1);
        Resource r2a = m.getResource(NS + "a");
        Resource r2b = m.getResource(NS + "b");
        Property p = m.getProperty(NS + "p");
        m.add(r2a, p, r2b);
        r0 = m.getOntResource(NS + "a");
        Assertions.assertInstanceOf(OntResource.class, r0);
        OntResource r3 = m.getOntResource(r2b);
        Assertions.assertInstanceOf(OntResource.class, r3);
    }

    @Test
    public void testGetOntClass() {
        OntModel m = ModelFactory.createOntologyModel();
        Resource r = m.getResource(NS + "r");
        Resource r0 = m.getResource(NS + "r0");
        m.add(r, RDF.type, r0);
        Resource s = m.createClass(NS + "s");
        Assertions.assertEquals(s, m.getOntClass(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getOntClass(NS + "q"), "result of get q");
        Assertions.assertNull(m.getOntClass(NS + "r"), "result of get r");
    }


    @Test
    public void testGetComplementClass() {
        OntModel m = ModelFactory.createOntologyModel();
        OntClass c = m.createClass(NS + "c");
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createComplementClass(NS + "s", c);
        Assertions.assertEquals(s, m.getComplementClass(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getComplementClass(NS + "q"), "result of get q");
        Assertions.assertNull(m.getComplementClass(NS + "r"), "result of get r");
    }


    @Test
    public void testGetEnumeratedClass() {
        OntModel m = ModelFactory.createOntologyModel();
        RDFList l = m.createList();
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createEnumeratedClass(NS + "s", l);
        Assertions.assertEquals(s, m.getEnumeratedClass(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getEnumeratedClass(NS + "q"), "result of get q");
        Assertions.assertNull(m.getEnumeratedClass(NS + "r"), "result of get r");
    }


    @Test
    public void testGetUnionClass() {
        OntModel m = ModelFactory.createOntologyModel();
        RDFList l = m.createList();
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createUnionClass(NS + "s", l);
        Assertions.assertEquals(s, m.getUnionClass(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getUnionClass(NS + "q"), "result of get q");
        Assertions.assertNull(m.getUnionClass(NS + "r"), "result of get r");
    }


    @Test
    public void testGetIntersectionClass() {
        OntModel m = ModelFactory.createOntologyModel();
        RDFList l = m.createList();
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createIntersectionClass(NS + "s", l);
        Assertions.assertEquals(s, m.getIntersectionClass(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getIntersectionClass(NS + "q"), "result of get q");
        Assertions.assertNull(m.getIntersectionClass(NS + "r"), "result of get r");
    }


    @Test
    public void testGetRestriction() {
        OntModel m = ModelFactory.createOntologyModel();
        Property p = m.createProperty(NS + "p");
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createRestriction(NS + "s", p);
        Assertions.assertEquals(s, m.getRestriction(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getRestriction(NS + "q"), "result of get q");
        Assertions.assertNull(m.getRestriction(NS + "r"), "result of get r");
    }


    @Test
    public void testGetHasValueRestriction() {
        OntModel m = ModelFactory.createOntologyModel();
        Property p = m.createProperty(NS + "p");
        OntClass c = m.createClass(NS + "c");
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createHasValueRestriction(NS + "s", p, c);
        Assertions.assertEquals(s, m.getHasValueRestriction(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getHasValueRestriction(NS + "q"), "result of get q");
        Assertions.assertNull(m.getHasValueRestriction(NS + "r"), "result of get r");
    }


    @Test
    public void testGetSomeValuesFromRestriction() {
        OntModel m = ModelFactory.createOntologyModel();
        Property p = m.createProperty(NS + "p");
        OntClass c = m.createClass(NS + "c");
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createSomeValuesFromRestriction(NS + "s", p, c);
        Assertions.assertEquals(s, m.getSomeValuesFromRestriction(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getSomeValuesFromRestriction(NS + "q"), "result of get q");
        Assertions.assertNull(m.getSomeValuesFromRestriction(NS + "r"), "result of get r");
    }


    @Test
    public void testGetAllValuesFromRestriction() {
        OntModel m = ModelFactory.createOntologyModel();
        Property p = m.createProperty(NS + "p");
        OntClass c = m.createClass(NS + "c");
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createAllValuesFromRestriction(NS + "s", p, c);
        Assertions.assertEquals(s, m.getAllValuesFromRestriction(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getAllValuesFromRestriction(NS + "q"), "result of get q");
        Assertions.assertNull(m.getAllValuesFromRestriction(NS + "r"), "result of get r");
    }


    @Test
    public void testGetCardinalityRestriction() {
        OntModel m = ModelFactory.createOntologyModel();
        Property p = m.createProperty(NS + "p");
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createCardinalityRestriction(NS + "s", p, 1);
        Assertions.assertEquals(s, m.getCardinalityRestriction(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getCardinalityRestriction(NS + "q"), "result of get q");
        Assertions.assertNull(m.getCardinalityRestriction(NS + "r"), "result of get r");
    }


    @Test
    public void testGetMinCardinalityRestriction() {
        OntModel m = ModelFactory.createOntologyModel();
        Property p = m.createProperty(NS + "p");
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createMinCardinalityRestriction(NS + "s", p, 1);
        Assertions.assertEquals(s, m.getMinCardinalityRestriction(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getMinCardinalityRestriction(NS + "q"), "result of get q");
        Assertions.assertNull(m.getMinCardinalityRestriction(NS + "r"), "result of get r");
    }


    @Test
    public void testGetMaxCardinalityRestriction() {
        OntModel m = ModelFactory.createOntologyModel();
        Property p = m.createProperty(NS + "p");
        Resource r = m.getResource(NS + "r");
        m.add(r, RDF.type, r);
        Resource s = m.createMaxCardinalityRestriction(NS + "s", p, 1);
        Assertions.assertEquals(s, m.getMaxCardinalityRestriction(NS + "s"), "Result of get s");
        Assertions.assertNull(m.getMaxCardinalityRestriction(NS + "q"), "result of get q");
        Assertions.assertNull(m.getMaxCardinalityRestriction(NS + "r"), "result of get r");
    }

    @Test
    public void testGetSubgraphs() {
        OntModel m = ModelFactory.createOntologyModel();
        m.read(IOTestUtils.normalize("file:jena/ont-model-test-import-6-a.owl"));
        Assertions.assertEquals(4, ModelTestUtils.countMarkers(m), "Marker count not correct");

        List<Graph> subs = m.getSubGraphs();

        Assertions.assertEquals(3, subs.size(), "n subgraphs should be ");
    }

    private static boolean hasImport(Collection<String> c, String x) {
        String x2 = x.substring("file:".length());
        return c.stream().anyMatch(elt -> elt.endsWith(x2));
    }


    @Test
    public void testListImportURIs() {
        OntModel m = ModelFactory.createOntologyModel();
        m.read(IOTestUtils.normalize("file:jena/ont-model-test-import-6-a.owl"));
        Collection<String> c = m.listImportedOntologyURIs();

        Assertions.assertEquals(2, c.size(), "Should be two non-closed import URI's");
        Assertions.assertTrue(hasImport(c, IOTestUtils.normalize("file:jena/ont-model-test-import-6-b.owl")), "b should be imported ");
        Assertions.assertFalse(hasImport(c, IOTestUtils.normalize("file:jena/ont-model-test-import-6-c.owl")), "c should not be imported ");
        Assertions.assertTrue(hasImport(c, IOTestUtils.normalize("file:jena/ont-model-test-import-6-d.owl")), "d should be imported ");

        c = m.listImportedOntologyURIs(true);

        Assertions.assertEquals(3, c.size(), "Should be two non-closed import URI's");
        Assertions.assertTrue(hasImport(c, IOTestUtils.normalize("file:jena/ont-model-test-import-6-b.owl")), "b should be imported ");
        Assertions.assertTrue(hasImport(c, IOTestUtils.normalize("file:jena/ont-model-test-import-6-c.owl")), "c should be imported ");
        Assertions.assertTrue(hasImport(c, IOTestUtils.normalize("file:jena/ont-model-test-import-6-d.owl")), "d should be imported ");
    }

    /**
     * Some tests for listing properties. See also {@link TestListSyntaxCategories}
     */

    @Test
    public void testListOntProperties0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        ObjectProperty op = m.createObjectProperty(NS + "op");
        DatatypeProperty dp = m.createDatatypeProperty(NS + "dp");
        AnnotationProperty ap = m.createAnnotationProperty(NS + "ap");
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        // no rdf:type entailment, so we don't find most properties ...

        Assertions.assertFalse(iteratorContains(m.listOntProperties(), op));
        Assertions.assertFalse(iteratorContains(m.listOntProperties(), dp));
        Assertions.assertFalse(iteratorContains(m.listOntProperties(), ap));
        Assertions.assertTrue(iteratorContains(m.listOntProperties(), ontp));
        Assertions.assertTrue(iteratorContains(m.listOntProperties(), rdfp));
    }

    @Test
    public void testListOntProperties1() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        ObjectProperty op = m.createObjectProperty(NS + "op");
        DatatypeProperty dp = m.createDatatypeProperty(NS + "dp");
        AnnotationProperty ap = m.createAnnotationProperty(NS + "ap");
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        Assertions.assertTrue(iteratorContains(m.listOntProperties(), op));
        Assertions.assertTrue(iteratorContains(m.listOntProperties(), dp));

        // note that owl:AnnotationProperty is an rdf:Property in OWL Full
        Assertions.assertTrue(iteratorContains(m.listOntProperties(), ap));
        Assertions.assertTrue(iteratorContains(m.listOntProperties(), ontp));
        Assertions.assertTrue(iteratorContains(m.listOntProperties(), rdfp));
    }

    @Test
    public void testListOntProperties2() {
        OntModelSpec owlDLReasoner = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
        owlDLReasoner.setReasoner(OntModelSpec.OWL_MEM_MICRO_RULE_INF.getReasoner());
        OntModel m = ModelFactory.createOntologyModel(owlDLReasoner);
        ObjectProperty op = m.createObjectProperty(NS + "op");
        DatatypeProperty dp = m.createDatatypeProperty(NS + "dp");
        AnnotationProperty ap = m.createAnnotationProperty(NS + "ap");
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        Assertions.assertTrue(iteratorContains(m.listOntProperties(), op));
        Assertions.assertTrue(iteratorContains(m.listOntProperties(), dp));

        // note that owl:AnnotationProperty not an rdf:Property in OWL DL
        Assertions.assertFalse(iteratorContains(m.listOntProperties(), ap));
        Assertions.assertTrue(iteratorContains(m.listOntProperties(), ontp));
        Assertions.assertTrue(iteratorContains(m.listOntProperties(), rdfp));
    }


    @Test
    public void testListAllOntProperties0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        ObjectProperty op = m.createObjectProperty(NS + "op");
        DatatypeProperty dp = m.createDatatypeProperty(NS + "dp");
        AnnotationProperty ap = m.createAnnotationProperty(NS + "ap");
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        // no rdf:type entailment, so we don't find most properties ...

        Assertions.assertTrue(iteratorContains(m.listAllOntProperties(), op));
        Assertions.assertTrue(iteratorContains(m.listAllOntProperties(), dp));
        Assertions.assertTrue(iteratorContains(m.listAllOntProperties(), ap));
        Assertions.assertTrue(iteratorContains(m.listAllOntProperties(), ontp));
        Assertions.assertTrue(iteratorContains(m.listAllOntProperties(), rdfp));
    }

    @Test
    public void testListObjectProperties0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        ObjectProperty op = m.createObjectProperty(NS + "op");
        DatatypeProperty dp = m.createDatatypeProperty(NS + "dp");
        AnnotationProperty ap = m.createAnnotationProperty(NS + "ap");
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        // no rdf:type entailment, so we don't find most properties ...

        Assertions.assertTrue(iteratorContains(m.listObjectProperties(), op));
        Assertions.assertFalse(iteratorContains(m.listObjectProperties(), dp));
        Assertions.assertFalse(iteratorContains(m.listObjectProperties(), ap));
        Assertions.assertFalse(iteratorContains(m.listObjectProperties(), ontp));
        Assertions.assertFalse(iteratorContains(m.listObjectProperties(), rdfp));
    }

    @Test
    public void testListDatatypeProperties0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        ObjectProperty op = m.createObjectProperty(NS + "op");
        DatatypeProperty dp = m.createDatatypeProperty(NS + "dp");
        AnnotationProperty ap = m.createAnnotationProperty(NS + "ap");
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        // no rdf:type entailment, so we don't find most properties ...

        Assertions.assertFalse(iteratorContains(m.listDatatypeProperties(), op));
        Assertions.assertTrue(iteratorContains(m.listDatatypeProperties(), dp));
        Assertions.assertFalse(iteratorContains(m.listDatatypeProperties(), ap));
        Assertions.assertFalse(iteratorContains(m.listDatatypeProperties(), ontp));
        Assertions.assertFalse(iteratorContains(m.listDatatypeProperties(), rdfp));
    }

    @Test
    public void testListAnnotationProperties0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        ObjectProperty op = m.createObjectProperty(NS + "op");
        DatatypeProperty dp = m.createDatatypeProperty(NS + "dp");
        AnnotationProperty ap = m.createAnnotationProperty(NS + "ap");
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        // no rdf:type entailment, so we don't find most properties ...

        Assertions.assertFalse(iteratorContains(m.listAnnotationProperties(), op));
        Assertions.assertFalse(iteratorContains(m.listAnnotationProperties(), dp));
        Assertions.assertTrue(iteratorContains(m.listAnnotationProperties(), ap));
        Assertions.assertFalse(iteratorContains(m.listAnnotationProperties(), ontp));
        Assertions.assertFalse(iteratorContains(m.listAnnotationProperties(), rdfp));
    }

    @Test
    public void testListSubModels0() {
        OntModel m = ModelFactory.createOntologyModel();
        m.read(IOTestUtils.normalize("file:jena/ont-model-test-import-6-a.owl"));
        Assertions.assertEquals(4, ModelTestUtils.countMarkers(m), "Marker count not correct");

        List<OntModel> importModels = new ArrayList<>();
        for (Iterator<OntModel> j = m.listSubModels(); j.hasNext(); ) {
            importModels.add(j.next());
        }

        Assertions.assertEquals(3, importModels.size(), "n import models should be ");

        int nImports = 0;

        for (OntModel x : importModels) {
            // count the number of imports of each sub-model
            nImports += x.countSubModels();
        }
        // listSubModels' default behaviour is *not* to include imports of sub-models
        Assertions.assertEquals(0, nImports, "Wrong number of sub-model imports");
    }

    @Test
    public void testListSubModels1() {
        OntModel m = ModelFactory.createOntologyModel();
        m.read(IOTestUtils.normalize("file:jena/ont-model-test-import-6-a.owl"));
        Assertions.assertEquals(4, ModelTestUtils.countMarkers(m), "Marker count not correct");

        List<OntModel> importModels = new ArrayList<>();
        for (Iterator<OntModel> j = m.listSubModels(true); j.hasNext(); ) {
            importModels.add(j.next());
        }

        Assertions.assertEquals(3, importModels.size(), "n import models should be ");

        int nImports = 0;

        for (OntModel x : importModels) {
            // count the number of imports of each sub-model
            nImports += x.countSubModels();
        }
        Assertions.assertEquals(2, nImports, "Wrong number of sub-model imports");
    }

    @Test
    public void testGetImportedModel() {
        OntModel m = ModelFactory.createOntologyModel();
        m.read(IOTestUtils.normalize("file:jena/ont-model-test-import-6-a.owl"));

        OntModel m0 = m.getImportedModel(IOTestUtils.normalize("file:jena/ont-model-test-import-6-b.owl"));
        OntModel m1 = m.getImportedModel(IOTestUtils.normalize("file:jena/ont-model-test-import-6-c.owl"));
        OntModel m2 = m.getImportedModel(IOTestUtils.normalize("file:jena/ont-model-test-import-6-d.owl"));
        OntModel m3 = m.getImportedModel(IOTestUtils.normalize("file:jena/ont-model-test-import-6-b.owl"))
                .getImportedModel(IOTestUtils.normalize("file:jena/ont-model-test-import-6-c.owl"));
        OntModel m4 = m.getImportedModel(IOTestUtils.normalize("file:jena/ont-model-test-import-6-a.owl"));

        Assertions.assertNotNull(m0, "Import model b should not be null");
        Assertions.assertNotNull(m1, "Import model c should not be null");
        Assertions.assertNotNull(m2, "Import model d should not be null");
        Assertions.assertNotNull(m3, "Import model b-c should not be null");
        Assertions.assertNull(m4, "Import model a should be null");
    }

    /**
     * Test that the supports checks that are defined in the OWL full profile are not
     * missing in the DL and Lite profiles, unless by design.
     * Not strictly a model test, but it has to go somewhere
     */
    @Test
    public void testProfiles() {
        List<Class<?>> notInLite = Arrays.asList(new Class<?>[]{DataRange.class, HasValueRestriction.class});

        Set<Class<?>> fullProfileMap = new OWLProfileExt().getSupportsSet();
        Set<Class<?>> dlProfileMap = new OWLDLProfileExt().getSupportsSet();
        Set<Class<?>> liteProfileMap = new OWLLiteProfileExt().getSupportsSet();

        for (Class<?> c : fullProfileMap) {
            Assertions.assertTrue(dlProfileMap.contains(c), "Key in OWL DL profile: " + c.getName());
            Assertions.assertTrue(liteProfileMap.contains(c) || notInLite.contains(c), "Key in OWL lite profile: " + c.getName());
        }
    }


    /**
     * Added by kers to ensure that bulk update works; should really be a test
     * of the ontology Graph using AbstractTestGraph, but that fails because there
     * are too many things that don't pass those tests.
     * <p>
     * <b>Yet</b>.
     */
    @Test
    public void testBulkAddWorks() {
        OntModel om1 = ModelFactory.createOntologyModel();
        OntModel om2 = ModelFactory.createOntologyModel();
        om1.add(om2);
    }

    @Test
    public void testRead() {
        String base0 = "http://example.com/test0";
        String ns0 = base0 + "#";
        String base1 = "http://example.com/test1";
        String ns1 = base1 + "#";

        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        m.getDocumentManager().reset();
        m.getDocumentManager().addAltEntry(base0, IOTestUtils.normalize("file:jena/ont-model-test-relativenames.rdf"));
        m.read(base0, "RDF/XML");
        Assertions.assertNotNull(m.getOntClass(ns0 + "A"), "Should be a class ns0:A");
        Assertions.assertNull(m.getOntClass(ns1 + "A"), "Should not be a class ns1:A");

        m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        m.getDocumentManager().reset();
        m.getDocumentManager().addAltEntry(base0, IOTestUtils.normalize("file:jena/ont-model-test-relativenames.rdf"));
        m.read(base0, base1, "RDF/XML");
        Assertions.assertNull(m.getOntClass(ns0 + "A"), "Should not be a class ns0:A");
        Assertions.assertNotNull(m.getOntClass(ns1 + "A"), "Should be a class ns1:A");
    }

    @Test
    public void testListDataRange() {
        String base = "http://jena.hpl.hp.com/test#";
        String doc =
                "<?xml version='1.0'?>"
                        + "<!DOCTYPE owl ["
                        + "      <!ENTITY rdf  'http://www.w3.org/1999/02/22-rdf-syntax-ns#' >"
                        + "      <!ENTITY rdfs 'http://www.w3.org/2000/01/rdf-schema#' >"
                        + "      <!ENTITY xsd  'http://www.w3.org/2001/XMLSchema#' >"
                        + "      <!ENTITY owl  'http://www.w3.org/2002/07/owl#' >"
                        + "      <!ENTITY dc   'http://purl.org/dc/elements/1.1/' >"
                        + "      <!ENTITY base  'http://jena.hpl.hp.com/test' >"
                        + "    ]>"
                        + "<rdf:RDF"
                        + "   xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'"
                        + "   xmlns:owl='http://www.w3.org/2002/07/owl#'>"
                        + "  <owl:DataRange>"
                        + "    <owl:oneOf>"
                        + "      <rdf:List>"
                        + "        <rdf:first rdf:datatype='&xsd;integer'>0</rdf:first>"
                        + "        <rdf:rest rdf:resource='&rdf;nil' />"
                        + "      </rdf:List>"
                        + "    </owl:oneOf>"
                        + "  </owl:DataRange>"
                        + "</rdf:RDF>";

        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        m.read(new StringReader(doc), base);

        Iterator<DataRange> i = m.listDataRanges();
        Assertions.assertTrue(i.hasNext(), "Should be at least one DataRange");
        Object dr = i.next();
        Assertions.assertInstanceOf(DataRange.class, dr);
        Assertions.assertFalse(i.hasNext(), "Should no more DataRange");
    }


    @Test
    public void testListHierarchyRoots0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        Assertions.assertFalse(m.listHierarchyRootClasses().hasNext());
        m = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
        Assertions.assertFalse(m.listHierarchyRootClasses().hasNext());
    }

    @Test
    public void testListHierarchyRoots1() {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a owl:Class. ";

        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        m.read(new StringReader(doc), NS, "N3");

        OntClass a = m.getOntClass(NS + "A");
        JunitExtensions.assertValues("", m.listHierarchyRootClasses(), a);
    }


    @Test
    public void testListHierarchyRoots2() {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a owl:Class. ";

        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF, null);
        m.read(new StringReader(doc), NS, "N3");

        OntClass a = m.getOntClass(NS + "A");
        JunitExtensions.assertValues("", m.listHierarchyRootClasses(), a);
    }


    @Test
    public void testListHierarchyRoots3() {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a owl:Class. "
                        + ":B a owl:Class ; rdfs:subClassOf :A . ";

        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MINI_RULE_INF, null);
        m.read(new StringReader(doc), NS, "N3");

        OntClass a = m.getOntClass(NS + "A");
        JunitExtensions.assertValues("", m.listHierarchyRootClasses(), a);
    }

    @Test
    public void testListHierarchyRoots4() {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a rdfs:Class. "
                        + ":C a rdfs:Class. "
                        + ":B a rdfs:Class ; rdfs:subClassOf :A . ";

        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF, null);
        m.read(new StringReader(doc), NS, "N3");

        OntClass a = m.getOntClass(NS + "A");
        OntClass c = m.getOntClass(NS + "C");
        JunitExtensions.assertValues("", m.listHierarchyRootClasses(), a, c);
    }

    /* Auto-loading of imports is off by default */
    @Test
    public void testLoadImports0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        Resource a = m.getResource(IOTestUtils.normalize("file:jena/ont-model-test-import-3-a.owl"));
        Resource b = m.getResource(IOTestUtils.normalize("file:jena/ont-model-test-import-3-b.owl"));
        m.add(a, m.getProfile().IMPORTS(), b);

        // not dymamically imported by default
        Assertions.assertEquals(0, ModelTestUtils.countMarkers(m), "Marker count not correct");

        Assertions.assertFalse(m.hasLoadedImport(IOTestUtils.normalize("file:jena/ont-model-test-import-3-c.owl")), "c should not be imported");
        Assertions.assertFalse(m.hasLoadedImport(IOTestUtils.normalize("file:jena/ont-model-test-import-3-b.owl")), "b should not be imported");

        m.loadImports();

        Assertions.assertEquals(2, ModelTestUtils.countMarkers(m), "Marker count not correct");

        Assertions.assertTrue(m.hasLoadedImport(IOTestUtils.normalize("file:jena/ont-model-test-import-3-c.owl")), "c should be imported");
        Assertions.assertTrue(m.hasLoadedImport(IOTestUtils.normalize("file:jena/ont-model-test-import-3-b.owl")), "b should be imported");
    }


    /* Auto-loading of imports = on */
    @Test
    public void testLoadImports1() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        Resource a = m.getResource(IOTestUtils.normalize("file:jena/ont-model-test-import-3-a.owl"));
        Resource b = m.getResource(IOTestUtils.normalize("file:jena/ont-model-test-import-3-b.owl"));

        m.setDynamicImports(true);
        m.add(a, m.getProfile().IMPORTS(), b);

        Assertions.assertEquals(2, ModelTestUtils.countMarkers(m), "Marker count not correct");

        Assertions.assertTrue(m.hasLoadedImport(IOTestUtils.normalize("file:jena/ont-model-test-import-3-c.owl")), "c should be imported");
        Assertions.assertTrue(m.hasLoadedImport(IOTestUtils.normalize("file:jena/ont-model-test-import-3-b.owl")), "b should be imported");

        // this should have no effect
        m.loadImports();

        Assertions.assertEquals(2, ModelTestUtils.countMarkers(m), "Marker count not correct");

        Assertions.assertTrue(m.hasLoadedImport(IOTestUtils.normalize("file:jena/ont-model-test-import-3-c.owl")), "c should be imported");
        Assertions.assertTrue(m.hasLoadedImport(IOTestUtils.normalize("file:jena/ont-model-test-import-3-b.owl")), "b should be imported");
    }

    /**
     * Test that resources are attached to the right sub-models when importing
     */
    @Test
    public void testLoadImports2() {
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        ontModel.read(IOTestUtils.normalize("file:jena/ont-model-test-import-8-a.owl"));

        String NSa = "http://incubator.apache.org/jena/2011/10/testont/a#";
        String NSb = "http://incubator.apache.org/jena/2011/10/testont/b#";

        OntClass A = ontModel.getOntClass(NSa + "A");
        Assertions.assertTrue(ontModel.isInBaseModel(A));

        OntClass B = ontModel.getOntClass(NSb + "B");
        Assertions.assertFalse(ontModel.isInBaseModel(B));

        Assertions.assertTrue(ontModel.isInBaseModel(ontModel.createStatement(A, RDF.type, OWL.Class)));
        Assertions.assertFalse(ontModel.isInBaseModel(ontModel.createStatement(B, RDF.type, OWL.Class)));

    }

    /**
     * Test getting conclusions after loading imports
     */
    @Test
    public void testAddImports0() {
        OntModel base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        base.createClass(NS + "A");
        base.createClass(NS + "B");

        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, base);

        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");

        // nothing is known about a and b yet
        Assertions.assertFalse(a.hasSubClass(b));

        // add some ontology data
        OntModel imp = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        imp.add(b, RDFS.subClassOf, a);

        m.addSubModel(imp, true);
        Assertions.assertTrue(a.hasSubClass(b));
    }

    @Test
    public void testAddImports1() {
        String ns = "http://jena.hpl.hp.com/2003/03/testont";
        OntModel base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        OntDocumentManager odm = OntDocumentManager.getInstance();
        odm.addAltEntry(ns + "#a", IOTestUtils.normalize("file:jena/ont-model-test-import-7-a.owl"));


        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, base);

        Ontology oo = base.createOntology(ns);
        oo.addImport(base.createResource(ns + "#a"));

        // nothing is known about a and b yet
        Resource a = m.getResource(ns + "#A");
        Resource c = m.getResource(ns + "#C");
        Assertions.assertFalse(m.contains(c, RDFS.subClassOf, a));

        // when we load the imports, the odm must kick the reasoner with a rebind()
        m.getDocumentManager().loadImports(m);
        Assertions.assertTrue(m.contains(c, RDFS.subClassOf, a));
    }

    /**
     * AddSubModel variant 2: base = no inf, import = no inf
     */
    @Test
    public void testaddSubModel0() {
        OntModel m0 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntModel m1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        OntClass c = m1.createClass(NS + "c");

        Assertions.assertFalse(m0.containsResource(c));

        m0.addSubModel(m1);
        Assertions.assertTrue(m0.containsResource(c));

        m0.removeSubModel(m1);
        Assertions.assertFalse(m0.containsResource(c));
    }

    /**
     * AddSubModel variant 2: base = inf, import = no inf
     */
    @Test
    public void testAddSubModel1() {
        OntDocumentManager.getInstance().setProcessImports(false);
        OntDocumentManager.getInstance().addAltEntry("http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine",
                IOTestUtils.normalize("file:jena/ont-model-test-wine.owl"));
        OntModel m0 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        OntModel m1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        String namespace = "http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine";
        String classURI = namespace + "#Wine";
        m1.read(namespace);
        OntClass c = m1.getOntClass(classURI);

        Assertions.assertFalse(m0.containsResource(c));
        m0.addSubModel(m1);
        Assertions.assertTrue(m0.containsResource(c));
        m0.removeSubModel(m1);
        Assertions.assertFalse(m0.containsResource(c));
    }

    /**
     * Variant 3: base = no inf, import = inf
     */
    @Test
    public void testAddSubModel3() {
        OntModel m0 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntModel m1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);

        OntClass c = m1.createClass(NS + "c");

        Assertions.assertFalse(m0.containsResource(c));

        m0.addSubModel(m1);
        Assertions.assertTrue(m0.containsResource(c));

        m0.removeSubModel(m1);
        Assertions.assertFalse(m0.containsResource(c));
    }

    /**
     * Variant 4: base = inf, import = inf
     */
    @Test
    public void testAddSubModel4() {
        OntModel m0 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
        OntModel m1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);

        OntClass c = m1.createClass(NS + "c");

        Assertions.assertFalse(m0.containsResource(c));

        m0.addSubModel(m1);
        Assertions.assertTrue(m0.containsResource(c));

        m0.removeSubModel(m1);
        Assertions.assertFalse(m0.containsResource(c));
    }

    /**
     * Remove a sub model (imported model)
     */
    @Test
    public void testRemoveSubModel0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        m.read(IOTestUtils.normalize("file:jena/ont-model-test-import-3-a.owl"));

        Assertions.assertEquals(2, m.getSubGraphs().size());

        for (Iterator<OntModel> it = m.listSubModels(); it.hasNext(); ) {
            m.removeSubModel(it.next());
        }

        Assertions.assertEquals(0, m.getSubGraphs().size());
    }


    /**
     * Getting the deductions model of an OntModel
     * see also {@code  TestBugs#testOntModelGetDeductions()}
     * <p>ijd: Feb 6th, 2008 - this test has been disabled for
     * the time being, since it is not correct as written. However,
     * I'm not removing or changing it just yet, since it is showing up
     * an infelicity in the rule engine that Dave will investigate
     * at some future date.</p>
     */
    @Test
    public void testGetDeductionsModel0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        OntClass c = m.createClass(NS + "C");

        b.addSubClass(c);

        // we see the entailments only in the deductions model
        Model dm = m.getDeductionsModel();
        Assertions.assertTrue(dm.contains(OWL.Nothing, RDFS.subClassOf, a));
        Assertions.assertTrue(dm.contains(OWL.Nothing, RDFS.subClassOf, c));

        a.addSubClass(b);

        Assertions.assertTrue(a.hasSubClass(c));

        dm = m.getDeductionsModel();

        Assertions.assertFalse(dm.contains(OWL.Nothing, RDFS.subClassOf, a));
        Assertions.assertTrue(dm.contains(OWL.Nothing, RDFS.subClassOf, c));
    }

    /**
     * Test that using closed models in imports does not raise an exception
     */
    @Test
    public void testImportClosedModel() {
        String SOURCEA =
                "<rdf:RDF" +
                        "    xmlns:rdf          ='http://www.w3.org/1999/02/22-rdf-syntax-ns#'" +
                        "    xmlns:owl          ='http://www.w3.org/2002/07/owl#'" +
                        "    xml:base           ='http://example.com/a#'" +
                        ">" +
                        "  <owl:Ontology>" +
                        "          <owl:imports rdf:resource='http://example.com/b' />" +
                        "  </owl:Ontology>" +
                        "</rdf:RDF>";

        OntDocumentManager.getInstance().addAltEntry("http://example.com/b", IOTestUtils.normalize("file:jena/ont-model-test-relativenames.rdf"));

        OntModel a0 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        a0.read(new StringReader(SOURCEA), null);
        long a0count = a0.size();

        // key step - close a model which is now in the ODM cache
        OntDocumentManager.getInstance().getModel("http://example.com/b").close();

        // this line threw an exception before the bug was fixed
        OntModel a1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        a1.read(new StringReader(SOURCEA), null);

        // for completeness, check that we have read the same contents
        Assertions.assertEquals(a0count, a1.size(), "Models should be same size");
    }

    /**
     * OntModel read should do content negotiation if no base URI is given
     */
    @Test
    public void testReadConneg0() {
        final boolean[] acceptHeaderSet = new boolean[]{false};

        // because ModelCom has private fields it references directly, we have to mock
        // a lot more pieces that I would prefer
        OntModel m = new OntModelImpl(OntModelSpec.OWL_MEM) {
            @Override
            protected Model readDelegate(String url) {
                acceptHeaderSet[0] = true;
                return super.readDelegate(url);
            }

            /** Allow pseudo-conneg even on file: uri's */
            @Override
            public boolean ignoreFileURI(String url) {
                return false;
            }
        };

        Assertions.assertFalse(acceptHeaderSet[0]);
        m.read(IOTestUtils.normalize("jena/ont-model-test-property.rdf"));
        Assertions.assertTrue(acceptHeaderSet[0]);

    }

    /**
     * No conneg for file: uri's normally
     */
    @Test
    public void testReadConneg1() {
        final boolean[] acceptHeaderSet = new boolean[]{false};

        // because ModelCom has private fields it references directly, we have to mock
        // a lot more pieces that I would prefer
        OntModel m = new OntModelImpl(OntModelSpec.OWL_MEM) {
            @Override
            protected Model readDelegate(String url) {
                acceptHeaderSet[0] = true;
                return super.readDelegate(url);
            }
        };

        Assertions.assertFalse(acceptHeaderSet[0]);
        m.read(IOTestUtils.normalize("jena/ont-model-test-property.rdf"));
        Assertions.assertFalse(acceptHeaderSet[0]);

    }

    /**
     * With RDF/XML syntax specified, conneg
     */
    @Test
    public void testReadConneg2() {
        final boolean[] acceptHeaderSet = new boolean[]{false};

        // because ModelCom has private fields it references directly, we have to mock
        // a lot more pieces that I would prefer
        OntModel m = new OntModelImpl(OntModelSpec.OWL_MEM) {
            @Override
            protected Model readDelegate(String url, String lang) {
                acceptHeaderSet[0] = true;
                return super.readDelegate(url, lang);
            }

            /** Allow pseudo-conneg even on file: uri's */
            @Override
            public boolean ignoreFileURI(String url) {
                return false;
            }
        };

        Assertions.assertFalse(acceptHeaderSet[0]);
        m.read(IOTestUtils.normalize("jena/ont-model-test-property.rdf"), "RDF/XML");
        Assertions.assertTrue(acceptHeaderSet[0]);

    }

    /**
     * With a base URI, no conneg
     */
    @Test
    public void testReadConneg3() {
        final boolean[] acceptHeaderSet = new boolean[]{false};

        // because ModelCom has private fields it references directly, we have to mock
        // a lot more pieces that I would prefer
        OntModel m = new OntModelImpl(OntModelSpec.OWL_MEM) {
            @Override
            protected Model readDelegate(String url, String lang) {
                acceptHeaderSet[0] = true;
                return super.readDelegate(url, lang);
            }

            /** Allow pseudo-conneg even on file: uri's */
            @Override
            public boolean ignoreFileURI(String url) {
                return false;
            }
        };

        Assertions.assertFalse(acceptHeaderSet[0]);
        m.read(IOTestUtils.normalize("jena/ont-model-test-property.rdf"), "http://foo.com", "RDF/XML");
        Assertions.assertFalse(acceptHeaderSet[0]);

    }


    // Internal implementation methods
    //////////////////////////////////

    /**
     * Answer true iff an iterator contains a given value.
     */
    private boolean iteratorContains(Iterator<?> i, Object x) {
        boolean found = false;
        while (i.hasNext()) {
            found = i.next().equals(x) || found;
        }
        return found;
    }


    protected static class OWLProfileExt extends OWLProfile {
        public Set<Class<?>> getSupportsSet() {
            return getCheckTable().keySet();
        }
    }

    protected static class OWLDLProfileExt extends OWLDLProfile {
        public Set<Class<?>> getSupportsSet() {
            return getCheckTable().keySet();
        }
    }

    protected static class OWLLiteProfileExt extends OWLLiteProfile {
        public Set<Class<?>> getSupportsSet() {
            return getCheckTable().keySet();
        }
    }

}
