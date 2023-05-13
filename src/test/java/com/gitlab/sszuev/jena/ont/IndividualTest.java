package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.common.CommonOntTestBase;
import com.gitlab.sszuev.jena.ont.common.CommonOntTestEngine;
import com.gitlab.sszuev.jena.ont.testutils.JunitExtensions;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.Profile;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.StringReader;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

public class IndividualTest extends CommonOntTestBase {

    private static Stream<Arguments> argumentsStream() {
        return testsAsArguments(getTests());
    }

    @Test
    public void testListOntClasses1() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
        OntClass a = m.createClass(CommonOntTestEngine.NS + "A");
        OntClass b = m.createClass(CommonOntTestEngine.NS + "B");
        OntClass c = m.createClass(CommonOntTestEngine.NS + "C");
        OntClass d = m.createClass(CommonOntTestEngine.NS + "D");
        OntClass e = m.createClass(CommonOntTestEngine.NS + "E");
        OntClass f = m.createClass(CommonOntTestEngine.NS + "F");
        OntClass g = m.createClass(CommonOntTestEngine.NS + "G");

        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E

        a.addSubClass(b);
        a.addSubClass(c);
        b.addSubClass(d);
        b.addSubClass(e);
        c.addSubClass(e);
        c.addSubClass(f);
        f.addSubClass(c);
        f.addSuperClass(g);

        m.listStatements(null, RDF.type, OWL.Class)
                .mapWith(Statement::getSubject)
                .mapWith(x -> x.as(OntClass.class))
                .toList()
                .forEach(x -> x.createIndividual(CommonOntTestEngine.NS + "i" + x.getLocalName()));

        Set<String> directA = m.getIndividual(CommonOntTestEngine.NS + "iA").listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getIndividual(CommonOntTestEngine.NS + "iA").listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getIndividual(CommonOntTestEngine.NS + "iB").listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getIndividual(CommonOntTestEngine.NS + "iB").listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getIndividual(CommonOntTestEngine.NS + "iC").listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getIndividual(CommonOntTestEngine.NS + "iC").listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getIndividual(CommonOntTestEngine.NS + "iD").listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getIndividual(CommonOntTestEngine.NS + "iD").listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getIndividual(CommonOntTestEngine.NS + "iE").listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getIndividual(CommonOntTestEngine.NS + "iE").listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getIndividual(CommonOntTestEngine.NS + "iF").listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getIndividual(CommonOntTestEngine.NS + "iF").listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directG = m.getIndividual(CommonOntTestEngine.NS + "iG").listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectG = m.getIndividual(CommonOntTestEngine.NS + "iG").listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-iA::" + directA);
        System.out.println("DIRECT-iB::" + directB);
        System.out.println("DIRECT-iC::" + directC);
        System.out.println("DIRECT-iD::" + directD);
        System.out.println("DIRECT-iE::" + directE);
        System.out.println("DIRECT-iF::" + directF);
        System.out.println("DIRECT-iG::" + directG);
        System.out.println("INDIRECT-iA::" + indirectA);
        System.out.println("INDIRECT-iB::" + indirectB);
        System.out.println("INDIRECT-iC::" + indirectC);
        System.out.println("INDIRECT-iD::" + indirectD);
        System.out.println("INDIRECT-iE::" + indirectE);
        System.out.println("INDIRECT-iF::" + indirectF);
        System.out.println("INDIRECT-iG::" + indirectG);

        Assertions.assertEquals(Set.of("A"), directA);
        Assertions.assertEquals(Set.of("B"), directB);
        Assertions.assertEquals(Set.of("C", "F"), directC);
        Assertions.assertEquals(Set.of("D"), directD);
        Assertions.assertEquals(Set.of("E"), directE);
        Assertions.assertEquals(Set.of("C", "F"), directF);
        Assertions.assertEquals(Set.of("G"), directG);

        Assertions.assertEquals(Set.of("A"), indirectA);
        Assertions.assertEquals(Set.of("A", "B"), indirectB);
        Assertions.assertEquals(Set.of("A", "C", "F", "G"), indirectC);
        Assertions.assertEquals(Set.of("A", "B", "D"), indirectD);
        Assertions.assertEquals(Set.of("A", "B", "C", "E", "F", "G"), indirectE);
        Assertions.assertEquals(Set.of("A", "C", "F", "G"), indirectF);
        Assertions.assertEquals(Set.of("G"), indirectG);
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    public void test(CommonOntTestEngine test) {
        test.runTest();
    }

