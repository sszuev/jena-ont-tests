package com.gitlab.sszuev.jena.ont;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;

class TestModelFactory {
    static final String NS = "http://example.com/test#";

    static OntModel createClassesABCDEF(OntModel m) {
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

    static OntModel createClassesABCDEFGHKLM(OntModel m) {
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

    static OntModel createClassesDGCFKBEHAG(OntModel m) {
        OntClass A = m.createClass(NS + "A");
        OntClass B = m.createClass(NS + "B");
        OntClass C = m.createClass(NS + "C");
        OntClass D = m.createClass(NS + "D");
        OntClass E = m.createClass(NS + "E");
        OntClass F = m.createClass(NS + "F");
        OntClass G = m.createClass(NS + "G");
        OntClass H = m.createClass(NS + "H");
        OntClass K = m.createClass(NS + "K");

        // D        G
        // |      / .
        // C  F  K  .
        // |  |  |  .
        // B  E  H  .
        // |      \ .
        // A        G

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

}
