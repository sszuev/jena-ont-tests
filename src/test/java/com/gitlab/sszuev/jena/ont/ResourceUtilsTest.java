package com.gitlab.sszuev.jena.ont;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.ResourceUtils;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResourceUtilsTest {

    public static final String NS = "http://jena.hp.com/test#";

    @Test
    public void testMaximalLowerElements() {
        Model m = ModelFactory.createDefaultModel();

        Resource a = m.createResource(NS + "a");
        Resource b = m.createResource(NS + "b");
        Resource c = m.createResource(NS + "c");
        Resource d = m.createResource(NS + "d");

        b.addProperty(RDFS.subClassOf, a);
        c.addProperty(RDFS.subClassOf, a);
        d.addProperty(RDFS.subClassOf, c);
        d.addProperty(RDFS.subClassOf, a);

        List<Resource> abcd = Arrays.asList(a, b, c, d);
        List<Resource> bcd = Arrays.asList(b, c, d);
        List<Resource> cd = Arrays.asList(c, d);

        List<Resource> abcdExpected = ResourceUtils.maximalLowerElements(abcd, RDFS.subClassOf, true); // a
        List<Resource> bcdExpected = ResourceUtils.maximalLowerElements(bcd, RDFS.subClassOf, true); // b, c
        List<Resource> cdExpected = ResourceUtils.maximalLowerElements(cd, RDFS.subClassOf, true); // c

        Assertions.assertEquals(1, abcdExpected.size(), "Wrong number of remaining resources");
        Assertions.assertEquals(a, abcdExpected.iterator().next(), "Result should be a");
        Assertions.assertEquals(2, bcdExpected.size(), "Wrong number of remaining resources");
        Assertions.assertEquals(1, cdExpected.size(), "Wrong number of remaining resources");
        Assertions.assertEquals(c, cdExpected.iterator().next(), "Result should be a");
    }

    @Test
    public void testRenameResource() {
        testRenameResource(ModelFactory.createDefaultModel());
    }

    private void testRenameResource(Model m) {
        Resource a = m.createResource(NS + "a");
        Resource b = m.createResource(NS + "b");
        Resource c = m.createResource(NS + "c");
        Resource d = m.createResource(NS + "d");

        Property p = m.createProperty(NS, "p");
        Property q = m.createProperty(NS, "q");

        a.addProperty(p, b);
        a.addProperty(q, c);
        d.addProperty(p, a);
        d.addProperty(p, b);

        // now rename a to e
        Resource e = ResourceUtils.renameResource(a, NS + "e");

        Assertions.assertFalse(a.listProperties().hasNext(), "should be no properties of a");
        Assertions.assertEquals(NS + "a", a.getURI(), "uri of a");
        Assertions.assertEquals(NS + "e", e.getURI(), "uri of e");

        Assertions.assertFalse(d.hasProperty(p, a), "d should not have p a");
        Assertions.assertTrue(d.hasProperty(p, e), "d should have p e");

        Assertions.assertTrue(e.hasProperty(p, b), "e should have p b");
        Assertions.assertTrue(e.hasProperty(q, c), "e should have q c");

        Assertions.assertTrue(d.hasProperty(p, b), "d p b should be unchanged");

        // now rename e to anon
        Resource anon = ResourceUtils.renameResource(e, null);

        Assertions.assertFalse(e.listProperties().hasNext(), "should be no properties of e");
        Assertions.assertEquals(NS + "e", e.getURI(), "uri of e");
        Assertions.assertTrue(anon.isAnon(), "anon");

        Assertions.assertFalse(d.hasProperty(p, e), "d should not have p e");
        Assertions.assertTrue(d.hasProperty(p, anon), "d should have p anon");

        Assertions.assertTrue(anon.hasProperty(p, b), "anon should have p b");
        Assertions.assertTrue(anon.hasProperty(q, c), "anon should have q c");

        Assertions.assertTrue(d.hasProperty(p, b), "d p b should be unchanged");

        // reflexive case
        Resource f = m.createResource(NS + "f");
        f.addProperty(p, f);

        Resource f1 = ResourceUtils.renameResource(f, NS + "f1");
        Assertions.assertFalse(m.listStatements(f, null, (RDFNode) null).hasNext(), "Should be no f statements");
        Assertions.assertTrue(f1.hasProperty(p, f1), "f1 has p f1");
    }

    @Test
    public void testReachableGraphClosure() {
        Model m0 = ModelFactory.createDefaultModel();
        Resource a = m0.createResource("a");
        Resource b = m0.createResource("b");
        Resource c = m0.createResource("c");
        Resource d = m0.createResource("d");
        Property p = m0.createProperty("p");

        m0.add(a, p, b);
        m0.add(a, p, c);
        m0.add(b, p, b);  // unit loop
        m0.add(b, p, a);  // loop
        m0.add(d, p, a);  // not reachable from a

        Model m1 = ModelFactory.createDefaultModel();
        m1.add(a, p, b);
        m1.add(a, p, c);
        m1.add(b, p, b);
        m1.add(b, p, a);

        Assertions.assertTrue(m1.isIsomorphicWith(ResourceUtils.reachableClosure(a)), "m1 should be isomorphic with the reachable sub-graph from a");
    }

    @Test
    public void testRemoveEquiv() {
        Model m = ModelFactory.createDefaultModel();

        Resource a = m.createResource(NS + "a");
        Resource b = m.createResource(NS + "b");
        Resource c = m.createResource(NS + "c");
        Resource d = m.createResource(NS + "d");
        Resource e = m.createResource(NS + "e");

        b.addProperty(RDFS.subClassOf, a);
        a.addProperty(RDFS.subClassOf, b);  // a,b are equivalent
        d.addProperty(RDFS.subClassOf, e);
        e.addProperty(RDFS.subClassOf, d);  // d,e are equivalent

        // reflexive relations - would be inferred by inf engine
        a.addProperty(RDFS.subClassOf, a);
        b.addProperty(RDFS.subClassOf, b);
        c.addProperty(RDFS.subClassOf, c);
        d.addProperty(RDFS.subClassOf, d);
        e.addProperty(RDFS.subClassOf, e);

        List<Resource> abcde = Arrays.asList(a, b, c, d, e);
        List<Resource> ab = Arrays.asList(a, b);
        List<Resource> cde = Arrays.asList(c, d, e);
        List<Resource> abde = Arrays.asList(a, b, d, e);
        List<Resource> de = Arrays.asList(d, e);

        List<Resource> in = new ArrayList<>(abcde);
        Assertions.assertEquals(in, abcde);
        Assertions.assertNotEquals(in, abde);
        Assertions.assertNotEquals(in, cde);

        List<Resource> out = ResourceUtils.removeEquiv(in, RDFS.subClassOf, a);

        Assertions.assertNotEquals(in, abcde);
        Assertions.assertNotEquals(in, abde);
        Assertions.assertEquals(in, cde);
        assertNotNull(out);
        Assertions.assertEquals(out, ab);

        out = ResourceUtils.removeEquiv(in, RDFS.subClassOf, e);

        Assertions.assertNotEquals(in, abcde);
        Assertions.assertNotEquals(in, abde);
        Assertions.assertEquals(in, Collections.singletonList(c));
        assertNotNull(out);
        Assertions.assertEquals(out, de);
    }

    @Test
    public void testPartition() {
        Model m = ModelFactory.createDefaultModel();

        Resource a = m.createResource(NS + "a");
        Resource b = m.createResource(NS + "b");
        Resource c = m.createResource(NS + "c");
        Resource d = m.createResource(NS + "d");
        Resource e = m.createResource(NS + "e");

        b.addProperty(RDFS.subClassOf, a);
        a.addProperty(RDFS.subClassOf, b);  // a,b are equivalent
        d.addProperty(RDFS.subClassOf, e);
        e.addProperty(RDFS.subClassOf, d);  // d,e are equivalent

        // reflexive relations - would be inferred by inf engine
        a.addProperty(RDFS.subClassOf, a);
        b.addProperty(RDFS.subClassOf, b);
        c.addProperty(RDFS.subClassOf, c);
        d.addProperty(RDFS.subClassOf, d);
        e.addProperty(RDFS.subClassOf, e);

        List<Resource> abcde = Arrays.asList(a, b, c, d, e);
        List<Resource> ab = Arrays.asList(b, a);
        List<Resource> cc = List.of(c);
        List<Resource> de = Arrays.asList(e, d);

        List<List<Resource>> partition = ResourceUtils.partition(abcde, RDFS.subClassOf);
        Assertions.assertEquals(3, partition.size(), "Should be 3 partitions");
        Assertions.assertEquals(ab, partition.get(0), "First partition should be (a,b)");
        Assertions.assertEquals(cc, partition.get(1), "First partition should be (c)");
        Assertions.assertEquals(de, partition.get(2), "First partition should be (d,e)");
    }

}
