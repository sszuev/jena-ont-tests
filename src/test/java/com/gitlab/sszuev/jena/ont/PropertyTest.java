package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.common.CommonOntTestBase;
import com.gitlab.sszuev.jena.ont.common.CommonOntTestEngine;
import com.gitlab.sszuev.jena.ont.testutils.IOTestUtils;
import com.gitlab.sszuev.jena.ont.testutils.JunitExtensions;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.ontology.Profile;
import org.apache.jena.ontology.Restriction;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.reasoner.test.TestUtil;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public class PropertyTest extends CommonOntTestBase {

    static Stream<Arguments> argumentsStream() {
        return testsAsArguments(getTests());
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    public void test(CommonOntTestEngine test) {
        test.runTest();
    }

    private static void readTestModel(Model m, CommonOntTestEngine.ProfileLang profileLang) {
        IOTestUtils.readResourceModel(m,
                CommonOntTestEngine.ProfileLang.RDFS == profileLang ? "/jena/property-test-rdfs.rdf" : "/jena/property-test-owl.rdf");
    }

    private static CommonOntTestEngine[] getTests() {
        return new CommonOntTestEngine[]{
                new CommonOntTestEngine("OntProperty.super-property", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createOntProperty(NS + "p");
                        OntProperty q = m.createOntProperty(NS + "q");
                        OntProperty r = m.createOntProperty(NS + "r");

                        p.addSuperProperty(q);
                        Assertions.assertEquals(1, p.getCardinality(prof.SUB_PROPERTY_OF()), "Cardinality should be 1");
                        Assertions.assertEquals(q, p.getSuperProperty(), "p have super-prop q");

                        p.addSuperProperty(r);
                        Assertions.assertEquals(2, p.getCardinality(prof.SUB_PROPERTY_OF()), "Cardinality should be 2");
                        JunitExtensions.assertValues( testNodeName,p.listSuperProperties(), q, r);

                        p.setSuperProperty(r);
                        Assertions.assertEquals(1, p.getCardinality(prof.SUB_PROPERTY_OF()), "Cardinality should be 1");
                        Assertions.assertEquals(r, p.getSuperProperty(), "p shuold have super-prop r");

                        p.removeSuperProperty(q);
                        Assertions.assertEquals(1, p.getCardinality(prof.SUB_PROPERTY_OF()), "Cardinality should be 1");
                        p.removeSuperProperty(r);
                        Assertions.assertEquals(0, p.getCardinality(prof.SUB_PROPERTY_OF()), "Cardinality should be 0");

                        // for symmetry with listSuperClasses(), exclude the reflexive case
                        List<? extends OntProperty> sp = p.listSuperProperties().toList();
                        Assertions.assertFalse(sp.contains(p), "super-properties should not include reflexive case");
                    }
                },
                new CommonOntTestEngine("OntProperty.sub-property", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createOntProperty(NS + "p");
                        OntProperty q = m.createOntProperty(NS + "q");
                        OntProperty r = m.createOntProperty(NS + "r");

                        p.addSubProperty(q);
                        Assertions.assertEquals(1, q.getCardinality(prof.SUB_PROPERTY_OF()), "Cardinality should be 1");
                        Assertions.assertEquals(q, p.getSubProperty(), "p have sub-prop q");

                        p.addSubProperty(r);
                        Assertions.assertEquals(2, q.getCardinality(prof.SUB_PROPERTY_OF()) + r.getCardinality(prof.SUB_PROPERTY_OF()), "Cardinality should be 2");                        
                        JunitExtensions.assertValues( testNodeName,p.listSubProperties(), q, r);
                        JunitExtensions.assertValues( testNodeName,q.listSuperProperties(), p);
                        JunitExtensions.assertValues( testNodeName,r.listSuperProperties(), p);

                        p.setSubProperty(r);
                        Assertions.assertEquals(1, q.getCardinality(prof.SUB_PROPERTY_OF()) + r.getCardinality(prof.SUB_PROPERTY_OF()), "Cardinality should be 1");
                        Assertions.assertEquals(r, p.getSubProperty(), "p should have sub-prop r");

                        p.removeSubProperty(q);
                        Assertions.assertTrue(p.hasSubProperty(r, false), "Should have sub-prop r");
                        p.removeSubProperty(r);
                        Assertions.assertFalse(p.hasSubProperty(r, false), "Should not have sub-prop r");
                    }
                },
                new CommonOntTestEngine("OntProperty.domain", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createOntProperty(NS + "p");
                        OntResource a = m.getResource(NS + "a").as(OntResource.class);
                        OntResource b = m.getResource(NS + "b").as(OntResource.class);

                        p.addDomain(a);
                        Assertions.assertEquals(1, p.getCardinality(prof.DOMAIN()), "Cardinality should be 1");
                        Assertions.assertEquals(a, p.getDomain(), "p have domain a");

                        p.addDomain(b);
                        Assertions.assertEquals(2, p.getCardinality(prof.DOMAIN()), "Cardinality should be 2");
                        JunitExtensions.assertValues( testNodeName,p.listDomain(), a, b);

                        p.setDomain(b);
                        Assertions.assertEquals(1, p.getCardinality(prof.DOMAIN()), "Cardinality should be 1");
                        Assertions.assertEquals(b, p.getDomain(), "p should have domain b");

                        p.removeDomain(a);
                        Assertions.assertEquals(1, p.getCardinality(prof.DOMAIN()), "Cardinality should be 1");
                        p.removeDomain(b);
                        Assertions.assertEquals(0, p.getCardinality(prof.DOMAIN()), "Cardinality should be 0");
                    }
                },
                new CommonOntTestEngine("OntProperty.range", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createOntProperty(NS + "p");
                        OntResource a = m.getResource(NS + "a").as(OntResource.class);
                        OntResource b = m.getResource(NS + "b").as(OntResource.class);

                        p.addRange(a);
                        Assertions.assertEquals(1, p.getCardinality(prof.RANGE()), "Cardinality should be 1");
                        Assertions.assertEquals(a, p.getRange(), "p have range a");

                        p.addRange(b);
                        Assertions.assertEquals(2, p.getCardinality(prof.RANGE()), "Cardinality should be 2");
                        JunitExtensions.assertValues( testNodeName,p.listRange(), a, b);

                        p.setRange(b);
                        Assertions.assertEquals(1, p.getCardinality(prof.RANGE()), "Cardinality should be 1");
                        Assertions.assertEquals(b, p.getRange(), "p should have range b");

                        p.removeRange(a);
                        Assertions.assertEquals(1, p.getCardinality(prof.RANGE()), "Cardinality should be 1");
                        p.removeRange(b);
                        Assertions.assertEquals(0, p.getCardinality(prof.RANGE()), "Cardinality should be 0");
                    }
                },
                new CommonOntTestEngine("OntProperty.equivalentProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");
                        OntProperty q = m.createObjectProperty(NS + "q");
                        OntProperty r = m.createObjectProperty(NS + "r");

                        p.addEquivalentProperty(q);
                        Assertions.assertEquals(1, p.getCardinality(prof.EQUIVALENT_PROPERTY()), "Cardinality should be 1");
                        Assertions.assertEquals(q, p.getEquivalentProperty(), "p have equivalentProperty q");

                        p.addEquivalentProperty(r);
                        Assertions.assertEquals(2, p.getCardinality(prof.EQUIVALENT_PROPERTY()), "Cardinality should be 2");
                        JunitExtensions.assertValues( testNodeName,p.listEquivalentProperties(), q, r);

                        p.setEquivalentProperty(r);
                        Assertions.assertEquals(1, p.getCardinality(prof.EQUIVALENT_PROPERTY()), "Cardinality should be 1");
                        Assertions.assertEquals(r, p.getEquivalentProperty(), "p should have equivalentProperty r");

                        p.removeEquivalentProperty(q);
                        Assertions.assertEquals(1, p.getCardinality(prof.EQUIVALENT_PROPERTY()), "Cardinality should be 1");
                        p.removeEquivalentProperty(r);
                        Assertions.assertEquals(0, p.getCardinality(prof.EQUIVALENT_PROPERTY()), "Cardinality should be 0");
                    }
                },
                new CommonOntTestEngine("OntProperty.inverseOf", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Profile prof = m.getProfile();
                        OntProperty p = m.createObjectProperty(NS + "p");
                        OntProperty q = m.createObjectProperty(NS + "q");
                        OntProperty r = m.createObjectProperty(NS + "r");

                        Assertions.assertFalse(p.isInverseOf(q));
                        Assertions.assertNull(p.getInverseOf());

                        p.addInverseOf(q);
                        Assertions.assertEquals(1, p.getCardinality(prof.INVERSE_OF()), "Cardinality should be 1");
                        Assertions.assertEquals(q, p.getInverseOf(), "p should have inverse q");
                        Assertions.assertTrue(p.getInverseOf() instanceof ObjectProperty, "inverse value should be an object property");
                        Assertions.assertTrue(q.getInverse() instanceof ObjectProperty, "inverse value should be an object property");

                        p.addInverseOf(r);
                        Assertions.assertEquals(2, p.getCardinality(prof.INVERSE_OF()), "Cardinality should be 2");
                        JunitExtensions.assertValues( testNodeName,p.listInverseOf(), q, r);

                        p.setInverseOf(r);
                        Assertions.assertEquals(1, p.getCardinality(prof.INVERSE_OF()), "Cardinality should be 1");
                        Assertions.assertEquals(r, p.getInverseOf(), "p should have inverse r");

                        p.removeInverseProperty(q);
                        Assertions.assertEquals(1, p.getCardinality(prof.INVERSE_OF()), "Cardinality should be 1");
                        p.removeInverseProperty(r);
                        Assertions.assertEquals(0, p.getCardinality(prof.INVERSE_OF()), "Cardinality should be 0");
                    }
                },
                new CommonOntTestEngine("OntProperty.subproperty.fromFile", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        readTestModel(m, profileLang);

                        OntProperty p = m.getProperty(NS, "p").as(OntProperty.class);
                        OntProperty q = m.getProperty(NS, "q").as(OntProperty.class);

                        JunitExtensions.assertValues( testNodeName,p.listSuperProperties(), q);
                        JunitExtensions.assertValues( testNodeName,q.listSubProperties(), p);
                    }
                },
                new CommonOntTestEngine("OntProperty.domain.fromFile", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        readTestModel(m, profileLang);

                        OntProperty p = m.getProperty(NS, "p").as(OntProperty.class);
                        OntClass A = m.getResource(NS + "ClassA").as(OntClass.class);

                        Assertions.assertTrue(p.hasDomain(A), "p should have domain A");
                    }
                },
                new CommonOntTestEngine("OntProperty.range.fromFile", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        readTestModel(m, profileLang);

                        OntProperty p = m.getProperty(NS, "p").as(OntProperty.class);
                        OntClass B = m.getResource(NS + "ClassB").as(OntClass.class);

                        Assertions.assertTrue(p.hasRange(B), "p should have domain B");
                    }
                },
                new CommonOntTestEngine("OntProperty.equivalentProeprty.fromFile", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        readTestModel(m, profileLang);

                        OntProperty p = m.getProperty(NS, "p").as(OntProperty.class);
                        OntProperty r = m.getProperty(NS, "r").as(OntProperty.class);

                        Assertions.assertTrue(p.hasEquivalentProperty(r), "p should have equiv prop r");
                    }
                },
                new CommonOntTestEngine("OntProperty.inversePropertyOf.fromFile", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        readTestModel(m, profileLang);

                        OntProperty p = m.getProperty(NS, "p").as(OntProperty.class);
                        OntProperty s = m.getProperty(NS, "s").as(OntProperty.class);

                        Assertions.assertTrue(p.isInverseOf(s), "p should have inv prop s");
                    }
                },

                // type tests
                new CommonOntTestEngine("OntProperty.isFunctionalProperty dt", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntProperty p = m.createDatatypeProperty(NS + "p", true);

                        Assertions.assertTrue(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertTrue(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("OntProperty.isFunctionalProperty object", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntProperty p = m.createObjectProperty(NS + "p", true);

                        Assertions.assertTrue(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertTrue(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("OntProperty.isDatatypeProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntProperty p = m.createDatatypeProperty(NS + "p", false);

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertTrue(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("OntProperty.isObjectProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntProperty p = m.createObjectProperty(NS + "p", false);

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertTrue(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("OntProperty.isTransitiveProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntProperty p = m.createTransitiveProperty(NS + "p");

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");    // this should be true by entailment, but we have reasoning switched off
                        Assertions.assertTrue(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("OntProperty.isInverseFunctionalProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntProperty p = m.createInverseFunctionalProperty(NS + "p");

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");    // this should be true by entailment, but we have reasoning switched off
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertTrue(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("OntProperty.isSymmetricProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntProperty p = m.createSymmetricProperty(NS + "p");

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");    // this should be true by entailment, but we have reasoning switched off
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertTrue(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("OntProperty.convertToFunctionalProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Property pSimple = m.createProperty(NS, "p");
                        pSimple.addProperty(RDF.type, RDF.Property);
                        OntProperty p = pSimple.as(OntProperty.class);

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }

                        p = p.convertToFunctionalProperty();

                        Assertions.assertTrue(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("OntProperty.convertToDatatypeProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Property pSimple = m.createProperty(NS, "p");
                        pSimple.addProperty(RDF.type, RDF.Property);
                        OntProperty p = pSimple.as(OntProperty.class);

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }

                        p = p.convertToDatatypeProperty();

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertTrue(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("OntProperty.convertToObjectProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Property pSimple = m.createProperty(NS, "p");
                        pSimple.addProperty(RDF.type, RDF.Property);
                        OntProperty p = pSimple.as(OntProperty.class);

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }

                        p = p.convertToObjectProperty();

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertTrue(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("OntProperty.convertToTransitiveProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Property pSimple = m.createProperty(NS, "p");
                        pSimple.addProperty(RDF.type, RDF.Property);
                        OntProperty p = pSimple.as(OntProperty.class);

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }

                        p = p.convertToTransitiveProperty();

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertTrue(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("OntProperty.convertToInverseFunctionalProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Property pSimple = m.createProperty(NS, "p");
                        pSimple.addProperty(RDF.type, RDF.Property);
                        OntProperty p = pSimple.as(OntProperty.class);

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }

                        p = p.convertToInverseFunctionalProperty();

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertTrue(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("OntProperty.convertToSymmetricProperty", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        Property pSimple = m.createProperty(NS, "p");
                        pSimple.addProperty(RDF.type, RDF.Property);
                        OntProperty p = pSimple.as(OntProperty.class);

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertFalse(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }

                        p = p.convertToSymmetricProperty();

                        Assertions.assertFalse(p.isFunctionalProperty(), "isFunctionalProperty not correct");
                        Assertions.assertFalse(p.isDatatypeProperty(), "isDatatypeProperty not correct");
                        Assertions.assertFalse(p.isObjectProperty(), "isObjectProperty not correct");
                        Assertions.assertFalse(p.isTransitiveProperty(), "isTransitiveProperty not correct");
                        Assertions.assertFalse(p.isInverseFunctionalProperty(), "isInverseFunctionalProperty not correct");
                        if (profileLang != ProfileLang.RDFS) {
                            Assertions.assertTrue(p.isSymmetricProperty(), "isSymmetricProperty not correct");
                        }
                    }
                },
                new CommonOntTestEngine("ObjectProperty.inverse", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        ObjectProperty q = m.createObjectProperty(NS + "q");
                        ObjectProperty r = m.createObjectProperty(NS + "r");

                        Assertions.assertFalse(p.hasInverse(), "No inverse of p");
                        Assertions.assertNull(p.getInverse());

                        q.addInverseOf(p);
                        Assertions.assertTrue(p.hasInverse(), "Inverse of p");
                        Assertions.assertEquals(q, p.getInverse(), "inverse of p ");

                        r.addInverseOf(p);
                        JunitExtensions.assertValues( testNodeName,p.listInverse(), q, r);
                    }
                },
                new CommonOntTestEngine("OntProperty.listReferringRestrictions", true, true, false) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        ObjectProperty p = m.createObjectProperty(NS + "p");
                        ObjectProperty q = m.createObjectProperty(NS + "q");
                        Restriction r0 = m.createCardinalityRestriction(null, p, 2);
                        Restriction r1 = m.createCardinalityRestriction(null, p, 3);
                        Restriction r2 = m.createCardinalityRestriction(null, q, 2);
                        Restriction r3 = m.createCardinalityRestriction(null, q, 3);

                        Assertions.assertTrue(JunitExtensions.iteratorContains(p.listReferringRestrictions(), r0));
                        Assertions.assertTrue(JunitExtensions.iteratorContains(p.listReferringRestrictions(), r1));
                        Assertions.assertFalse(JunitExtensions.iteratorContains(p.listReferringRestrictions(), r2));
                        Assertions.assertFalse(JunitExtensions.iteratorContains(p.listReferringRestrictions(), r3));

                        Assertions.assertNotNull(p.listReferringRestrictions().next());
                    }
                },
                new CommonOntTestEngine("no duplication from imported models", true, true, true) {
                    @Override
                    public void performTest(OntModel m, ProfileLang profileLang) {
                        OntModel m0 = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF, null);
                        FileManager.getInternal().readModelInternal(m0, IOTestUtils.normalize("file:testing/ontology/testImport9/a.ttl"));

                        OntProperty p0 = m0.getOntProperty("http://incubator.apache.org/jena/2011/10/testont/b#propB");
                        TestUtil.assertIteratorLength(p0.listDomain(), 3);

                        // repeat test - thus using previously cached model for import

                        OntModel m1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF, null);
                        FileManager.getInternal().readModelInternal(m1, IOTestUtils.normalize("file:testing/ontology/testImport9/a.ttl"));

                        OntProperty p1 = m1.getOntProperty("http://incubator.apache.org/jena/2011/10/testont/b#propB");
                        TestUtil.assertIteratorLength(p1.listDomain(), 3);
                    }
                }
        };
    }

}
