package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.MiscUtils;
import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.gitlab.sszuev.jena.ont.TestModelFactory.NS;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDAEB;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesABCDEF;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesAGBCFDE;
import static com.gitlab.sszuev.jena.ont.TestModelFactory.createClassesiAEDcCABiAE;

public class IndividualClassesTest {

    @ParameterizedTest
    @EnumSource
    public void testGetOntClass0(TestSpec spec) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F

        OntModel m = createClassesABCDEF(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getOntClass(NS + "A");
        Individual iA = A.createIndividual("iA");
        Assertions.assertEquals(A, iA.getOntClass());
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
    public void testListOntClasses1a(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        // A [x]
        // |
        // B
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);

        Individual x = m.createIndividual(A);
        x.addRDFType(B);

        Assertions.assertEquals(Set.of(A, B), x.listOntClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListOntClasses1b(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);

        Individual x = m.createIndividual(A);
        x.addRDFType(B);

        Assertions.assertEquals(Set.of(A, B, OWL.Thing, RDFS.Resource), x.listOntClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_MICRO_RULE_INF",
    })
    public void testListOntClasses1c(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);

        Individual x = m.createIndividual(A);
        x.addRDFType(B);

        Assertions.assertEquals(Set.of(A, B, OWL.Thing), x.listOntClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "RDFS_MEM_RDFS_INF",
    })
    public void testListOntClasses1d(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);

        Individual x = m.createIndividual(A);
        x.addRDFType(B);

