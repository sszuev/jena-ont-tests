package com.gitlab.sszuev.jena.ont.testutils;

import org.apache.jena.ontology.OntModelSpec;

/*
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
*/
public enum TestSpec {
    OWL_MEM(OntModelSpec.OWL_MEM),
    OWL_MEM_RULE_INF(OntModelSpec.OWL_MEM_RULE_INF),
    OWL_MEM_RDFS_INF(OntModelSpec.OWL_MEM_RDFS_INF),
    OWL_MEM_TRANS_INF(OntModelSpec.OWL_MEM_TRANS_INF),
    OWL_MEM_MICRO_RULE_INF(OntModelSpec.OWL_MEM_MICRO_RULE_INF),
    OWL_MEM_MINI_RULE_INF(OntModelSpec.OWL_MEM_MINI_RULE_INF),

    OWL_DL_MEM(OntModelSpec.OWL_DL_MEM),
    OWL_DL_MEM_RDFS_INF(OntModelSpec.OWL_DL_MEM_RDFS_INF),
    OWL_DL_MEM_RULE_INF(OntModelSpec.OWL_DL_MEM_RULE_INF),
    OWL_DL_MEM_TRANS_INF(OntModelSpec.OWL_DL_MEM_TRANS_INF),

    OWL_LITE_MEM(OntModelSpec.OWL_LITE_MEM),
    OWL_LITE_MEM_RDFS_INF(OntModelSpec.OWL_LITE_MEM_RDFS_INF),
    OWL_LITE_MEM_RULES_INF(OntModelSpec.OWL_LITE_MEM_RULES_INF),
    OWL_LITE_MEM_TRANS_INF(OntModelSpec.OWL_LITE_MEM_TRANS_INF),

    RDFS_MEM(OntModelSpec.RDFS_MEM),
    RDFS_MEM_RDFS_INF(OntModelSpec.RDFS_MEM_RDFS_INF),
    RDFS_MEM_TRANS_INF(OntModelSpec.RDFS_MEM_TRANS_INF),
    ;

    public final OntModelSpec inst;

    TestSpec(OntModelSpec inst) {
        this.inst = inst;
    }
}
