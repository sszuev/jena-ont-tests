package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.IOTestUtils;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.ProfileException;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.stream.Collectors;

public class OntModelAllPropertiesTest {

    private static final String BASE = "http://www.test.com/test";
    private static final String NS = BASE + "#";

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
    })
    public void testListAllOntProperties1a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        m.createObjectProperty(NS + "op");
        m.createDatatypeProperty(NS + "dp");
        m.createAnnotationProperty(NS + "ap");
        m.createOntProperty(NS + "ontp");
        m.createProperty(NS + "rdfp").addProperty(RDF.type, RDF.Property);

        List<String> expected = null;
        if (spec == TestSpec.OWL_MEM
                || spec == TestSpec.OWL_MEM_TRANS_INF
                || spec == TestSpec.OWL_DL_MEM
                || spec == TestSpec.OWL_DL_MEM_TRANS_INF
                || spec == TestSpec.OWL_LITE_MEM
                || spec == TestSpec.OWL_LITE_MEM_TRANS_INF) {
            expected = List.of("ap", "dp", "ontp", "op", "rdfp");
        }
        if (spec == TestSpec.OWL_MEM_MINI_RULE_INF
                || spec == TestSpec.OWL_MEM_RULE_INF
                || spec == TestSpec.OWL_DL_MEM_RULE_INF
                || spec == TestSpec.OWL_LITE_MEM_RULES_INF) {
            expected = List.of(
                    "ap", "backwardCompatibleWith", "differentFrom", "disjointWith", "domain", "dp", "equivalentClass",
                    "first", "imports", "incompatibleWith", "intersectionOf", "isDefinedBy", "object", "onProperty", "oneOf",
                    "ontp", "op", "predicate", "priorVersion", "range", "rdfp", "rest", "sameAs", "seeAlso", "subClassOf",
                    "subPropertyOf", "subject", "type", "versionInfo");
        }
        if (spec == TestSpec.OWL_MEM_MICRO_RULE_INF) {
            expected = List.of("ap", "backwardCompatibleWith", "comment", "differentFrom", "disjointWith", "domain",
                    "dp", "equivalentClass", "equivalentProperty", "first", "imports", "incompatibleWith", "intersectionOf",
                    "isDefinedBy", "label", "object", "onProperty", "oneOf", "ontp", "op", "predicate", "priorVersion",
                    "range", "rdfp", "rest", "sameAs", "seeAlso", "subClassOf", "subPropertyOf", "subject", "type", "versionInfo");
        }
        if (spec == TestSpec.OWL_MEM_RDFS_INF
                || spec == TestSpec.OWL_DL_MEM_RDFS_INF
                || spec == TestSpec.OWL_LITE_MEM_RDFS_INF) {
            expected = List.of(
                    "ap", "comment", "domain", "dp", "first", "isDefinedBy", "label", "object", "ontp", "op",
                    "predicate", "range", "rdfp", "rest", "seeAlso", "subClassOf", "subPropertyOf", "subject", "type");
        }
        if (expected == null) {
            throw new IllegalStateException();
        }

        List<String> actual = m.listAllOntProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
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
    })
    public void testListAllOntProperties2a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");

        int expected1;
        int expected2;
        if (spec == TestSpec.OWL_MEM) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_MEM_RULE_INF) {
            expected1 = 33;
            expected2 = 33;
        } else if (spec == TestSpec.OWL_MEM_RDFS_INF) {
            expected1 = 19;
            expected2 = 19;
        } else if (spec == TestSpec.OWL_MEM_TRANS_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_MEM_MICRO_RULE_INF) {
            expected1 = 36;
            expected2 = 36;
        } else if (spec == TestSpec.OWL_MEM_MINI_RULE_INF) {
            expected1 = 33;
            expected2 = 33;
        } else if (spec == TestSpec.OWL_DL_MEM) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_DL_MEM_RDFS_INF) {
            expected1 = 19;
            expected2 = 19;
        } else if (spec == TestSpec.OWL_DL_MEM_RULE_INF) {
            expected1 = 33;
            expected2 = 33;
        } else if (spec == TestSpec.OWL_DL_MEM_TRANS_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_LITE_MEM) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_LITE_MEM_RDFS_INF) {
            expected1 = 19;
            expected2 = 19;
        } else if (spec == TestSpec.OWL_LITE_MEM_RULES_INF) {
            expected1 = 33;
            expected2 = 33;
        } else if (spec == TestSpec.OWL_LITE_MEM_TRANS_INF) {
            expected1 = 5;
            expected2 = 5;
        } else {
            throw new IllegalStateException();
        }
        int actual1 = m.listAllOntProperties().toList().size();
        int actual2 = m.listAllOntProperties().toSet().size();

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListAllOntProperties2b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");

        Assertions.assertThrows(ProfileException.class, () -> m.listAllOntProperties().toList());
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
    })
    public void testListAllOntProperties3a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        int expected1;
        int expected2;

        if (spec == TestSpec.OWL_MEM) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_MEM_RULE_INF) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_MEM_RDFS_INF) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_MEM_TRANS_INF) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_MEM_MICRO_RULE_INF) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_MEM_MINI_RULE_INF) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_DL_MEM) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_DL_MEM_RDFS_INF) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_DL_MEM_RULE_INF) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_DL_MEM_TRANS_INF) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_LITE_MEM) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_LITE_MEM_RDFS_INF) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_LITE_MEM_RULES_INF) {
            expected1 = 44;
            expected2 = 44;
        } else if (spec == TestSpec.OWL_LITE_MEM_TRANS_INF) {
            expected1 = 44;
            expected2 = 44;
        } else {
            throw new IllegalStateException();
        }
        int actual1 = m.listAllOntProperties().toList().size();
        int actual2 = m.listAllOntProperties().toSet().size();

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListAllOntProperties3b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        Assertions.assertThrows(ProfileException.class, () -> m.listAllOntProperties().toList());
    }

}