        Assertions.assertEquals(Set.of(A, B, RDFS.Resource), x.listOntClasses(false).toSet());
    }

    @ParameterizedTest
    @EnumSource
    public void testListOntClasses2(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        A.addSubClass(B);

        Individual x = m.createIndividual(A);
        x.addRDFType(B);

        Assertions.assertEquals(Set.of(B), x.listOntClasses(true).toSet());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM_RDFS_INF",
    })
    public void testListOntClasses3a(TestSpec spec) {
        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E
        OntModel m = createClassesAGBCFDE(ModelFactory.createOntologyModel(spec.inst));

        Individual iA = m.createIndividual("iA", m.getOntClass(NS + "A"));
        Individual iB = m.createIndividual("iB", m.getOntClass(NS + "B"));
        Individual iC = m.createIndividual("iC", m.getOntClass(NS + "C"));
        Individual iD = m.createIndividual("iD", m.getOntClass(NS + "D"));
        Individual iE = m.createIndividual("iE", m.getOntClass(NS + "E"));
        Individual iF = m.createIndividual("iF", m.getOntClass(NS + "F"));
        Individual iG = m.createIndividual("iG", m.getOntClass(NS + "G"));

        Set<String> directA = iA.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = iA.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = iB.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = iB.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = iC.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = iC.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = iD.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = iD.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = iE.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = iE.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = iF.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = iF.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directG = iG.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectG = iG.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-iA::" + directA);
        System.out.println("DIRECT-iB::" + directB);
        System.out.println("DIRECT-iC::" + directC);
        System.out.println("DIRECT-iD::" + directD);
        System.out.println("DIRECT-iE::" + directE);
        System.out.println("DIRECT-iF::" + directF);
        System.out.println("DIRECT-iG::" + directG);
        System.out.println("INDIRECT-iA::" + indirectA);
        System.out.println("INDIRECT-iB::" + indirectB);
        System.out.println("INDIRECT-iC::" + indirectC);
        System.out.println("INDIRECT-iD::" + indirectD);
        System.out.println("INDIRECT-iE::" + indirectE);
        System.out.println("INDIRECT-iF::" + indirectF);
        System.out.println("INDIRECT-iG::" + indirectG);

        Assertions.assertEquals(Set.of("A"), directA);
        Assertions.assertEquals(Set.of("B"), directB);
        Assertions.assertEquals(Set.of("C", "F"), directC);
        Assertions.assertEquals(Set.of("D"), directD);
        Assertions.assertEquals(Set.of("E"), directE);
        Assertions.assertEquals(Set.of("C", "F"), directF);
        Assertions.assertEquals(Set.of("G"), directG);

        Assertions.assertEquals(Set.of("A"), indirectA);
        Assertions.assertEquals(Set.of("A", "B"), indirectB);
        Assertions.assertEquals(Set.of("A", "C", "F", "G"), indirectC);
        Assertions.assertEquals(Set.of("A", "B", "D"), indirectD);
        Assertions.assertEquals(Set.of("A", "B", "C", "E", "F", "G"), indirectE);
        Assertions.assertEquals(Set.of("A", "C", "F", "G"), indirectF);
        Assertions.assertEquals(Set.of("G"), indirectG);
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
    public void testListOntClasses3b(TestSpec spec) {
        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E
        OntModel m = createClassesAGBCFDE(ModelFactory.createOntologyModel(spec.inst));

        Individual iA = m.createIndividual("iA", m.getOntClass(NS + "A"));
        Individual iB = m.createIndividual("iB", m.getOntClass(NS + "B"));
        Individual iC = m.createIndividual("iC", m.getOntClass(NS + "C"));
        Individual iD = m.createIndividual("iD", m.getOntClass(NS + "D"));
        Individual iE = m.createIndividual("iE", m.getOntClass(NS + "E"));
        Individual iF = m.createIndividual("iF", m.getOntClass(NS + "F"));
        Individual iG = m.createIndividual("iG", m.getOntClass(NS + "G"));

        Set<String> directA = iA.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = iA.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = iB.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = iB.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = iC.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = iC.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = iD.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = iD.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = iE.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = iE.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = iF.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = iF.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directG = iG.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectG = iG.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-iA::" + directA);
        System.out.println("DIRECT-iB::" + directB);
        System.out.println("DIRECT-iC::" + directC);
        System.out.println("DIRECT-iD::" + directD);
        System.out.println("DIRECT-iE::" + directE);
        System.out.println("DIRECT-iF::" + directF);
        System.out.println("DIRECT-iG::" + directG);
        System.out.println("INDIRECT-iA::" + indirectA);
        System.out.println("INDIRECT-iB::" + indirectB);
        System.out.println("INDIRECT-iC::" + indirectC);
        System.out.println("INDIRECT-iD::" + indirectD);
        System.out.println("INDIRECT-iE::" + indirectE);
        System.out.println("INDIRECT-iF::" + indirectF);
        System.out.println("INDIRECT-iG::" + indirectG);

        Assertions.assertEquals(Set.of("A"), directA);
        Assertions.assertEquals(Set.of("B"), directB);
        Assertions.assertEquals(Set.of("C"), directC);
        Assertions.assertEquals(Set.of("D"), directD);
        Assertions.assertEquals(Set.of("E"), directE);
        Assertions.assertEquals(Set.of("F"), directF);
        Assertions.assertEquals(Set.of("G"), directG);

        Assertions.assertEquals(Set.of("A"), indirectA);
        Assertions.assertEquals(Set.of("B"), indirectB);
        Assertions.assertEquals(Set.of("C"), indirectC);
        Assertions.assertEquals(Set.of("D"), indirectD);
        Assertions.assertEquals(Set.of("E"), indirectE);
        Assertions.assertEquals(Set.of("F"), indirectF);
        Assertions.assertEquals(Set.of("G"), indirectG);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListOntClasses3c(TestSpec spec) {
        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E
        OntModel m = createClassesAGBCFDE(ModelFactory.createOntologyModel(spec.inst));

        Individual iA = m.createIndividual("iA", m.getOntClass(NS + "A"));
        Individual iB = m.createIndividual("iB", m.getOntClass(NS + "B"));
        Individual iC = m.createIndividual("iC", m.getOntClass(NS + "C"));
        Individual iD = m.createIndividual("iD", m.getOntClass(NS + "D"));
        Individual iE = m.createIndividual("iE", m.getOntClass(NS + "E"));
        Individual iF = m.createIndividual("iF", m.getOntClass(NS + "F"));
        Individual iG = m.createIndividual("iG", m.getOntClass(NS + "G"));

        Set<String> directA = iA.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectA = iA.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directB = iB.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectB = iB.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directC = iC.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectC = iC.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directD = iD.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectD = iD.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directE = iE.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectE = iE.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directF = iF.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectF = iF.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directG = iG.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectG = iG.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        String R = spec.equals(TestSpec.OWL_MEM_MICRO_RULE_INF) ? null : "Resource";
        String T = spec.equals(TestSpec.RDFS_MEM_RDFS_INF) ? null : "Thing";

        Assertions.assertEquals(Set.of("A"), directA);
        Assertions.assertEquals(Set.of("B"), directB);
        Assertions.assertEquals(Set.of("C", "F"), directC);
        Assertions.assertEquals(Set.of("D"), directD);
        Assertions.assertEquals(Set.of("E"), directE);
        Assertions.assertEquals(Set.of("C", "F"), directF);
        Assertions.assertEquals(Set.of("G"), directG);

        Assertions.assertEquals(Stream.of("A", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectA);
        Assertions.assertEquals(Stream.of("A", "B", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectB);
        Assertions.assertEquals(Stream.of("A", "C", "F", "G", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectC);
        Assertions.assertEquals(Stream.of("A", "B", "D", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectD);
        Assertions.assertEquals(Stream.of("A", "B", "C", "E", "F", "G", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectE);
        Assertions.assertEquals(Stream.of("A", "C", "F", "G", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectF);
        Assertions.assertEquals(Stream.of("G", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectG);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM_RDFS_INF",
    })
    public void testListOntClasses4a(TestSpec spec) {
        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E
        OntModel m = createClassesAGBCFDE(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");
        OntClass G = m.getOntClass(NS + "G");

        Individual iAG = m.createIndividual("iA", A);
        iAG.addOntClass(G);

        Individual iBDC = m.createIndividual("iB", B);
        iBDC.addOntClass(D);
        iBDC.addOntClass(C);

        Individual iDE = m.createIndividual("iD", D);
        iDE.addOntClass(E);

        Set<String> directAG = iAG.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectAG = iAG.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directBDC = iBDC.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectBDC = iBDC.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directDE = iDE.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectDE = iDE.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-iAG::" + directAG);
        System.out.println("DIRECT-iBDC::" + directBDC);
        System.out.println("DIRECT-iDE::" + directDE);
        System.out.println("INDIRECT-iAG::" + indirectAG);
        System.out.println("INDIRECT-iBDC::" + indirectBDC);
        System.out.println("INDIRECT-iDE::" + indirectDE);

        Assertions.assertEquals(Set.of("A", "G"), directAG);
        Assertions.assertEquals(Set.of("C", "D", "F"), directBDC);
        Assertions.assertEquals(Set.of("D", "E"), directDE);

        Assertions.assertEquals(Set.of("A", "G"), indirectAG);
        Assertions.assertEquals(Set.of("A", "B", "C", "D", "F", "G"), indirectBDC);
        Assertions.assertEquals(Set.of("A", "B", "C", "D", "E", "F", "G"), indirectDE);
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
    public void testListOntClasses4b(TestSpec spec) {
        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E
        OntModel m = createClassesAGBCFDE(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");
        OntClass G = m.getOntClass(NS + "G");

        Individual iAG = m.createIndividual("iA", A);
        iAG.addOntClass(G);

        Individual iBDC = m.createIndividual("iB", B);
        iBDC.addOntClass(D);
        iBDC.addOntClass(C);

        Individual iDE = m.createIndividual("iD", D);
        iDE.addOntClass(E);

        Set<String> directAG = iAG.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectAG = iAG.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directBDC = iBDC.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectBDC = iBDC.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directDE = iDE.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectDE = iDE.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Assertions.assertEquals(Set.of("A", "G"), directAG);
        Assertions.assertEquals(Set.of("C", "D"), directBDC);
        Assertions.assertEquals(Set.of("D", "E"), directDE);

        Assertions.assertEquals(Set.of("A", "G"), indirectAG);
        Assertions.assertEquals(Set.of("B", "C", "D"), indirectBDC);
        Assertions.assertEquals(Set.of("D", "E"), indirectDE);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListOntClasses4c(TestSpec spec) {
        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E
        OntModel m = createClassesAGBCFDE(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass C = m.getOntClass(NS + "C");
        OntClass D = m.getOntClass(NS + "D");
        OntClass E = m.getOntClass(NS + "E");
        OntClass G = m.getOntClass(NS + "G");

        Individual iAG = m.createIndividual("iA", A);
        iAG.addOntClass(G);

        Individual iBDC = m.createIndividual("iB", B);
        iBDC.addOntClass(D);
        iBDC.addOntClass(C);

        Individual iDE = m.createIndividual("iD", D);
        iDE.addOntClass(E);

        Set<String> directAG = iAG.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectAG = iAG.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directBDC = iBDC.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectBDC = iBDC.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        Set<String> directDE = iDE.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectDE = iDE.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        String R = spec.equals(TestSpec.OWL_MEM_MICRO_RULE_INF) ? null : "Resource";
        String T = spec.equals(TestSpec.RDFS_MEM_RDFS_INF) ? null : "Thing";

        Assertions.assertEquals(Set.of("A", "G"), directAG);
        Assertions.assertEquals(Set.of("C", "D", "F"), directBDC);
        Assertions.assertEquals(Set.of("D", "E"), directDE);

        Assertions.assertEquals(Stream.of("A", "G", T, R).filter(Objects::nonNull).collect(Collectors.toSet()), indirectAG);
        Assertions.assertEquals(Stream.of("A", "B", "C", "D", "F", "G", T, R).filter(Objects::nonNull).collect(Collectors.toSet()), indirectBDC);
        Assertions.assertEquals(Stream.of("A", "B", "C", "D", "E", "F", "G", T, R).filter(Objects::nonNull).collect(Collectors.toSet()), indirectDE);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_LITE_MEM_RDFS_INF",
    })
    public void textListOntClasses5a(TestSpec spec) {
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

        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass D = m.getOntClass(NS + "D");

        Individual iAD = A.createIndividual("i");
        iAD.addOntClass(D);
        Individual iDB = D.createIndividual();
        iDB.addOntClass(B);

        Set<String> directDB = iDB.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectDB = iDB.listOntClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> directAD = iAD.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectAD = iAD.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-DB::" + directDB);
        System.out.println("INDIRECT-DB::" + indirectDB);
        System.out.println("DIRECT-AD::" + directAD);
        System.out.println("INDIRECT-AD::" + indirectAD);

        Assertions.assertEquals(Set.of("A", "B", "C", "D"), directDB);
        Assertions.assertEquals(Set.of("A", "B", "C", "D", "E"), indirectDB);
        Assertions.assertEquals(Set.of("A", "B", "C", "D"), directAD);
        Assertions.assertEquals(Set.of("A", "B", "C", "D", "E"), indirectAD);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
            "OWL_LITE_MEM",
            "RDFS_MEM",
    })
    public void textListOntClasses5b(TestSpec spec) {
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

        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass D = m.getOntClass(NS + "D");

        Individual iAD = A.createIndividual("i");
        iAD.addOntClass(D);
        Individual iDB = D.createIndividual();
        iDB.addOntClass(B);

        Set<String> directDB = iDB.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectDB = iDB.listOntClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> directAD = iAD.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectAD = iAD.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-DB::" + directDB);
        System.out.println("INDIRECT-DB::" + indirectDB);
        System.out.println("DIRECT-AD::" + directAD);
        System.out.println("INDIRECT-AD::" + indirectAD);

        Assertions.assertEquals(Set.of("B", "D"), directDB);
        Assertions.assertEquals(Set.of("B", "D"), indirectDB);
        Assertions.assertEquals(Set.of("A"), directAD);
        Assertions.assertEquals(Set.of("A", "D"), indirectAD);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void textListOntClasses5c(TestSpec spec) {
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

        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass D = m.getOntClass(NS + "D");

        Individual iAD = A.createIndividual("i");
        iAD.addOntClass(D);
        Individual iDB = D.createIndividual();
        iDB.addOntClass(B);

        Set<String> directDB = iDB.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectDB = iDB.listOntClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> directAD = iAD.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectAD = iAD.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-DB::" + directDB);
        System.out.println("INDIRECT-DB::" + indirectDB);
        System.out.println("DIRECT-AD::" + directAD);
        System.out.println("INDIRECT-AD::" + indirectAD);

        String R = null;
        String T = null;
        if (TestSpec.OWL_MEM_RULE_INF.equals(spec) ||
                TestSpec.OWL_DL_MEM_RULE_INF.equals(spec) ||
                TestSpec.OWL_MEM_MINI_RULE_INF.equals(spec) ||
                TestSpec.OWL_LITE_MEM_RULES_INF.equals(spec)) {
            R = "Resource";
            T = "Thing";
        } else if (TestSpec.OWL_MEM_MICRO_RULE_INF.equals(spec)) {
            T = "Thing";
        } else if (TestSpec.RDFS_MEM_RDFS_INF.equals(spec)) {
            R = "Resource";
        }

        Assertions.assertEquals(Stream.of("A", "B", "C", "D").collect(Collectors.toSet()), directDB);
        Assertions.assertEquals(Stream.of("A", "B", "C", "D", "E", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectDB);
        Assertions.assertEquals(Stream.of("A", "B", "C", "D").collect(Collectors.toSet()), directAD);
        Assertions.assertEquals(Stream.of("A", "B", "C", "D", "E", R, T).filter(Objects::nonNull).collect(Collectors.toSet()), indirectAD);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void textListOntClasses5d(TestSpec spec) {
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

        OntClass A = m.getOntClass(NS + "A");
        OntClass B = m.getOntClass(NS + "B");
        OntClass D = m.getOntClass(NS + "D");

        Individual iAD = A.createIndividual("i");
        iAD.addOntClass(D);
        Individual iDB = D.createIndividual();
        iDB.addOntClass(B);

        Set<String> directDB = iDB.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectDB = iDB.listOntClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> directAD = iAD.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectAD = iAD.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-DB::" + directDB);
        System.out.println("INDIRECT-DB::" + indirectDB);
        System.out.println("DIRECT-AD::" + directAD);
        System.out.println("INDIRECT-AD::" + indirectAD);

        Assertions.assertEquals(Set.of("B", "D"), directDB);
        Assertions.assertEquals(Set.of("B", "D"), indirectDB);
        Assertions.assertEquals(Set.of("A", "D"), directAD);
        Assertions.assertEquals(Set.of("A", "D"), indirectAD);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_DL_MEM",
    })
    public void testListOntClasses6a(TestSpec spec) {
        //         I_AE
        //         |  .
        //        D   .
        //       /    .
        // C_C  A     .
        //  \  / \    .
        //   B    \   .
        //     \  /   .
        //       I_A_E
        OntModel m = createClassesiAEDcCABiAE(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);

        Individual iAD = A.createIndividual("iAD");
        iAD.addOntClass(D);
        Individual iDB = D.createIndividual();
        iDB.addOntClass(B);

        Set<String> directDB = iDB.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectDB = iDB.listOntClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> directAD = iAD.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectAD = iAD.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-DB::" + directDB);
        System.out.println("INDIRECT-DB::" + indirectDB);
        System.out.println("DIRECT-AD::" + directAD);
        System.out.println("INDIRECT-AD::" + indirectAD);

        Assertions.assertEquals(Set.of("B", "D"), directDB);
        Assertions.assertEquals(Set.of("B", "D"), indirectDB);
        Assertions.assertEquals(Set.of("A"), directAD);
        Assertions.assertEquals(Set.of("A", "D"), indirectAD);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RDFS_INF",
            "OWL_DL_MEM_RDFS_INF",
    })
    public void testListOntClasses6b(TestSpec spec) {
        //         I_AE
        //         |  .
        //        D   .
        //       /    .
        // C_C  A     .
        //  \  / \    .
        //   B    \   .
        //     \  /   .
        //       I_A_E
        OntModel m = createClassesiAEDcCABiAE(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);

        Individual iAD = A.createIndividual("iAD");
        iAD.addOntClass(D);
        Individual iDB = D.createIndividual();
        iDB.addOntClass(B);

        Set<String> directDB = iDB.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectDB = iDB.listOntClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> directAD = iAD.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectAD = iAD.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-DB::" + directDB);
        System.out.println("INDIRECT-DB::" + indirectDB);
        System.out.println("DIRECT-AD::" + directAD);
        System.out.println("INDIRECT-AD::" + indirectAD);

        Assertions.assertEquals(MiscUtils.hashSetOf(null, "A", "B", "D"), directDB);
        Assertions.assertEquals(MiscUtils.hashSetOf(null, "A", "B", "D"), indirectDB);
        Assertions.assertEquals(MiscUtils.hashSetOf(null, "A", "B", "D"), directAD);
        Assertions.assertEquals(MiscUtils.hashSetOf(null, "A", "B", "D"), indirectAD);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
    })
    public void testListOntClasses6c(TestSpec spec) {
        //         I_AE
        //         |  .
        //        D   .
        //       /    .
        // C_C  A     .
        //  \  / \    .
        //   B    \   .
        //     \  /   .
        //       I_A_E
        OntModel m = createClassesiAEDcCABiAE(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);

        Individual iAD = A.createIndividual("iAD");
        iAD.addOntClass(D);
        Individual iDB = D.createIndividual();
        iDB.addOntClass(B);

        Set<String> directDB = iDB.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectDB = iDB.listOntClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> directAD = iAD.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectAD = iAD.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-DB::" + directDB);
        System.out.println("INDIRECT-DB::" + indirectDB);
        System.out.println("DIRECT-AD::" + directAD);
        System.out.println("INDIRECT-AD::" + indirectAD);

        Set<String> expectedDirect = MiscUtils.hashSetOf(null, "A", "B", "D");
        Set<String> expectedIndirect = spec == TestSpec.OWL_MEM_MICRO_RULE_INF ?
                MiscUtils.hashSetOf(null, "A", "B", "D", "E", "Thing") :
                MiscUtils.hashSetOf(null, "A", "B", "D", "E", "Resource", "Thing");
        Assertions.assertEquals(expectedDirect, directDB);
        Assertions.assertEquals(expectedIndirect, indirectDB);
        Assertions.assertEquals(expectedDirect, directAD);
        Assertions.assertEquals(expectedIndirect, indirectAD);
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
    })
    public void testListOntClasses6d(TestSpec spec) {
        //         I_AE
        //         |  .
        //        D   .
        //       /    .
        // C_C  A     .
        //  \  / \    .
        //   B    \   .
        //     \  /   .
        //       I_A_E
        OntModel m = createClassesiAEDcCABiAE(ModelFactory.createOntologyModel(spec.inst));
        OntClass A = m.getResource(NS + "A").as(OntClass.class);
        OntClass B = m.getResource(NS + "B").as(OntClass.class);
        OntClass D = m.getResource(NS + "D").as(OntClass.class);

        Individual iAD = A.createIndividual("iAD");
        iAD.addOntClass(D);
        Individual iDB = D.createIndividual();
        iDB.addOntClass(B);

        Set<String> directDB = iDB.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectDB = iDB.listOntClasses(false).mapWith(Resource::getLocalName).toSet();
        Set<String> directAD = iAD.listOntClasses(true).mapWith(Resource::getLocalName).toSet();
        Set<String> indirectAD = iAD.listOntClasses(false).mapWith(Resource::getLocalName).toSet();

        System.out.println("DIRECT-DB::" + directDB);
        System.out.println("INDIRECT-DB::" + indirectDB);
        System.out.println("DIRECT-AD::" + directAD);
        System.out.println("INDIRECT-AD::" + indirectAD);

        Assertions.assertEquals(MiscUtils.hashSetOf("B", "D"), directDB);
        Assertions.assertEquals(MiscUtils.hashSetOf("B", "D"), indirectDB);
        Assertions.assertEquals(MiscUtils.hashSetOf("A", "D"), directAD);
        Assertions.assertEquals(MiscUtils.hashSetOf("A", "D"), indirectAD);
    }
}