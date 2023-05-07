package com.gitlab.sszuev.jena.ont.testutils;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.rdf.model.test.ModelTestBase;
import org.apache.jena.vocabulary.RDF;

import java.util.StringTokenizer;

public class ModelTestUtils {
    /**
     * Creates a Statement in a given Model with (S, P, O) extracted by parsing a string.
     *
     * @param m    the model the statement is attached to
     * @param fact "S P O" string.
     * @return m.createStatement(S, P, O)
     */
    public static Statement statement(final Model m, final String fact) {
        final StringTokenizer st = new StringTokenizer(fact);
        final Resource sub = ModelTestBase.resource(m, st.nextToken());
        final Property pred = ModelTestBase.property(m, st.nextToken());
        final RDFNode obj = ModelTestBase.rdfNode(m, st.nextToken());
        return m.createStatement(sub, pred, obj);
    }

    /* count the number of marker statements in the combined model */
    public static int countMarkers(Model m) {
        int count = 0;

        Resource marker = m.getResource("http://jena.hpl.hp.com/2003/03/testont#Marker");
        for (StmtIterator i = m.listStatements(null, RDF.type, marker); i.hasNext(); ) {
            count++;
            i.next();
        }

        return count;
    }
}
