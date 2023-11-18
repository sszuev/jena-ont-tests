package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.JunitExtensions;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Set;
import java.util.stream.Stream;

import static com.gitlab.sszuev.jena.ont.TestModelFactory.NS;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDAEB;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDEF;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDEFGHKLM;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesBCA;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesDBCA;

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
        OntClass a = m.getResource(NS + "A").as(OntClass.class);
        OntClass b = m.getResource(NS + "B").as(OntClass.class);
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
        OntClass a = m.getResource(NS + "A").as(OntClass.class);
        OntClass b = m.getResource(NS + "B").as(OntClass.class);

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
        OntClass a = m.getResource(NS + "A").as(OntClass.class);
        OntClass b = m.getResource(NS + "B").as(OntClass.class);
        OntClass c = m.getResource(NS + "C").as(OntClass.class);
        OntClass d = m.getResource(NS + "D").as(OntClass.class);
        OntClass e = m.getResource(NS + "E").as(OntClass.class);

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
        OntClass a = m.getResource(NS + "A").as(OntClass.class);
        OntClass b = m.getResource(NS + "B").as(OntClass.class);
        OntClass c = m.getResource(NS + "C").as(OntClass.class);
        OntClass d = m.getResource(NS + "D").as(OntClass.class);
        OntClass e = m.getResource(NS + "E").as(OntClass.class);

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
    public void testListInstances3a(TestSpec spec) {
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
        Stream.of("A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M")
                .forEach(s -> m.createResource(NS + "i" + s, m.getResource(NS + s)));

        Set<String> directA = m.getResource(NS + "A").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getResource(NS + "A").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getResource(NS + "B").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getResource(NS + "B").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getResource(NS + "C").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getResource(NS + "C").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getResource(NS + "D").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getResource(NS + "D").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = m.getResource(NS + "E").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = m.getResource(NS + "E").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = m.getResource(NS + "F").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = m.getResource(NS + "F").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directG = m.getResource(NS + "G").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectG = m.getResource(NS + "G").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directH = m.getResource(NS + "H").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectH = m.getResource(NS + "H").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();

        Set<String> directK = m.getResource(NS + "K").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectK = m.getResource(NS + "K").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directL = m.getResource(NS + "L").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectL = m.getResource(NS + "L").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directM = m.getResource(NS + "M").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectM = m.getResource(NS + "M").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

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
            "OWL_MEM",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListInstances3b(TestSpec spec) {
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
        Stream.of("A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M")
                .forEach(s -> m.createResource(NS + "i" + s, m.getResource(NS + s)));

        Stream.of("A", "B", "C", "D", "E", "F", "G", "H", "K", "L", "M")
                .forEach(s -> {
                            Set<String> direct = m.getResource(NS + s).as(OntClass.class)
                                    .listInstances(true).mapWith(Resource::getLocalName).toSet();
                            Set<String> indirect = m.getResource(NS + s).as(OntClass.class)
                                    .listInstances(false).mapWith(Resource::getLocalName).toSet();
                            Assertions.assertEquals(Set.of("i" + s), direct);
                            Assertions.assertEquals(Set.of("i" + s), indirect);
                        }
                );

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
    public void testListInstances4a(TestSpec spec) {
        // B = C
        //  \ |
        //    A
        OntModel m = createClassesBCA(ModelFactory.createOntologyModel(spec.inst));

        m.createResource(NS + "iA", m.getResource(NS + "A"));
        m.createResource(NS + "iB", m.getResource(NS + "B"));
        m.createResource(NS + "iC", m.getResource(NS + "C"));

        Set<String> directA = m.getResource(NS + "A").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getResource(NS + "A").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getResource(NS + "B").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getResource(NS + "B").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getResource(NS + "C").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getResource(NS + "C").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

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
            "OWL_MEM",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListInstances4b(TestSpec spec) {
        // B = C
        //  \ |
        //    A
        OntModel m = createClassesBCA(ModelFactory.createOntologyModel(spec.inst));

        m.createResource(NS + "iA", m.getResource(NS + "A"));
        m.createResource(NS + "iB", m.getResource(NS + "B"));
        m.createResource(NS + "iC", m.getResource(NS + "C"));

        Set<String> directA = m.getResource(NS + "A").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getResource(NS + "A").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getResource(NS + "B").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getResource(NS + "B").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getResource(NS + "C").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getResource(NS + "C").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-A::" + directA);
        System.out.println("DIRECT-B::" + directB);
        System.out.println("DIRECT-C::" + directC);
        System.out.println("INDIRECT-A::" + indirectA);
        System.out.println("INDIRECT-B::" + indirectB);
        System.out.println("INDIRECT-C::" + indirectC);

        Assertions.assertEquals(Set.of("iA"), directA);
        Assertions.assertEquals(Set.of("iB"), directB);
        Assertions.assertEquals(Set.of("iC"), directC);
        Assertions.assertEquals(Set.of("iA"), indirectA);
        Assertions.assertEquals(Set.of("iB"), indirectB);
        Assertions.assertEquals(Set.of("iC"), indirectC);
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
    public void testListInstances5a(TestSpec spec) {
        //     D
        //    | \
        // B  |  C
        //  \ | /
        //    A
        OntModel m = createClassesDBCA(ModelFactory.createOntologyModel(spec.inst));

        m.createResource(NS + "iA", m.getResource(NS + "A"));
        m.createResource(NS + "iB", m.getResource(NS + "B"));
        m.createResource(NS + "iC", m.getResource(NS + "C"));
        m.createResource(NS + "iD", m.getResource(NS + "D"));

        Set<String> directA = m.getResource(NS + "A").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getResource(NS + "A").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getResource(NS + "B").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getResource(NS + "B").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getResource(NS + "C").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getResource(NS + "C").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getResource(NS + "D").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getResource(NS + "D").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

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
            "OWL_MEM",
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListInstances5b(TestSpec spec) {
        //     D
        //    | \
        // B  |  C
        //  \ | /
        //    A
        OntModel m = createClassesDBCA(ModelFactory.createOntologyModel(spec.inst));

        m.createResource(NS + "iA", m.getResource(NS + "A"));
        m.createResource(NS + "iB", m.getResource(NS + "B"));
        m.createResource(NS + "iC", m.getResource(NS + "C"));
        m.createResource(NS + "iD", m.getResource(NS + "D"));

        Set<String> directA = m.getResource(NS + "A").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = m.getResource(NS + "A").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = m.getResource(NS + "B").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = m.getResource(NS + "B").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = m.getResource(NS + "C").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = m.getResource(NS + "C").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = m.getResource(NS + "D").as(OntClass.class).listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = m.getResource(NS + "D").as(OntClass.class).listInstances(false).mapWith(Resource::getLocalName).toSet();

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
        Assertions.assertEquals(Set.of("iB"), indirectB);
        Assertions.assertEquals(Set.of("iC"), indirectC);
        Assertions.assertEquals(Set.of("iD"), indirectD);
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
    public void testListInstances6a(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);
        OntClass E = m.getResource(NS + "E").as(OntClass.class);
        OntClass F = m.getResource(NS + "F").as(OntClass.class);

        A.createIndividual(NS + "iA");
        B.createIndividual(NS + "iB");
        C.createIndividual(NS + "iC");
        D.createIndividual(NS + "iD");
        E.createIndividual(NS + "iE");

        Set<String> directA = A.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = D.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = E.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = F.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = F.listInstances(false).mapWith(Resource::getLocalName).toSet();

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
    public void testListInstances6b(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F
        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);
        OntClass E = m.getResource(NS + "E").as(OntClass.class);
        OntClass F = m.getResource(NS + "F").as(OntClass.class);

        A.createIndividual(NS + "iA");
        B.createIndividual(NS + "iB");
        C.createIndividual(NS + "iC");
        D.createIndividual(NS + "iD");
        E.createIndividual(NS + "iE");

        Set<String> directA = A.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = D.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = E.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = F.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = F.listInstances(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("iA"), directA);
        Assertions.assertEquals(Set.of("iB"), directB);
        Assertions.assertEquals(Set.of("iC"), directC);
        Assertions.assertEquals(Set.of("iD"), directD);
        Assertions.assertEquals(Set.of("iE"), directE);
        Assertions.assertEquals(Set.of(), directF);
        Assertions.assertEquals(Set.of("iA"), indirectA);
        Assertions.assertEquals(Set.of("iB"), indirectB);
        Assertions.assertEquals(Set.of("iC"), indirectC);
        Assertions.assertEquals(Set.of("iD"), indirectD);
        Assertions.assertEquals(Set.of("iE"), indirectE);
        Assertions.assertEquals(Set.of(), indirectF);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void testListInstances7a(TestSpec spec) {
        //  A   B
        //  .\ /.
        //  . C .
        //  . | .
        //  . D .
        //  ./    .
        //  A   .   E
        //   \  .  |
        //    \ . /
        //      B
        OntModel m = createClassesABCDAEB(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);
        OntClass E = m.getResource(NS + "E").as(OntClass.class);

        A.createIndividual(NS + "iA");
        B.createIndividual(NS + "iB");
        Individual CE = C.createIndividual(NS + "iCE");
        CE.addOntClass(E);
        Individual DBA = B.createIndividual(NS + "iDBA");
        DBA.addOntClass(B);
        DBA.addOntClass(A);

        Set<String> directA = A.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = D.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = E.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listInstances(false).mapWith(Resource::getLocalName).toSet();

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
        Assertions.assertEquals(Set.of("iB", "iDBA"), directB);
        Assertions.assertEquals(Set.of("iCE"), directC);
        Assertions.assertEquals(Set.of(), directD);
        Assertions.assertEquals(Set.of("iCE"), directE);
        Assertions.assertEquals(Set.of("iA", "iDBA"), indirectA);
        Assertions.assertEquals(Set.of("iB", "iDBA"), indirectB);
        Assertions.assertEquals(Set.of("iCE"), indirectC);
        Assertions.assertEquals(Set.of(), indirectD);
        Assertions.assertEquals(Set.of("iCE"), indirectE);
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
    public void testListInstances7b(TestSpec spec) {
        //  A   B
        //  .\ /.
        //  . C .
        //  . | .
        //  . D .
        //  ./    .
        //  A   .   E
        //   \  .  |
        //    \ . /
        //      B
        OntModel m = createClassesABCDAEB(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);
        OntClass E = m.getResource(NS + "E").as(OntClass.class);

        A.createIndividual(NS + "iA");
        B.createIndividual(NS + "iB");
        Individual CE = C.createIndividual(NS + "iCE");
        CE.addOntClass(E);
        Individual DBA = B.createIndividual(NS + "iDBA");
        DBA.addOntClass(B);
        DBA.addOntClass(A);

        Set<String> directA = A.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = D.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = E.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listInstances(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("iA", "iB", "iDBA", "iCE"), directA);
        Assertions.assertEquals(Set.of("iA", "iB", "iDBA", "iCE"), directB);
        Assertions.assertEquals(Set.of("iA", "iB", "iDBA", "iCE"), directC);
        Assertions.assertEquals(Set.of("iA", "iB", "iDBA", "iCE"), directD);
        Assertions.assertEquals(Set.of(), directE);
        Assertions.assertEquals(Set.of("iA", "iB", "iDBA", "iCE"), indirectA);
        Assertions.assertEquals(Set.of("iA", "iB", "iDBA", "iCE"), indirectB);
        Assertions.assertEquals(Set.of("iA", "iB", "iDBA", "iCE"), indirectC);
        Assertions.assertEquals(Set.of("iA", "iB", "iDBA", "iCE"), indirectD);
        Assertions.assertEquals(Set.of("iA", "iB", "iDBA", "iCE"), indirectE);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListInstances7c(TestSpec spec) {
        //  A   B
        //  .\ /.
        //  . C .
        //  . | .
        //  . D .
        //  ./    .
        //  A   .   E
        //   \  .  |
        //    \ . /
        //      B
        OntModel m = createClassesABCDAEB(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass C = m.getResource(NS + "C").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);
        OntClass E = m.getResource(NS + "E").as(OntClass.class);

        A.createIndividual(NS + "iA");
        B.createIndividual(NS + "iB");
        Individual CE = C.createIndividual(NS + "iCE");
        CE.addOntClass(E);
        Individual DBA = B.createIndividual(NS + "iDBA");
        DBA.addOntClass(B);
        DBA.addOntClass(A);

        Set<String> directA = A.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = A.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = B.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = B.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = C.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = C.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = D.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = D.listInstances(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = E.listInstances(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = E.listInstances(false).mapWith(Resource::getLocalName).toSet();

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

        Assertions.assertEquals(Set.of("iA", "iDBA"), directA);
        Assertions.assertEquals(Set.of("iB", "iDBA"), directB);
        Assertions.assertEquals(Set.of("iCE"), directC);
        Assertions.assertEquals(Set.of(), directD);
        Assertions.assertEquals(Set.of(), directE);
        Assertions.assertEquals(Set.of("iA", "iDBA"), indirectA);
        Assertions.assertEquals(Set.of("iB", "iDBA"), indirectB);
        Assertions.assertEquals(Set.of("iCE"), indirectC);
        Assertions.assertEquals(Set.of(), indirectD);
        Assertions.assertEquals(Set.of("iCE"), indirectE);
    }
}
