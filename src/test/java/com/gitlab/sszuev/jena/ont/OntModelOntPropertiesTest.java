package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.IOTestUtils;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OntModelOntPropertiesTest {
    private static final String BASE = "http://www.test.com/test";
    private static final String NS = BASE + "#";

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_TRANS_INF",
    })
    public void testListOntProperties1a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        m.createObjectProperty(NS + "op");
        m.createDatatypeProperty(NS + "dp");
        m.createAnnotationProperty(NS + "ap");
        m.createOntProperty(NS + "ontp");
        m.createProperty(NS + "rdfp").addProperty(RDF.type, RDF.Property);

        List<String> actual = m.listOntProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        Assertions.assertEquals(List.of("ontp", "rdfp"), actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM_RDFS_INF",
    })
    public void testListOntProperties1b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        m.createObjectProperty(NS + "op");
        m.createDatatypeProperty(NS + "dp");
        m.createAnnotationProperty(NS + "ap");
        Resource ontp = m.createOntProperty(NS + "ontp");
        Resource rdfp = m.createProperty(NS + "rdfp").addProperty(RDF.type, RDF.Property);

        List<String> actual = m.listOntProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        List<String> expected = Stream.of(
                RDF.first, RDF.rest, RDF.subject, RDF.predicate, RDF.object, RDF.type, // RDF.value,
                RDFS.comment, RDFS.domain, RDFS.label, RDFS.isDefinedBy, RDFS.range, RDFS.seeAlso, RDFS.subClassOf, RDFS.subPropertyOf, // RDFS.member,
                ontp, rdfp
        ).map(Resource::getLocalName).sorted().collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
    })
    public void testListOntProperties1c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        ObjectProperty op = m.createObjectProperty(NS + "op");
        DatatypeProperty dp = m.createDatatypeProperty(NS + "dp");
        AnnotationProperty ap = m.createAnnotationProperty(NS + "ap");
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        List<String> actual = m.listOntProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        List<String> expected = Stream.of(
                RDF.first, RDF.rest, RDF.subject, RDF.predicate, RDF.object, RDF.type,
                RDFS.domain, RDFS.isDefinedBy, RDFS.range, RDFS.seeAlso, RDFS.subClassOf, RDFS.subPropertyOf,
                OWL.onProperty, OWL.sameAs, OWL.oneOf, OWL.equivalentClass, OWL.priorVersion, OWL.differentFrom, OWL.disjointWith,
                OWL.incompatibleWith, OWL.intersectionOf, OWL.backwardCompatibleWith, OWL.imports, OWL.versionInfo,
                ontp, rdfp, dp, op, ap
        ).map(Resource::getLocalName).sorted().collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RDFS_INF",
    })
    public void testListOntProperties1d(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        m.createObjectProperty(NS + "op");
        m.createDatatypeProperty(NS + "dp");
        AnnotationProperty ap = m.createAnnotationProperty(NS + "ap");
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        List<String> actual = m.listOntProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        List<String> expected = Stream.of(
                RDF.first, RDF.rest, RDF.subject, RDF.predicate, RDF.object, RDF.type,
                RDFS.domain, RDFS.comment, RDFS.isDefinedBy, RDFS.label, RDFS.seeAlso, RDFS.range, RDFS.subClassOf, RDFS.subPropertyOf,
                ontp, rdfp, ap
        ).map(Resource::getLocalName).sorted().collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListOntProperties1e(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        ObjectProperty op = m.createObjectProperty(NS + "op");
        DatatypeProperty dp = m.createDatatypeProperty(NS + "dp");
        AnnotationProperty ap = m.createAnnotationProperty(NS + "ap");
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        List<String> actual = m.listOntProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());
        System.out.println(actual);

        List<String> expected = Stream.of(
                RDF.first, RDF.rest, RDF.subject, RDF.predicate, RDF.object, RDF.type,
                RDFS.domain, RDFS.comment, RDFS.isDefinedBy, RDFS.label, RDFS.seeAlso, RDFS.range, RDFS.subClassOf, RDFS.subPropertyOf,
                OWL.onProperty, OWL.sameAs, OWL.oneOf, OWL.equivalentClass, OWL.equivalentProperty, OWL.priorVersion, OWL.differentFrom, OWL.disjointWith,
                OWL.incompatibleWith, OWL.intersectionOf, OWL.backwardCompatibleWith, OWL.imports, OWL.versionInfo,
                ontp, rdfp, ap, dp, op
        ).map(Resource::getLocalName).sorted().collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
    })
    public void testListOntProperties1f(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        m.createObjectProperty(NS + "op");
        m.createDatatypeProperty(NS + "dp");
        AnnotationProperty ap = m.createAnnotationProperty(NS + "ap");
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        List<String> actual = m.listOntProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        List<String> expected = Stream.of(
                ontp, rdfp, ap
        ).map(Resource::getLocalName).sorted().collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListOntProperties1g(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        ObjectProperty op = m.createObjectProperty(NS + "op");
        DatatypeProperty dp = m.createDatatypeProperty(NS + "dp");
        m.createAnnotationProperty(NS + "ap");
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        List<String> actual = m.listOntProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        List<String> expected = Stream.of(
                RDF.first, RDF.rest, RDF.subject, RDF.predicate, RDF.object, RDF.type,
                RDFS.domain, RDFS.isDefinedBy, RDFS.seeAlso, RDFS.range, RDFS.subClassOf, RDFS.subPropertyOf,
                OWL.onProperty, OWL.sameAs, OWL.oneOf, OWL.equivalentClass, OWL.priorVersion, OWL.differentFrom, OWL.disjointWith,
                OWL.incompatibleWith, OWL.intersectionOf, OWL.backwardCompatibleWith, OWL.imports,
                ontp, rdfp, op, dp
        ).map(Resource::getLocalName).sorted().collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListOntProperties1h(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        List<String> actual = m.listOntProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        List<String> expected = Stream.of(ontp, rdfp).map(Resource::getLocalName).sorted().collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM_RDFS_INF",
    })
    public void testListOntProperties1k(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntProperty ontp = m.createOntProperty(NS + "ontp");
        Property rdfp = m.createProperty(NS + "rdfp");
        rdfp.addProperty(RDF.type, RDF.Property);

        List<String> actual = m.listOntProperties()
                .mapWith(Property::getLocalName)
                .toList()
                .stream()
                .sorted()
                .collect(Collectors.toList());

        List<String> expected = Stream.of(
                RDF.first, RDF.rest, RDF.subject, RDF.predicate, RDF.object, RDF.type,
                RDFS.comment, RDFS.domain, RDFS.isDefinedBy, RDFS.label, RDFS.seeAlso, RDFS.range, RDFS.subClassOf, RDFS.subPropertyOf,
                ontp, rdfp
        ).map(Resource::getLocalName).sorted().collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListOntProperties2a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");

        List<OntProperty> actual = m.listOntProperties().toList();

        Assertions.assertEquals(1, actual.size());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM_RDFS_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListOntProperties2b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");

        List<OntProperty> actual = m.listOntProperties().toList();

        Assertions.assertEquals(15, actual.size());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListOntProperties2d(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");

        List<OntProperty> actual = m.listOntProperties().toList();

        Assertions.assertEquals(32, actual.size());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
    })
    public void testListOntProperties2e(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");

        List<OntProperty> actual1 = m.listOntProperties().toList();
        Set<OntProperty> actual2 = m.listOntProperties().toSet();
        Assertions.assertEquals(34, actual1.size());
        Assertions.assertEquals(33, actual2.size());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListOntProperties2f(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");

        int expected1;
        int expected2;
        if (spec == TestSpec.OWL_MEM_RDFS_INF) {
            expected1 = 16;
            expected2 = 16;
        } else if (spec == TestSpec.OWL_MEM_TRANS_INF) {
            expected1 = 2;
            expected2 = 2;
        } else if (spec == TestSpec.OWL_MEM_MICRO_RULE_INF) {
            expected1 = 37;
            expected2 = 36;
        } else {
            throw new IllegalStateException();
        }
        int actual1 = m.listOntProperties().toList().size();
        int actual2 = m.listOntProperties().toSet().size();
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListOntProperties3a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        int actual1 = m.listOntProperties().toList().size();
        int actual2 = m.listOntProperties().toSet().size();

        Assertions.assertEquals(44, actual1);
        Assertions.assertEquals(44, actual2);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_TRANS_INF",
    })
    public void testListOntProperties3b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        int actual1 = m.listOntProperties().toList().size();
        int actual2 = m.listOntProperties().toSet().size();

        Assertions.assertEquals(40, actual1);
        Assertions.assertEquals(40, actual2);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListOntProperties3c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        int actual1 = m.listOntProperties().toList().size();
        int actual2 = m.listOntProperties().toSet().size();

        Assertions.assertEquals(1, actual1);
        Assertions.assertEquals(1, actual2);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
    })
    public void testListOntProperties3d(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        int actual1 = m.listOntProperties().toList().size();
        int actual2 = m.listOntProperties().toSet().size();

        Assertions.assertEquals(50, actual1);
        Assertions.assertEquals(44, actual2);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListOntProperties3e(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        int expected1;
        int expected2;
        if (spec == TestSpec.OWL_MEM_TRANS_INF) {
            expected1 = 46;
            expected2 = 41;
        } else if (spec == TestSpec.RDFS_MEM_RDFS_INF) {
            expected1 = 15;
            expected2 = 15;
        } else {
            throw new IllegalStateException();
        }

        int actual1 = m.listOntProperties().toList().size();
        int actual2 = m.listOntProperties().toSet().size();

        System.out.println(spec + " " + actual1 + " " + actual2);
        Assertions.assertEquals(expected1, actual1);
        Assertions.assertEquals(expected2, actual2);
    }
}
