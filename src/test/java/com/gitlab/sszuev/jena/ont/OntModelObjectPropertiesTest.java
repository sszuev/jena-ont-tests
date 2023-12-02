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

public class OntModelObjectPropertiesTest {
    private static final String BASE = "http://www.test.com/test";
    private static final String NS = BASE + "#";

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_TRANS_INF",
    })
    public void testListObjectProperties1a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        m.createObjectProperty(NS + "op");
        m.createDatatypeProperty(NS + "dp");
        m.createAnnotationProperty(NS + "ap");
        m.createOntProperty(NS + "ontp");
        m.createProperty(NS + "rdfp").addProperty(RDF.type, RDF.Property);

        List<String> expected = List.of("op");
        List<String> actual = m.listObjectProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListObjectProperties1b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        m.createObjectProperty(NS + "op");
        m.createDatatypeProperty(NS + "dp");
        m.createAnnotationProperty(NS + "ap");
        m.createOntProperty(NS + "ontp");
        m.createProperty(NS + "rdfp").addProperty(RDF.type, RDF.Property);

        List<String> expected = List.of("differentFrom", "disjointWith", "op", "sameAs");

        List<String> actual = m.listObjectProperties()
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
    public void testListObjectProperties2a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");

        int expected1;
        int expected2;

        if (spec == TestSpec.OWL_MEM) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_MEM_RULE_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_MEM_RDFS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_MEM_TRANS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_MEM_MICRO_RULE_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_MEM_MINI_RULE_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_DL_MEM) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_DL_MEM_RDFS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_DL_MEM_RULE_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_DL_MEM_TRANS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_LITE_MEM) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_LITE_MEM_RDFS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_LITE_MEM_RULES_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_LITE_MEM_TRANS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else {
            throw new IllegalStateException();
        }

        int actual1 = m.listObjectProperties().toList().size();
        int actual2 = m.listObjectProperties().toSet().size();

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListObjectProperties2b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");

        Assertions.assertThrows(ProfileException.class, () -> m.listObjectProperties().toList());
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
    public void testListObjectProperties3a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        int expected1;
        int expected2;

        if (spec == TestSpec.OWL_MEM) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_MEM_RULE_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_MEM_RDFS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_MEM_TRANS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_MEM_MICRO_RULE_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_MEM_MINI_RULE_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_DL_MEM) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_DL_MEM_RDFS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_DL_MEM_RULE_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_DL_MEM_TRANS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_LITE_MEM) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_LITE_MEM_RDFS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_LITE_MEM_RULES_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_LITE_MEM_TRANS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else {
            throw new IllegalStateException();
        }

        int actual1 = m.listObjectProperties().toList().size();
        int actual2 = m.listObjectProperties().toSet().size();

        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListObjectProperties3b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        Assertions.assertThrows(ProfileException.class, () -> m.listObjectProperties().toList());
    }

}
