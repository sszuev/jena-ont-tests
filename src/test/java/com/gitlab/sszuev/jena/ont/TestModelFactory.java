package com.gitlab.sszuev.jena.ont;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

class TestModelFactory {
    static final String NS = "http://example.com/test#";

    static OntModel createClassesABCD(OntModel m) {
        //    A
        //  / |
        // B  C
        //     \
        //      D
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        A.addSubClass(B);
        A.addSubClass(C);
        C.addSubClass(D);
        return m;
    }

    static OntModel createClassesABCDEF(OntModel m) {
        //      A
        //     / \
        //    B   C
        //   / \ / \
        //  D   E   F

        OntClass a = m.createClass(NS + "A");
        OntClass b = m.createClass(NS + "B");
        OntClass c = m.createClass(NS + "C");
        OntClass d = m.createClass(NS + "D");
        OntClass e = m.createClass(NS + "E");
        OntClass f = m.createClass(NS + "F");

        a.addSubClass(b);
        a.addSubClass(c);
        b.addSubClass(d);
        b.addSubClass(e);
        c.addSubClass(e);
        c.addSubClass(f);
        return m;
    }

    static OntModel createClassesABCDEFGHKLM(OntModel m) {
        //     A
        //   /  / \
        //  /  B   C
        //  | / \ / \
        //  D   E   F
        // / \
        // G  H = K
        //       / \
        //      L   M

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

    static OntModel createClassesDGCFKBEHAG(OntModel m) {
        // D        G
        // |      / .
        // C  F  K  .
        // |  |  |  .
        // B  E  H  .
        // |      \ .
        // A        G

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass F = m.createClass(NS + "F");
        OntClass G = m.createClass(NS + "G");
        OntClass H = m.createClass(NS + "H");
        OntClass K = m.createClass(NS + "K");

        A.addSuperClass(B);
        B.addSuperClass(C);
        C.addSuperClass(D);
        E.addSuperClass(F);
        G.addSuperClass(H);
        H.addSuperClass(K);
        K.addSuperClass(G);
        return m;
    }

    static OntModel createClassesABCA(OntModel m) {
        //    A
        //  / .
        // B  .
        // |  .
        // C  .
        //  \ .
        //    A

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");

        A.addSubClass(B);
        B.addSubClass(C);
        C.addSubClass(A);
        return m;
    }

    static OntModel createClassesDBFCEA(OntModel m) {
        //    D
        //  /  \
        // B    F
        // |    |
        // C    E
        //  \  /
        //    A

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass F = m.createClass(NS + "F");
        D.addSubClass(F);
        D.addSubClass(B);
        B.addSubClass(C);
        F.addSubClass(E);
        E.addSubClass(A);
        C.addSubClass(A);
        return m;
    }

    static OntModel createClassesAGBCFDE(OntModel m) {
        //      A   G
        //     / \   \
        //    B   C = F
        //   / \ /
        //  D   E

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass F = m.createClass(NS + "F");
        OntClass G = m.createClass(NS + "G");

        A.addSubClass(B);
        A.addSubClass(C);
        B.addSubClass(D);
        B.addSubClass(E);
        C.addSubClass(E);
        C.addSubClass(F);
        F.addSubClass(C);
        F.addSuperClass(G);
        return m;
    }

    static OntModel createClassesABCDAEB(OntModel m) {
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

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");

        B.addSuperClass(E);
        B.addSuperClass(A);

        A.addSuperClass(D);
        D.addSuperClass(C);
        C.addSuperClass(A);
        C.addSuperClass(B);

        return m;
    }

    static OntModel createClassesABCDEFBCF(OntModel m) {
        //      A       B
        //    /   \   / |
        //  /       C   |
        // |      / .   |
        // |    D   .   |
        // |  / |   .   |
        // E    |   .   |
        //   \  |   .   |
        //     F ...... F
        //       \  .
        //        \ .
        //          C

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass F = m.createClass(NS + "F");

        C.addSuperClass(F);
        C.addSuperClass(A);
        C.addSuperClass(B);

        F.addSuperClass(E);
        F.addSuperClass(D);
        F.addSuperClass(B);

        E.addSuperClass(A);
        E.addSuperClass(D);

        D.addSuperClass(C);

        return m;
    }

    static OntModel createClassesBCA(OntModel m) {
        // B = C
        //  \ |
        //    A

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");

        A.addSuperClass(B);
        A.addSuperClass(C);
        B.addSuperClass(C);
        C.addSuperClass(B);
        return m;
    }

    static OntModel createClassesABC(OntModel m) {
        //    A
        //  /  \
        // B  = C

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");

        B.addSuperClass(A);
        C.addSuperClass(A);
        B.addSuperClass(C);
        C.addSuperClass(B);
        return m;
    }

    static OntModel createClassesiAEDcCABiAE(OntModel m) {
        //         I_AE
        //         |  .
        //        D   .
        //       /    .
        // C_C  A     .
        //  \  / \    .
        //   B    \   .
        //     \  /   .
        //       I_A_E

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass complementOf_C = m.createComplementClass(null, C);
        OntClass intersectionOf_A_E = m.createIntersectionClass(null, m.createList(A, E));

        B.addSuperClass(complementOf_C);
        B.addSuperClass(A);
        D.addSuperClass(intersectionOf_A_E);
        A.addSuperClass(D);
        intersectionOf_A_E.addSuperClass(A);
        intersectionOf_A_E.addSuperClass(B);

        return m;
    }

    public static OntModel createClassesDBCA(OntModel m) {
        //     D
        //    | \
        // B  |  C
        //  \ | /
        //    A

        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");

        A.addSuperClass(D);
        A.addSuperClass(B);
        A.addSuperClass(C);
        C.addSuperClass(D);

        return m;
    }
}
