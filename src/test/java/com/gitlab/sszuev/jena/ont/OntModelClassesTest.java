package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.IOTestUtils;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.atlas.iterator.Iter;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OntModelClassesTest {

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
    public void testListClasses1a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");
        List<String> actual = Iter.asStream(m.listClasses())
                .map(it -> it.isAnon() ? "null" : it.getLocalName())
                .sorted()
                .collect(Collectors.toList());
        List<String> expected = List.of("A", "B", "C", "D", "E", "X0", "X1", "Y0", "Y1", "Z", "null");
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListClasses1b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");
        Assertions.assertEquals(0, m.listClasses().toList().size());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListClasses1c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test.rdf");
        int expectedClassesSize;
        if (spec == TestSpec.OWL_MEM_MICRO_RULE_INF) {
            expectedClassesSize = 36;
        } else if (spec == TestSpec.RDFS_MEM_RDFS_INF) {
            expectedClassesSize = 25;
        } else {
            expectedClassesSize = 42;
        }
        boolean expectedContainsAll = TestSpec.RDFS_MEM_RDFS_INF != spec;
        Assertions.assertEquals(expectedClassesSize, m.listClasses().toList().size());
        Assertions.assertEquals(
                expectedContainsAll,
                m.listClasses().mapWith(Resource::getLocalName).toSet()
                        .containsAll(Set.of("Y0", "Z", "B", "D", "Y1", "X0", "C", "E", "X1", "A"))
        );
    }

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
    public void testListClasses2a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");
        Assertions.assertEquals(
                List.of("A", "B", "C", "D", "E", "Nothing", "Thing", "X0", "X1", "Y0", "Y1", "Z", "null", "null"),
                Iter.asStream(m.listClasses()
                                .mapWith(it -> it.isAnon() ? "null" : it.getLocalName()))
                        .sorted()
                        .collect(Collectors.toList())
        );
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListClasses2b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        Assertions.assertTrue(m.listClasses().toSet().isEmpty());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListClasses2c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.getDocumentManager().addAltEntry("http://www.w3.org/2002/07/owl", IOTestUtils.normalize("file:jena/builtins-owl.rdf"));
        m.getDocumentManager().addAltEntry("http://www.w3.org/2000/01/rdf-schema", IOTestUtils.normalize("file:jena/builtins-rdfs.rdf"));
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-with-import.rdf");

        List<String> actual = Iter.asStream(m.listClasses()
                        .mapWith(it -> it.isAnon() ? "null" : it.getLocalName()))
                .sorted()
                .collect(Collectors.toList());
        Set<String> probes = Set.of("A", "B", "C", "D", "E", "Nothing", "Thing", "X0", "X1", "Y0", "Y1", "Z", "null");

        boolean expectedContainsAll = spec != TestSpec.RDFS_MEM_RDFS_INF;
        int expectedClassCount;
        if (spec == TestSpec.RDFS_MEM_RDFS_INF) {
            expectedClassCount = 25;
        } else if (spec == TestSpec.OWL_MEM_MICRO_RULE_INF) {
            expectedClassCount = 43;
        } else {
            expectedClassCount = 61;
        }
        Assertions.assertEquals(expectedContainsAll, actual.containsAll(probes));
        Assertions.assertEquals(expectedClassCount, actual.size());
    }

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
    public void testListClasses3a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-comps.xml");
        List<String> expected = List.of(
                "eg:Bundle", "eg:Computer", "eg:GameBundle", "eg:GamingComputer", "eg:GraphicsCard", "eg:MotherBoard",
                "null", "null", "null"
        );
        List<String> actual = Iter.asStream(m.listClasses())
                .map(it -> it.isAnon() ? "null" : m.shortForm(it.getURI()))
                .sorted()
                .collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListClasses3b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-comps.xml");
        List<String> expected = List.of(
                "eg:Bundle", "eg:Computer", "eg:GameBundle", "eg:GamingComputer", "eg:GraphicsCard", "eg:MotherBoard",
                "null", "null", "null", "null", "null",
                "owl:Class", "owl:Nothing", "owl:Ontology", "owl:Property", "owl:Restriction", "owl:Thing",
                "rdf:List", "rdf:Property", "rdf:Statement",
                "rdfs:Class", "rdfs:Literal", "rdfs:Resource",
                "xsd:boolean", "xsd:byte", "xsd:date", "xsd:dateTime", "xsd:decimal", "xsd:duration", "xsd:float", "xsd:int",
                "xsd:integer", "xsd:long", "xsd:nonNegativeInteger", "xsd:nonPositiveInteger", "xsd:short", "xsd:string",
                "xsd:time", "xsd:unsignedByte", "xsd:unsignedInt", "xsd:unsignedLong", "xsd:unsignedShort"
        );
        List<String> actual = Iter.asStream(m.listClasses())
                .map(it -> it.isAnon() ? "null" : m.shortForm(it.getURI()))
                .sorted()
                .collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListClasses3c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-comps.xml");
        List<String> expected = List.of();
        List<String> actual = Iter.asStream(m.listClasses())
                .map(it -> it.isAnon() ? "null" : m.shortForm(it.getURI()))
                .sorted()
                .collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM_RDFS_INF",
    })
    public void testListClasses3d(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-comps.xml");
        List<String> expected = List.of(
                "eg:Bundle", "eg:Computer", "eg:GameBundle", "eg:GraphicsCard", "eg:MotherBoard",
                "null",
                "owl:Class", "owl:ObjectProperty", "owl:Restriction", "owl:TransitiveProperty",
                "rdf:Alt", "rdf:Bag", "rdf:List", "rdf:Property", "rdf:Seq", "rdf:Statement", "rdf:XMLLiteral",
                "rdfs:Class", "rdfs:Container", "rdfs:ContainerMembershipProperty", "rdfs:Datatype", "rdfs:Literal", "rdfs:Resource"
        );
        List<String> actual = Iter.asStream(m.listClasses())
                .map(it -> it.isAnon() ? "null" : m.shortForm(it.getURI()))
                .sorted()
                .collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListClasses3e(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        IOTestUtils.readResourceModel(m, "/jena/list-syntax-categories-test-comps.xml");
        List<String> expected = List.of(
                "eg:Bundle", "eg:Computer", "eg:GameBundle", "eg:GamingComputer", "eg:GraphicsCard", "eg:MotherBoard",
                "null", "null", "null", "null", "null", "owl:Class",
                "owl:DatatypeProperty", "owl:FunctionalProperty", "owl:InverseFunctionalProperty", "owl:Nothing",
                "owl:ObjectProperty", "owl:Ontology", "owl:OntologyProperty", "owl:Property",
                "owl:Restriction", "owl:SymmetricProperty", "owl:Thing", "owl:TransitiveProperty",
                "rdf:Alt", "rdf:Bag", "rdf:List", "rdf:Property", "rdf:Seq", "rdf:Statement",
                "rdfs:Class", "rdfs:Container", "rdfs:ContainerMembershipProperty", "rdfs:Datatype", "rdfs:Literal", "rdfs:Resource"
        );
        List<String> actual = Iter.asStream(m.listClasses())
                .map(it -> it.isAnon() ? "null" : m.shortForm(it.getURI()))
                .sorted()
                .collect(Collectors.toList());
        Assertions.assertEquals(expected, actual);
    }
}