    private static CommonOntTestEngine[] getTests() {
        return new CommonOntTestEngine[]{
                new CommonOntTestEngine("Individual.sameAs", true, false, false) {
                    /** Note: 6/Nov/2003 - updated to use sameAs not sameIndividualAs, following changes to OWL spec */
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntClass A = m.createClass(NS + "A");
                        Individual x = m.createIndividual(A);
                        Individual y = m.createIndividual(A);
                        Individual z = m.createIndividual(A);

                        x.addSameAs(y);
                        Assertions.assertEquals(1, x.getCardinality(prof.SAME_AS()), "Cardinality should be 1");
                        Assertions.assertEquals(y, x.getSameAs(), "x should be the same as y");
                        Assertions.assertTrue(x.isSameAs(y), "x should be the same as y");

                        x.addSameAs(z);
                        Assertions.assertEquals(2, x.getCardinality(prof.SAME_AS()), "Cardinality should be 2");
                        JunitExtensions.assertValues(testNodeName, x.listSameAs(), z, y);

                        x.setSameAs(z);
                        Assertions.assertEquals(1, x.getCardinality(prof.SAME_AS()), "Cardinality should be 1");
                        Assertions.assertEquals(z, x.getSameAs(), "x should be same indiv. as z");

                        x.removeSameAs(y);
                        Assertions.assertEquals(1, x.getCardinality(prof.SAME_AS()), "Cardinality should be 1");
                        x.removeSameAs(z);
                        Assertions.assertEquals(0, x.getCardinality(prof.SAME_AS()), "Cardinality should be 0");
                    }
                },

                new CommonOntTestEngine("Individual.hasOntClass", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");
                        Individual x = m.createIndividual(A);

                        Assertions.assertTrue(x.hasOntClass(A));
                        Assertions.assertFalse(x.hasOntClass(B));
                    }
                },

