package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.JunitExtensions;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Set;

import static com.gitlab.sszuev.jena.ont.TestModelFactory.NS;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDEF;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDEFGHKLM;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesBCA;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesDBFCEA;

public class ClassSuperClassesTest {

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
    public void testSuperClassNE(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass a = m.createClass(NS + "A");
        Assertions.assertNull(a.getSuperClass());
        Assertions.assertFalse(a.hasSuperClass());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testListSuperClasses1a(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of(), directA);
        Assertions.assertEquals(Set.of("A"), directB);
        Assertions.assertEquals(Set.of("A"), directC);
        Assertions.assertEquals(Set.of("B"), directD);
        Assertions.assertEquals(Set.of("B", "C"), directE);
        Assertions.assertEquals(Set.of("C"), directF);
        Assertions.assertEquals(Set.of(), indirectA);
        Assertions.assertEquals(Set.of("A"), indirectB);
        Assertions.assertEquals(Set.of("A"), indirectC);
        Assertions.assertEquals(Set.of("B"), indirectD);
        Assertions.assertEquals(Set.of("B", "C"), indirectE);
        Assertions.assertEquals(Set.of("C"), indirectF);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListSuperClasses1b(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of(), directA);
        Assertions.assertEquals(Set.of("A"), directB);
        Assertions.assertEquals(Set.of("A"), directC);
        Assertions.assertEquals(Set.of("B"), directD);
        Assertions.assertEquals(Set.of("B", "C"), directE);
        Assertions.assertEquals(Set.of("C"), directF);
        Assertions.assertEquals(Set.of(), indirectA);
        Assertions.assertEquals(Set.of("A"), indirectB);
        Assertions.assertEquals(Set.of("A"), indirectC);
        Assertions.assertEquals(Set.of("A", "B"), indirectD);
        Assertions.assertEquals(Set.of("A", "B", "C"), indirectE);
        Assertions.assertEquals(Set.of("A", "C"), indirectF);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListSuperClasses1c(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("Thing"), directA);
        Assertions.assertEquals(Set.of("A"), directB);
        Assertions.assertEquals(Set.of("A"), directC);
        Assertions.assertEquals(Set.of("B"), directD);
        Assertions.assertEquals(Set.of("B", "C"), directE);
        Assertions.assertEquals(Set.of("C"), directF);

        Assertions.assertEquals(Set.of("Resource", "Thing"), indirectA);
        Assertions.assertEquals(Set.of("A", "Resource", "Thing"), indirectB);
        Assertions.assertEquals(Set.of("A", "Resource", "Thing"), indirectC);
        Assertions.assertEquals(Set.of("A", "B", "Resource", "Thing"), indirectD);
        Assertions.assertEquals(Set.of("A", "B", "C", "Resource", "Thing"), indirectE);
        Assertions.assertEquals(Set.of("A", "C", "Resource", "Thing"), indirectF);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListSuperClasses1d(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

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

        String TR;
        if (TestSpec.OWL_MEM_MICRO_RULE_INF == spec) {
            TR = "Thing";
        } else if (TestSpec.RDFS_MEM_RDFS_INF == spec) {
            TR = "Resource";
        } else {
            throw new IllegalStateException();
        }

        Assertions.assertEquals(Set.of(TR), directA);
        Assertions.assertEquals(Set.of("A"), directB);
        Assertions.assertEquals(Set.of("A"), directC);
        Assertions.assertEquals(Set.of("B"), directD);
        Assertions.assertEquals(Set.of("B", "C"), directE);
        Assertions.assertEquals(Set.of("C"), directF);

        Assertions.assertEquals(Set.of(TR), indirectA);
        Assertions.assertEquals(Set.of("A", TR), indirectB);
        Assertions.assertEquals(Set.of("A", TR), indirectC);
        Assertions.assertEquals(Set.of("A", "B", TR), indirectD);
        Assertions.assertEquals(Set.of("A", "B", "C", TR), indirectE);
        Assertions.assertEquals(Set.of("A", "C", TR), indirectF);
    }

    @ParameterizedTest
    @EnumSource
    public void testListSuperClasses2(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        A.addSuperClass(B);
        A.addSuperClass(C);
        B.addSuperClass(C);
        C.addSuperClass(B);

        JunitExtensions.assertValues("", A.listSuperClasses(true), B, C);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "RDFS_MEM",
    })
    public void testListSuperClasses3a(TestSpec spec) {
        // B = C
        //  \ |
        //    A
        OntModel m = createClassesBCA(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);

        Set<String> directA = A.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = B.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = C.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = A.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of("B", "C"), directA);
        Assertions.assertEquals(Set.of(), directB);
        Assertions.assertEquals(Set.of(), directC);

