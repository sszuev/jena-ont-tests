package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.IOTestUtils;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntTools;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gitlab.sszuev.jena.ont.TestModelFactory.NS;

public class OntToolsTest {

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "RDFS_MEM",
            "OWL_MEM_MICRO_RULE_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testNamedHierarchyRoots1a(TestSpec spec) {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M
        OntModel m = TestModelFactory.createClassesABCDEFGHKLM(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getOntClass(NS + "A");
        Assertions.assertEquals(List.of(A), OntTools.namedHierarchyRoots(m));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testNamedHierarchyRoots1b(TestSpec spec) {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M
        OntModel m = TestModelFactory.createClassesABCDEFGHKLM(ModelFactory.createOntologyModel(spec.inst));
        Assertions.assertEquals(List.of(), OntTools.namedHierarchyRoots(m));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
    })
    public void testNamedHierarchyRoots2a(TestSpec spec) {
        OntModel m = IOTestUtils.readResourceModel(ModelFactory.createOntologyModel(spec.inst), "/jena/pizza.ttl", "ttl");
        Set<String> actual = OntTools.namedHierarchyRoots(m).stream().map(Resource::getLocalName).collect(Collectors.toSet());
        Set<String> expected = Set.of(
                "NonVegetarianPizza",
                "VegetarianTopping",
                "DomainConcept",
                "SpicyPizza",
                "VegetarianPizza",
                "SpicyPizzaEquivalent",
                "MeatyPizza",
                "CheeseyPizza",
                "VegetarianPizzaEquivalent2",
                "Country",
                "ThinAndCrispyPizza",
                "SpicyTopping",
                "ValuePartition",
                "VegetarianPizzaEquivalent1",
                "InterestingPizza",
                "RealItalianPizza");
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM_RDFS_INF",
    })
    public void testNamedHierarchyRoots2b(TestSpec spec) {
        OntModel m = IOTestUtils.readResourceModel(ModelFactory.createOntologyModel(spec.inst), "/jena/pizza.ttl", "ttl");
        Set<String> actual = OntTools.namedHierarchyRoots(m).stream().map(Resource::getLocalName).collect(Collectors.toSet());
        Set<String> expected = Set.of(
                "DomainConcept",
                "Country",
                "ValuePartition",
                "RealItalianPizza");
        Assertions.assertEquals(expected, actual);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testNamedHierarchyRoots2c(TestSpec spec) {
        OntModel m = IOTestUtils.readResourceModel(ModelFactory.createOntologyModel(spec.inst), "/jena/pizza.ttl", "ttl");
        Set<String> actual = OntTools.namedHierarchyRoots(m).stream().map(Resource::getLocalName).collect(Collectors.toSet());
        Assertions.assertEquals(Set.of(), actual);
    }

    @Test
    public void testNamedHierarchyRoots3() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass F = m.createClass(NS + "F");
        OntClass G = m.createClass(NS + "G");

        A.addSubClass(B);
        B.addSubClass(C);
        C.addSubClass(D);
        E.addSubClass(E);
        E.addSubClass(F);

        List<OntClass> nhr = OntTools.namedHierarchyRoots(m);
        Assertions.assertEquals(3, nhr.size());
        Assertions.assertTrue(nhr.contains(A));
        Assertions.assertTrue(nhr.contains(E));
        Assertions.assertTrue(nhr.contains(G));
    }

    @Test
    public void testNamedHierarchyRoots4() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass F = m.createClass(NS + "F");
        OntClass G = m.createClass(NS + "G");

        A.addSubClass(B);
        B.addSubClass(C);
        C.addSubClass(D);
        E.addSubClass(E);
        E.addSubClass(F);

        OntClass anon0 = m.createUnionClass(null, m.createList(A, F));
        anon0.addSubClass(A);
        anon0.addSubClass(E);

        List<OntClass> nhr = OntTools.namedHierarchyRoots(m);
        Assertions.assertEquals(3, nhr.size());
        Assertions.assertTrue(nhr.contains(A));
        Assertions.assertTrue(nhr.contains(E));
        Assertions.assertTrue(nhr.contains(G));
    }

    @Test
    public void testNamedHierarchyRoots5() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass F = m.createClass(NS + "F");
        OntClass G = m.createClass(NS + "G");

        OntClass anon0 = m.createUnionClass(null, m.createList(A, B, G));
        OntClass anon1 = m.createUnionClass(null, m.createList(C, D, anon0));
        anon0.addSubClass(A);
        anon0.addSubClass(E);
        anon0.addSubClass(anon1);
        anon1.addSubClass(G);

        A.addSubClass(B);
        B.addSubClass(C);
        C.addSubClass(D);
        E.addSubClass(E);
        E.addSubClass(F);

        List<OntClass> nhr = OntTools.namedHierarchyRoots(m);
        Assertions.assertEquals(3, nhr.size());
        Assertions.assertTrue(nhr.contains(A));
        Assertions.assertTrue(nhr.contains(E));
        Assertions.assertTrue(nhr.contains(G));
    }

    @Test
    public void testNamedHierarchyRoots6() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass F = m.createClass(NS + "F");
        OntClass G = m.createClass(NS + "G");

        OntClass anon0 = m.createComplementClass(null, F);
        OntClass anon1 = m.createUnionClass(null, m.createList(F));
        anon0.addSubClass(A);
        anon1.addSubClass(A);

        // only a is root
        A.addSubClass(B);
        A.addSubClass(C);
        A.addSubClass(D);
        A.addSubClass(E);
        A.addSubClass(F);
        A.addSubClass(G);

        List<OntClass> nhr = OntTools.namedHierarchyRoots(m);
        Assertions.assertEquals(1, nhr.size());
        Assertions.assertTrue(nhr.contains(A));
    }

    @Test
    public void testNamedHierarchyRoots7() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass F = m.createClass(NS + "F");
        OntClass G = m.createClass(NS + "G");

        OntClass anon0 = m.createUnionClass(null, m.createList(A, B));
        OntClass anon1 = m.createUnionClass(null, m.createList(C, D, anon0));
        anon0.addSubClass(A);
        anon1.addSubClass(B);

        // only a is root, because b is a subclass of a
        // even though b is a sub-class of an anon root
        A.addSubClass(B);
        A.addSubClass(C);
        A.addSubClass(D);
        A.addSubClass(E);
        A.addSubClass(F);
        A.addSubClass(G);

        List<OntClass> nhr = OntTools.namedHierarchyRoots(m);
        Assertions.assertEquals(1, nhr.size());
        Assertions.assertTrue(nhr.contains(A));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testNamedHierarchyRoots8a(TestSpec spec) {
        // D        G
        // |      / .
        // C  F  K  .
        // |  |  |  .
        // B  E  H  .
        // |      \ .
        // A        G
        OntModel m = TestModelFactory.createClassesDGCFKBEHAG(ModelFactory.createOntologyModel(spec.inst));
        OntClass F = m.getOntClass(NS + "F");
        OntClass D = m.getOntClass(NS + "D");
        Assertions.assertEquals(Set.of(F, D), new HashSet<>(OntTools.namedHierarchyRoots(m)));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
    })
    public void testNamedHierarchyRoots9a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);

        OntClass c0 = m.createClass(":C0");
        OntClass c1 = m.createClass(":C1");
        OntClass c2 = m.createClass(":C2");
        OntClass c3 = m.createClass(":C3");
        OntClass c4 = m.createClass(":C4");
        OntClass c5 = m.createClass(":C5");
        OntClass c6 = m.createClass(":C6");
        OntClass c7 = m.createClass(":C7");
        OntClass c13 = m.createClass(":C13");
        OntClass c8 = m.createSomeValuesFromRestriction(
                null, m.createDatatypeProperty(":p1"), m.createDataRange(m.createList(m.createLiteral("42")))
        );
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
        c13.addSuperClass(c10);

        List<OntClass> actual = OntTools.namedHierarchyRoots(m);
        Set<Resource> expected = Set.of(c0, c4, c7, c13);

        Assertions.assertEquals(4, actual.size());
        Assertions.assertEquals(expected, new HashSet<>(actual));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
    })
    public void testNamedHierarchyRoots9b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);

        OntClass c0 = m.createClass(":C0");
        OntClass c1 = m.createClass(":C1");
        OntClass c2 = m.createClass(":C2");
        OntClass c3 = m.createClass(":C3");
        OntClass c4 = m.createClass(":C4");
        OntClass c5 = m.createClass(":C5");
        OntClass c6 = m.createClass(":C6");
        OntClass c7 = m.createClass(":C7");
        OntClass c13 = m.createClass(":C13");

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
        c13.addSuperClass(c10);

        List<OntClass> actual = OntTools.namedHierarchyRoots(m);
        Assertions.assertTrue(actual.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
    })
    public void testNamedHierarchyRoots9c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);

        OntClass c0 = m.createClass(":C0");
        OntClass c1 = m.createClass(":C1");
        OntClass c2 = m.createClass(":C2");
        OntClass c3 = m.createClass(":C3");
        OntClass c4 = m.createClass(":C4");
        OntClass c5 = m.createClass(":C5");
        OntClass c6 = m.createClass(":C6");
        OntClass c7 = m.createClass(":C7");
        OntClass c13 = m.createClass(":C13");

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
        c13.addSuperClass(c10);

        List<OntClass> actual = OntTools.namedHierarchyRoots(m);
        Set<Resource> expected = Set.of(c11, c13);

        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(expected, new HashSet<>(actual));
    }

    @Test
    public void testShortestPath0() {
        Model m = TestModelFactory.createStdModelClassesABCDEFGThing();
        Resource A = m.getResource(NS + "A");
        Resource B = m.getResource(NS + "B");

        List<Statement> actual = OntTools.findShortestPath(m, A, B, s -> true);
        Assertions.assertNull(actual);
    }

    @Test
    public void testShortestPath1() {
        Model m = TestModelFactory.createStdModelClassesABCDEFGThing();
        Resource A = m.getResource(NS + "A");
        Resource B = m.getResource(NS + "B");
        Property p = m.createProperty(NS + "p");
        A.addProperty(p, B);

        List<Statement> actual = OntTools.findShortestPath(m, A, B, s -> true);
        Assertions.assertEquals(List.of(p), actual.stream().map(Statement::getPredicate).toList());
    }

    @Test
    public void testShortestPath2() {
        Model m = TestModelFactory.createStdModelClassesABCDEFGThing();
        Resource A = m.getResource(NS + "A");
        Resource B = m.getResource(NS + "B");
        Resource C = m.getResource(NS + "C");
        Property p = m.createProperty(NS + "p");
        A.addProperty(p, B);
        B.addProperty(p, C);

        List<Statement> actual = OntTools.findShortestPath(m, A, C, s -> true);
        Assertions.assertEquals(List.of(p, p), actual.stream().map(Statement::getPredicate).toList());
    }

    @Test
    public void testShortestPath3() {
        Model m = TestModelFactory.createStdModelClassesABCDEFGThing();
        Resource A = m.getResource(NS + "A");
        Resource B = m.getResource(NS + "B");
        Resource C = m.getResource(NS + "C");
        Resource D = m.getResource(NS + "D");
        Resource E = m.getResource(NS + "E");
        Resource F = m.getResource(NS + "F");
        Property p = m.createProperty(NS + "p");
        // a - b - c
        A.addProperty(p, B);
        B.addProperty(p, C);

        // a - d - e - f
        A.addProperty(p, D);
        D.addProperty(p, E);
        E.addProperty(p, F);

        List<Statement> actual1 = OntTools.findShortestPath(m, A, C, s -> true);
        Assertions.assertEquals(List.of(p, p), actual1.stream().map(Statement::getPredicate).toList());

        List<Statement> actual2 = OntTools.findShortestPath(m, A, F, s -> true);
        Assertions.assertEquals(List.of(p, p, p), actual2.stream().map(Statement::getPredicate).toList());

        List<Statement> actual3 = OntTools.findShortestPath(m, A, C, s -> p.equals(s.getPredicate()));
        Assertions.assertEquals(List.of(p, p), actual3.stream().map(Statement::getPredicate).toList());

        List<Statement> actual4 = OntTools.findShortestPath(m, A, F, s -> p.equals(s.getPredicate()));
        Assertions.assertEquals(List.of(p, p, p), actual4.stream().map(Statement::getPredicate).toList());
    }

    @Test
    public void testShortestPath4() {
        Model m = TestModelFactory.createStdModelClassesABCDEFGThing();
        Resource A = m.getResource(NS + "A");
        Resource B = m.getResource(NS + "B");
        Resource C = m.getResource(NS + "C");
        Resource D = m.getResource(NS + "D");
        Resource E = m.getResource(NS + "E");
        Resource F = m.getResource(NS + "F");
        Property p = m.createProperty(NS + "p");
        Property q = m.createProperty(NS + "q");

        // a - b - c by q
        A.addProperty(q, B);
        B.addProperty(q, C);

        // a - d - e - f by p
        A.addProperty(p, D);
        D.addProperty(p, E);
        E.addProperty(p, F);

        List<Statement> actual1 = OntTools.findShortestPath(m, A, C, s -> p.equals(s.getPredicate()));
        Assertions.assertNull(actual1);
        List<Statement> actual2 = OntTools.findShortestPath(m, A, F, s -> p.equals(s.getPredicate()));
        Assertions.assertEquals(List.of(p, p, p), actual2.stream().map(Statement::getPredicate).toList());
    }

    @Test
    public void testShortestPath5() {
        Model m = TestModelFactory.createStdModelClassesABCDEFGThing();
        Resource A = m.getResource(NS + "A");
        Property p = m.createProperty(NS + "p");
        A.addProperty(p, A);

        List<Statement> actual = OntTools.findShortestPath(m, A, A, s -> true);
        Assertions.assertEquals(List.of(p), actual.stream().map(Statement::getPredicate).toList());
    }

    @Test
    public void testShortestPath6() {
        Model m = TestModelFactory.createStdModelClassesABCDEFGThing();
        Resource A = m.getResource(NS + "A");
        Resource B = m.getResource(NS + "B");
        Resource C = m.getResource(NS + "C");
        Property p = m.createProperty(NS + "p");
        Property q = m.createProperty(NS + "q");
        // a - b - a by q
        // tests loop detection
        A.addProperty(q, B);
        B.addProperty(q, A);

        List<Statement> actual = OntTools.findShortestPath(m, A, C, s -> Set.of(p, q).contains(s.getPredicate()));
        Assertions.assertNull(actual);
    }

    @Test
    public void testShortestPath7() {
        Model m = TestModelFactory.createStdModelClassesABCDEFGThing();
        Resource A = m.getResource(NS + "A");
        Resource B = m.getResource(NS + "B");
        Resource D = m.getResource(NS + "D");
        Resource E = m.getResource(NS + "E");
        Resource F = m.getResource(NS + "F");
        Property p = m.createProperty(NS + "p");
        Property q = m.createProperty(NS + "q");

        // a - d - e - f by p and q
        A.addProperty(p, D);
        D.addProperty(q, E);
        D.addProperty(q, B);
        E.addProperty(p, F);

        List<Statement> actual = OntTools.findShortestPath(m, A, F, s -> Set.of(p, q).contains(s.getPredicate()));
        Assertions.assertEquals(List.of(p, q, p), actual.stream().map(Statement::getPredicate).toList());
    }

    @Test
    public void testShortestPath8() {
        Model m = TestModelFactory.createStdModelClassesABCDEFGThing();
        Resource A = m.getResource(NS + "A");
        Resource B = m.getResource(NS + "B");
        Resource D = m.getResource(NS + "D");
        Resource E = m.getResource(NS + "E");
        Resource F = m.getResource(NS + "F");
        Property p = m.createProperty(NS + "p");
        Property q = m.createProperty(NS + "q");

        // a - d - e - f by p and q
        A.addProperty(p, D);
        D.addProperty(q, E);
        D.addProperty(q, "bluff");
        D.addProperty(q, B);
        E.addProperty(p, F);
        F.addProperty(q, "arnie");

        List<Statement> actual = OntTools.findShortestPath(m, A, ResourceFactory.createPlainLiteral("arnie"),
                s -> Set.of(p, q).contains(s.getPredicate()));
        Assertions.assertEquals(List.of(p, q, p, q), actual.stream().map(Statement::getPredicate).toList());
    }

    @Test
    public void testIndexLCA0() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        A.addSubClass(B);
        A.addSubClass(C);

        Assertions.assertEquals(A, OntTools.getLCA(m, B, C));
    }

    @Test
    public void testIndexLCA1() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        A.addSubClass(B);
        A.addSubClass(C);

        Assertions.assertEquals(A, OntTools.getLCA(m, C, B));
    }

    @Test
    public void testIndexLCA2() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        A.addSubClass(B);
        A.addSubClass(C);

        Assertions.assertEquals(A, OntTools.getLCA(m, A, C));
    }

    @Test
    public void testIndexLCA3() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        A.addSubClass(B);
        A.addSubClass(C);

        Assertions.assertEquals(A, OntTools.getLCA(m, B, A));
    }

    @Test
    public void testIndexLCA4() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        A.addSubClass(B);
        A.addSubClass(C);
        B.addSubClass(D);

        Assertions.assertEquals(A, OntTools.getLCA(m, D, C));
    }

    @Test
    public void testIndexLCA5() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        A.addSubClass(B);
        A.addSubClass(C);
        B.addSubClass(D);

        Assertions.assertEquals(A, OntTools.getLCA(m, C, D));
    }

    @Test
    public void testIndexLCA6() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");

        A.addSubClass(B);
        A.addSubClass(C);
        B.addSubClass(D);
        C.addSubClass(E);

        Assertions.assertEquals(A, OntTools.getLCA(m, D, E));
    }

    @Test
    public void testIndexLCA7() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");

        A.addSubClass(B);
        A.addSubClass(C);
        B.addSubClass(D);
        C.addSubClass(E);

        Assertions.assertEquals(A, OntTools.getLCA(m, E, D));
    }

    @Test
    public void testIndexLCA8() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");

        A.addSubClass(B);
        A.addSubClass(C);
        B.addSubClass(D);
        D.addSubClass(E);

        Assertions.assertEquals(A, OntTools.getLCA(m, C, E));
    }

    @Test
    public void testIndexLCA9() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");

        A.addSubClass(B);
        A.addSubClass(C);
        B.addSubClass(D);
        D.addSubClass(E);

        Assertions.assertEquals(A, OntTools.getLCA(m, B, C));
    }

    @Test
    public void testIndexLCA10() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");
        OntClass F = m.getOntClass(NS + "F");

        A.addSubClass(B);
        A.addSubClass(C);
        A.addSubClass(D);
        C.addSubClass(E);
        D.addSubClass(F);

        Assertions.assertEquals(A, OntTools.getLCA(m, B, E));
    }

    @Test
    public void testIndexLCA11() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");
        OntClass F = m.getOntClass(NS + "F");

        A.addSubClass(B);
        A.addSubClass(C);
        A.addSubClass(D);
        C.addSubClass(E);
        D.addSubClass(F);

        Assertions.assertEquals(A, OntTools.getLCA(m, B, F));
    }

    @Test
    public void testIndexLCA12() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");
        OntClass F = m.getOntClass(NS + "F");

        A.addSubClass(B);
        A.addSubClass(C);
        A.addSubClass(D);
        D.addSubClass(E);
        D.addSubClass(F);

        Assertions.assertEquals(D, OntTools.getLCA(m, F, E));
    }

    @Test
    public void testIndexLCA13() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");
        OntClass F = m.getOntClass(NS + "F");

        A.addSubClass(B);
        A.addSubClass(C);
        A.addSubClass(D);
        C.addSubClass(E);
        D.addSubClass(E);
        D.addSubClass(F);

        Assertions.assertEquals(D, OntTools.getLCA(m, F, E));
    }

    @Test
    public void testIndexLCA14() {
        OntModel m = TestModelFactory.createClassesABCDEFGThing(
                ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF)
        );
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass E = m.getOntClass(NS + "E");

        A.addSubClass(B);
        A.addSubClass(C);

        Assertions.assertEquals(OWL2.Thing, OntTools.getLCA(m, B, E));
        Assertions.assertEquals(OWL2.Thing, OntTools.getLCA(m, C, E));
        Assertions.assertEquals(OWL2.Thing, OntTools.getLCA(m, A, E));
    }

}
