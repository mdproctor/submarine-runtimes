package org.drools.visualize;
/*
 * Copyright 2005 JBoss Inc
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;

public class ReteooLayoutSolver {

    private Vertex  root;

    private RowList rowList;

    public ReteooLayoutSolver(Vertex root) {
        this.root = root;
        solve();
    }

    protected void solve() {
        rowList = new RowList();

        rowList.add( 0,
                     root );

        int curRow = 0;

        Set seenVertices = new HashSet();
        seenVertices.add( root );

        while ( curRow < rowList.getDepth() ) {
            List rowVertices = rowList.get( curRow ).getVertices();

            for ( Iterator rowVertexIter = rowVertices.iterator(); rowVertexIter.hasNext(); ) {
                Vertex rowVertex = (Vertex) rowVertexIter.next();

                Set edges = rowVertex.getOutEdges();

                for ( Iterator edgeIter = edges.iterator(); edgeIter.hasNext(); ) {

                    Edge edge = (Edge) edgeIter.next();
                    Vertex destVertex = edge.getOpposite( rowVertex );

                    if ( !seenVertices.contains( destVertex ) ) {
                        rowList.add( curRow + 1,
                                     destVertex );
                        seenVertices.add( destVertex );
                    }
                }

                seenVertices.add( rowVertex );
            }

            ++curRow;
        }

        rowList.optimize();

        rowList.dump();
    }

    public RowList getRowList() {
        return rowList;
    }
}