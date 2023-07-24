package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static com.gitlab.sszuev.jena.ont.TestModelFactory.NS;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDEFGHKLM;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesDGCFKBEHAG;

public class ClassHierarchyRootTest {

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
    public void testIsHierarchyRoot1(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        Assertions.assertTrue(m.getOntClass(OWL2.Thing.getURI()).isHierarchyRoot());
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
    })
    public void testIsHierarchyRoot2(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        Assertions.assertFalse(OWL2.Nothing.inModel(m).as(OntClass.class).isHierarchyRoot());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testIsHierarchyRoot3(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        Assertions.assertTrue(OWL2.Nothing.inModel(m).as(OntClass.class).isHierarchyRoot());
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
    public void testIsHierarchyRoot4(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        Assertions.assertTrue(a.isHierarchyRoot());
        Assertions.assertFalse(b.isHierarchyRoot());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_TRANS_INF",
    })
    public void testIsHierarchyRoot5(TestSpec spec) {
        // D  THING    G
        // |    |    / .
        // C    F   K  .
        // |    |   |  .
        // B    E   H  .
        // |         \ .
        // A           G
        OntModel m = createClassesDGCFKBEHAG(ModelFactory.createOntologyModel(spec.spec));
        OntClass Thing = OWL.Thing.inModel(m).as(OntClass.class);
        OntClass Nothing = OWL.Nothing.inModel(m).as(OntClass.class);
        m.getOntClass(NS + "F").addSuperClass(Thing);

        Assertions.assertFalse(m.getOntClass(NS + "A").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "B").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "C").isHierarchyRoot());
        Assertions.assertTrue(m.getOntClass(NS + "D").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "E").isHierarchyRoot());
        Assertions.assertTrue(m.getOntClass(NS + "F").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "G").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "H").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "K").isHierarchyRoot());
        Assertions.assertTrue(Thing.isHierarchyRoot());
        Assertions.assertFalse(Nothing.isHierarchyRoot());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
    })
    public void testIsHierarchyRoot6(TestSpec spec) {
        // D  THING    G
        // |    |    / .
        // C    F   K  .
        // |    |   |  .
        // B    E   H  .
        // |         \ .
        // A           G
        OntModel m = createClassesDGCFKBEHAG(ModelFactory.createOntologyModel(spec.spec));
        OntClass Thing = OWL.Thing.inModel(m).as(OntClass.class);
        OntClass Nothing = OWL.Nothing.inModel(m).as(OntClass.class);
        m.getOntClass(NS + "F").addSuperClass(Thing);

        Assertions.assertFalse(m.getOntClass(NS + "A").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "B").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "C").isHierarchyRoot());
        Assertions.assertTrue(m.getOntClass(NS + "D").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "E").isHierarchyRoot());
        Assertions.assertTrue(m.getOntClass(NS + "F").isHierarchyRoot());
        Assertions.assertTrue(m.getOntClass(NS + "G").isHierarchyRoot());
        Assertions.assertTrue(m.getOntClass(NS + "H").isHierarchyRoot());
        Assertions.assertTrue(m.getOntClass(NS + "K").isHierarchyRoot());
        Assertions.assertTrue(Thing.isHierarchyRoot());
        Assertions.assertFalse(Nothing.isHierarchyRoot());
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
    public void testIsHierarchyRoot7(TestSpec spec) {
        // D        G
        // |      / .
        // C  F  K  .
        // |  |  |  .
        // B  E  H  .
        // |      \ .
        // A        G
        OntModel m = createClassesDGCFKBEHAG(ModelFactory.createOntologyModel(spec.spec));

        Assertions.assertFalse(m.getOntClass(NS + "A").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "B").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "C").isHierarchyRoot());
        Assertions.assertTrue(m.getOntClass(NS + "D").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "E").isHierarchyRoot());
        Assertions.assertTrue(m.getOntClass(NS + "F").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "G").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "H").isHierarchyRoot());
        Assertions.assertFalse(m.getOntClass(NS + "K").isHierarchyRoot());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testIsHierarchyRoot8(TestSpec spec) {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M

        OntModel m = createClassesABCDEFGHKLM(ModelFactory.createOntologyModel(spec.spec));

        boolean isHierarchyRootA = m.getOntClass(NS + "A").isHierarchyRoot();
        boolean isHierarchyRootB = m.getOntClass(NS + "B").isHierarchyRoot();
        boolean isHierarchyRootC = m.getOntClass(NS + "C").isHierarchyRoot();
        boolean isHierarchyRootD = m.getOntClass(NS + "D").isHierarchyRoot();
        boolean isHierarchyRootE = m.getOntClass(NS + "E").isHierarchyRoot();
        boolean isHierarchyRootF = m.getOntClass(NS + "F").isHierarchyRoot();
        boolean isHierarchyRootG = m.getOntClass(NS + "G").isHierarchyRoot();
        boolean isHierarchyRootH = m.getOntClass(NS + "H").isHierarchyRoot();
        boolean isHierarchyRootK = m.getOntClass(NS + "K").isHierarchyRoot();
        boolean isHierarchyRootL = m.getOntClass(NS + "L").isHierarchyRoot();
        boolean isHierarchyRootM = m.getOntClass(NS + "M").isHierarchyRoot();

        System.out.println("A:" + isHierarchyRootA);
        System.out.println("B:" + isHierarchyRootB);
        System.out.println("C:" + isHierarchyRootC);
        System.out.println("D:" + isHierarchyRootD);
        System.out.println("E:" + isHierarchyRootE);
        System.out.println("F:" + isHierarchyRootF);
        System.out.println("G:" + isHierarchyRootG);
        System.out.println("H:" + isHierarchyRootH);
        System.out.println("K:" + isHierarchyRootK);
        System.out.println("L:" + isHierarchyRootL);
        System.out.println("M:" + isHierarchyRootM);

        Assertions.assertTrue(isHierarchyRootA);
        Assertions.assertFalse(isHierarchyRootB);
        Assertions.assertFalse(isHierarchyRootC);
        Assertions.assertFalse(isHierarchyRootD);
        Assertions.assertFalse(isHierarchyRootE);
        Assertions.assertFalse(isHierarchyRootF);
        Assertions.assertFalse(isHierarchyRootG);
        Assertions.assertFalse(isHierarchyRootH);
        Assertions.assertTrue(isHierarchyRootK);
        Assertions.assertFalse(isHierarchyRootL);
        Assertions.assertFalse(isHierarchyRootM);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_TRANS_INF",
            "OWL_MEM_MICRO_RULE_INF",
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
    public void testIsHierarchyRoot9(TestSpec spec) {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M

        OntModel m = createClassesABCDEFGHKLM(ModelFactory.createOntologyModel(spec.spec));

        boolean isHierarchyRootA = m.getOntClass(NS + "A").isHierarchyRoot();
        boolean isHierarchyRootB = m.getOntClass(NS + "B").isHierarchyRoot();
        boolean isHierarchyRootC = m.getOntClass(NS + "C").isHierarchyRoot();
        boolean isHierarchyRootD = m.getOntClass(NS + "D").isHierarchyRoot();
        boolean isHierarchyRootE = m.getOntClass(NS + "E").isHierarchyRoot();
        boolean isHierarchyRootF = m.getOntClass(NS + "F").isHierarchyRoot();
        boolean isHierarchyRootG = m.getOntClass(NS + "G").isHierarchyRoot();
        boolean isHierarchyRootH = m.getOntClass(NS + "H").isHierarchyRoot();
        boolean isHierarchyRootK = m.getOntClass(NS + "K").isHierarchyRoot();
        boolean isHierarchyRootL = m.getOntClass(NS + "L").isHierarchyRoot();
        boolean isHierarchyRootM = m.getOntClass(NS + "M").isHierarchyRoot();

        System.out.println("A:" + isHierarchyRootA);
        System.out.println("B:" + isHierarchyRootB);
        System.out.println("C:" + isHierarchyRootC);
        System.out.println("D:" + isHierarchyRootD);
        System.out.println("E:" + isHierarchyRootE);
        System.out.println("F:" + isHierarchyRootF);
        System.out.println("G:" + isHierarchyRootG);
        System.out.println("H:" + isHierarchyRootH);
        System.out.println("K:" + isHierarchyRootK);
        System.out.println("L:" + isHierarchyRootL);
        System.out.println("M:" + isHierarchyRootM);

        Assertions.assertTrue(isHierarchyRootA);
        Assertions.assertFalse(isHierarchyRootB);
        Assertions.assertFalse(isHierarchyRootC);
        Assertions.assertFalse(isHierarchyRootD);
        Assertions.assertFalse(isHierarchyRootE);
        Assertions.assertFalse(isHierarchyRootF);
        Assertions.assertFalse(isHierarchyRootG);
        Assertions.assertFalse(isHierarchyRootH);
        Assertions.assertFalse(isHierarchyRootK);
        Assertions.assertFalse(isHierarchyRootL);
        Assertions.assertFalse(isHierarchyRootM);
    }

}
