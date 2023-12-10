package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.atlas.iterator.Iter;
import org.apache.jena.ontology.DataRange;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.util.NodeCmp;
import org.apache.jena.vocabulary.OWL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OntModelHierarchyRootsTest {
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
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListHierarchyRoots0(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        Assertions.assertFalse(m.listHierarchyRootClasses().hasNext());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListHierarchyRoots1a(TestSpec spec) {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a owl:Class. ";

        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.read(new StringReader(doc), NS, "N3");
        Resource A = m.getResource(NS + "A");
        Assertions.assertEquals(List.of(A), m.listHierarchyRootClasses().toList());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListHierarchyRoots1b(TestSpec spec) {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a owl:Class. ";
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.read(new StringReader(doc), NS, "N3");
        Assertions.assertEquals(List.of(), m.listHierarchyRootClasses().toList());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListHierarchyRoots2a(TestSpec spec) {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a owl:Class. "
                        + ":B a owl:Class ; rdfs:subClassOf :A . ";

        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.read(new StringReader(doc), NS, "N3");
        Resource A = m.getResource(NS + "A");
        Assertions.assertEquals(List.of(A), m.listHierarchyRootClasses().toList());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListHierarchyRoots2b(TestSpec spec) {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a owl:Class. "
                        + ":B a owl:Class ; rdfs:subClassOf :A . ";

        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.read(new StringReader(doc), NS, "N3");
        Assertions.assertEquals(List.of(), m.listHierarchyRootClasses().toList());
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
    public void testListHierarchyRoots3a(TestSpec spec) {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a rdfs:Class. "
                        + ":C a rdfs:Class. "
                        + ":B a rdfs:Class ; rdfs:subClassOf :A . ";

        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.read(new StringReader(doc), NS, "N3");
        Assertions.assertEquals(Set.of(), m.listHierarchyRootClasses().toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListHierarchyRoots3b(TestSpec spec) {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a rdfs:Class. "
                        + ":C a rdfs:Class. "
                        + ":B a rdfs:Class ; rdfs:subClassOf :A . ";

        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.read(new StringReader(doc), NS, "N3");

        Resource A = m.getResource(NS + "A");
        Resource C = m.getResource(NS + "C");
        Assertions.assertEquals(Set.of(A, C), m.listHierarchyRootClasses().toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
    })
    public void testListHierarchyRoots4a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);

        OntClass c0 = m.createClass(":C0");
        OntClass c1 = m.createClass(":C1");
        OntClass c2 = m.createClass(":C2");
        OntClass c3 = m.createClass(":C3");
        OntClass c4 = m.createClass(":C4");
        OntClass c5 = m.createClass(":C5");
        OntClass c6 = m.createClass(":C6");
        OntClass c7 = m.createClass(":C7");

        //_:x rdf:type owl:Restriction.
        //_:x owl:onProperty R.
        //_:x owl:someValuesFrom D.
        DataRange dr = m.createDataRange(m.createList(m.createLiteral("42")));
        System.out.println(dr);
        OntClass c8 = m.createSomeValuesFromRestriction(
                null, m.createDatatypeProperty(":p1"), dr
        );

        // _:x rdf:type owl:Class.
        // _:x owl:oneOf ( a1 … an ).
        OntClass c9 = m.createEnumeratedClass(
                null, m.createList(m.createIndividual(null, c0), m.createIndividual(null, c1))
        );

        OntClass c10 = m.createComplementClass(null, c6);

        OntClass c11 = OWL.Thing.inModel(m).as(OntClass.class);
        OntClass c12 = OWL.Nothing.inModel(m).as(OntClass.class);

        c1.addSuperClass(c2);
        c2.addSuperClass(c3);
        c3.addSuperClass(c4);
        c5.addSuperClass(c6);
        c6.addSuperClass(c12);
        c8.addSuperClass(c9);
        c9.addSuperClass(c5);
        c9.addSuperClass(c7);
        c10.addSuperClass(c11);

        List<Resource> actual = Iter.asStream(m.listHierarchyRootClasses())
                .sorted((r1, r2) -> NodeCmp.compareRDFTerms(r1.asNode(), r2.asNode()))
                .collect(Collectors.toList());

        Set<Resource> expected = Set.of(c10, c0, c4, c7);

        Assertions.assertEquals(4, actual.size());
        Assertions.assertEquals(expected, new HashSet<>(actual));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
    })
    public void testListHierarchyRoots4b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);

        OntClass c0 = m.createClass(":C0");
        OntClass c1 = m.createClass(":C1");
        OntClass c2 = m.createClass(":C2");
        OntClass c3 = m.createClass(":C3");
        OntClass c4 = m.createClass(":C4");
        OntClass c5 = m.createClass(":C5");
        OntClass c6 = m.createClass(":C6");
        OntClass c7 = m.createClass(":C7");

        //_:x rdf:type owl:Restriction.
        //_:x owl:onProperty R.
        //_:x owl:someValuesFrom D.
        DataRange dr = m.createDataRange(m.createList(m.createLiteral("42")));
        OntClass c8 = m.createSomeValuesFromRestriction(
                null, m.createDatatypeProperty(":p1"), dr
        );

        // _:x rdf:type owl:Class.
        // _:x owl:oneOf ( a1 … an ).
        OntClass c9 = m.createEnumeratedClass(
                null, m.createList(m.createIndividual(null, c0), m.createIndividual(null, c1))
        );

        OntClass c10 = m.createComplementClass(null, c6);

        OntClass c11 = OWL.Thing.inModel(m).as(OntClass.class);
        OntClass c12 = OWL.Nothing.inModel(m).as(OntClass.class);

        c1.addSuperClass(c2);
        c2.addSuperClass(c3);
        c3.addSuperClass(c4);
        c5.addSuperClass(c6);
        c6.addSuperClass(c12);
        c8.addSuperClass(c9);
        c9.addSuperClass(c5);
        c9.addSuperClass(c7);
        c10.addSuperClass(c11);

        List<Resource> actual = Iter.asStream(m.listHierarchyRootClasses())
                .collect(Collectors.toList());
        Set<Resource> expected = Set.of(dr, c10, c0, c4, c7);

        Assertions.assertEquals(5, actual.size());
        Assertions.assertEquals(expected, new HashSet<>(actual));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
    })
    public void testListHierarchyRoots4c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);

        OntClass c0 = m.createClass(":C0");
        OntClass c1 = m.createClass(":C1");
        OntClass c2 = m.createClass(":C2");
        OntClass c3 = m.createClass(":C3");
        OntClass c4 = m.createClass(":C4");
        OntClass c5 = m.createClass(":C5");
        OntClass c6 = m.createClass(":C6");
        OntClass c7 = m.createClass(":C7");

        //_:x rdf:type owl:Restriction.
        //_:x owl:onProperty R.
        //_:x owl:someValuesFrom D.
        OntClass c8 = m.createSomeValuesFromRestriction(
                null, m.createDatatypeProperty(":p1"), m.createDataRange(m.createList(m.createLiteral("42")))
        );

        // _:x rdf:type owl:Class.
        // _:x owl:oneOf ( a1 … an ).
        OntClass c9 = m.createEnumeratedClass(
                null, m.createList(m.createIndividual(null, c0), m.createIndividual(null, c1))
        );

        OntClass c10 = m.createComplementClass(null, c6);

        OntClass c11 = OWL.Thing.inModel(m).as(OntClass.class);
        OntClass c12 = OWL.Nothing.inModel(m).as(OntClass.class);

        c1.addSuperClass(c2);
        c2.addSuperClass(c3);
        c3.addSuperClass(c4);
        c5.addSuperClass(c6);
        c6.addSuperClass(c12);
        c8.addSuperClass(c9);
        c9.addSuperClass(c5);
        c9.addSuperClass(c7);
        c10.addSuperClass(c11);

        List<Resource> actual = Iter.asStream(m.listHierarchyRootClasses())
                .sorted((r1, r2) -> NodeCmp.compareRDFTerms(r1.asNode(), r2.asNode()))
                .collect(Collectors.toList());
        Set<Resource> expected = Set.of(c10, c11);

        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(expected, new HashSet<>(actual));
    }
}
