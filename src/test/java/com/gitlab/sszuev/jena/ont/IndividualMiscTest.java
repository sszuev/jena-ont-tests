package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.common.CommonOntTestBase;
import com.gitlab.sszuev.jena.ont.common.CommonOntTestEngine;
import com.gitlab.sszuev.jena.ont.testutils.JunitExtensions;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.Profile;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static com.gitlab.sszuev.jena.ont.common.CommonOntTestEngine.NS;

public class IndividualMiscTest extends CommonOntTestBase {

    private static Stream<Arguments> argumentsStream() {
        return testsAsArguments(getTests());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testGetOntClass(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);

        Individual x = m.createIndividual(A);
        x.addRDFType(B);

        // should never get A since it's not a direct class
        Assertions.assertEquals(B, x.getOntClass(true));

        Assertions.assertEquals(List.of(B), x.listOntClasses(true).toList());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testHasOntClass(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);

        Individual x = m.createIndividual(A);
        x.addRDFType(B);

        Assertions.assertTrue(x.hasOntClass(A, false));
        Assertions.assertTrue(x.hasOntClass(B, false));

        Assertions.assertFalse(x.hasOntClass(A, true));
        Assertions.assertTrue(x.hasOntClass(B, true));
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