        Assertions.assertEquals(Set.of("B", "C"), indirectA);
        Assertions.assertEquals(Set.of("C"), indirectB);
        Assertions.assertEquals(Set.of("B"), indirectC);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListSuperClasses3b(TestSpec spec) {
        // B = C
        //  \ |
        //    A
        OntModel m = createClassesBCA(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);

        Set<String> directA = A.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = B.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = C.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = A.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);

        Assertions.assertEquals(Set.of("B", "C"), directA);
        Assertions.assertEquals(Set.of("Thing"), directB);
        Assertions.assertEquals(Set.of("Thing"), directC);

        Assertions.assertEquals(Set.of("B", "C", "Resource", "Thing"), indirectA);
        Assertions.assertEquals(Set.of("C", "Resource", "Thing"), indirectB);
        Assertions.assertEquals(Set.of("B", "Resource", "Thing"), indirectC);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListSuperClasses3c(TestSpec spec) {
        // B = C
        //  \ |
        //    A
        OntModel m = createClassesBCA(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);

        Set<String> directA = A.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = B.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = C.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = A.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);

        Assertions.assertEquals(Set.of("B", "C"), directA);
        Assertions.assertEquals(Set.of("C"), directB);
        Assertions.assertEquals(Set.of("B"), directC);

