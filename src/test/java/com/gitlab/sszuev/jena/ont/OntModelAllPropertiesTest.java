package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.IOTestUtils;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.atlas.iterator.Iter;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.ProfileException;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OntModelAllPropertiesTest {

    private static final String BASE = "http://www.test.com/test";
    private static final String NS = BASE + "#";

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_TRANS_INF",
    })
    public void testListAllOntProperties1a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        // named object property
        Resource op = m.createResource(NS + "op", OWL.ObjectProperty);
        // inverse object property:
        m.createResource().addProperty(OWL.inverseOf, op);
        // datatype property
        m.createResource(NS + "dp", OWL.DatatypeProperty);
        // annotation property
        m.createResource(NS + "ap", OWL.AnnotationProperty);

        m.createResource(NS + "rp1", RDF.Property);
        m.createResource(NS + "rp2", RDF.Property);
        m.createResource(NS + "rest", RDF.rest);

        List<String> expected = Stream.of("ap", "dp", "op", "rp1", "rp2").sorted().collect(Collectors.toList());
        List<String> actual = Iter.asStream(m.listAllOntProperties())
                .map(Resource::getLocalName)
                .sorted()
                .collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM_RDFS_INF",
    })
    public void testListAllOntProperties1b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        // named object property
        Resource op = m.createResource(NS + "op", OWL.ObjectProperty);
        // inverse object property:
        m.createResource().addProperty(OWL.inverseOf, op);
        // datatype property
        m.createResource(NS + "dp", OWL.DatatypeProperty);
        // annotation property
        m.createResource(NS + "ap", OWL.AnnotationProperty);

        m.createResource(NS + "rp1", RDF.Property);
        m.createResource(NS + "rp2", RDF.Property);
        m.createResource(NS + "rest", RDF.rest);

        List<String> expected = Stream.of(
                "ap", "comment", "domain", "dp", "first", "isDefinedBy", "label", "object", "rp1", "op",
                "predicate", "range", "rp2", "rest", "seeAlso", "subClassOf", "subPropertyOf", "subject", "type"
        ).sorted().collect(Collectors.toList());

        List<String> actual = Iter.asStream(m.listAllOntProperties())
                .map(it -> it.isAnon() ? "null" : it.getLocalName())
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
    public void testListAllOntProperties1c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        // named object property
        Resource op = m.createResource(NS + "op", OWL.ObjectProperty);
        // inverse object property:
        m.createResource().addProperty(OWL.inverseOf, op);
        // datatype property
        m.createResource(NS + "dp", OWL.DatatypeProperty);
        // annotation property
        m.createResource(NS + "ap", OWL.AnnotationProperty);

        m.createResource(NS + "rp1", RDF.Property);
        m.createResource(NS + "rp2", RDF.Property);
        m.createResource(NS + "rest", RDF.rest);

        List<String> expected;
        if (spec == TestSpec.OWL_MEM_MICRO_RULE_INF) {
            expected = Stream.of("ap", "backwardCompatibleWith", "comment", "differentFrom", "disjointWith", "domain",
                    "dp", "equivalentClass", "equivalentProperty", "first", "imports", "incompatibleWith", "intersectionOf",
                    "isDefinedBy", "label", "object", "onProperty", "oneOf", "rp1", "op", "predicate", "priorVersion",
                    "range", "rp2", "rest", "sameAs", "seeAlso", "subClassOf", "subPropertyOf", "subject", "type", "versionInfo"
                    , "null", "inverseOf"
            ).sorted().collect(Collectors.toList());
        } else {
            expected = Stream.of(
                    "ap", "backwardCompatibleWith", "differentFrom", "disjointWith", "domain", "dp", "equivalentClass",
                    "first", "imports", "incompatibleWith", "intersectionOf", "isDefinedBy", "object", "onProperty", "oneOf",
                    "rp1", "op", "predicate", "priorVersion", "range", "rp2", "rest", "sameAs", "seeAlso", "subClassOf",
                    "subPropertyOf", "subject", "type", "versionInfo", "null", "inverseOf"
            ).sorted().collect(Collectors.toList());
        }

        List<String> actual = Iter.asStream(m.listAllOntProperties())
                .map(it -> it.isAnon() ? "null" : it.getLocalName())
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
        if (spec == TestSpec.OWL_MEM
                || spec == TestSpec.OWL_MEM_TRANS_INF
                || spec == TestSpec.OWL_DL_MEM
                || spec == TestSpec.OWL_DL_MEM_TRANS_INF
                || spec == TestSpec.OWL_LITE_MEM
                || spec == TestSpec.OWL_LITE_MEM_TRANS_INF) {
            expected1 = 5;
            expected2 = 5;
        } else if (spec == TestSpec.OWL_MEM_RULE_INF
                || spec == TestSpec.OWL_MEM_MINI_RULE_INF
                || spec == TestSpec.OWL_DL_MEM_RULE_INF
                || spec == TestSpec.OWL_LITE_MEM_RULES_INF) {
            expected1 = 33;
            expected2 = 33;
        } else if (spec == TestSpec.OWL_MEM_RDFS_INF
                || spec == TestSpec.OWL_DL_MEM_RDFS_INF
                || spec == TestSpec.OWL_LITE_MEM_RDFS_INF) {
            expected1 = 19;
            expected2 = 19;
        } else if (spec == TestSpec.OWL_MEM_MICRO_RULE_INF) {
            expected1 = 36;
            expected2 = 36;
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

        int expected1 = 44;
        int expected2 = 44;

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
