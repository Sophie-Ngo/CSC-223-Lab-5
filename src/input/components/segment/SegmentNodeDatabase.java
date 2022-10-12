package input.components.segment;

import input.components.ComponentNode;
import input.components.point.PointNode;
import input.visitor.ComponentNodeVisitor;

import java.util.*;
import java.util.Map.Entry;

/**
 * Stores line segments as an adjacency list.
 *
 * @author brycenaddison
 * @date Wed Aug 31 2022
 */
public class SegmentNodeDatabase implements ComponentNode {
    protected Map<PointNode, Set<PointNode>> _adjLists;

    /**
     * Create a new empty SegmentNodeDatabase.
     */
    public SegmentNodeDatabase() {
        this._adjLists = new HashMap<>();
    }

    /**
     * Create a new SegmentNode database from a map of points and adjacency lists.
     *
     * @param adjLists A map of adjacency lists to create the database from
     */
    public SegmentNodeDatabase(Map<PointNode, Set<PointNode>> adjLists) {
        this._adjLists = adjLists;
    }

    /**
     * @return the number of edges in the SegmentNodeDatabase
     */
    public int numUndirectedEdges() {
        int count = 0;
        for (Set<PointNode> adjList : this._adjLists.values()) {
            count += adjList.size();
        }
        return count / 2;
    }

    /**
     * Add an edge going in one direction. Ex: For a segment AB, add the vector AB and
     * not the vector BA.
     *
     * @param a the key point
     * @param b the point to add to the key point's adjacency list
     */
    private void addDirectedEdge(PointNode a, PointNode b) {
        Set<PointNode> adjList = this._adjLists.computeIfAbsent(a, k -> new HashSet<>());
        adjList.add(b);
    }

    /**
     * Add an edge going in both directions. Ex: For a segment AB, add both the vectors
     * AB and BA.
     *
     * @param a the first point in the edge
     * @param b the other point in the edge
     */
    public void addUndirectedEdge(PointNode a, PointNode b) {
        this.addDirectedEdge(a, b);
        this.addDirectedEdge(b, a);
    }

    /**
     * Adds segments to the database based on an adjacency list
     *
     * @param p    a point to add
     * @param list a list of points to create segments with
     */
    public void addAdjacencyList(PointNode p, List<PointNode> list) {
        for (PointNode q : list) {
            this.addUndirectedEdge(p, q);
        }
    }

    /**
     * Returns a list of SegmentNode objects based on the connections in the
     * database. Segments in both directions are returned: say for a connection AB,
     * both SegmentNode AB and BA are returned in the list.
     *
     * @return a list of SegmentNode objects created from the adjacency lists in the
     * database.
     */
    public List<SegmentNode> asSegmentList() {
        ArrayList<SegmentNode> list = new ArrayList<>();
        for (Entry<PointNode, Set<PointNode>> entry : this._adjLists.entrySet()) {
            PointNode a = entry.getKey();
            for (PointNode b : entry.getValue()) {
                list.add(new SegmentNode(a, b));

            }
        }
        return list;
    }

    /**
     * Returns a list of SegmentNode objects based on the connections in the
     * database. Segments in one direction are returned: say for a connection AB,
     * only one of segment AB or BA are returned.
     *
     * @return a list of SegmentNode objects created from the adjacency lists in the
     * database.
     */
    public List<SegmentNode> asUniqueSegmentList() {
        Set<SegmentNode> set = new HashSet<>(this.asSegmentList());
        return new ArrayList<>(set);
    }

    /**
     * Accept the call to be visited by calling the 
     */
    @Override
    public Object accept(ComponentNodeVisitor visitor, Object o) {
    	return visitor.visitSegmentDatabaseNode(this, o);
    }
    
    /**
     * Generate an entry set of adjacency lists.
     */
    public Set<Entry<PointNode, Set<PointNode>>> entrySet() {
        return this._adjLists.entrySet();
    }

    public Set<Entry<PointNode, Set<PointNode>>> uniqueEntrySet() {
        SegmentNodeDatabase db  = new SegmentNodeDatabase();
        for (SegmentNode segment: this.asUniqueSegmentList()) {
            db.addDirectedEdge(segment.getPoint1(), segment.getPoint2());
        }
        return db.entrySet();
    }
    /**
     * TODO
     * 
     */
//    @Override
//    public void unparse(StringBuilder sb, int level) {
//        sb.append("    ".repeat(level)).append("{\n");
//
//        for (Entry<PointNode, Set<PointNode>> entry : this._adjLists.entrySet()) {
//
//            PointNode a = entry.getKey();
//            sb.append("    ".repeat(level + 1)).append(a.getName()).append(" :");
//
//            for (PointNode b : entry.getValue()) {
//                sb.append(" ").append(b.getName());
//            }
//
//            sb.append("\n");
//        }
//
//        sb.append("    ".repeat(level)).append("}\n");
//    }
}
