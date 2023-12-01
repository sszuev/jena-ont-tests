package com.gitlab.sszuev.jena.ont;

import com.gitlab.sszuev.jena.ont.testutils.TestSpec;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.StringReader;
import java.util.List;
import java.util.Set;

public class OntModelHierarchyRootsTest {
    private static final String BASE = "http://www.test.com/test";
    private static final String NS = BASE + "#";

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
    public void testListHierarchyRoots0(TestSpec spec) {
        OntModel m = ModelFactory.createOntologyModel(spec.inst);
        Assertions.assertFalse(m.listHierarchyRootClasses().hasNext());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
    })
    public void testListHierarchyRoots1a(TestSpec spec) {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a owl:Class. ";

        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.read(new StringReader(doc), NS, "N3");
        Resource A = m.getResource(NS + "A");
        Assertions.assertEquals(List.of(A), m.listHierarchyRootClasses().toList());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListHierarchyRoots1b(TestSpec spec) {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a owl:Class. ";
        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.read(new StringReader(doc), NS, "N3");
        Assertions.assertEquals(List.of(), m.listHierarchyRootClasses().toList());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM",
            "OWL_MEM_RULE_INF",
            "OWL_MEM_RDFS_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM",
            "OWL_DL_MEM_RDFS_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM",
            "OWL_LITE_MEM_RDFS_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM_RDFS_INF",
    })
    public void testListHierarchyRoots2a(TestSpec spec) {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a owl:Class. "
                        + ":B a owl:Class ; rdfs:subClassOf :A . ";

        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.read(new StringReader(doc), NS, "N3");
        Resource A = m.getResource(NS + "A");
        Assertions.assertEquals(List.of(A), m.listHierarchyRootClasses().toList());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_TRANS_INF",
            "OWL_DL_MEM_TRANS_INF",
            "OWL_LITE_MEM_TRANS_INF",
            "RDFS_MEM",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListHierarchyRoots2b(TestSpec spec) {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a owl:Class. "
                        + ":B a owl:Class ; rdfs:subClassOf :A . ";

        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.read(new StringReader(doc), NS, "N3");
        Assertions.assertEquals(List.of(), m.listHierarchyRootClasses().toList());
    }

    @ParameterizedTest
    @EnumSource(names = {
            "OWL_MEM_RULE_INF",
            "OWL_MEM_MICRO_RULE_INF",
            "OWL_MEM_MINI_RULE_INF",
            "OWL_DL_MEM_RULE_INF",
            "OWL_LITE_MEM_RULES_INF",
            "RDFS_MEM",
            "RDFS_MEM_RDFS_INF",
            "RDFS_MEM_TRANS_INF",
    })
    public void testListHierarchyRoots4a(TestSpec spec) {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a rdfs:Class. "
                        + ":C a rdfs:Class. "
                        + ":B a rdfs:Class ; rdfs:subClassOf :A . ";

        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.read(new StringReader(doc), NS, "N3");

        Resource A = m.getResource(NS + "A");
        Resource C = m.getResource(NS + "C");
        Assertions.assertEquals(Set.of(A, C), m.listHierarchyRootClasses().toSet());
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
    public void testListHierarchyRoots4b(TestSpec spec) {
        String doc =
                "@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>. "
                        + "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>. "
                        + "@prefix xsd: <http://www.w3.org/2001/XMLSchema#>. "
                        + "@prefix owl: <http://www.w3.org/2002/07/owl#>. "
                        + "@prefix : <" + NS + ">. "
                        + ":A a rdfs:Class. "
                        + ":C a rdfs:Class. "
                        + ":B a rdfs:Class ; rdfs:subClassOf :A . ";

        OntModel m = ModelFactory.createOntologyModel(spec.inst, null);
        m.read(new StringReader(doc), NS, "N3");
        Assertions.assertEquals(Set.of(), m.listHierarchyRootClasses().toSet());
    }
}
