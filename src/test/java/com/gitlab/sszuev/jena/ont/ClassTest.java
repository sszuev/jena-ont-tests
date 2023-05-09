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
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass a = m.createClass(NS + "A");
        a.addSubClass(a);

        Assertions.assertTrue(a.listSubClasses(true).toList().isEmpty());
        Assertions.assertTrue(a.listSuperClasses(true).toList().isEmpty());
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

    protected OntModel createABCDEFModel(OntModelSpec spec) {
        return createABCDEFModel(ModelFactory.createOntologyModel(spec));
    }

    protected OntModel createABCDEFModel(OntModel m) {
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
}
