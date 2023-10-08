package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.JunitExtensions;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Set;

import static com.gitlab.sszuev.jena.ont.TestModelFactory.NS;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDEF;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDEFGHKLM;

public class ClassIndividualsTest {

    @ParameterizedTest
    @EnumSource
    public void testCreateIndividual(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass a = m.createClass(NS + "A");
        Individual i = a.createIndividual(NS + "i");
        Assertions.assertTrue(i.hasRDFType(a));

        Individual j = a.createIndividual();
        Assertions.assertTrue(j.hasRDFType(a));
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
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));
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
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));
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
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));
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
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));
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

        OntModel m = createClassesABCDEFGHKLM(ModelFactory.createOntologyModel(spec.inst));
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
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
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
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
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
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));
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

}
