package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.JunitExtensions;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Set;

public class ClassTest {
    private static final String NS = "http://example.com/test#";

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
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        OntClass a = m.createClass(NS + "A");
        Assertions.assertNull(a.getSuperClass());
        Assertions.assertFalse(a.hasSuperClass());
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
    public void testSubClassNE(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        OntClass a = m.createClass(NS + "A");
        Assertions.assertNull(a.getSubClass());
        Assertions.assertFalse(a.hasSubClass());
    }

    @ParameterizedTest
    @EnumSource
    public void testCreateIndividual(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        OntClass a = m.createClass(NS + "A");
        Individual i = a.createIndividual(NS + "i");
        Assertions.assertTrue(i.hasRDFType(a));

        Individual j = a.createIndividual();
        Assertions.assertTrue(j.hasRDFType(a));
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
    @EnumSource
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
        // D  THING    K
        // |  |      / |
        // C  F     H  |
        // |  |      \ |
        // B  E        G
        // |
        // A
        OntModel m = createABCDEFGHKModel(ModelFactory.createOntologyModel(spec.spec));
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
        // D  THING    K
        // |  |      / |
        // C  F     H  |
        // |  |      \ |
        // B  E        G
        // |
        // A
        OntModel m = createABCDEFGHKModel(ModelFactory.createOntologyModel(spec.spec));
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
        // D  F     K
        // |  |   / |
        // C  E  H  |
        // |      \ |
        // B        G
        // |
        // A
        OntModel m = createABCDEFGHKModel(ModelFactory.createOntologyModel(spec.spec));

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

        OntModel m = createABCDEFGHKLMModel(ModelFactory.createOntologyModel(spec.spec));

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

        OntModel m = createABCDEFGHKLMModel(ModelFactory.createOntologyModel(spec.spec));

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

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testListSubClasses0(TestSpec spec) {
        // no inference
        OntModel m = createABCDEFModel(spec.spec);
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");
        OntClass c = m.getOntClass(NS + "C");
        OntClass d = m.getOntClass(NS + "D");
        OntClass e = m.getOntClass(NS + "E");

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

        JunitExtensions.assertValues("", a.listSubClasses(), b, c);
        JunitExtensions.assertValues("", a.listSubClasses(false), b, c);
        JunitExtensions.assertValues("", a.listSubClasses(true), b, c);
        JunitExtensions.assertValues("", b.listSubClasses(true), d, e);

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
    public void testListSubClasses1(TestSpec spec) {
        // rule inference
        OntModel m = createABCDEFModel(spec.spec);

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

    @Test
    public void testListSubClasses2() {
        OntModel m = createABCDEFModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
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
    public void testListSubClasses2(TestSpec spec) {
        // no inference
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        OntClass c = m.createClass(NS + "C");
        OntClass d = m.createClass(NS + "D");
        a.addSubClass(b);
        a.addSubClass(c);
        c.addSubClass(d);

        Assertions.assertEquals(Set.of(b, c), a.listSubClasses().toSet());
        Assertions.assertEquals(Set.of(b, c), a.listSubClasses(true).toSet());
        Assertions.assertEquals(Set.of(b, c), a.listSubClasses(false).toSet());

        a.addSubClass(d);
        Assertions.assertEquals(Set.of(b, c, d), a.listSubClasses().toSet());
        Assertions.assertEquals(Set.of(b, c), a.listSubClasses(true).toSet());
        Assertions.assertEquals(Set.of(b, c, d), a.listSubClasses(false).toSet());
    }

    @Test
    public void testListSubClasses4() {
        // no inference
        OntModel m1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass a1 = m1.createClass(NS + "A");
        a1.addSubClass(a1);

        Assertions.assertTrue(a1.listSubClasses(true).toList().isEmpty());
        Assertions.assertTrue(a1.listSubClasses(false).toList().isEmpty());

        // inference 1
        OntModel m2 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
        OntClass a2 = m2.createClass(NS + "A");
        a2.addSubClass(a2);

        Assertions.assertTrue(a2.listSubClasses(true).toList().isEmpty());
        Assertions.assertTrue(a2.listSubClasses(false).toList().isEmpty());

        // inference 2
        OntModel m3 = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
        OntClass a3 = m3.createClass(NS + "A");
        a3.addSubClass(a3);

        Assertions.assertEquals(Set.of(), a3.listSubClasses(true).toSet());
        Assertions.assertEquals(Set.of(), a3.listSubClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testListSubClasses6(TestSpec spec) {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M

        OntModel m = createABCDEFGHKLMModel(ModelFactory.createOntologyModel(spec.spec));

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
    public void testListSubClasses7(TestSpec spec) {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M

        OntModel m = createABCDEFGHKLMModel(ModelFactory.createOntologyModel(spec.spec));

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
    public void testListSubClasses8(TestSpec spec) {
        //     A
        //     |
        // D = B = C
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
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
    public void testListSubClasses9(TestSpec spec) {
        //     A
        //     |
        // D = B = C
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
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
    public void testListSubClasses10(TestSpec spec) {
        // B = C
        //  \ |
        //    A
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
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
    public void testListSuperClasses3NoInf(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F
        OntModel m = createABCDEFModel(spec.spec);

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
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListSuperClasses1(TestSpec spec) {
        // rule inference
        OntModel m = createABCDEFModel(spec.spec);
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass E = m.getOntClass(NS + "E");

        Set<String> directA = A.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = B.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = C.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directE = E.listSuperClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = A.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listSuperClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of("Thing"), directA);
        Assertions.assertEquals(Set.of("A"), directB);
        Assertions.assertEquals(Set.of("A"), directC);
        Assertions.assertEquals(Set.of("B", "C"), directE);

        Assertions.assertEquals(Set.of("Resource", "Thing"), indirectA);
        Assertions.assertEquals(Set.of("A", "Resource", "Thing"), indirectB);
        Assertions.assertEquals(Set.of("A", "Resource", "Thing"), indirectC);
        Assertions.assertEquals(Set.of("A", "B", "C", "Resource", "Thing"), indirectE);
    }

    @Test
    public void testListSuperClasses2() {
        OntModel m = createABCDEFModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");
        OntClass c = m.getOntClass(NS + "C");
        OntClass e = m.getOntClass(NS + "E");

        JunitExtensions.assertValues("", e.listSuperClasses(), b, c, a, OWL.Thing);
        JunitExtensions.assertValues("", e.listSuperClasses(false), b, c, a, OWL.Thing);
        JunitExtensions.assertValues("", e.listSuperClasses(true), b, c);
        JunitExtensions.assertValues("", b.listSuperClasses(true), a);
    }

    @ParameterizedTest
    @EnumSource
    public void testListSuperClasses3(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
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
    public void testListSuperClasses4(TestSpec spec) {
        // B = C
        //  \ |
        //    A
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        A.addSuperClass(B);
        A.addSuperClass(C);
        B.addSuperClass(C);
        C.addSuperClass(B);

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

    @Test
    public void testListSuperClasses5() {
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
    public void testListSuperClasses6(TestSpec spec) {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M

        OntModel m = createABCDEFGHKLMModel(ModelFactory.createOntologyModel(spec.spec));

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
    public void testListSuperClasses7(TestSpec spec) {
        //     D
        //    | \
        // B  |  C
        //  \ | /
        //    A
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        C.addSubClass(A);
        B.addSubClass(A);
        D.addSubClass(C);
        D.addSubClass(A);

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
    public void testListSuperClasses8(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        OntClass A = m.createClass(NS + "A");
        A.addSuperClass(A);

        Assertions.assertTrue(A.listSuperClasses(true).toList().isEmpty());
        Assertions.assertTrue(A.listSuperClasses(false).toList().isEmpty());
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
    public void testListInstances0(TestSpec spec) {
        OntModel m = createABCDEFModel(spec.spec);
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");

        Individual ia = a.createIndividual();
        Individual ib = b.createIndividual();

        JunitExtensions.assertValues("", a.listInstances(), ia);
        JunitExtensions.assertValues("", b.listInstances(), ib);

        JunitExtensions.assertValues("", a.listInstances(true), ia);
        JunitExtensions.assertValues("", b.listInstances(true), ib);
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
    public void testListInstances1(TestSpec spec) {
        OntModel m = createABCDEFModel(spec.spec);
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");
        OntClass c = m.getOntClass(NS + "C");
        OntClass d = m.getOntClass(NS + "D");
        OntClass e = m.getOntClass(NS + "E");

        Individual ia = a.createIndividual(NS + "iA");
        Individual ib = b.createIndividual(NS + "iB");
        Individual ic = c.createIndividual(NS + "iC");
        Individual id = d.createIndividual(NS + "iD");
        Individual ie = e.createIndividual(NS + "iE");

        JunitExtensions.assertValues("", a.listInstances(), ia, ib, ic, id, ie);
        JunitExtensions.assertValues("", b.listInstances(), ib, id, ie);

        JunitExtensions.assertValues("", a.listInstances(true), ia);
        JunitExtensions.assertValues("", b.listInstances(true), ib);
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
    public void testListInstances2(TestSpec spec) {
        OntModel m = createABCDEFModel(spec.spec);
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");
        OntClass c = m.getOntClass(NS + "C");
        OntClass d = m.getOntClass(NS + "D");
        OntClass e = m.getOntClass(NS + "E");

        Individual ia = a.createIndividual(NS + "iA");
        Individual ib = b.createIndividual(NS + "iB");
        Individual ic = c.createIndividual(NS + "iC");
        Individual id = d.createIndividual(NS + "iD");
        Individual ie = e.createIndividual(NS + "iE");

        JunitExtensions.assertValues("", a.listInstances(), ia, ib, ic, id, ie);
        JunitExtensions.assertValues("", b.listInstances(), ib, id, ie);

        JunitExtensions.assertValues("", a.listInstances(true), ia);
        JunitExtensions.assertValues("", b.listInstances(true), ib);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM_RDFS_INF",
    })
    public void testListInstances3(TestSpec spec) {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M

        OntModel m = createABCDEFGHKLMModel(ModelFactory.createOntologyModel(spec.spec));
        m.listStatements(null, RDF.type, OWL.Class)
                .mapWith(Statement::getSubject)
                .mapWith(x -> x.as(OntClass.class))
                .toList()
                .forEach(x -> x.createIndividual(NS + "i" + x.getLocalName()));
        Set<String> directA = m.getOntClass(NS + "A").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getOntClass(NS + "E").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getOntClass(NS + "E").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getOntClass(NS + "F").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getOntClass(NS + "F").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directG = m.getOntClass(NS + "G").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectG = m.getOntClass(NS + "G").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directH = m.getOntClass(NS + "H").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectH = m.getOntClass(NS + "H").listInstances(true).mapWith(Resource::getLocalName).toSet();

        Set<String> directK = m.getOntClass(NS + "K").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectK = m.getOntClass(NS + "K").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directL = m.getOntClass(NS + "L").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectL = m.getOntClass(NS + "L").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directM = m.getOntClass(NS + "M").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectM = m.getOntClass(NS + "M").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of("iA"), directA);
        Assertions.assertEquals(Set.of("iB"), directB);
        Assertions.assertEquals(Set.of("iC"), directC);
        Assertions.assertEquals(Set.of("iD"), directD);
        Assertions.assertEquals(Set.of("iE"), directE);
        Assertions.assertEquals(Set.of("iF"), directF);
        Assertions.assertEquals(Set.of("iG"), directG);
        Assertions.assertEquals(Set.of("iK", "iH"), directH);
        Assertions.assertEquals(Set.of("iK", "iH"), directK);
        Assertions.assertEquals(Set.of("iL"), directL);
        Assertions.assertEquals(Set.of("iM"), directM);

        Assertions.assertEquals(Set.of("iA", "iB", "iC", "iD", "iE", "iF", "iG", "iH", "iK", "iL", "iM"), indirectA);
        Assertions.assertEquals(Set.of("iB", "iD", "iE", "iG", "iH", "iK", "iL", "iM"), indirectB);
        Assertions.assertEquals(Set.of("iC", "iE", "iF"), indirectC);
        Assertions.assertEquals(Set.of("iD", "iG", "iH", "iK", "iL", "iM"), indirectD);
        Assertions.assertEquals(Set.of("iE"), indirectE);
        Assertions.assertEquals(Set.of("iF"), indirectF);
        Assertions.assertEquals(Set.of("iG"), indirectG);
        Assertions.assertEquals(Set.of("iK", "iH"), indirectH);
        Assertions.assertEquals(Set.of("iH", "iK", "iL", "iM"), indirectK);
        Assertions.assertEquals(Set.of("iL"), indirectL);
        Assertions.assertEquals(Set.of("iM"), indirectM);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM_RDFS_INF",
    })
    public void testListInstances4(TestSpec spec) {
        // B = C
        //  \ |
        //    A
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        A.addSuperClass(B);
        A.addSuperClass(C);
        B.addSuperClass(C);
        C.addSuperClass(B);
        m.listStatements(null, RDF.type, OWL.Class)
                .mapWith(Statement::getSubject)
                .mapWith(x -> x.as(OntClass.class))
                .toList()
                .forEach(x -> x.createIndividual(NS + "i" + x.getLocalName()));

        Set<String> directA = m.getOntClass(NS + "A").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listInstances(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);
        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);

        Assertions.assertEquals(Set.of("iA"), directA);
        Assertions.assertEquals(Set.of("iB", "iC"), directB);
        Assertions.assertEquals(Set.of("iB", "iC"), directC);
        Assertions.assertEquals(Set.of("iA"), indirectA);
        Assertions.assertEquals(Set.of("iA", "iB", "iC"), indirectB);
        Assertions.assertEquals(Set.of("iA", "iB", "iC"), indirectC);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM_RDFS_INF",
    })
    public void testListInstances5(TestSpec spec) {
        //     D
        //    | \
        // B  |  C
        //  \ | /
        //    A
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        C.addSubClass(A);
        B.addSubClass(A);
        D.addSubClass(C);
        D.addSubClass(A);
        m.listStatements(null, RDF.type, OWL.Class)
                .mapWith(Statement::getSubject)
                .mapWith(x -> x.as(OntClass.class))
                .toList()
                .forEach(x -> x.createIndividual(NS + "i" + x.getLocalName()));

        Set<String> directA = m.getOntClass(NS + "A").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getOntClass(NS + "A").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getOntClass(NS + "B").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getOntClass(NS + "B").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getOntClass(NS + "C").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getOntClass(NS + "C").listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getOntClass(NS + "D").listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getOntClass(NS + "D").listInstances(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);
        System.out.println("DIRECT-D::" + directD);
        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);
        System.out.println("INDIRECT-D::" + indirectD);

        Assertions.assertEquals(Set.of("iA"), directA);
        Assertions.assertEquals(Set.of("iB"), directB);
        Assertions.assertEquals(Set.of("iC"), directC);
        Assertions.assertEquals(Set.of("iD"), directD);
        Assertions.assertEquals(Set.of("iA"), indirectA);
        Assertions.assertEquals(Set.of("iA", "iB"), indirectB);
        Assertions.assertEquals(Set.of("iA", "iC"), indirectC);
        Assertions.assertEquals(Set.of("iA", "iC", "iD"), indirectD);
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
    public void testListInstances6(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F
        OntModel m = createABCDEFModel(spec.spec);
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");
        OntClass c = m.getOntClass(NS + "C");
        OntClass d = m.getOntClass(NS + "D");
        OntClass e = m.getOntClass(NS + "E");
        OntClass f = m.getOntClass(NS + "F");

        a.createIndividual(NS + "iA");
        b.createIndividual(NS + "iB");
        c.createIndividual(NS + "iC");
        d.createIndividual(NS + "iD");
        e.createIndividual(NS + "iE");

        Set<String> directA = a.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = a.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = b.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = b.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = c.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = c.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = d.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = d.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = e.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = e.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = f.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = f.listInstances(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("iA"), directA);
        Assertions.assertEquals(Set.of("iB"), directB);
        Assertions.assertEquals(Set.of("iC"), directC);
        Assertions.assertEquals(Set.of("iD"), directD);
        Assertions.assertEquals(Set.of("iE"), directE);
        Assertions.assertEquals(Set.of(), directF);
        Assertions.assertEquals(Set.of("iA", "iB", "iC", "iD", "iE"), indirectA);
        Assertions.assertEquals(Set.of("iB", "iD", "iE"), indirectB);
        Assertions.assertEquals(Set.of("iE", "iC"), indirectC);
        Assertions.assertEquals(Set.of("iD"), indirectD);
        Assertions.assertEquals(Set.of("iE"), indirectE);
        Assertions.assertEquals(Set.of(), indirectF);
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
    public void testDropIndividual(TestSpec spec) {
        OntModel m = createABCDEFModel(spec.spec);
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");
        Individual ia = a.createIndividual(NS + "iA");
        ia.addOntClass(b);

        Assertions.assertTrue(ia.hasOntClass(a));
        Assertions.assertTrue(ia.hasOntClass(b));

        // drop ia from the extension of A
        a.dropIndividual(ia);

        Assertions.assertFalse(ia.hasOntClass(a));
        Assertions.assertTrue(ia.hasOntClass(b));

        // do it again - should be a no-op
        a.dropIndividual(ia);

        Assertions.assertFalse(ia.hasOntClass(a));
        Assertions.assertTrue(ia.hasOntClass(b));

        // drop ia from the extension of b
        b.dropIndividual(ia);

        Assertions.assertFalse(ia.hasOntClass(a));
        Assertions.assertFalse(ia.hasOntClass(b));
    }

    @ParameterizedTest
    @EnumSource
    public void testDatatypeIsClass(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.spec);
        Resource c = m.createResource();
        c.addProperty(RDF.type, RDFS.Datatype);
        Assertions.assertTrue(c.canAs(OntClass.class));
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
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testOwlThingNothingClass(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.spec);

        Resource r = OWL.Thing.inModel(m);
        OntClass thingClass = r.as(OntClass.class);
        Assertions.assertNotNull(thingClass);

        r = OWL.Nothing.inModel(m);
        OntClass nothingClass = r.as(OntClass.class);
        Assertions.assertNotNull(nothingClass);

        OntClass c = m.getOntClass(OWL.Thing.getURI());
        Assertions.assertNotNull(c);
        Assertions.assertEquals(OWL.Thing, c);

        c = m.getOntClass(OWL.Nothing.getURI());
        Assertions.assertNotNull(c);
        Assertions.assertEquals(c, OWL.Nothing);
    }

    private static OntModel createABCDEFModel(OntModelSpec spec) {
        return createABCDEFModel(ModelFactory.createOntologyModel(spec));
    }

    private static OntModel createABCDEFModel(OntModel m) {
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        OntClass c = m.createClass(NS + "C");
        OntClass d = m.createClass(NS + "D");
        OntClass e = m.createClass(NS + "E");
        OntClass f = m.createClass(NS + "F");

        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F

        a.addSubClass(b);
        a.addSubClass(c);
        b.addSubClass(d);
        b.addSubClass(e);
        c.addSubClass(e);
        c.addSubClass(f);
        return m;
    }

    private static OntModel createABCDEFGHKLMModel(OntModel m) {
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass F = m.createClass(NS + "F");
        OntClass G = m.createClass(NS + "G");
        OntClass H = m.createClass(NS + "H");
        OntClass K = m.createClass(NS + "K");
        OntClass L = m.createClass(NS + "L");
        OntClass M = m.createClass(NS + "M");

        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M

        A.addSubClass(D);
        A.addSubClass(B);
        A.addSubClass(C);

        B.addSubClass(D);
        B.addSubClass(E);

        C.addSubClass(E);
        C.addSubClass(F);

        D.addSubClass(G);
        D.addSubClass(H);

        H.addSubClass(K);

        K.addSubClass(H);
        K.addSubClass(L);
        K.addSubClass(M);
        return m;
    }

    private OntModel createABCDEFGHKModel(OntModel m) {
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass F = m.createClass(NS + "F");
        OntClass G = m.createClass(NS + "G");
        OntClass H = m.createClass(NS + "H");
        OntClass K = m.createClass(NS + "K");

        // D  F     K
        // |  |   / |
        // C  E  H  |
        // |      \ |
        // B       G
        // |
        // A

        A.addSuperClass(B);
        B.addSuperClass(C);
        C.addSuperClass(D);
        E.addSuperClass(F);
        G.addSuperClass(H);
        H.addSuperClass(K);
        K.addSuperClass(G);
        return m;
    }
}