        Assertions.assertEquals(Set.of("B", "C"), indirectA);
        Assertions.assertEquals(Set.of("C"), indirectB);
        Assertions.assertEquals(Set.of("B"), indirectC);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListSuperClasses3d(TestSpec spec) {
        // B = C
        //  \ |
        //    A
        OntModel m = createClassesBCA(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);

        Set<String> directA = A.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = B.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = C.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = A.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);

        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);

        String RT = spec == TestSpec.RDFS_MEM_RDFS_INF ? "Resource" : "Thing";

        Assertions.assertEquals(Set.of("B", "C"), directA);
        Assertions.assertEquals(Set.of(RT), directB);
        Assertions.assertEquals(Set.of(RT), directC);

        Assertions.assertEquals(Set.of("B", "C", RT), indirectA);
        Assertions.assertEquals(Set.of("C", RT), indirectB);
        Assertions.assertEquals(Set.of("B", RT), indirectC);
    }

    @Test
    public void testListSuperClasses4() {
        // no inference
        OntModel m1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass a1 = m1.createClass(NS + "A");
        a1.addSubClass(a1);

        Assertions.assertTrue(a1.listSuperClasses(true).toList().isEmpty());
        Assertions.assertTrue(a1.listSuperClasses(false).toList().isEmpty());

        // inference 1
        OntModel m2 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
        OntClass a2 = m2.createClass(NS + "A");
        a2.addSubClass(a2);

        Assertions.assertTrue(a2.listSuperClasses(true).toList().isEmpty());
        Assertions.assertTrue(a2.listSuperClasses(false).toList().isEmpty());

        // inference 2
        OntModel m3 = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
        OntClass a3 = m3.createClass(NS + "A");
        a3.addSubClass(a3);

        Assertions.assertEquals(Set.of(OWL.Thing), a3.listSuperClasses(true).toSet());
        Assertions.assertEquals(Set.of(OWL.Thing, RDFS.Resource), a3.listSuperClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM_RDFS_INF",
    })
    public void testListSuperClasses5a(TestSpec spec) {
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

        Set<String> directA = m.getOntClass(NS + "A").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directG = m.getOntClass(NS + "G").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectG = m.getOntClass(NS + "G").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directH = m.getOntClass(NS + "H").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectH = m.getOntClass(NS + "H").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> directK = m.getOntClass(NS + "K").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectK = m.getOntClass(NS + "K").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directL = m.getOntClass(NS + "L").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectL = m.getOntClass(NS + "L").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directM = m.getOntClass(NS + "M").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectM = m.getOntClass(NS + "M").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of(), directA);
        Assertions.assertEquals(Set.of("A"), directB);
        Assertions.assertEquals(Set.of("A"), directC);
        Assertions.assertEquals(Set.of("B"), directD);
        Assertions.assertEquals(Set.of("B", "C"), directE);
        Assertions.assertEquals(Set.of("C"), directF);
        Assertions.assertEquals(Set.of("D"), directG);
        Assertions.assertEquals(Set.of("D"), directH);
        Assertions.assertEquals(Set.of("D"), directK);
        Assertions.assertEquals(Set.of("H", "K"), directL);
        Assertions.assertEquals(Set.of("H", "K"), directM);

        Assertions.assertEquals(Set.of(), indirectA);
        Assertions.assertEquals(Set.of("A"), indirectB);
        Assertions.assertEquals(Set.of("A"), indirectC);
        Assertions.assertEquals(Set.of("A", "B"), indirectD);
        Assertions.assertEquals(Set.of("A", "B", "C"), indirectE);
        Assertions.assertEquals(Set.of("A", "C"), indirectF);
        Assertions.assertEquals(Set.of("A", "B", "D"), indirectG);
        Assertions.assertEquals(Set.of("D"), indirectH);
        Assertions.assertEquals(Set.of("A", "B", "D", "H"), indirectK);
        Assertions.assertEquals(Set.of("A", "B", "D", "H", "K"), indirectL);
        Assertions.assertEquals(Set.of("A", "B", "D", "H", "K"), indirectM);
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
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListSuperClasses6a(TestSpec spec) {
        //     D
        //    | \
        // B  |  C
        //  \ | /
        //    A
        OntModel m = TestModelFactory.createClassesDBCA(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");

        Set<String> directA = A.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = B.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = C.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directD = D.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = A.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of("B", "C"), directA);
        Assertions.assertEquals(Set.of(), directB);
        Assertions.assertEquals(Set.of("D"), directC);
        Assertions.assertEquals(Set.of(), directD);

        Assertions.assertEquals(Set.of("B", "C", "D"), indirectA);
        Assertions.assertEquals(Set.of(), indirectB);
        Assertions.assertEquals(Set.of("D"), indirectC);
        Assertions.assertEquals(Set.of(), indirectD);
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
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListSuperClasses7a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        A.addSuperClass(A);

        Assertions.assertTrue(A.listSuperClasses(true).toList().isEmpty());
        Assertions.assertTrue(A.listSuperClasses(false).toList().isEmpty());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testListSuperClasses8a(TestSpec spec) {
        //    D
        //  /  \
        // B    F
        // |    |
        // C    E
        //  \  /
        //    A

        OntModel m = createClassesDBFCEA(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("C", "E"), directA);
        Assertions.assertEquals(Set.of("D"), directB);
        Assertions.assertEquals(Set.of("B"), directC);
        Assertions.assertEquals(Set.of(), directD);
        Assertions.assertEquals(Set.of("F"), directE);
        Assertions.assertEquals(Set.of("D"), directF);

        Assertions.assertEquals(Set.of("C", "E"), indirectA);
        Assertions.assertEquals(Set.of("D"), indirectB);
        Assertions.assertEquals(Set.of("B"), indirectC);
        Assertions.assertEquals(Set.of(), indirectD);
        Assertions.assertEquals(Set.of("F"), indirectE);
        Assertions.assertEquals(Set.of("D"), indirectF);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListSuperClasses8b(TestSpec spec) {
        //    D
        //  /  \
        // B    F
        // |    |
        // C    E
        //  \  /
        //    A

        OntModel m = createClassesDBFCEA(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("C", "E"), directA);
        Assertions.assertEquals(Set.of("D"), directB);
        Assertions.assertEquals(Set.of("B"), directC);
        Assertions.assertEquals(Set.of(), directD);
        Assertions.assertEquals(Set.of("F"), directE);
        Assertions.assertEquals(Set.of("D"), directF);

        Assertions.assertEquals(Set.of("B", "C", "D", "E", "F"), indirectA);
        Assertions.assertEquals(Set.of("D"), indirectB);
        Assertions.assertEquals(Set.of("B", "D"), indirectC);
        Assertions.assertEquals(Set.of(), indirectD);
        Assertions.assertEquals(Set.of("D", "F"), indirectE);
        Assertions.assertEquals(Set.of("D"), indirectF);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListSuperClasses8c(TestSpec spec) {
        //    D
        //  /  \
        // B    F
        // |    |
        // C    E
        //  \  /
        //   A

        OntModel m = createClassesDBFCEA(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("C", "E"), directA);
        Assertions.assertEquals(Set.of("D"), directB);
        Assertions.assertEquals(Set.of("B"), directC);
        Assertions.assertEquals(Set.of("Thing"), directD);
        Assertions.assertEquals(Set.of("F"), directE);
        Assertions.assertEquals(Set.of("D"), directF);

        Assertions.assertEquals(Set.of("B", "C", "D", "E", "F", "Resource", "Thing"), indirectA);
        Assertions.assertEquals(Set.of("D", "Resource", "Thing"), indirectB);
        Assertions.assertEquals(Set.of("B", "D", "Resource", "Thing"), indirectC);
        Assertions.assertEquals(Set.of("Resource", "Thing"), indirectD);
        Assertions.assertEquals(Set.of("D", "F", "Resource", "Thing"), indirectE);
        Assertions.assertEquals(Set.of("D", "Resource", "Thing"), indirectF);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListSuperClasses8d(TestSpec spec) {
        //    D
        //  /  \
        // B    F
        // |    |
        // C    E
        //  \  /
        //    A

        OntModel m = createClassesDBFCEA(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("C", "E"), directA);
        Assertions.assertEquals(Set.of("D"), directB);
        Assertions.assertEquals(Set.of("B"), directC);
        Assertions.assertEquals(Set.of("Thing"), directD);
        Assertions.assertEquals(Set.of("F"), directE);
        Assertions.assertEquals(Set.of("D"), directF);

        Assertions.assertEquals(Set.of("B", "C", "D", "E", "F", "Thing"), indirectA);
        Assertions.assertEquals(Set.of("D", "Thing"), indirectB);
        Assertions.assertEquals(Set.of("B", "D", "Thing"), indirectC);
        Assertions.assertEquals(Set.of("Thing"), indirectD);
        Assertions.assertEquals(Set.of("D", "F", "Thing"), indirectE);
        Assertions.assertEquals(Set.of("D", "Thing"), indirectF);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM_RDFS_INF",
    })
    public void testListSuperClasses8g(TestSpec spec) {
        //    D
        //  /  \
        // B    F
        // |    |
        // C    E
        //  \  /
        //   A

        OntModel m = createClassesDBFCEA(ModelFactory.createOntologyModel(spec.inst));

        Set<String> directA = m.getOntClass(NS + "A").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("C", "E"), directA);
        Assertions.assertEquals(Set.of("D"), directB);
        Assertions.assertEquals(Set.of("B"), directC);
        Assertions.assertEquals(Set.of("Resource"), directD);
        Assertions.assertEquals(Set.of("F"), directE);
        Assertions.assertEquals(Set.of("D"), directF);

        Assertions.assertEquals(Set.of("B", "C", "D", "E", "F", "Resource"), indirectA);
        Assertions.assertEquals(Set.of("D", "Resource"), indirectB);
        Assertions.assertEquals(Set.of("B", "D", "Resource"), indirectC);
        Assertions.assertEquals(Set.of("Resource"), indirectD);
        Assertions.assertEquals(Set.of("D", "F", "Resource"), indirectE);
        Assertions.assertEquals(Set.of("D", "Resource"), indirectF);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testHasSuperClass1a(TestSpec spec) {
        //     D
        //    | \
        // B  |  C
        //  \ | /
        //    A
        OntModel m = TestModelFactory.createClassesDBCA(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");

        Assertions.assertFalse(A.hasSuperClass(A, false));
        Assertions.assertTrue(A.hasSuperClass(B, false));
        Assertions.assertTrue(A.hasSuperClass(C, false));
        Assertions.assertTrue(A.hasSuperClass(D, false));
        Assertions.assertFalse(B.hasSuperClass(A, false));
        Assertions.assertFalse(B.hasSuperClass(B, false));
        Assertions.assertFalse(B.hasSuperClass(C, false));
        Assertions.assertFalse(B.hasSuperClass(D, false));
        Assertions.assertFalse(C.hasSuperClass(A, false));
        Assertions.assertFalse(C.hasSuperClass(B, false));
        Assertions.assertFalse(C.hasSuperClass(C, false));
        Assertions.assertTrue(C.hasSuperClass(D, false));
        Assertions.assertFalse(D.hasSuperClass(A, false));
        Assertions.assertFalse(D.hasSuperClass(B, false));
        Assertions.assertFalse(D.hasSuperClass(C, false));
        Assertions.assertFalse(D.hasSuperClass(D, false));

        Assertions.assertFalse(A.hasSuperClass(A, true));
        Assertions.assertTrue(A.hasSuperClass(B, true));
        Assertions.assertTrue(A.hasSuperClass(C, true));
        Assertions.assertFalse(A.hasSuperClass(D, true));
        Assertions.assertFalse(B.hasSuperClass(A, true));
        Assertions.assertFalse(B.hasSuperClass(B, true));
        Assertions.assertFalse(B.hasSuperClass(C, true));
        Assertions.assertFalse(B.hasSuperClass(D, true));
        Assertions.assertFalse(C.hasSuperClass(A, true));
        Assertions.assertFalse(C.hasSuperClass(B, true));
        Assertions.assertFalse(C.hasSuperClass(C, true));
        Assertions.assertTrue(C.hasSuperClass(D, true));
        Assertions.assertFalse(D.hasSuperClass(A, true));
        Assertions.assertFalse(D.hasSuperClass(B, true));
        Assertions.assertFalse(D.hasSuperClass(C, true));
        Assertions.assertFalse(D.hasSuperClass(D, true));
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
    public void testHasSuperClass1b(TestSpec spec) {
        //     D
        //    | \
        // B  |  C
        //  \ | /
        //    A
        OntModel m = TestModelFactory.createClassesDBCA(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");

        Assertions.assertTrue(A.hasSuperClass(A, false));
        Assertions.assertTrue(A.hasSuperClass(B, false));
        Assertions.assertTrue(A.hasSuperClass(C, false));
        Assertions.assertTrue(A.hasSuperClass(D, false));
        Assertions.assertFalse(B.hasSuperClass(A, false));
        Assertions.assertTrue(B.hasSuperClass(B, false));
        Assertions.assertFalse(B.hasSuperClass(C, false));
        Assertions.assertFalse(B.hasSuperClass(D, false));
        Assertions.assertFalse(C.hasSuperClass(A, false));
        Assertions.assertFalse(C.hasSuperClass(B, false));
        Assertions.assertTrue(C.hasSuperClass(C, false));
        Assertions.assertTrue(C.hasSuperClass(D, false));
        Assertions.assertFalse(D.hasSuperClass(A, false));
        Assertions.assertFalse(D.hasSuperClass(B, false));
        Assertions.assertFalse(D.hasSuperClass(C, false));
        Assertions.assertTrue(D.hasSuperClass(D, false));

        Assertions.assertFalse(A.hasSuperClass(A, true));
        Assertions.assertTrue(A.hasSuperClass(B, true));
        Assertions.assertTrue(A.hasSuperClass(C, true));
        Assertions.assertFalse(A.hasSuperClass(D, true));
        Assertions.assertFalse(B.hasSuperClass(A, true));
        Assertions.assertFalse(B.hasSuperClass(B, true));
        Assertions.assertFalse(B.hasSuperClass(C, true));
        Assertions.assertFalse(B.hasSuperClass(D, true));
        Assertions.assertFalse(C.hasSuperClass(A, true));
        Assertions.assertFalse(C.hasSuperClass(B, true));
        Assertions.assertFalse(C.hasSuperClass(C, true));
        Assertions.assertTrue(C.hasSuperClass(D, true));
        Assertions.assertFalse(D.hasSuperClass(A, true));
        Assertions.assertFalse(D.hasSuperClass(B, true));
        Assertions.assertFalse(D.hasSuperClass(C, true));
        Assertions.assertFalse(D.hasSuperClass(D, true));
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testHasSuperClass1c(TestSpec spec) {
        //     D
        //    | \
        // B  |  C
        //  \ | /
        //    A
        OntModel m = TestModelFactory.createClassesDBCA(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");

        Assertions.assertTrue(A.hasSuperClass(A, false));
        Assertions.assertTrue(A.hasSuperClass(B, false));
        Assertions.assertTrue(A.hasSuperClass(C, false));
        Assertions.assertTrue(A.hasSuperClass(D, false));
        Assertions.assertFalse(B.hasSuperClass(A, false));
        Assertions.assertTrue(B.hasSuperClass(B, false));
        Assertions.assertFalse(B.hasSuperClass(C, false));
        Assertions.assertFalse(B.hasSuperClass(D, false));
        Assertions.assertFalse(C.hasSuperClass(A, false));
        Assertions.assertFalse(C.hasSuperClass(B, false));
        Assertions.assertTrue(C.hasSuperClass(C, false));
        Assertions.assertTrue(C.hasSuperClass(D, false));
        Assertions.assertFalse(D.hasSuperClass(A, false));
        Assertions.assertFalse(D.hasSuperClass(B, false));
        Assertions.assertFalse(D.hasSuperClass(C, false));
        Assertions.assertTrue(D.hasSuperClass(D, false));

        Assertions.assertTrue(A.hasSuperClass(A, true));
        Assertions.assertTrue(A.hasSuperClass(B, true));
        Assertions.assertTrue(A.hasSuperClass(C, true));
        Assertions.assertFalse(A.hasSuperClass(D, true));
        Assertions.assertFalse(B.hasSuperClass(A, true));
        Assertions.assertTrue(B.hasSuperClass(B, true));
        Assertions.assertFalse(B.hasSuperClass(C, true));
        Assertions.assertFalse(B.hasSuperClass(D, true));
        Assertions.assertFalse(C.hasSuperClass(A, true));
        Assertions.assertFalse(C.hasSuperClass(B, true));
        Assertions.assertTrue(C.hasSuperClass(C, true));
        Assertions.assertTrue(C.hasSuperClass(D, true));
        Assertions.assertFalse(D.hasSuperClass(A, true));
        Assertions.assertFalse(D.hasSuperClass(B, true));
        Assertions.assertFalse(D.hasSuperClass(C, true));
        Assertions.assertTrue(D.hasSuperClass(D, true));
    }
}
