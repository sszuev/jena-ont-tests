package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gitlab.sszuev.jena.ont.common.CommonOntTestEngine.NS;

public class IndividualClassesTest {

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
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListOntClasses1a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);

        Individual x = m.createIndividual(A);
        x.addRDFType(B);

        Assertions.assertEquals(Set.of(A, B), x.listOntClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListOntClasses1b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);

        Individual x = m.createIndividual(A);
        x.addRDFType(B);

        Assertions.assertEquals(Set.of(A, B, OWL.Thing, RDFS.Resource), x.listOntClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListOntClasses1c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);

        Individual x = m.createIndividual(A);
        x.addRDFType(B);

        Assertions.assertEquals(Set.of(A, B, OWL.Thing), x.listOntClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM_RDFS_INF",
    })
    public void testListOntClasses1d(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);

        Individual x = m.createIndividual(A);
        x.addRDFType(B);

        Assertions.assertEquals(Set.of(A, B, RDFS.Resource), x.listOntClasses(false).toSet());
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
    public void testListOntClasses2(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);

        Individual x = m.createIndividual(A);
        x.addRDFType(B);

        Assertions.assertEquals(Set.of(B), x.listOntClasses(true).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM_RDFS_INF",
    })
    public void testListOntClasses3a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        OntClass c = m.createClass(NS + "C");
        OntClass d = m.createClass(NS + "D");
        OntClass e = m.createClass(NS + "E");
        OntClass f = m.createClass(NS + "F");
        OntClass g = m.createClass(NS + "G");

        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E

        a.addSubClass(b);
        a.addSubClass(c);
        b.addSubClass(d);
        b.addSubClass(e);
        c.addSubClass(e);
        c.addSubClass(f);
        f.addSubClass(c);
        f.addSuperClass(g);

        Individual A = m.createIndividual("iA", a);
        Individual B = m.createIndividual("iB", b);
        Individual C = m.createIndividual("iC", c);
        Individual D = m.createIndividual("iD", d);
        Individual E = m.createIndividual("iE", e);
        Individual F = m.createIndividual("iF", f);
        Individual G = m.createIndividual("iG", g);

        Set<String> directA = A.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = D.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = E.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = F.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = F.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directG = G.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectG = G.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-iA::" + directA);
        System.out.println("DIRECT-iB::" + directB);
        System.out.println("DIRECT-iC::" + directC);
        System.out.println("DIRECT-iD::" + directD);
        System.out.println("DIRECT-iE::" + directE);
        System.out.println("DIRECT-iF::" + directF);
        System.out.println("DIRECT-iG::" + directG);
        System.out.println("INDIRECT-iA::" + indirectA);
        System.out.println("INDIRECT-iB::" + indirectB);
        System.out.println("INDIRECT-iC::" + indirectC);
        System.out.println("INDIRECT-iD::" + indirectD);
        System.out.println("INDIRECT-iE::" + indirectE);
        System.out.println("INDIRECT-iF::" + indirectF);
        System.out.println("INDIRECT-iG::" + indirectG);

        Assertions.assertEquals(Set.of("A"), directA);
        Assertions.assertEquals(Set.of("B"), directB);
        Assertions.assertEquals(Set.of("C", "F"), directC);
        Assertions.assertEquals(Set.of("D"), directD);
        Assertions.assertEquals(Set.of("E"), directE);
        Assertions.assertEquals(Set.of("C", "F"), directF);
        Assertions.assertEquals(Set.of("G"), directG);

        Assertions.assertEquals(Set.of("A"), indirectA);
        Assertions.assertEquals(Set.of("A", "B"), indirectB);
        Assertions.assertEquals(Set.of("A", "C", "F", "G"), indirectC);
        Assertions.assertEquals(Set.of("A", "B", "D"), indirectD);
        Assertions.assertEquals(Set.of("A", "B", "C", "E", "F", "G"), indirectE);
        Assertions.assertEquals(Set.of("A", "C", "F", "G"), indirectF);
        Assertions.assertEquals(Set.of("G"), indirectG);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListOntClasses3b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        OntClass c = m.createClass(NS + "C");
        OntClass d = m.createClass(NS + "D");
        OntClass e = m.createClass(NS + "E");
        OntClass f = m.createClass(NS + "F");
        OntClass g = m.createClass(NS + "G");

        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E

        a.addSubClass(b);
        a.addSubClass(c);
        b.addSubClass(d);
        b.addSubClass(e);
        c.addSubClass(e);
        c.addSubClass(f);
        f.addSubClass(c);
        f.addSuperClass(g);

        Individual A = m.createIndividual("iA", a);
        Individual B = m.createIndividual("iB", b);
        Individual C = m.createIndividual("iC", c);
        Individual D = m.createIndividual("iD", d);
        Individual E = m.createIndividual("iE", e);
        Individual F = m.createIndividual("iF", f);
        Individual G = m.createIndividual("iG", g);

        Set<String> directA = A.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = D.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = E.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = F.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = F.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directG = G.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectG = G.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of("A"), directA);
        Assertions.assertEquals(Set.of("B"), directB);
        Assertions.assertEquals(Set.of("C"), directC);
        Assertions.assertEquals(Set.of("D"), directD);
        Assertions.assertEquals(Set.of("E"), directE);
        Assertions.assertEquals(Set.of("F"), directF);
        Assertions.assertEquals(Set.of("G"), directG);

        Assertions.assertEquals(Set.of("A"), indirectA);
        Assertions.assertEquals(Set.of("B"), indirectB);
        Assertions.assertEquals(Set.of("C"), indirectC);
        Assertions.assertEquals(Set.of("D"), indirectD);
        Assertions.assertEquals(Set.of("E"), indirectE);
        Assertions.assertEquals(Set.of("F"), indirectF);
        Assertions.assertEquals(Set.of("G"), indirectG);
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
    public void testListOntClasses3c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        OntClass c = m.createClass(NS + "C");
        OntClass d = m.createClass(NS + "D");
        OntClass e = m.createClass(NS + "E");
        OntClass f = m.createClass(NS + "F");
        OntClass g = m.createClass(NS + "G");

        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E

        a.addSubClass(b);
        a.addSubClass(c);
        b.addSubClass(d);
        b.addSubClass(e);
        c.addSubClass(e);
        c.addSubClass(f);
        f.addSubClass(c);
        f.addSuperClass(g);

        Individual A = m.createIndividual("iA", a);
        Individual B = m.createIndividual("iB", b);
        Individual C = m.createIndividual("iC", c);
        Individual D = m.createIndividual("iD", d);
        Individual E = m.createIndividual("iE", e);
        Individual F = m.createIndividual("iF", f);
        Individual G = m.createIndividual("iG", g);

        Set<String> directA = A.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = D.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = E.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = F.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = F.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directG = G.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectG = G.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        String R = spec.equals(TestSpec.OWL_MEM_MICRO_RULE_INF) ? null : "Resource";
        String T = spec.equals(TestSpec.RDFS_MEM_RDFS_INF) ? null :  "Thing";

        Assertions.assertEquals(Set.of("A"), directA);
        Assertions.assertEquals(Set.of("B"), directB);
        Assertions.assertEquals(Set.of("C", "F"), directC);
        Assertions.assertEquals(Set.of("D"), directD);
        Assertions.assertEquals(Set.of("E"), directE);
        Assertions.assertEquals(Set.of("C", "F"), directF);
        Assertions.assertEquals(Set.of("G"), directG);

        Assertions.assertEquals(Stream.of("A", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectA);
        Assertions.assertEquals(Stream.of("A", "B", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectB);
        Assertions.assertEquals(Stream.of("A", "C", "F", "G", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectC);
        Assertions.assertEquals(Stream.of("A", "B", "D", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectD);
        Assertions.assertEquals(Stream.of("A", "B", "C", "E", "F", "G", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectE);
        Assertions.assertEquals(Stream.of("A", "C", "F", "G", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectF);
        Assertions.assertEquals(Stream.of("G", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectG);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM_RDFS_INF",
    })
    public void testListOntClasses4a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        OntClass c = m.createClass(NS + "C");
        OntClass d = m.createClass(NS + "D");
        OntClass e = m.createClass(NS + "E");
        OntClass f = m.createClass(NS + "F");
        OntClass g = m.createClass(NS + "G");

        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E

        a.addSubClass(b);
        a.addSubClass(c);
        b.addSubClass(d);
        b.addSubClass(e);
        c.addSubClass(e);
        c.addSubClass(f);
        f.addSubClass(c);
        f.addSuperClass(g);

        Individual A = m.createIndividual("iA", a);
        A.addOntClass(g);

        Individual B = m.createIndividual("iB", b);
        B.addOntClass(d);
        B.addOntClass(c);

        Individual C = m.createIndividual("iC", d);
        C.addOntClass(e);

        Set<String> directA = A.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-iA::" + directA);
        System.out.println("DIRECT-iB::" + directB);
        System.out.println("DIRECT-iC::" + directC);
        System.out.println("INDIRECT-iA::" + indirectA);
        System.out.println("INDIRECT-iB::" + indirectB);
        System.out.println("INDIRECT-iC::" + indirectC);

        Assertions.assertEquals(Set.of("A", "G"), directA);
        Assertions.assertEquals(Set.of("C", "D", "F"), directB);
        Assertions.assertEquals(Set.of("D", "E"), directC);

        Assertions.assertEquals(Set.of("A", "G"), indirectA);
        Assertions.assertEquals(Set.of("A", "B", "C", "D", "F", "G"), indirectB);
        Assertions.assertEquals(Set.of("A", "B", "C", "D", "E", "F", "G"), indirectC);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListOntClasses4b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        OntClass c = m.createClass(NS + "C");
        OntClass d = m.createClass(NS + "D");
        OntClass e = m.createClass(NS + "E");
        OntClass f = m.createClass(NS + "F");
        OntClass g = m.createClass(NS + "G");

        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E

        a.addSubClass(b);
        a.addSubClass(c);
        b.addSubClass(d);
        b.addSubClass(e);
        c.addSubClass(e);
        c.addSubClass(f);
        f.addSubClass(c);
        f.addSuperClass(g);

        Individual A = m.createIndividual("iA", a);
        A.addOntClass(g);

        Individual B = m.createIndividual("iB", b);
        B.addOntClass(d);
        B.addOntClass(c);

        Individual C = m.createIndividual("iC", d);
        C.addOntClass(e);

        Set<String> directA = A.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of("A", "G"), directA);
        Assertions.assertEquals(Set.of("C", "D"), directB);
        Assertions.assertEquals(Set.of("D", "E"), directC);

        Assertions.assertEquals(Set.of("A", "G"), indirectA);
        Assertions.assertEquals(Set.of("B", "C", "D"), indirectB);
        Assertions.assertEquals(Set.of("D", "E"), indirectC);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListOntClasses4c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        OntClass c = m.createClass(NS + "C");
        OntClass d = m.createClass(NS + "D");
        OntClass e = m.createClass(NS + "E");
        OntClass f = m.createClass(NS + "F");
        OntClass g = m.createClass(NS + "G");

        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E

        a.addSubClass(b);
        a.addSubClass(c);
        b.addSubClass(d);
        b.addSubClass(e);
        c.addSubClass(e);
        c.addSubClass(f);
        f.addSubClass(c);
        f.addSuperClass(g);

        Individual A = m.createIndividual("iA", a);
        A.addOntClass(g);

        Individual B = m.createIndividual("iB", b);
        B.addOntClass(d);
        B.addOntClass(c);

        Individual C = m.createIndividual("iC", d);
        C.addOntClass(e);

        Set<String> directA = A.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        String R = spec.equals(TestSpec.OWL_MEM_MICRO_RULE_INF) ? null : "Resource";
        String T = spec.equals(TestSpec.RDFS_MEM_RDFS_INF) ? null :  "Thing";

        Assertions.assertEquals(Set.of("A", "G"), directA);
        Assertions.assertEquals(Set.of("C", "D", "F"), directB);
        Assertions.assertEquals(Set.of("D", "E"), directC);

        Assertions.assertEquals(Stream.of("A", "G", T, R).filter(Objects::nonNull).collect(Collectors.toSet()), indirectA);
        Assertions.assertEquals(Stream.of("A", "B", "C", "D", "F", "G", T, R).filter(Objects::nonNull).collect(Collectors.toSet()), indirectB);
        Assertions.assertEquals(Stream.of("A", "B", "C", "D", "E", "F", "G", T, R).filter(Objects::nonNull).collect(Collectors.toSet()), indirectC);
    }
}
