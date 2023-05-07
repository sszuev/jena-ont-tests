package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.common.CommonOntTestBase;
import com.gitlab.sszuev.jena.ont.testutils.JunitExtensions;
import org.apache.jena.enhanced.EnhGraph;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.ontology.ProfileRegistry;
import org.apache.jena.ontology.Restriction;
import org.apache.jena.ontology.impl.OntClassImpl;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.reasoner.test.TestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ReasoningTest extends CommonOntTestBase {
    public static final String BASE = "http://jena.hpl.hp.com/testing/ontology";
    public static final String NS = BASE + "#";

    @Test
    public void testSubClassDirectTransInf1a() {
        OntModel m = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LITE_LANG);

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");

        A.addSubClass(B);
        A.addSubClass(C);
        C.addSubClass(D);

        iteratorTest(A.listSubClasses(), new Object[]{B, C, D});
        iteratorTest(A.listSubClasses(true), new Object[]{B, C});
    }

    @Test
    public void testSubClassDirectTransInf1b() {
        OntModel m = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LITE_LANG);

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");

        A.addSubClass(B);
        A.addSubClass(C);
        C.addSubClass(D);
        A.addSubClass(D);     // directly asserts a link that could be inferred

        iteratorTest(A.listSubClasses(), new Object[]{B, C, D});
        iteratorTest(A.listSubClasses(true), new Object[]{B, C});
    }

    @Test
    public void testSubClassDirectTransInf2a() {
        // test the code path for generating direct sc with no reasoner
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_LITE_MEM);
        spec.setReasonerFactory(null);
        OntModel m = ModelFactory.createOntologyModel(spec, null);

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");

        A.addSubClass(B);
        A.addSubClass(C);
        C.addSubClass(D);

        iteratorTest(A.listSubClasses(), new Object[]{B, C});
        iteratorTest(A.listSubClasses(true), new Object[]{B, C});
    }

    @Test
    public void testSubClassDirectTransInf2b() {
        // test the code path for generating direct sc with no reasoner
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_LITE_MEM);
        spec.setReasonerFactory(null);
        OntModel m = ModelFactory.createOntologyModel(spec, null);

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");

        A.addSubClass(B);
        A.addSubClass(C);
        C.addSubClass(D);
        A.addSubClass(D);     // directly asserts a link that could be inferred

        iteratorTest(A.listSubClasses(), new Object[]{B, C, D});
        iteratorTest(A.listSubClasses(true), new Object[]{B, C});
    }

    @Test
    public void testListSuperClassesDirect() {
        String ns = "http://example.org/test#";
        OntModel m0 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass c0 = m0.createClass(ns + "C0");
        OntClass c1 = m0.createClass(ns + "C1");
        OntClass c2 = m0.createClass(ns + "C2");
        OntClass c3 = m0.createClass(ns + "C3");

        c0.addSubClass(c1);
        c1.addSubClass(c2);
        c2.addEquivalentClass(c3);

        // now c1 is the direct super-class of c2, even allowing for the equiv with c3
        Assertions.assertFalse(c2.hasSuperClass(c0, true), "pass 1: c0 should not be a direct super of c2");
        Assertions.assertFalse(c2.hasSuperClass(c3, true), "pass 1: c3 should not be a direct super of c2");
        Assertions.assertFalse(c2.hasSuperClass(c2, true), "pass 1: c2 should not be a direct super of c2");
        Assertions.assertTrue(c2.hasSuperClass(c1, true), "pass 1: c1 should be a direct super of c2");

        // second pass - with inference
        m0 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF);
        c0 = m0.createClass(ns + "C0");
        c1 = m0.createClass(ns + "C1");
        c2 = m0.createClass(ns + "C2");
        c3 = m0.createClass(ns + "C3");

        c0.addSubClass(c1);
        c1.addSubClass(c2);
        c2.addEquivalentClass(c3);

        // now c1 is the direct super-class of c2, even allowing for the equiv with c3
        Assertions.assertFalse(c2.hasSuperClass(c0, true), "pass 2: c0 should not be a direct super of c2");
        Assertions.assertFalse(c2.hasSuperClass(c3, true), "pass 2: c3 should not be a direct super of c2");
        Assertions.assertFalse(c2.hasSuperClass(c2, true), "pass 2: c2 should not be a direct super of c2");
        Assertions.assertTrue(c2.hasSuperClass(c1, true), "pass 2: c1 should be a direct super of c2");
    }

    @Test
    public void testSubPropertyDirectTransInf1a() {
        OntModel m = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LITE_LANG);

        OntProperty p = m.createObjectProperty(NS + "p");
        OntProperty q = m.createObjectProperty(NS + "q");
        OntProperty r = m.createObjectProperty(NS + "r");
        OntProperty s = m.createObjectProperty(NS + "s");

        p.addSubProperty(q);
        p.addSubProperty(r);
        r.addSubProperty(s);

        iteratorTest(p.listSubProperties(), new Object[]{p, q, r, s});
        iteratorTest(p.listSubProperties(true), new Object[]{q, r});
    }

    @Test
    public void testSubPropertyDirectTransInf1b() {
        OntModel m = ModelFactory.createOntologyModel(ProfileRegistry.OWL_LITE_LANG);

        OntProperty p = m.createObjectProperty(NS + "p");
        OntProperty q = m.createObjectProperty(NS + "q");
        OntProperty r = m.createObjectProperty(NS + "r");
        OntProperty s = m.createObjectProperty(NS + "s");

        p.addSubProperty(q);
        p.addSubProperty(r);
        r.addSubProperty(s);
        p.addSubProperty(s);     // directly asserts a link that could be inferred

        iteratorTest(p.listSubProperties(), new Object[]{p, q, r, s});
        iteratorTest(p.listSubProperties(true), new Object[]{q, r});
    }

    @Test
    public void testSubPropertyDirectTransInf2a() {
        // test the code path for generating direct sc with no reasoner
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_LITE_MEM);
        spec.setReasonerFactory(null);
        OntModel m = ModelFactory.createOntologyModel(spec, null);

        OntProperty p = m.createObjectProperty(NS + "p");
        OntProperty q = m.createObjectProperty(NS + "q");
        OntProperty r = m.createObjectProperty(NS + "r");
        OntProperty s = m.createObjectProperty(NS + "s");

        p.addSubProperty(q);
        p.addSubProperty(r);
        r.addSubProperty(s);

        iteratorTest(p.listSubProperties(), new Object[]{q, r});
        iteratorTest(p.listSubProperties(true), new Object[]{q, r});
    }

    @Test
    public void testSubPropertyDirectTransInf2b() {
        // test the code path for generating direct sc with no reasoner
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_LITE_MEM);
        spec.setReasonerFactory(null);
        OntModel m = ModelFactory.createOntologyModel(spec, null);

        OntProperty p = m.createObjectProperty(NS + "p");
        OntProperty q = m.createObjectProperty(NS + "q");
        OntProperty r = m.createObjectProperty(NS + "r");
        OntProperty s = m.createObjectProperty(NS + "s");

        p.addSubProperty(q);
        p.addSubProperty(r);
        r.addSubProperty(s);
        p.addSubProperty(s);     // directly asserts a link that could be inferred

        iteratorTest(p.listSubProperties(), new Object[]{q, r, s});
        iteratorTest(p.listSubProperties(true), new Object[]{q, r});
    }

    @Test
    public void testListDeclaredProperties0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF, null);

        // a simple class hierarchy  organism -> vertebrate -> mammal -> dog
        OntClass organism = m.createClass(NS + "Organism");
        OntClass vertebrate = m.createClass(NS + "Vertebrate");
        OntClass mammal = m.createClass(NS + "Mammal");
        OntClass dog = m.createClass(NS + "Dog");

        organism.addSubClass(vertebrate);
        vertebrate.addSubClass(mammal);
        mammal.addSubClass(dog);

        // hair as a covering
        OntClass covering = m.createClass(NS + "Covering");
        Individual hair = m.createIndividual(NS + "hair", covering);

        // various properties
        DatatypeProperty limbsCount = m.createDatatypeProperty(NS + "limbsCount");
        DatatypeProperty hasCovering = m.createDatatypeProperty(NS + "hasCovering");
        DatatypeProperty numYoung = m.createDatatypeProperty(NS + "numYoung");

        // vertebrates have limbs, mammals have live young
        limbsCount.addDomain(vertebrate);
        numYoung.addDomain(mammal);

        // mammals have-covering = hair
        Restriction r = m.createRestriction(hasCovering);
        r.convertToHasValueRestriction(hair);
        mammal.addSuperClass(r);

        iteratorTest(organism.listDeclaredProperties(), new Object[]{hasCovering});
        iteratorTest(vertebrate.listDeclaredProperties(), new Object[]{limbsCount, hasCovering});
        iteratorTest(mammal.listDeclaredProperties(), new Object[]{limbsCount, hasCovering, numYoung});
        iteratorTest(dog.listDeclaredProperties(), new Object[]{limbsCount, hasCovering, numYoung});
        iteratorTest(r.listDeclaredProperties(), new Object[]{hasCovering});

        iteratorTest(organism.listDeclaredProperties(true), new Object[]{hasCovering});
        iteratorTest(vertebrate.listDeclaredProperties(true), new Object[]{limbsCount});
        iteratorTest(mammal.listDeclaredProperties(true), new Object[]{numYoung});
        iteratorTest(dog.listDeclaredProperties(true), new Object[]{});
        iteratorTest(r.listDeclaredProperties(true), new Object[]{hasCovering});

        iteratorTest(organism.listDeclaredProperties(false), new Object[]{hasCovering});
        iteratorTest(vertebrate.listDeclaredProperties(false), new Object[]{hasCovering, limbsCount});
        iteratorTest(mammal.listDeclaredProperties(false), new Object[]{hasCovering, numYoung, limbsCount});
        iteratorTest(dog.listDeclaredProperties(false), new Object[]{hasCovering, numYoung, limbsCount});
        iteratorTest(r.listDeclaredProperties(false), new Object[]{hasCovering});
    }

    /**
     * Test LDP with anonymous classes
     */
    @Test
    public void testListDeclaredProperties1() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntProperty p = m.createOntProperty(NS + "p");
        OntClass a = m.createClass(NS + "a");
        Restriction r = m.createMinCardinalityRestriction(null, p, 1);
        r.addSubClass(a);
        TestUtil.assertIteratorLength(a.listDeclaredProperties(), 1);
    }

    /**
     * Test LDP with resources in different sub-models
     */
    @Test
    public void testListDeclaredProperties2() {
        OntModel m0 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

        // in model M0, p0 has class c0 in the domain
        OntClass c0 = m0.createClass(NS + "c0");
        ObjectProperty p0 = m0.createObjectProperty(NS + "p0");
        p0.setDomain(c0);

        // in model M1, class c1 is a subClass of c0
        OntModel m1 = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        OntClass c1 = m1.createClass(NS + "c1");
        c1.addSuperClass(c0);

        // simulate imports
        m1.addSubModel(m0);

        // get a c0 reference from m1
        OntClass cc0 = m1.getOntClass(NS + "c0");
        Assertions.assertNotNull(cc0);

        JunitExtensions.assertValues("", c1.listDeclaredProperties(), p0);
        JunitExtensions.assertValues("", c0.listDeclaredProperties(false), p0);
        JunitExtensions.assertValues("", cc0.listDeclaredProperties(false), p0);
    }

    /**
     * Problem reported by Andy Seaborne - combine abox and tbox in RDFS with
     * ontmodel
     */
    @Test
    public void testRDFSAbox() {
        String sourceT =
                "<rdf:RDF "
                        + "    xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'"
                        + "    xmlns:rdfs='http://www.w3.org/2000/01/rdf-schema#'"
                        + "   xmlns:owl=\"http://www.w3.org/2002/07/owl#\">"
                        + "    <owl:Class rdf:about='http://example.org/foo#A'>"
                        + "   </owl:Class>"
                        + "</rdf:RDF>";

        String sourceA =
                "<rdf:RDF "
                        + "    xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'"
                        + "    xmlns:rdfs='http://www.w3.org/2000/01/rdf-schema#' "
                        + "   xmlns:owl=\"http://www.w3.org/2002/07/owl#\">"
                        + "    <rdf:Description rdf:about='http://example.org/foo#x'>"
                        + "    <rdf:type rdf:resource='http://example.org/foo#A' />"
                        + "   </rdf:Description>"
                        + "</rdf:RDF>";

        Model tBox = ModelFactory.createDefaultModel();
        tBox.read(new ByteArrayInputStream(sourceT.getBytes()), "http://example.org/foo");

        Model aBox = ModelFactory.createDefaultModel();
        aBox.read(new ByteArrayInputStream(sourceA.getBytes()), "http://example.org/foo");

        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(tBox);

        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM_RULE_INF);
        spec.setReasoner(reasoner);

        OntModel m = ModelFactory.createOntologyModel(spec, aBox);

        List<Resource> inds = new ArrayList<>();
        for (Iterator<Individual> i = m.listIndividuals(); i.hasNext(); ) {
            inds.add(i.next());
        }

        Assertions.assertTrue(inds.contains(m.getResource("http://example.org/foo#x")), "x should be an individual");

    }

    @Test
    public void testInvokeDirectClassReasoning() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_TRANS_INF, null);
        Resource a = m.createResource("http://example.org#A");
        Resource b = m.createResource("http://example.org#B");
        OntClass A = new OntClassImpl(a.asNode(), (EnhGraph) m) {
            @Override
            protected boolean hasSuperClassDirect(Resource cls) {
                throw new RuntimeException("did not find direct reasoner");
            }
        };

        // will throw an exception if the wrong code path is taken
        A.hasSuperClass(b, true);
    }

    @Test
    public void testListIndividualsWithReasoner() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
        OntClass C = m.createClass(NS + "C");
        Resource a = m.createResource(NS + "a", C);

        JunitExtensions.assertValues("", m.listIndividuals(), a);
    }

    /**
     * Bug report by kers - maximal lower elements calculation not correct in models
     * with no reasoner. Manifests as direct sub-class bug.
     */
    @Test
    public void testListSubClassesDirectNoReasoner() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM);
        OntClass r = m.createClass(NS + "r");
        OntClass a = m.createClass(NS + "a");
        OntClass b = m.createClass(NS + "b");
        OntClass c = m.createClass(NS + "c");
        OntClass d = m.createClass(NS + "d");
        OntClass e = m.createClass(NS + "e");
        OntClass f = m.createClass(NS + "f");
        OntClass g = m.createClass(NS + "g");

        g.addSuperClass(c);
        f.addSuperClass(c);
        e.addSuperClass(b);
        d.addSuperClass(b);
        c.addSuperClass(a);
        b.addSuperClass(a);

        // simulated closure
        r.addSubClass(a);
        r.addSubClass(b);
        r.addSubClass(c);
        r.addSubClass(d);
        r.addSubClass(e);
        r.addSubClass(f);
        r.addSubClass(g);

        JunitExtensions.assertValues("", r.listSubClasses(true), a);
    }


    @Test
    public void testOwlLiteClasses() {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_LITE_MEM_TRANS_INF);

        OntClass b = model.createClass(NS + "B");
        OntProperty p0 = model.createOntProperty(NS + "p0");
        ObjectProperty p1 = model.createObjectProperty(NS + "p1");
        DatatypeProperty p2 = model.createDatatypeProperty(NS + "p2");

        model.createIndividual(NS + "i0", b);

        model.setStrictMode(true);

        for (OntResource r : new OntResource[]{b, p0, p1, p2}) {
            Assertions.assertFalse(r.canAs(Individual.class), r + " should not be an individual");
        }
    }

    /**
     * Bugrep from Benson Margulies: see
     * <a href="https://issues.apache.org/jira/browse/JENA-21">JENA-21</a>
     */
    @Test
    public void testBM0() {
        OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RDFS_INF);
        // should not throw NPE:
        m.listStatements(null, null, (RDFNode) null, null);
    }


    // Internal implementation methods
    //////////////////////////////////

    /**
     * Test that an iterator delivers the expected values
     */
    protected void iteratorTest(Iterator<?> i, Object[] expected) {
        List<Object> expList = new ArrayList<>(Arrays.asList(expected));
        while (i.hasNext()) {
            Object next = i.next();
            Assertions.assertTrue(expList.contains(next), "Value " + next + " was not expected as a result from this iterator ");
            Assertions.assertTrue(expList.remove(next), "Value " + next + " was not removed from the list ");
        }
        Assertions.assertEquals(0, expList.size(), "There were expected elements from the iterator that were not found");
    }

}
