package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.JunitExtensions;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Set;

import static com.gitlab.sszuev.jena.ont.TestModelFactory.NS;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCA;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCD;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDEF;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDEFGHKLM;

public class ClassSubClassesTest {

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
    public void testSubClassNE(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass a = m.createClass(NS + "A");
        Assertions.assertNull(a.getSubClass());
        Assertions.assertFalse(a.hasSubClass());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
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
    public void testListSubClasses1a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        A.addSubClass(A);

        Assertions.assertTrue(A.listSubClasses(true).toList().isEmpty());
        Assertions.assertTrue(A.listSubClasses(false).toList().isEmpty());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListSubClasses1b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        A.addSubClass(A);
        Assertions.assertEquals(List.of(OWL2.Nothing), A.listSubClasses(true).toList());
        Assertions.assertEquals(List.of(OWL2.Nothing), A.listSubClasses(false).toList());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testListSubClasses2(TestSpec spec) {
        //    A
        //  / |
        // B  C
        //     \
        //      D

        OntModel m = createClassesABCD(ModelFactory.createOntologyModel(spec.inst));
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");
        OntClass c = m.getOntClass(NS + "C");
        OntClass d = m.getOntClass(NS + "D");

        Assertions.assertEquals(Set.of(b, c), a.listSubClasses().toSet());
        Assertions.assertEquals(Set.of(b, c), a.listSubClasses(true).toSet());
        Assertions.assertEquals(Set.of(b, c), a.listSubClasses(false).toSet());

        a.addSubClass(d);
        Assertions.assertEquals(Set.of(b, c, d), a.listSubClasses().toSet());
        Assertions.assertEquals(Set.of(b, c), a.listSubClasses(true).toSet());
        Assertions.assertEquals(Set.of(b, c, d), a.listSubClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testListSubClasses3a(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F

        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);
        System.out.println("DIRECT-D::" + directD);
        System.out.println("DIRECT-E::" + directE);
        System.out.println("DIRECT-F::" + directF);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);
        System.out.println("INDIRECT-D::" + indirectD);
        System.out.println("INDIRECT-E::" + indirectE);
        System.out.println("INDIRECT-F::" + indirectF);

        Assertions.assertEquals(Set.of("C", "B"), directA);
        Assertions.assertEquals(Set.of("D", "E"), directB);
        Assertions.assertEquals(Set.of("F", "E"), directC);
        Assertions.assertEquals(Set.of(), directD);
        Assertions.assertEquals(Set.of(), directE);
        Assertions.assertEquals(Set.of(), directF);

        Assertions.assertEquals(Set.of("C", "B"), indirectA);
        Assertions.assertEquals(Set.of("E", "D"), indirectB);
        Assertions.assertEquals(Set.of("F", "E"), indirectC);
        Assertions.assertEquals(Set.of(), indirectD);
        Assertions.assertEquals(Set.of(), indirectE);
        Assertions.assertEquals(Set.of(), indirectF);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListSubClasses3b(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F

        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of("C", "B"), directA);
        Assertions.assertEquals(Set.of("D", "E"), directB);
        Assertions.assertEquals(Set.of("F", "E"), directC);
        Assertions.assertEquals(Set.of(), directD);
        Assertions.assertEquals(Set.of(), directE);
        Assertions.assertEquals(Set.of(), directF);

        Assertions.assertEquals(Set.of("C", "B", "D", "E", "F"), indirectA);
        Assertions.assertEquals(Set.of("E", "D"), indirectB);
        Assertions.assertEquals(Set.of("F", "E"), indirectC);
        Assertions.assertEquals(Set.of(), indirectD);
        Assertions.assertEquals(Set.of(), indirectE);
        Assertions.assertEquals(Set.of(), indirectF);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListSubClasses3c(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F

        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");
        OntClass c = m.getOntClass(NS + "C");
        OntClass d = m.getOntClass(NS + "D");
        OntClass e = m.getOntClass(NS + "E");
        OntClass f = m.getOntClass(NS + "F");

        JunitExtensions.assertValues("", a.listSubClasses(), b, c, d, e, f, OWL.Nothing);
        JunitExtensions.assertValues("", a.listSubClasses(false), b, c, d, e, f, OWL.Nothing);
        JunitExtensions.assertValues("", a.listSubClasses(true), b, c);
        JunitExtensions.assertValues("", b.listSubClasses(true), d, e);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testListSubClasses4a(TestSpec spec) {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M

        OntModel m = createClassesABCDEFGHKLM(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directG = m.getOntClass(NS + "G").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectG = m.getOntClass(NS + "G").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directH = m.getOntClass(NS + "H").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectH = m.getOntClass(NS + "H").listSubClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> directK = m.getOntClass(NS + "K").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectK = m.getOntClass(NS + "K").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directL = m.getOntClass(NS + "L").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectL = m.getOntClass(NS + "L").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directM = m.getOntClass(NS + "M").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectM = m.getOntClass(NS + "M").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);
        System.out.println("DIRECT-D::" + directD);
        System.out.println("DIRECT-E::" + directE);
        System.out.println("DIRECT-F::" + directF);
        System.out.println("DIRECT-G::" + directG);
        System.out.println("DIRECT-H::" + directH);
        System.out.println("DIRECT-K::" + directK);
        System.out.println("DIRECT-L::" + directL);
        System.out.println("DIRECT-M::" + directM);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);
        System.out.println("INDIRECT-D::" + indirectD);
        System.out.println("INDIRECT-E::" + indirectE);
        System.out.println("INDIRECT-F::" + indirectF);
        System.out.println("INDIRECT-G::" + indirectG);
        System.out.println("INDIRECT-H::" + indirectH);
        System.out.println("INDIRECT-K::" + indirectK);
        System.out.println("INDIRECT-L::" + indirectL);
        System.out.println("INDIRECT-M::" + indirectM);

        Assertions.assertEquals(Set.of("C", "B"), directA);
        Assertions.assertEquals(Set.of("E", "D"), directB);
        Assertions.assertEquals(Set.of("F", "E"), directC);
        Assertions.assertEquals(Set.of("H", "G"), directD);
        Assertions.assertEquals(Set.of(), directE);
        Assertions.assertEquals(Set.of(), directF);
        Assertions.assertEquals(Set.of(), directG);
        Assertions.assertEquals(Set.of(), directH);
        Assertions.assertEquals(Set.of("M", "L"), directK);
        Assertions.assertEquals(Set.of(), directL);
        Assertions.assertEquals(Set.of(), directM);

        Assertions.assertEquals(Set.of("C", "B", "D"), indirectA);
        Assertions.assertEquals(Set.of("E", "D"), indirectB);
        Assertions.assertEquals(Set.of("F", "E"), indirectC);
        Assertions.assertEquals(Set.of("H", "G"), indirectD);
        Assertions.assertEquals(Set.of(), indirectE);
        Assertions.assertEquals(Set.of(), indirectF);
        Assertions.assertEquals(Set.of(), indirectG);
        Assertions.assertEquals(Set.of(), indirectH);
        Assertions.assertEquals(Set.of("M", "L", "H"), indirectK);
        Assertions.assertEquals(Set.of(), indirectL);
        Assertions.assertEquals(Set.of(), indirectM);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListSubClasses4b(TestSpec spec) {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M

        OntModel m = createClassesABCDEFGHKLM(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directG = m.getOntClass(NS + "G").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectG = m.getOntClass(NS + "G").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directH = m.getOntClass(NS + "H").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectH = m.getOntClass(NS + "H").listSubClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> directK = m.getOntClass(NS + "K").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectK = m.getOntClass(NS + "K").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directL = m.getOntClass(NS + "L").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectL = m.getOntClass(NS + "L").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directM = m.getOntClass(NS + "M").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectM = m.getOntClass(NS + "M").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);
        System.out.println("DIRECT-D::" + directD);
        System.out.println("DIRECT-E::" + directE);
        System.out.println("DIRECT-F::" + directF);
        System.out.println("DIRECT-G::" + directG);
        System.out.println("DIRECT-H::" + directH);
        System.out.println("DIRECT-K::" + directK);
        System.out.println("DIRECT-L::" + directL);
        System.out.println("DIRECT-M::" + directM);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);
        System.out.println("INDIRECT-D::" + indirectD);
        System.out.println("INDIRECT-E::" + indirectE);
        System.out.println("INDIRECT-F::" + indirectF);
        System.out.println("INDIRECT-G::" + indirectG);
        System.out.println("INDIRECT-H::" + indirectH);
        System.out.println("INDIRECT-K::" + indirectK);
        System.out.println("INDIRECT-L::" + indirectL);
        System.out.println("INDIRECT-M::" + indirectM);

        Assertions.assertEquals(Set.of("C", "B"), directA);
        Assertions.assertEquals(Set.of("E", "D"), directB);
        Assertions.assertEquals(Set.of("F", "E"), directC);
        Assertions.assertEquals(Set.of("H", "G", "K"), directD);
        Assertions.assertEquals(Set.of(), directE);
        Assertions.assertEquals(Set.of(), directF);
        Assertions.assertEquals(Set.of(), directG);
        Assertions.assertEquals(Set.of("M", "L"), directH);
        Assertions.assertEquals(Set.of("M", "L"), directK);
        Assertions.assertEquals(Set.of(), directL);
        Assertions.assertEquals(Set.of(), directM);

        Assertions.assertEquals(Set.of("B", "C", "D", "E", "F", "G", "H", "K", "L", "M"), indirectA);
        Assertions.assertEquals(Set.of("D", "E", "G", "H", "K", "L", "M"), indirectB);
        Assertions.assertEquals(Set.of("F", "E"), indirectC);
        Assertions.assertEquals(Set.of("G", "H", "K", "L", "M"), indirectD);
        Assertions.assertEquals(Set.of(), indirectE);
        Assertions.assertEquals(Set.of(), indirectF);
        Assertions.assertEquals(Set.of(), indirectG);
        Assertions.assertEquals(Set.of("L", "M"), indirectH);
        Assertions.assertEquals(Set.of("H", "L", "M"), indirectK);
        Assertions.assertEquals(Set.of(), indirectL);
        Assertions.assertEquals(Set.of(), indirectM);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListSubClasses5a(TestSpec spec) {
        //     A
        //     |
        // D = B = C
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        A.addSubClass(B);
        B.addEquivalentClass(C);
        D.addEquivalentClass(B);

        Set<String> directA = A.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = B.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = C.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directD = D.listSubClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = A.listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);
        System.out.println("DIRECT-D::" + directD);
        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);
        System.out.println("INDIRECT-D::" + indirectD);

        Assertions.assertEquals(Set.of("C", "B", "D"), directA);
        Assertions.assertEquals(Set.of(), directB);
        Assertions.assertEquals(Set.of(), directC);
        Assertions.assertEquals(Set.of(), directD);

        Assertions.assertEquals(Set.of("B", "C", "D"), indirectA);
        Assertions.assertEquals(Set.of("C", "D"), indirectB);
        Assertions.assertEquals(Set.of("B", "D"), indirectC);
        Assertions.assertEquals(Set.of("B", "C"), indirectD);
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
    public void testListSubClasses5b(TestSpec spec) {
        //     A
        //     |
        // D = B = C
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        A.addSubClass(B);
        B.addEquivalentClass(C);
        D.addEquivalentClass(B);

        Set<String> directA = A.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = B.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = C.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directD = D.listSubClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = A.listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of("B"), directA);
        Assertions.assertEquals(Set.of(), directB);
        Assertions.assertEquals(Set.of(), directC);
        Assertions.assertEquals(Set.of(), directD);

        Assertions.assertEquals(Set.of("B"), indirectA);
        Assertions.assertEquals(Set.of(), indirectB);
        Assertions.assertEquals(Set.of(), indirectC);
        Assertions.assertEquals(Set.of(), indirectD);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListSubClasses6a(TestSpec spec) {
        // B = C
        //  \ |
        //    A
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        A.addSuperClass(B);
        A.addSuperClass(C);
        B.addSuperClass(C);
        C.addSuperClass(B);

        Set<String> directA = A.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = B.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = C.listSubClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = A.listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of(), directA);
        Assertions.assertEquals(Set.of("A"), directB);
        Assertions.assertEquals(Set.of("A"), directC);

        Assertions.assertEquals(Set.of(), indirectA);
        Assertions.assertEquals(Set.of("A", "C"), indirectB);
        Assertions.assertEquals(Set.of("A", "B"), indirectC);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testListSubClasses7a(TestSpec spec) {
        //    A
        //  / .
        // B  .
        // |  .
        // C  .
        //  \ .
        //    A

        OntModel m = createClassesABCA(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = m.getOntClass(NS + "B").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = m.getOntClass(NS + "C").listSubClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = m.getOntClass(NS + "A").listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of("B"), directA);
        Assertions.assertEquals(Set.of("C"), directB);
        Assertions.assertEquals(Set.of("A"), directC);

        Assertions.assertEquals(Set.of("B"), indirectA);
        Assertions.assertEquals(Set.of("C"), indirectB);
        Assertions.assertEquals(Set.of("A"), indirectC);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListSubClasses7b(TestSpec spec) {
        //    A
        //  / .
        // B  .
        // |  .
        // C  .
        //  \ .
        //    A

        OntModel m = createClassesABCA(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = m.getOntClass(NS + "B").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = m.getOntClass(NS + "C").listSubClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = m.getOntClass(NS + "A").listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of(), directA);
        Assertions.assertEquals(Set.of(), directB);
        Assertions.assertEquals(Set.of(), directC);

        Assertions.assertEquals(Set.of("B", "C"), indirectA);
        Assertions.assertEquals(Set.of("A", "C"), indirectB);
        Assertions.assertEquals(Set.of("A", "B"), indirectC);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListSubClasses7c(TestSpec spec) {
        //    A
        //  / .
        // B  .
        // |  .
        // C  .
        //  \ .
        //    A

        OntModel m = createClassesABCA(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = m.getOntClass(NS + "B").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = m.getOntClass(NS + "C").listSubClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = m.getOntClass(NS + "A").listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of("B", "C"), directA);
        Assertions.assertEquals(Set.of("A", "C"), directB);
        Assertions.assertEquals(Set.of("A", "B"), directC);

        Assertions.assertEquals(Set.of("B", "C"), indirectA);
        Assertions.assertEquals(Set.of("A", "C"), indirectB);
        Assertions.assertEquals(Set.of("A", "B"), indirectC);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListSubClasses7d(TestSpec spec) {
        //    A
        //  / .
        // B  .
        // |  .
        // C  .
        //  \ .
        //    A

        OntModel m = createClassesABCA(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = m.getOntClass(NS + "B").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = m.getOntClass(NS + "C").listSubClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = m.getOntClass(NS + "A").listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of("Nothing"), directA);
        Assertions.assertEquals(Set.of("Nothing"), directB);
        Assertions.assertEquals(Set.of("Nothing"), directC);

        Assertions.assertEquals(Set.of("B", "C", "Nothing"), indirectA);
        Assertions.assertEquals(Set.of("A", "C", "Nothing"), indirectB);
        Assertions.assertEquals(Set.of("A", "B", "Nothing"), indirectC);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListSubClasses8a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);
        B.addSubClass(A);

        Set<String> directA = m.getOntClass(NS + "A").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);

        Assertions.assertEquals(Set.of(), directA);
        Assertions.assertEquals(Set.of(), directB);

        Assertions.assertEquals(Set.of("B"), indirectA);
        Assertions.assertEquals(Set.of("A"), indirectB);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListSubClasses8b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);
        B.addSubClass(A);

        Set<String> directA = m.getOntClass(NS + "A").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);

        Assertions.assertEquals(Set.of("B"), directA);
        Assertions.assertEquals(Set.of("A"), directB);

        Assertions.assertEquals(Set.of("B"), indirectA);
        Assertions.assertEquals(Set.of("A"), indirectB);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListSubClasses8c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);
        B.addSubClass(A);

        Set<String> directA = m.getOntClass(NS + "A").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);

        Assertions.assertEquals(Set.of("Nothing"), directA);
        Assertions.assertEquals(Set.of("Nothing"), directB);

        Assertions.assertEquals(Set.of("B", "Nothing"), indirectA);
        Assertions.assertEquals(Set.of("A", "Nothing"), indirectB);
    }

}
