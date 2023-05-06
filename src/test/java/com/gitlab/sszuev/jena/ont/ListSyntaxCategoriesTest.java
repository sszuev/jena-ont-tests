package com.gitlab.sszuev.jena.ont;

import org.apache.jena.IOTestUtils;
import org.apache.jena.ontology.AllDifferent;
import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.FunctionalProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.InverseFunctionalProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.ontology.OntologyException;
import org.apache.jena.ontology.Restriction;
import org.apache.jena.ontology.SymmetricProperty;
import org.apache.jena.ontology.TransitiveProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ListSyntaxCategoriesTest extends CommonOntTestBase {

    public static final String NS = "http://jena.hpl.hp.com/testing/ontology#";

    static Stream<Arguments> argumentsStream() {
        return testsAsArguments(testCases);
    }

    @ParameterizedTest
    @MethodSource("argumentsStream")
    public void test(ListTestEngine test) {
        test.runTest();
    }

    protected static ListTestEngine[] testCases = {
            // Ontology
            new ListTestEngine("OWL list ontologies", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    "http://jena.hpl.hp.com/testing/ontology") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listOntologies();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Ontology;
                }
            },

            // Properties
            new ListTestEngine("OWL list properties", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 2,
                    NS + "p", NS + "karma") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listOntProperties();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntProperty;
                }
            },
            new ListTestEngine("OWL list properties", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM, 1,
                    NS + "p") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listOntProperties();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntProperty;
                }
            },
            new ListTestEngine("OWL list object properties", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 2,
                    NS + "op", NS + "op1") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listObjectProperties();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntProperty;
                }
            },
            new ListTestEngine("OWL list datatype properties", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    NS + "dp") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listDatatypeProperties();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntProperty;
                }
            },
            new ListTestEngine("OWL list functional properties", "/jena/list-syntax-categories-test-prototypes.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    NS + "fp") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listFunctionalProperties();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof FunctionalProperty;
                }
            },
            new ListTestEngine("OWL list transitive properties", "/jena/list-syntax-categories-test-prototypes.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    NS + "tp") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listTransitiveProperties();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof TransitiveProperty;
                }
            },
            new ListTestEngine("OWL list symmetric properties", "/jena/list-syntax-categories-test-prototypes.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    NS + "sp") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listSymmetricProperties();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof SymmetricProperty;
                }
            },
            new ListTestEngine("OWL list inverse functional properties", "/jena/list-syntax-categories-test-prototypes.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    NS + "ifp") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listInverseFunctionalProperties();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof InverseFunctionalProperty;
                }
            },

            // individuals
            new ListTestEngine("OWL list individuals", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 8,
                    NS + "A0", NS + "A1", NS + "C0", NS + "a0", NS + "a1", NS + "a2", NS + "z0", NS + "z1") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIndividuals();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },
            new ListTestEngine("OWL list typed individuals", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM, 2,
                    NS + "A0", NS + "A1") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    Model mVocab = ModelFactory.createDefaultModel();
                    Resource cA = mVocab.createResource("http://jena.hpl.hp.com/testing/ontology#A");
                    return m.listIndividuals(cA);
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },
            new ListTestEngine("OWL list individuals negative case 1", null, OntModelSpec.OWL_MEM, 0) {
                @Override
                protected void addAxioms(OntModel m) {
                    // A0 should not an individual
                    m.add(m.createResource(NS + "A0"), RDF.type, OWL.Class);
                }

                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIndividuals();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },
            new ListTestEngine("OWL list individuals negative case 2", null, OntModelSpec.OWL_MEM_MICRO_RULE_INF, 0) {
                @Override
                protected void addAxioms(OntModel m) {
                    // A0 should not an individual
                    m.add(m.createResource(NS + "A0"), RDF.type, OWL.Class);
                }

                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIndividuals();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },
            new ListTestEngine("OWL list individuals negative case 3", null, OntModelSpec.OWL_MEM, 0) {
                @Override
                protected void addAxioms(OntModel m) {
                    // A0 should not an individual, even though we have materialised some of the entailment triples
                    Resource a0 = m.createResource(NS + "A0");
                    m.add(a0, RDF.type, OWL.Class);
                    m.add(OWL.Class, RDF.type, OWL.Class);
                }

                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIndividuals();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },
            new ListTestEngine("OWL list individuals negative case 4", null, OntModelSpec.OWL_MEM, 0) {
                @Override
                protected void addAxioms(OntModel m) {
                    // A0 should not an individual, even though we have materialised some of the entailment triples
                    Resource a0 = m.createResource(NS + "A0");
                    m.add(a0, RDF.type, OWL.Class);
                    m.add(OWL.Class, RDF.type, RDFS.Class);
                }

                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIndividuals();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },
            new ListTestEngine("OWL list individuals - punning", null, OntModelSpec.OWL_MEM, 1, NS + "A0") {
                @Override
                protected void addAxioms(OntModel m) {
                    // A0 should be an individual, since we are punning
                    Resource a0 = m.createResource(NS + "A0");
                    Resource a1 = m.createResource(NS + "A1");
                    m.add(a0, RDF.type, OWL.Class);
                    m.add(a1, RDF.type, OWL.Class);
                    m.add(a0, RDF.type, a1);
                }

                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIndividuals();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },
            new ListTestEngine("empty OWL list individuals", null, OntModelSpec.OWL_MEM, 0) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIndividuals();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },
            new ListTestEngine("empty OWL+rule list individuals", null, OntModelSpec.OWL_MEM_RULE_INF, 0) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIndividuals();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },
            new ListTestEngine("empty OWL+RDFS rule list individuals (bug report JENA-3)", null, OntModelSpec.RDFS_MEM_RDFS_INF, 0) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIndividuals();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },
            new ListTestEngine("OWL list individuals with inference", "/jena/list-syntax-categories-test-comps.xml", OntModelSpec.OWL_LITE_MEM_RULES_INF, 6) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIndividuals();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },
            new ListTestEngine("OWL list individuals in composite model", null, OntModelSpec.OWL_MEM, 1, "http://example.com/foo#anInd") {
                @Override
                public Iterator<? extends Resource> doList(OntModel schema) {
                    Model data = ModelFactory.createDefaultModel();
                    Resource c = schema.createResource("http://example.com/foo#AClass");
                    Resource i = data.createResource("http://example.com/foo#anInd");
                    schema.add(c, RDF.type, OWL.Class);
                    data.add(i, RDF.type, c);

                    OntModel composite = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, schema);
                    composite.addSubModel(data);

                    return composite.listIndividuals();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },

            new ListTestEngine("OWL list all different", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listAllDifferent();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof AllDifferent;
                }
            },

            // classes
            new ListTestEngine("OWL list classes", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 11,
                    NS + "A", NS + "B", NS + "C", NS + "D", NS + "E", NS + "X0", NS + "X1", NS + "Y0", NS + "Y1", NS + "Z") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listClasses();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntClass;
                }
            },
            new ListTestEngine("OWL list named classes", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 10,
                    NS + "A", NS + "B", NS + "C", NS + "D", NS + "E", NS + "X0", NS + "X1", NS + "Y0", NS + "Y1", NS + "Z") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listNamedClasses();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntClass;
                }
            },
            new ListTestEngine("OWL list intersection classes", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    NS + "A") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIntersectionClasses();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntClass;
                }
            },
            new ListTestEngine("OWL list union classes", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    NS + "B") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listUnionClasses();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntClass;
                }
            },
            new ListTestEngine("OWL list complement classes", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    NS + "C") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listComplementClasses();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntClass;
                }
            },
            new ListTestEngine("OWL list enumerated classes", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    NS + "D") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listEnumeratedClasses();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntClass;
                }
            },
            new ListTestEngine("OWL list restrictions", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listRestrictions();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Restriction;
                }
            },

            // Annotation property
            new ListTestEngine("OWL list annotation properties", "/jena/list-syntax-categories-test.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listAnnotationProperties();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof AnnotationProperty;
                }
            },

            // !!!!!!! Following tests use ontology that imports owl.owl !!!!!!!!!!!

            // ontologies
            new ListTestEngine("OWL+import list ontologies", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 2,
                    "http://jena.hpl.hp.com/testing/ontology", "http://www.w3.org/2002/07/owl") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listOntologies();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Ontology;
                }
            },
            // Properties
            new ListTestEngine("OWL+import list properties", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 46) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listOntProperties();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntProperty;
                }
            },
            new ListTestEngine("OWL+import list object properties", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 2,
                    NS + "op", NS + "op1") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listObjectProperties();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntProperty;
                }
            },
            new ListTestEngine("OWL+import list datatype properties", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    NS + "dp") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listDatatypeProperties();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntProperty;
                }
            },

            // individuals
            new ListTestEngine("OWL+import list individuals", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 8) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIndividuals();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Individual;
                }
            },

            new ListTestEngine("OWL+import list all different", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listAllDifferent();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof AllDifferent;
                }
            },

            // classes
            new ListTestEngine("OWL+import list classes", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 14) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listClasses();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntClass;
                }
            },
            new ListTestEngine("OWL+import list named classes", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 12,
                    NS + "A", NS + "B", NS + "C", NS + "D", NS + "E", NS + "X0", NS + "X1", NS + "Y0", NS + "Y1", NS + "Z",
                    OWL.Thing.getURI(), OWL.Nothing.getURI()) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listNamedClasses();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntClass;
                }
            },
            new ListTestEngine("OWL+import list intersection classes", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    NS + "A") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listIntersectionClasses();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntClass;
                }
            },
            new ListTestEngine("OWL+import list union classes", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 2,
                    NS + "B", OWL.Thing.getURI()) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listUnionClasses();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntClass;
                }
            },
            new ListTestEngine("OWL+import list complement classes", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 3) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listComplementClasses();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntClass;
                }
            },
            new ListTestEngine("OWL+import list enumerated classes", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1,
                    NS + "D") {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listEnumeratedClasses();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof OntClass;
                }
            },
            new ListTestEngine("OWL+import list restrictions", "/jena/list-syntax-categories-test-with-import.rdf", OntModelSpec.OWL_MEM_TRANS_INF, 1) {
                @Override
                public Iterator<? extends Resource> doList(OntModel m) {
                    return m.listRestrictions();
                }

                @Override
                public boolean test(Resource r) {
                    return r instanceof Restriction;
                }
            },
    };

    protected static class ListTestEngine implements EngineWithName {
        protected String testName;
        protected String testResourcePath;
        protected OntModelSpec spec;
        protected int expectedMembersCount;
        protected String[] expectedMembersUris;
        protected boolean expectException;

        protected ListTestEngine(String testName, String testResourcePath, OntModelSpec spec, int expectedMembersCount) {
            this.testName = testName;
            this.testResourcePath = testResourcePath;
            this.spec = spec;
            this.expectedMembersCount = expectedMembersCount;
            this.expectedMembersUris = null;
            this.expectException = false;
        }

        protected ListTestEngine(String testName, String testResourcePath, OntModelSpec spec, int expectedMembersCount, String... expected) {
            this(testName, testResourcePath, spec, expectedMembersCount, false, expected);
        }

        protected ListTestEngine(String testName, String testResourcePath, OntModelSpec spec, int expectedMembersCount, boolean expectException, String... expectedMembersUris) {
            this.testName = testName;
            this.testResourcePath = testResourcePath;
            this.spec = spec;
            this.expectedMembersCount = expectedMembersCount;
            this.expectedMembersUris = expectedMembersUris;
            this.expectException = expectException;
        }

        public void runTest() {
            OntModel m = ModelFactory.createOntologyModel(spec, null);
            m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/owl-builtins.rdf"));
            m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/rdfs-builtins.rdf"));

            if (testResourcePath != null) {
                IOTestUtils.readResourceModel(m, testResourcePath);
            }

            // hook to add extra axioms
            addAxioms(m);

            boolean hasException = false;
            Iterator<? extends Resource> i = null;
            try {
                i = doList(m);
            } catch (OntologyException e) {
                hasException = true;
            }

            Assertions.assertEquals(expectException, hasException, "Ontology exception" + (expectException ? " was " : " was not ") + "expected");
            if (hasException) {
                return;
            }
            List<Resource> expected = expected(m);
            List<Resource> actual = new ArrayList<>();
            int extraneous = 0;

            // now we walk the iterator
            while (i.hasNext()) {
                Resource res = i.next();
                Assertions.assertTrue(test(res), "Should not fail node test on " + res);

                actual.add(res);
                if (expected != null) {
                    if (expected.contains(res)) {
                        expected.remove(res);
                    } else {
                        if (!res.isAnon()) {
                            // since we can't list expected anon resources, we ignore them in this check
                            extraneous++;
                        }
                    }
                }
            }

            Assertions.assertEquals(expectedMembersCount, actual.size(), testName + ": wrong number of results returned");
            if (expected != null) {
                Assertions.assertTrue(expected.isEmpty(), "Did not find all expected resources in iterator");
                Assertions.assertEquals(0, extraneous, "Found extraneous results, not in expected list");
            }
        }

        public Iterator<? extends Resource> doList(OntModel m) {
            return null;
        }

        public boolean test(Resource r) {
            return true;
        }

        protected List<Resource> expected(OntModel m) {
            if (expectedMembersUris == null) {
                return null;
            }
            List<Resource> expected = new ArrayList<>();
            for (String e : expectedMembersUris) {
                expected.add(m.getResource(e));
            }
            return expected;
        }

        /**
         * Add extra axioms hook
         */
        protected void addAxioms(OntModel m) {
            // default is no-op
        }

        @Override
        public String getName() {
            return testName;
        }
    }
}
