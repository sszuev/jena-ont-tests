package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Set;

import static com.gitlab.sszuev.jena.ont.TestModelFactory.NS;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABC;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCA;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCD;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDAEB;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDEF;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDEFBCF;
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
    public void testGetSubClass1a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass a = m.createClass(NS + "A");
        Assertions.assertNull(a.getSubClass());
        Assertions.assertFalse(a.hasSubClass());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testGetSubClass1b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass a = m.createClass(NS + "A");
        System.out.println(m.listStatements(null, RDFS.subClassOf, a).toList());
        Assertions.assertEquals(a, a.getSubClass());
        Assertions.assertTrue(a.hasSubClass());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testGetSubClass1c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass a = m.createClass(NS + "A");
        System.out.println(m.listStatements(null, RDFS.subClassOf, a).toList());
        Set<? extends Resource> subClasses =
                m.listStatements(null, RDFS.subClassOf, a).mapWith(it -> it.getSubject().as(OntClass.class)).toSet();
        Assertions.assertEquals(Set.of(OWL2.Nothing, a), subClasses);
        Assertions.assertTrue(a.hasSubClass());
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
        OntClass a = m.getResource(NS + "A").as(OntClass.class);
        OntClass b = m.getResource(NS + "B").as(OntClass.class);
        OntClass c = m.getResource(NS + "C").as(OntClass.class);
        OntClass d = m.getResource(NS + "D").as(OntClass.class);

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
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
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
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListSubClasses3c(TestSpec spec) {
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
        Assertions.assertEquals(Set.of("Nothing"), directD);
        Assertions.assertEquals(Set.of("Nothing"), directE);
        Assertions.assertEquals(Set.of("Nothing"), directF);

        Assertions.assertEquals(Set.of("C", "B", "D", "E", "F", "Nothing"), indirectA);
        Assertions.assertEquals(Set.of("E", "D", "Nothing"), indirectB);
        Assertions.assertEquals(Set.of("F", "E", "Nothing"), indirectC);
        Assertions.assertEquals(Set.of("Nothing"), indirectD);
        Assertions.assertEquals(Set.of("Nothing"), indirectE);
        Assertions.assertEquals(Set.of("Nothing"), indirectF);
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
    public void testListSubClasses9a(TestSpec spec) {
        //  A   B
        //  .\ /.
        //  . C .
        //  . | .
        //  . D .
        //  ./  .
        //  A   .   E
        //   \  .  |
        //    \ . /
        //      B
        OntModel m = createClassesABCDAEB(ModelFactory.createOntologyModel(spec.inst));

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

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);
        System.out.println("DIRECT-D::" + directD);
        System.out.println("DIRECT-E::" + directE);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);
        System.out.println("INDIRECT-D::" + indirectD);
        System.out.println("INDIRECT-E::" + indirectE);

        Assertions.assertEquals(Set.of(), directA);
        Assertions.assertEquals(Set.of(), directB);
        Assertions.assertEquals(Set.of(), directC);
        Assertions.assertEquals(Set.of(), directD);
        Assertions.assertEquals(Set.of("A", "B", "C", "D"), directE);

        Assertions.assertEquals(Set.of("B", "C", "D"), indirectA);
        Assertions.assertEquals(Set.of("A", "C", "D"), indirectB);
        Assertions.assertEquals(Set.of("A", "B", "D"), indirectC);
        Assertions.assertEquals(Set.of("A", "B", "C"), indirectD);
        Assertions.assertEquals(Set.of("A", "B", "C", "D"), indirectE);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testListSubClasses9b(TestSpec spec) {
        //  A   B
        //  .\ /.
        //  . C .
        //  . | .
        //  . D .
        //  ./  .
        //  A   .   E
        //   \  .  |
        //    \ . /
        //      B
        OntModel m = createClassesABCDAEB(ModelFactory.createOntologyModel(spec.inst));

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

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);
        System.out.println("DIRECT-D::" + directD);
        System.out.println("DIRECT-E::" + directE);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);
        System.out.println("INDIRECT-D::" + indirectD);
        System.out.println("INDIRECT-E::" + indirectE);

        Assertions.assertEquals(Set.of("B"), directA);
        Assertions.assertEquals(Set.of("C"), directB);
        Assertions.assertEquals(Set.of("D"), directC);
        Assertions.assertEquals(Set.of("A"), directD);
        Assertions.assertEquals(Set.of("B"), directE);

        Assertions.assertEquals(Set.of("B", "C"), indirectA);
        Assertions.assertEquals(Set.of("C"), indirectB);
        Assertions.assertEquals(Set.of("D"), indirectC);
        Assertions.assertEquals(Set.of("A"), indirectD);
        Assertions.assertEquals(Set.of("B"), indirectE);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListSubClasses9c(TestSpec spec) {
        //  A   B
        //  .\ /.
        //  . C .
        //  . | .
        //  . D .
        //  ./  .
        //  A   .   E
        //   \  .  |
        //    \ . /
        //      B
        OntModel m = createClassesABCDAEB(ModelFactory.createOntologyModel(spec.inst));

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

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);
        System.out.println("DIRECT-D::" + directD);
        System.out.println("DIRECT-E::" + directE);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);
        System.out.println("INDIRECT-D::" + indirectD);
        System.out.println("INDIRECT-E::" + indirectE);

        Assertions.assertEquals(Set.of("B", "C", "D"), directA);
        Assertions.assertEquals(Set.of("A", "C", "D"), directB);
        Assertions.assertEquals(Set.of("A", "B", "D"), directC);
        Assertions.assertEquals(Set.of("A", "B", "C"), directD);
        Assertions.assertEquals(Set.of("A", "B", "C", "D"), directE);

        Assertions.assertEquals(Set.of("B", "C", "D"), indirectA);
        Assertions.assertEquals(Set.of("A", "C", "D"), indirectB);
        Assertions.assertEquals(Set.of("A", "B", "D"), indirectC);
        Assertions.assertEquals(Set.of("A", "B", "C"), indirectD);
        Assertions.assertEquals(Set.of("A", "B", "C", "D"), indirectE);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListSubClasses9d(TestSpec spec) {
        //  A   B
        //  .\ /.
        //  . C .
        //  . | .
        //  . D .
        //  ./  .
        //  A   .   E
        //   \  .  |
        //    \ . /
        //      B
        OntModel m = createClassesABCDAEB(ModelFactory.createOntologyModel(spec.inst));

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

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);
        System.out.println("DIRECT-D::" + directD);
        System.out.println("DIRECT-E::" + directE);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);
        System.out.println("INDIRECT-D::" + indirectD);
        System.out.println("INDIRECT-E::" + indirectE);

        Assertions.assertEquals(Set.of("Nothing"), directA);
        Assertions.assertEquals(Set.of("Nothing"), directB);
        Assertions.assertEquals(Set.of("Nothing"), directC);
        Assertions.assertEquals(Set.of("Nothing"), directD);
        Assertions.assertEquals(Set.of("A", "B", "C", "D"), directE);

        Assertions.assertEquals(Set.of("B", "C", "D", "Nothing"), indirectA);
        Assertions.assertEquals(Set.of("A", "C", "D", "Nothing"), indirectB);
        Assertions.assertEquals(Set.of("A", "B", "D", "Nothing"), indirectC);
        Assertions.assertEquals(Set.of("A", "B", "C", "Nothing"), indirectD);
        Assertions.assertEquals(Set.of("A", "B", "C", "D", "Nothing"), indirectE);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testListSubClasses10a(TestSpec spec) {
        //      A       B
        //    /   \   / |
        //  /       C   |
        // |      / .   |
        // |    D   .   |
        // |  / |   .   |
        // E    |   .   |
        //   \  |   .   |
        //     F ...... F
        //       \  .
        //        \ .
        //          C
        OntModel m = createClassesABCDEFBCF(ModelFactory.createOntologyModel(spec.inst));

        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);
        OntClass E = m.getResource(NS + "E").as(OntClass.class);
        OntClass F = m.getResource(NS + "F").as(OntClass.class);

        Set<String> directA = A.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = D.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = E.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = F.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = F.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("C", "E"), directA, "wrong direct nodes for A");
        Assertions.assertEquals(Set.of("F"), directB, "wrong direct nodes for B");
        Assertions.assertEquals(Set.of("D"), directC, "wrong direct nodes for C");
        Assertions.assertEquals(Set.of("E"), directD, "wrong direct nodes for D");
        Assertions.assertEquals(Set.of("F"), directE, "wrong direct nodes for E");
        Assertions.assertEquals(Set.of("C"), directF, "wrong direct nodes for F");

        Assertions.assertEquals(Set.of("C", "E"), indirectA, "wrong indirect nodes for A");
        Assertions.assertEquals(Set.of("C", "F"), indirectB, "wrong indirect nodes for B");
        Assertions.assertEquals(Set.of("D"), indirectC, "wrong indirect nodes for C");
        Assertions.assertEquals(Set.of("E", "F"), indirectD, "wrong indirect nodes for D");
        Assertions.assertEquals(Set.of("F"), indirectE, "wrong indirect nodes for E");
        Assertions.assertEquals(Set.of("C"), indirectF, "wrong indirect nodes for F");
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
    public void testListSubClasses10b(TestSpec spec) {
        //      A       B
        //    /   \   / |
        //  /       C   |
        // |      / .   |
        // |    D   .   |
        // |  / |   .   |
        // E    |   .   |
        //   \  |   .   |
        //     F ...... F
        //       \  .
        //        \ .
        //          C
        OntModel m = createClassesABCDEFBCF(ModelFactory.createOntologyModel(spec.inst));

        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);
        OntClass E = m.getResource(NS + "E").as(OntClass.class);
        OntClass F = m.getResource(NS + "F").as(OntClass.class);

        Set<String> directA = A.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = D.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = E.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = F.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = F.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("C", "D", "E", "F"), directA, "wrong direct nodes for A");
        Assertions.assertEquals(Set.of("C", "D", "E", "F"), directB, "wrong direct nodes for B");
        Assertions.assertEquals(Set.of(), directC, "wrong direct nodes for C");
        Assertions.assertEquals(Set.of(), directD, "wrong direct nodes for D");
        Assertions.assertEquals(Set.of(), directE, "wrong direct nodes for E");
        Assertions.assertEquals(Set.of(), directF, "wrong direct nodes for F");

        Assertions.assertEquals(Set.of("C", "D", "E", "F"), indirectA, "wrong indirect nodes for A");
        Assertions.assertEquals(Set.of("C", "D", "E", "F"), indirectB, "wrong indirect nodes for B");
        Assertions.assertEquals(Set.of("D", "E", "F"), indirectC, "wrong indirect nodes for C");
        Assertions.assertEquals(Set.of("C", "E", "F"), indirectD, "wrong indirect nodes for D");
        Assertions.assertEquals(Set.of("C", "D", "F"), indirectE, "wrong indirect nodes for E");
        Assertions.assertEquals(Set.of("C", "D", "E"), indirectF, "wrong indirect nodes for F");
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListSubClasses10c(TestSpec spec) {
        //      A       B
        //    /   \   / |
        //  /       C   |
        // |      / .   |
        // |    D   .   |
        // |  / |   .   |
        // E    |   .   |
        //   \  |   .   |
        //     F ...... F
        //       \  .
        //        \ .
        //          C
        OntModel m = createClassesABCDEFBCF(ModelFactory.createOntologyModel(spec.inst));

        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);
        OntClass E = m.getResource(NS + "E").as(OntClass.class);
        OntClass F = m.getResource(NS + "F").as(OntClass.class);

        Set<String> directA = A.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = D.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = E.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = F.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = F.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("C", "D", "E", "F"), directA, "wrong direct nodes for A");
        Assertions.assertEquals(Set.of("C", "D", "E", "F"), directB, "wrong direct nodes for B");
        Assertions.assertEquals(Set.of("D", "E", "F"), directC, "wrong direct nodes for C");
        Assertions.assertEquals(Set.of("C", "E", "F"), directD, "wrong direct nodes for D");
        Assertions.assertEquals(Set.of("C", "D", "F"), directE, "wrong direct nodes for E");
        Assertions.assertEquals(Set.of("C", "D", "E"), directF, "wrong direct nodes for F");

        Assertions.assertEquals(Set.of("C", "D", "E", "F"), indirectA, "wrong indirect nodes for A");
        Assertions.assertEquals(Set.of("C", "D", "E", "F"), indirectB, "wrong indirect nodes for B");
        Assertions.assertEquals(Set.of("D", "E", "F"), indirectC, "wrong indirect nodes for C");
        Assertions.assertEquals(Set.of("C", "E", "F"), indirectD, "wrong indirect nodes for D");
        Assertions.assertEquals(Set.of("C", "D", "F"), indirectE, "wrong indirect nodes for E");
        Assertions.assertEquals(Set.of("C", "D", "E"), indirectF, "wrong indirect nodes for F");
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListSubClasses10d(TestSpec spec) {
        //      A       B
        //    /   \   / |
        //  /       C   |
        // |      / .   |
        // |    D   .   |
        // |  / |   .   |
        // E    |   .   |
        //   \  |   .   |
        //     F ...... F
        //       \  .
        //        \ .
        //          C
        OntModel m = createClassesABCDEFBCF(ModelFactory.createOntologyModel(spec.inst));

        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);
        OntClass E = m.getResource(NS + "E").as(OntClass.class);
        OntClass F = m.getResource(NS + "F").as(OntClass.class);

        Set<String> directA = A.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = D.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = E.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = F.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = F.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("C", "D", "E", "F"), directA, "wrong direct nodes for A");
        Assertions.assertEquals(Set.of("C", "D", "E", "F"), directB, "wrong direct nodes for B");
        Assertions.assertEquals(Set.of("Nothing"), directC, "wrong direct nodes for C");
        Assertions.assertEquals(Set.of("Nothing"), directD, "wrong direct nodes for D");
        Assertions.assertEquals(Set.of("Nothing"), directE, "wrong direct nodes for E");
        Assertions.assertEquals(Set.of("Nothing"), directF, "wrong direct nodes for F");

        Assertions.assertEquals(Set.of("C", "D", "E", "F", "Nothing"), indirectA, "wrong indirect nodes for A");
        Assertions.assertEquals(Set.of("C", "D", "E", "F", "Nothing"), indirectB, "wrong indirect nodes for B");
        Assertions.assertEquals(Set.of("D", "E", "F", "Nothing"), indirectC, "wrong indirect nodes for C");
        Assertions.assertEquals(Set.of("C", "E", "F", "Nothing"), indirectD, "wrong indirect nodes for D");
        Assertions.assertEquals(Set.of("C", "D", "F", "Nothing"), indirectE, "wrong indirect nodes for E");
        Assertions.assertEquals(Set.of("C", "D", "E", "Nothing"), indirectF, "wrong indirect nodes for F");
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
    public void testListSubClasses11a(TestSpec spec) {
        //    A
        //  /  \
        // B  = C
        OntModel m = createClassesABC(ModelFactory.createOntologyModel(spec.inst));

        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);

        Set<String> directA = A.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);

        Assertions.assertEquals(Set.of("B", "C"), directA, "wrong direct nodes for A");
        Assertions.assertEquals(Set.of(), directB, "wrong direct nodes for B");
        Assertions.assertEquals(Set.of(), directC, "wrong direct nodes for C");

        Assertions.assertEquals(Set.of("B", "C"), indirectA, "wrong indirect nodes for A");
        Assertions.assertEquals(Set.of("C"), indirectB, "wrong indirect nodes for B");
        Assertions.assertEquals(Set.of("B"), indirectC, "wrong indirect nodes for C");
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListSubClasses11b(TestSpec spec) {
        //    A
        //  /  \
        // B  = C
        OntModel m = createClassesABC(ModelFactory.createOntologyModel(spec.inst));

        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);

        Set<String> directA = A.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);

        Assertions.assertEquals(Set.of("B", "C"), directA, "wrong direct nodes for A");
        Assertions.assertEquals(Set.of("C"), directB, "wrong direct nodes for B");
        Assertions.assertEquals(Set.of("B"), directC, "wrong direct nodes for C");

        Assertions.assertEquals(Set.of("B", "C"), indirectA, "wrong indirect nodes for A");
        Assertions.assertEquals(Set.of("C"), indirectB, "wrong indirect nodes for B");
        Assertions.assertEquals(Set.of("B"), indirectC, "wrong indirect nodes for C");
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListSubClasses11d(TestSpec spec) {
        //    A
        //  /  \
        // B  = C
        OntModel m = createClassesABC(ModelFactory.createOntologyModel(spec.inst));

        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);

        Set<String> directA = A.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);

        Assertions.assertEquals(Set.of("B", "C"), directA, "wrong direct nodes for A");
        Assertions.assertEquals(Set.of("Nothing"), directB, "wrong direct nodes for B");
        Assertions.assertEquals(Set.of("Nothing"), directC, "wrong direct nodes for C");

        Assertions.assertEquals(Set.of("B", "C", "Nothing"), indirectA, "wrong indirect nodes for A");
        Assertions.assertEquals(Set.of("C", "Nothing"), indirectB, "wrong indirect nodes for B");
        Assertions.assertEquals(Set.of("B", "Nothing"), indirectC, "wrong indirect nodes for C");
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testHasSubClasses1a(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));

        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");
        OntClass F = m.getOntClass(NS + "F");

        Assertions.assertFalse(A.hasSubClass(A, false));
        Assertions.assertTrue(A.hasSubClass(B, false));
        Assertions.assertTrue(A.hasSubClass(C, false));
        Assertions.assertFalse(A.hasSubClass(D, false));
        Assertions.assertFalse(A.hasSubClass(E, false));
        Assertions.assertFalse(A.hasSubClass(F, false));
        Assertions.assertFalse(B.hasSubClass(A, false));
        Assertions.assertFalse(B.hasSubClass(B, false));
        Assertions.assertFalse(B.hasSubClass(C, false));
        Assertions.assertTrue(B.hasSubClass(D, false));
        Assertions.assertTrue(B.hasSubClass(E, false));
        Assertions.assertFalse(B.hasSubClass(F, false));
        Assertions.assertFalse(C.hasSubClass(A, false));
        Assertions.assertFalse(C.hasSubClass(B, false));
        Assertions.assertFalse(C.hasSubClass(C, false));
        Assertions.assertFalse(C.hasSubClass(D, false));
        Assertions.assertTrue(C.hasSubClass(E, false));
        Assertions.assertTrue(C.hasSubClass(F, false));
        Assertions.assertFalse(D.hasSubClass(A, false));
        Assertions.assertFalse(D.hasSubClass(B, false));
        Assertions.assertFalse(D.hasSubClass(C, false));
        Assertions.assertFalse(D.hasSubClass(D, false));
        Assertions.assertFalse(D.hasSubClass(E, false));
        Assertions.assertFalse(D.hasSubClass(F, false));
        Assertions.assertFalse(E.hasSubClass(A, false));
        Assertions.assertFalse(E.hasSubClass(B, false));
        Assertions.assertFalse(E.hasSubClass(C, false));
        Assertions.assertFalse(E.hasSubClass(D, false));
        Assertions.assertFalse(E.hasSubClass(E, false));
        Assertions.assertFalse(E.hasSubClass(F, false));
        Assertions.assertFalse(F.hasSubClass(A, false));
        Assertions.assertFalse(F.hasSubClass(B, false));
        Assertions.assertFalse(F.hasSubClass(C, false));
        Assertions.assertFalse(F.hasSubClass(D, false));
        Assertions.assertFalse(F.hasSubClass(E, false));
        Assertions.assertFalse(F.hasSubClass(F, false));

        Assertions.assertFalse(A.hasSubClass(A, true));
        Assertions.assertTrue(A.hasSubClass(B, true));
        Assertions.assertTrue(A.hasSubClass(C, true));
        Assertions.assertFalse(A.hasSubClass(D, true));
        Assertions.assertFalse(A.hasSubClass(E, true));
        Assertions.assertFalse(A.hasSubClass(F, true));
        Assertions.assertFalse(B.hasSubClass(A, true));
        Assertions.assertFalse(B.hasSubClass(B, true));
        Assertions.assertFalse(B.hasSubClass(C, true));
        Assertions.assertTrue(B.hasSubClass(D, true));
        Assertions.assertTrue(B.hasSubClass(E, true));
        Assertions.assertFalse(B.hasSubClass(F, true));
        Assertions.assertFalse(C.hasSubClass(A, true));
        Assertions.assertFalse(C.hasSubClass(B, true));
        Assertions.assertFalse(C.hasSubClass(C, true));
        Assertions.assertFalse(C.hasSubClass(D, true));
        Assertions.assertTrue(C.hasSubClass(E, true));
        Assertions.assertTrue(C.hasSubClass(F, true));
        Assertions.assertFalse(D.hasSubClass(A, true));
        Assertions.assertFalse(D.hasSubClass(B, true));
        Assertions.assertFalse(D.hasSubClass(C, true));
        Assertions.assertFalse(D.hasSubClass(D, true));
        Assertions.assertFalse(D.hasSubClass(E, true));
        Assertions.assertFalse(D.hasSubClass(F, true));
        Assertions.assertFalse(E.hasSubClass(A, true));
        Assertions.assertFalse(E.hasSubClass(B, true));
        Assertions.assertFalse(E.hasSubClass(C, true));
        Assertions.assertFalse(E.hasSubClass(D, true));
        Assertions.assertFalse(E.hasSubClass(E, true));
        Assertions.assertFalse(E.hasSubClass(F, true));
        Assertions.assertFalse(F.hasSubClass(A, true));
        Assertions.assertFalse(F.hasSubClass(B, true));
        Assertions.assertFalse(F.hasSubClass(C, true));
        Assertions.assertFalse(F.hasSubClass(D, true));
        Assertions.assertFalse(F.hasSubClass(E, true));
        Assertions.assertFalse(F.hasSubClass(F, true));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testHasSubClasses1b(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));

        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");
        OntClass F = m.getOntClass(NS + "F");

        Assertions.assertTrue(A.hasSubClass(A, false));
        Assertions.assertTrue(A.hasSubClass(B, false));
        Assertions.assertTrue(A.hasSubClass(C, false));
        Assertions.assertTrue(A.hasSubClass(D, false));
        Assertions.assertTrue(A.hasSubClass(E, false));
        Assertions.assertTrue(A.hasSubClass(F, false));
        Assertions.assertFalse(B.hasSubClass(A, false));
        Assertions.assertTrue(B.hasSubClass(B, false));
        Assertions.assertFalse(B.hasSubClass(C, false));
        Assertions.assertTrue(B.hasSubClass(D, false));
        Assertions.assertTrue(B.hasSubClass(E, false));
        Assertions.assertFalse(B.hasSubClass(F, false));
        Assertions.assertFalse(C.hasSubClass(A, false));
        Assertions.assertFalse(C.hasSubClass(B, false));
        Assertions.assertTrue(C.hasSubClass(C, false));
        Assertions.assertFalse(C.hasSubClass(D, false));
        Assertions.assertTrue(C.hasSubClass(E, false));
        Assertions.assertTrue(C.hasSubClass(F, false));
        Assertions.assertFalse(D.hasSubClass(A, false));
        Assertions.assertFalse(D.hasSubClass(B, false));
        Assertions.assertFalse(D.hasSubClass(C, false));
        Assertions.assertTrue(D.hasSubClass(D, false));
        Assertions.assertFalse(D.hasSubClass(E, false));
        Assertions.assertFalse(D.hasSubClass(F, false));
        Assertions.assertFalse(E.hasSubClass(A, false));
        Assertions.assertFalse(E.hasSubClass(B, false));
        Assertions.assertFalse(E.hasSubClass(C, false));
        Assertions.assertFalse(E.hasSubClass(D, false));
        Assertions.assertTrue(E.hasSubClass(E, false));
        Assertions.assertFalse(E.hasSubClass(F, false));
        Assertions.assertFalse(F.hasSubClass(A, false));
        Assertions.assertFalse(F.hasSubClass(B, false));
        Assertions.assertFalse(F.hasSubClass(C, false));
        Assertions.assertFalse(F.hasSubClass(D, false));
        Assertions.assertFalse(F.hasSubClass(E, false));
        Assertions.assertTrue(F.hasSubClass(F, false));

        Assertions.assertFalse(A.hasSubClass(A, true));
        Assertions.assertTrue(A.hasSubClass(B, true));
        Assertions.assertTrue(A.hasSubClass(C, true));
        Assertions.assertFalse(A.hasSubClass(D, true));
        Assertions.assertFalse(A.hasSubClass(E, true));
        Assertions.assertFalse(A.hasSubClass(F, true));
        Assertions.assertFalse(B.hasSubClass(A, true));
        Assertions.assertFalse(B.hasSubClass(B, true));
        Assertions.assertFalse(B.hasSubClass(C, true));
        Assertions.assertTrue(B.hasSubClass(D, true));
        Assertions.assertTrue(B.hasSubClass(E, true));
        Assertions.assertFalse(B.hasSubClass(F, true));
        Assertions.assertFalse(C.hasSubClass(A, true));
        Assertions.assertFalse(C.hasSubClass(B, true));
        Assertions.assertFalse(C.hasSubClass(C, true));
        Assertions.assertFalse(C.hasSubClass(D, true));
        Assertions.assertTrue(C.hasSubClass(E, true));
        Assertions.assertTrue(C.hasSubClass(F, true));
        Assertions.assertFalse(D.hasSubClass(A, true));
        Assertions.assertFalse(D.hasSubClass(B, true));
        Assertions.assertFalse(D.hasSubClass(C, true));
        Assertions.assertFalse(D.hasSubClass(D, true));
        Assertions.assertFalse(D.hasSubClass(E, true));
        Assertions.assertFalse(D.hasSubClass(F, true));
        Assertions.assertFalse(E.hasSubClass(A, true));
        Assertions.assertFalse(E.hasSubClass(B, true));
        Assertions.assertFalse(E.hasSubClass(C, true));
        Assertions.assertFalse(E.hasSubClass(D, true));
        Assertions.assertFalse(E.hasSubClass(E, true));
        Assertions.assertFalse(E.hasSubClass(F, true));
        Assertions.assertFalse(F.hasSubClass(A, true));
        Assertions.assertFalse(F.hasSubClass(B, true));
        Assertions.assertFalse(F.hasSubClass(C, true));
        Assertions.assertFalse(F.hasSubClass(D, true));
        Assertions.assertFalse(F.hasSubClass(E, true));
        Assertions.assertFalse(F.hasSubClass(F, true));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testHasSubClasses1c(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));

        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");
        OntClass F = m.getOntClass(NS + "F");

        Assertions.assertTrue(A.hasSubClass(A, false));
        Assertions.assertTrue(A.hasSubClass(B, false));
        Assertions.assertTrue(A.hasSubClass(C, false));
        Assertions.assertTrue(A.hasSubClass(D, false));
        Assertions.assertTrue(A.hasSubClass(E, false));
        Assertions.assertTrue(A.hasSubClass(F, false));
        Assertions.assertFalse(B.hasSubClass(A, false));
        Assertions.assertTrue(B.hasSubClass(B, false));
        Assertions.assertFalse(B.hasSubClass(C, false));
        Assertions.assertTrue(B.hasSubClass(D, false));
        Assertions.assertTrue(B.hasSubClass(E, false));
        Assertions.assertFalse(B.hasSubClass(F, false));
        Assertions.assertFalse(C.hasSubClass(A, false));
        Assertions.assertFalse(C.hasSubClass(B, false));
        Assertions.assertTrue(C.hasSubClass(C, false));
        Assertions.assertFalse(C.hasSubClass(D, false));
        Assertions.assertTrue(C.hasSubClass(E, false));
        Assertions.assertTrue(C.hasSubClass(F, false));
        Assertions.assertFalse(D.hasSubClass(A, false));
        Assertions.assertFalse(D.hasSubClass(B, false));
        Assertions.assertFalse(D.hasSubClass(C, false));
        Assertions.assertTrue(D.hasSubClass(D, false));
        Assertions.assertFalse(D.hasSubClass(E, false));
        Assertions.assertFalse(D.hasSubClass(F, false));
        Assertions.assertFalse(E.hasSubClass(A, false));
        Assertions.assertFalse(E.hasSubClass(B, false));
        Assertions.assertFalse(E.hasSubClass(C, false));
        Assertions.assertFalse(E.hasSubClass(D, false));
        Assertions.assertTrue(E.hasSubClass(E, false));
        Assertions.assertFalse(E.hasSubClass(F, false));
        Assertions.assertFalse(F.hasSubClass(A, false));
        Assertions.assertFalse(F.hasSubClass(B, false));
        Assertions.assertFalse(F.hasSubClass(C, false));
        Assertions.assertFalse(F.hasSubClass(D, false));
        Assertions.assertFalse(F.hasSubClass(E, false));
        Assertions.assertTrue(F.hasSubClass(F, false));

        Assertions.assertTrue(A.hasSubClass(A, true));
        Assertions.assertTrue(A.hasSubClass(B, true));
        Assertions.assertTrue(A.hasSubClass(C, true));
        Assertions.assertFalse(A.hasSubClass(D, true));
        Assertions.assertFalse(A.hasSubClass(E, true));
        Assertions.assertFalse(A.hasSubClass(F, true));
        Assertions.assertFalse(B.hasSubClass(A, true));
        Assertions.assertTrue(B.hasSubClass(B, true));
        Assertions.assertFalse(B.hasSubClass(C, true));
        Assertions.assertTrue(B.hasSubClass(D, true));
        Assertions.assertTrue(B.hasSubClass(E, true));
        Assertions.assertFalse(B.hasSubClass(F, true));
        Assertions.assertFalse(C.hasSubClass(A, true));
        Assertions.assertFalse(C.hasSubClass(B, true));
        Assertions.assertTrue(C.hasSubClass(C, true));
        Assertions.assertFalse(C.hasSubClass(D, true));
        Assertions.assertTrue(C.hasSubClass(E, true));
        Assertions.assertTrue(C.hasSubClass(F, true));
        Assertions.assertFalse(D.hasSubClass(A, true));
        Assertions.assertFalse(D.hasSubClass(B, true));
        Assertions.assertFalse(D.hasSubClass(C, true));
        Assertions.assertTrue(D.hasSubClass(D, true));
        Assertions.assertFalse(D.hasSubClass(E, true));
        Assertions.assertFalse(D.hasSubClass(F, true));
        Assertions.assertFalse(E.hasSubClass(A, true));
        Assertions.assertFalse(E.hasSubClass(B, true));
        Assertions.assertFalse(E.hasSubClass(C, true));
        Assertions.assertFalse(E.hasSubClass(D, true));
        Assertions.assertTrue(E.hasSubClass(E, true));
        Assertions.assertFalse(E.hasSubClass(F, true));
        Assertions.assertFalse(F.hasSubClass(A, true));
        Assertions.assertFalse(F.hasSubClass(B, true));
        Assertions.assertFalse(F.hasSubClass(C, true));
        Assertions.assertFalse(F.hasSubClass(D, true));
        Assertions.assertFalse(F.hasSubClass(E, true));
        Assertions.assertTrue(F.hasSubClass(F, true));
    }
}
