package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.JunitExtensions;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class ClassTest {
    private static final String NS = "http://example.com/test#";

    @Test
    public void testSuperClassNE() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass a = m.createClass(NS + "A");

        Assertions.assertNull(a.getSuperClass());
        Assertions.assertFalse(a.hasSuperClass());
    }

    @Test
    public void testSubClassNE() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass a = m.createClass(NS + "A");

        Assertions.assertNull(a.getSubClass());
        Assertions.assertFalse(a.hasSubClass());
    }

    @Test
    public void testCreateIndividual() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass a = m.createClass(NS + "A");
        Individual i = a.createIndividual(NS + "i");
        Assertions.assertTrue(i.hasRDFType(a));

        Individual j = a.createIndividual();
        Assertions.assertTrue(j.hasRDFType(a));
    }

    @Test
    public void testIsHierarchyRoot0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        Assertions.assertTrue(a.isHierarchyRoot());
        Assertions.assertFalse(b.isHierarchyRoot());
    }

    @Test
    public void testIsHierarchyRoot1() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        Assertions.assertTrue(a.isHierarchyRoot());
        Assertions.assertFalse(b.isHierarchyRoot());
    }

    @Test
    public void testIsHierarchyRoot2() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        Assertions.assertTrue(a.isHierarchyRoot());
        Assertions.assertFalse(b.isHierarchyRoot());
    }

    @Test
    public void testIsHierarchyRoot3() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_TRANS_INF);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        Assertions.assertTrue(a.isHierarchyRoot());
        Assertions.assertFalse(b.isHierarchyRoot());
    }

    @Test
    public void testIsHierarchyRoot4() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        Assertions.assertTrue(a.isHierarchyRoot());
        Assertions.assertFalse(b.isHierarchyRoot());
    }

    @Test
    public void testIsHierarchyRoot5() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_LITE_MEM);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        Assertions.assertTrue(a.isHierarchyRoot());
        Assertions.assertFalse(b.isHierarchyRoot());
    }

    @Test
    public void testIsHierarchyRoot8() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        Assertions.assertTrue(a.isHierarchyRoot());
        Assertions.assertFalse(b.isHierarchyRoot());
    }

    @Test
    public void testIsHierarchyRoot9() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF);
        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        a.addSubClass(b);
        Assertions.assertTrue(a.isHierarchyRoot());
        Assertions.assertFalse(b.isHierarchyRoot());
    }

    @Test
    public void testIsHierarchyRoot10() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass c1 = m.createClass(":C1");
        OntClass c2 = m.createClass(":C2");
        OntClass c3 = m.createClass(":C3");
        OntClass c4 = m.createClass(":C4");
        OntClass c5 = m.createClass(":C5");
        OntClass c6 = m.createClass(":C6");
        OntClass c7 = m.createClass(":C7");
        OntClass c8 = m.createClass(":C8");
        OntClass c9 = m.createClass(":C9");
        OntClass c10 = OWL.Thing.inModel(m).as(OntClass.class);
        OntClass c11 = OWL.Nothing.inModel(m).as(OntClass.class);

        c1.addSuperClass(c2);
        c2.addSuperClass(c3);
        c3.addSuperClass(c4);
        c5.addSuperClass(c6);
        c6.addSuperClass(c10);
        c7.addSuperClass(c8);
        c8.addSuperClass(c9);
        c9.addSuperClass(c7);

        Assertions.assertFalse(c1.isHierarchyRoot());   // false
        Assertions.assertFalse(c2.isHierarchyRoot());   // false
        Assertions.assertFalse(c3.isHierarchyRoot());   // false
        Assertions.assertTrue(c4.isHierarchyRoot());    // true
        Assertions.assertFalse(c5.isHierarchyRoot());   // false
        Assertions.assertTrue(c6.isHierarchyRoot());    // true
        Assertions.assertFalse(c7.isHierarchyRoot());   // false
        Assertions.assertFalse(c8.isHierarchyRoot());   // false
        Assertions.assertFalse(c9.isHierarchyRoot());   // false
        Assertions.assertTrue(c10.isHierarchyRoot());   // true
        Assertions.assertFalse(c11.isHierarchyRoot());  // false
    }

    @Test
    public void testListSubClasses0() {
        // no inference
        OntModel m = createABCDEFModel(OntModelSpec.OWL_MEM);
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");
        OntClass c = m.getOntClass(NS + "C");
        OntClass d = m.getOntClass(NS + "D");
        OntClass e = m.getOntClass(NS + "E");

        JunitExtensions.assertValues("", a.listSubClasses(), b, c);
        JunitExtensions.assertValues("", a.listSubClasses(false), b, c);
        JunitExtensions.assertValues("", a.listSubClasses(true), b, c);
        JunitExtensions.assertValues("", b.listSubClasses(true), d, e);
    }

    @Test
    public void testListSubClasses1() {
        // rule inference
        OntModel m = createABCDEFModel(OntModelSpec.OWL_MEM_RULE_INF);
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");
        OntClass c = m.getOntClass(NS + "C");
        OntClass d = m.getOntClass(NS + "D");
        OntClass e = m.getOntClass(NS + "E");
        OntClass f = m.getOntClass(NS + "F");

        JunitExtensions.assertValues("", a.listSubClasses(), b, c, d, e, f);
        JunitExtensions.assertValues("", a.listSubClasses(false), b, c, d, e, f);
        JunitExtensions.assertValues("", a.listSubClasses(true), b, c);
        JunitExtensions.assertValues("", b.listSubClasses(true), d, e);
    }

    @Test
    public void testListSubClasses2() {
        // micro rule inference
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

    @Test
    public void testListSubClasses3() {
        // no inference
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
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

    @Test
    public void testListSubClasses6() {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M

        OntModel m = createABCDEFGHKLMModel(ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM));

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

        System.out.println("DIRECT-A::" + directA); // C, B
        System.out.println("DIRECT-B::" + directB); // E, D
        System.out.println("DIRECT-C::" + directC); // F, E
        System.out.println("DIRECT-D::" + directD); // H, G
        System.out.println("DIRECT-E::" + directE); // ()
        System.out.println("DIRECT-F::" + directF); // ()
        System.out.println("DIRECT-G::" + directG); // ()
        System.out.println("DIRECT-H::" + directH); // ()
        System.out.println("DIRECT-K::" + directK); // M, L
        System.out.println("DIRECT-L::" + directL); // ()
        System.out.println("DIRECT-M::" + directM); // ()

        System.out.println("INDIRECT-A::" + indirectA); // C, B, D
        System.out.println("INDIRECT-B::" + indirectB); // E, D
        System.out.println("INDIRECT-C::" + indirectC); // F, E
        System.out.println("INDIRECT-D::" + indirectD); // H, G
        System.out.println("INDIRECT-E::" + indirectE); // ()
        System.out.println("INDIRECT-F::" + indirectF); // ()
        System.out.println("INDIRECT-G::" + indirectG); // ()
        System.out.println("INDIRECT-H::" + indirectH); // ()
        System.out.println("INDIRECT-K::" + indirectK); // M, L, H
        System.out.println("INDIRECT-L::" + indirectL); // ()
        System.out.println("INDIRECT-M::" + indirectM); // ()

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

    @Test
    public void testListSubClasses7() {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M

        OntModel m = createABCDEFGHKLMModel(ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF));

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

        System.out.println("DIRECT-A::" + directA); // B, C
        System.out.println("DIRECT-B::" + directB); // D, E
        System.out.println("DIRECT-C::" + directC); // E, F
        System.out.println("DIRECT-D::" + directD); // G, H, K
        System.out.println("DIRECT-E::" + directE); // ()
        System.out.println("DIRECT-F::" + directF); // ()
        System.out.println("DIRECT-G::" + directG); // ()
        System.out.println("DIRECT-H::" + directH); // L, M
        System.out.println("DIRECT-K::" + directK); // L, M
        System.out.println("DIRECT-L::" + directL); // ()
        System.out.println("DIRECT-M::" + directM); // ()

        System.out.println("INDIRECT-A::" + indirectA); // B, C, D, E, F, G, H, K, L, M
        System.out.println("INDIRECT-B::" + indirectB); // D, E, G, H, K, L, M
        System.out.println("INDIRECT-C::" + indirectC); // E, F
        System.out.println("INDIRECT-D::" + indirectD); // G, H, K, L, M
        System.out.println("INDIRECT-E::" + indirectE); // ()
        System.out.println("INDIRECT-F::" + indirectF); // ()
        System.out.println("INDIRECT-G::" + indirectG); // ()
        System.out.println("INDIRECT-H::" + indirectH); // L, M
        System.out.println("INDIRECT-K::" + indirectK); // H, L, M
        System.out.println("INDIRECT-L::" + indirectL); // ()
        System.out.println("INDIRECT-M::" + indirectM); // ()

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

    @Test
    public void testListSubClasses8() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        A.addSubClass(B);
        B.addEquivalentClass(C);

        Set<String> directA = A.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directB = B.listSubClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> directC = C.listSubClasses(true).mapWith(Resource::getLocalName).toSet();

        Set<String> indirectA = A.listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listSubClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listSubClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA); // B, C
        System.out.println("DIRECT-B::" + directB); // ()
        System.out.println("DIRECT-C::" + directC); // ()
        System.out.println("INDIRECT-A::" + indirectA); // B, C
        System.out.println("INDIRECT-B::" + indirectB); // C
        System.out.println("INDIRECT-C::" + indirectC); // B

        Assertions.assertEquals(Set.of("C", "B"), directA);
        Assertions.assertEquals(Set.of(), directB);
        Assertions.assertEquals(Set.of(), directC);

        Assertions.assertEquals(Set.of("B", "C"), indirectA);
        Assertions.assertEquals(Set.of("C"), indirectB);
        Assertions.assertEquals(Set.of("B"), indirectC);
    }

    @Test
    public void testListSuperClasses0() {
        // no inference
        OntModel m = createABCDEFModel(OntModelSpec.OWL_DL_MEM);
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");
        OntClass c = m.getOntClass(NS + "C");
        OntClass e = m.getOntClass(NS + "E");

        JunitExtensions.assertValues("", e.listSuperClasses(), b, c);
        JunitExtensions.assertValues("", e.listSuperClasses(false), b, c);
        JunitExtensions.assertValues("", e.listSuperClasses(true), b, c);
        JunitExtensions.assertValues("", b.listSuperClasses(true), a);
    }

    @Test
    public void testListSuperClasses1() {
        // rule inference
        OntModel m = createABCDEFModel(OntModelSpec.OWL_MEM_RULE_INF);
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");
        OntClass c = m.getOntClass(NS + "C");
        OntClass e = m.getOntClass(NS + "E");

        JunitExtensions.assertValues("", e.listSuperClasses(), b, c, a, RDFS.Resource, OWL.Thing);
        JunitExtensions.assertValues("", e.listSuperClasses(false), b, c, a, RDFS.Resource, OWL.Thing);
        JunitExtensions.assertValues("", e.listSuperClasses(true), b, c);
        JunitExtensions.assertValues("", b.listSuperClasses(true), a);
    }

    @Test
    public void testListSuperClasses2() {
        // micro rule inference
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

    @Test
    public void testListSuperClasses3() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        A.addSuperClass(B);
        A.addSuperClass(C);
        B.addSuperClass(C);
        C.addSuperClass(B);

        JunitExtensions.assertValues("", A.listSuperClasses(true), B, C);
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

    @Test
    public void testListInstances0() {
        // no inference
        OntModel m = createABCDEFModel(OntModelSpec.OWL_MEM);
        OntClass a = m.getOntClass(NS + "A");
        OntClass b = m.getOntClass(NS + "B");

        Individual ia = a.createIndividual();
        Individual ib = b.createIndividual();

        JunitExtensions.assertValues("", a.listInstances(), ia);
        JunitExtensions.assertValues("", b.listInstances(), ib);

        JunitExtensions.assertValues("", a.listInstances(true), ia);
        JunitExtensions.assertValues("", b.listInstances(true), ib);
    }

    @Test
    public void testListInstances1() {
        // no inference
        OntModel m = createABCDEFModel(OntModelSpec.OWL_MEM_RULE_INF);
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

    @Test
    public void testListInstances2() {
        // no inference
        OntModel m = createABCDEFModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF);
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

    @Test
    public void testDropIndividual() {
        OntModel m = createABCDEFModel(OntModelSpec.OWL_MEM);
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

    @Test
    public void testDatatypeIsClassOwlFull() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        Resource c = m.createResource();
        c.addProperty(RDF.type, RDFS.Datatype);
        Assertions.assertTrue(c.canAs(OntClass.class));
    }

    @Test
    public void testDatatypeIsClassOwlDL() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        Resource c = m.createResource();
        c.addProperty(RDF.type, RDFS.Datatype);
        Assertions.assertTrue(c.canAs(OntClass.class));
    }

    @Test
    public void testDatatypeIsClassOwlLite() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_LITE_MEM);
        Resource c = m.createResource();
        c.addProperty(RDF.type, RDFS.Datatype);
        Assertions.assertTrue(c.canAs(OntClass.class));
    }

    @Test
    public void testDatatypeIsClassOwlRDFS() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
        Resource c = m.createResource();
        c.addProperty(RDF.type, RDFS.Datatype);
        Assertions.assertTrue(c.canAs(OntClass.class));
    }

    @Test
    public void testOwlThingNothingClass() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

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
}