                new CommonOntTestEngine("Individual.hasOntClass direct", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");
                        A.addSubClass(B);

                        Individual x = m.createIndividual(A);
                        x.addRDFType(B);

                        Assertions.assertTrue(x.hasOntClass(A, false));
                        Assertions.assertTrue(x.hasOntClass(B, false));

                        Assertions.assertTrue(x.hasOntClass(A, false));
                        Assertions.assertTrue(x.hasOntClass(B, true));

                    }
                },

                new CommonOntTestEngine("Individual.hasOntClass string", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");

                        Individual x = m.createIndividual(A);

                        Assertions.assertTrue(x.hasOntClass(NS + "A"));
                    }
                },

                new CommonOntTestEngine("Individual.getOntClass", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        Individual x = m.createIndividual(A);

                        Assertions.assertEquals(A, x.getOntClass());
                    }
                },

                new CommonOntTestEngine("Individual.getOntClass direct", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");
                        A.addSubClass(B);

                        Individual x = m.createIndividual(A);
                        x.addRDFType(B);

                        // should never get A since it's not a direct class
                        Assertions.assertEquals(B, x.getOntClass(true));
                    }
                },

                new CommonOntTestEngine("Individual.listOntClasses", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");
                        A.addSubClass(B);

                        Individual x = m.createIndividual(A);
                        x.addRDFType(B);

                        JunitExtensions.assertValues(testNodeName, x.listOntClasses(false), A, B);

                        // now check the return types
                        for (Iterator<OntClass> i = x.listOntClasses(false); i.hasNext(); ) {
                            Assertions.assertNotNull(i.next());
                        }
                    }
                },

                new CommonOntTestEngine("Individual.listOntClasses direct", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");
                        A.addSubClass(B);

                        Individual x = m.createIndividual(A);
                        x.addRDFType(B);

                        JunitExtensions.assertValues(testNodeName, x.listOntClasses(true), B);

                        // now check the return types
                        for (Iterator<OntClass> i = x.listOntClasses(true); i.hasNext(); ) {
                            Assertions.assertNotNull(i.next());
                        }
                    }
                },

                new CommonOntTestEngine("Individual.addOntClass", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");
                        A.addSubClass(B);

                        Individual x = m.createIndividual(A);

                        JunitExtensions.assertValues(testNodeName, x.listOntClasses(false), A);

                        // add a class
                        x.addOntClass(B);

                        // test again
                        JunitExtensions.assertValues(testNodeName, x.listOntClasses(false), A, B);
                        for (Iterator<OntClass> i = x.listOntClasses(false); i.hasNext(); ) {
                            Assertions.assertNotNull(i.next());
                        }
                    }
                },

                new CommonOntTestEngine("Individual.setOntClass", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");
                        A.addSubClass(B);

                        Individual x = m.createIndividual(A);

                        JunitExtensions.assertValues(testNodeName, x.listOntClasses(false), A);

                        // replace the class
                        x.setOntClass(B);

                        // test again
                        JunitExtensions.assertValues(testNodeName, x.listOntClasses(false), B);
                        for (Iterator<OntClass> i = x.listOntClasses(false); i.hasNext(); ) {
                            Assertions.assertNotNull(i.next());
                        }
                    }
                },

                new CommonOntTestEngine("Individual.removeOntClass", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        OntClass B = m.createClass(NS + "B");

                        Individual x = m.createIndividual(A);
                        x.addOntClass(B);

                        JunitExtensions.assertValues(testNodeName, x.listOntClasses(false), A, B);

                        x.removeOntClass(A);
                        JunitExtensions.assertValues(testNodeName, x.listOntClasses(false), B);

                        x.removeOntClass(A);
                        JunitExtensions.assertValues(testNodeName, x.listOntClasses(false), B);

                        x.removeOntClass(B);
                        JunitExtensions.assertValues(testNodeName, x.listOntClasses(false));
                    }
                },

                new CommonOntTestEngine("Individual.canAs", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntClass A = m.createClass(NS + "A");
                        Resource r = m.createResource(NS + "r");
                        Resource s = m.createResource(NS + "s");

                        m.add(r, RDF.type, A);
                        Assertions.assertTrue(r.canAs(Individual.class));
                        Assertions.assertTrue(s.canAs(Individual.class)); // does not have to have an rdf:type to be an Individual

                        Property p = m.createDatatypeProperty(NS + "p");
                        m.add(r, p, m.createTypedLiteral(42));
                        Assertions.assertFalse(r.getProperty(p).getObject().canAs(Individual.class));
                    }
                },

                /* Test case for SF bug 945436 - a xml:lang='' in the dataset causes string index exception in getLabel() */
                new CommonOntTestEngine("Individual.canAs", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        String SOURCE =
                                "<?xml version='1.0'?>" +
                                        "<!DOCTYPE owl [" +
                                        "      <!ENTITY rdf  'http://www.w3.org/1999/02/22-rdf-syntax-ns#' >" +
                                        "      <!ENTITY rdfs 'http://www.w3.org/2000/01/rdf-schema#' >" +
                                        "      <!ENTITY xsd  'http://www.w3.org/2001/XMLSchema#' >" +
                                        "      <!ENTITY owl  'http://www.w3.org/2002/07/owl#' >" +
                                        "      <!ENTITY dc   'http://purl.org/dc/elements/1.1/' >" +
                                        "      <!ENTITY base  'http://jena.hpl.hp.com/test' >" +
                                        "    ]>" +
                                        "<rdf:RDF xmlns:owl ='&owl;' xmlns:rdf='&rdf;' xmlns:rdfs='&rdfs;' xmlns:dc='&dc;' xmlns='&base;#' xml:base='&base;'>" +
                                        "  <C rdf:ID='x'>" +
                                        "    <rdfs:label xml:lang=''>a_label</rdfs:label>" +
                                        "  </C>" +
                                        "  <owl:Class rdf:ID='C'>" +
                                        "  </owl:Class>" +
                                        "</rdf:RDF>";
                        m.read(new StringReader(SOURCE), null);
                        Individual x = m.getIndividual("http://jena.hpl.hp.com/test#x");
                        Assertions.assertEquals("a_label", x.getLabel(null), "Label on resource x");
                        Assertions.assertEquals("a_label", x.getLabel(""), "Label on resource x");
                        Assertions.assertSame(null, x.getLabel("fr"), "fr label on resource x");
                    }
                },

                new CommonOntTestEngine("OntResource.isIndividual 1", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntModel defModel = ModelFactory.createOntologyModel();
                        OntClass c = defModel.createClass("http://example.com/test#A");
                        Individual i = c.createIndividual();
                        Assertions.assertTrue(i.isIndividual(), "i should be an individual");
                    }
                },
                new CommonOntTestEngine("OntResource.isIndividual 1", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        String NS = "http://jena.hpl.hp.com/example#";
                        m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

                        m.createClass(NS + "C1");

                        for (Iterator<OntClass> it = m.listClasses(); it.hasNext(); ) {
                            OntClass ontClass = it.next();
                            Assertions.assertFalse(ontClass.isIndividual(), ontClass.getLocalName() + "should not be an individual");
                        }
                    }
                },

                new CommonOntTestEngine("OntResource.isIndividual 1", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        String NS = "http://jena.hpl.hp.com/example#";
                        m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);

                        m.createClass(NS + "C1");

                        for (Iterator<OntClass> it = m.listClasses(); it.hasNext(); ) {
                            OntClass ontClass = it.next();
                            Assertions.assertFalse(ontClass.isIndividual(), ontClass.getLocalName() + "should not be an individual");
                        }
                    }
                },

                /* Edge case - suppose we imagine that user has materialised results of offline inference */
                new CommonOntTestEngine("OntResource.isIndividual 1", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        String NS = "http://jena.hpl.hp.com/example#";
                        m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

                        m.createClass(NS + "C1");
                        m.add(OWL.Class, RDF.type, OWL.Class);

                        for (Iterator<OntClass> it = m.listClasses(); it.hasNext(); ) {
                            OntClass ontClass = it.next();
                            Assertions.assertFalse(ontClass.isIndividual(), ontClass.getLocalName() + " should not be an individual");
                        }
                    }
                },

                new CommonOntTestEngine("OntResource.isIndividual 1", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        String NS = "http://jena.hpl.hp.com/example#";
                        m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

                        m.createClass(NS + "C1");
                        m.add(OWL.Class, RDF.type, RDFS.Class);

                        for (Iterator<OntClass> it = m.listClasses(); it.hasNext(); ) {
                            OntClass ontClass = it.next();
                            Assertions.assertFalse(ontClass.isIndividual(), ontClass.getLocalName() + " should not be an individual");
                        }
                    }
                },

                new CommonOntTestEngine("OntResource.isIndividual 1", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        String NS = "http://jena.hpl.hp.com/example#";
                        m = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);

                        m.createClass(NS + "C1");
                        m.add(RDFS.Class, RDF.type, RDFS.Class);

                        for (Iterator<OntClass> it = m.listClasses(); it.hasNext(); ) {
                            OntClass ontClass = it.next();
                            Assertions.assertFalse(ontClass.isIndividual(), ontClass.getLocalName() + " should not be an individual");
                        }
                    }
                },

                /* But we do allow punning */
                new CommonOntTestEngine("OntResource.isIndividual 1", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        String NS = "http://jena.hpl.hp.com/example#";
                        m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

                        OntClass punned = m.createClass(NS + "C1");
                        OntClass c2 = m.createClass(NS + "C2");
                        m.add(punned, RDF.type, c2); // punned is a class and instance of c2

                        Assertions.assertFalse(c2.isIndividual(), "should not be an individual");
                        Assertions.assertTrue(punned.isIndividual(), "should be an individual");
                    }
                },

                new CommonOntTestEngine("OntResource.isIndividual 1", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        String NS = "http://jena.hpl.hp.com/example#";
                        m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);

                        OntClass punned = m.createClass(NS + "C1");
                        OntClass c2 = m.createClass(NS + "C2");
                        m.add(punned, RDF.type, c2); // punned is a class and and instance of c2

                        Assertions.assertFalse(c2.isIndividual(), "should not be an individual");
                        Assertions.assertTrue(punned.isIndividual(), "should be an individual");
                    }
                }


        };
    }

}
