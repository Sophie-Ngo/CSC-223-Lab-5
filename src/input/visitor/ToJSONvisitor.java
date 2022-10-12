package input.visitor;

import input.components.FigureNode;
import input.components.point.PointNode;
import input.components.point.PointNodeDatabase;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;
import input.parser.JSON_Constants;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * @author brycenaddison
 * @created Wed Oct 05 2022
 */
public class ToJSONvisitor implements ComponentNodeVisitor {
    /**
     * Returns a JSONObject representing a FigureNode
     */
    @Override
    public Object visitFigureNode(FigureNode node, Object o) {
        JSONObject figure = new JSONObject();

        figure.put(JSON_Constants.JSON_DESCRIPTION, node.getDescription());
        figure.put(JSON_Constants.JSON_POINT_S, visitPointNodeDatabase(node.getPointsDatabase(), null));
        figure.put(JSON_Constants.JSON_SEGMENTS, visitSegmentDatabaseNode(node.getSegments(), null));

        JSONObject obj = new JSONObject();

        obj.put("Figure", figure);

        return obj;
    }

    /**
     * Returns a JSONArray representing a SegmentNodeDatabase
     */
    @Override
    public Object visitSegmentDatabaseNode(SegmentNodeDatabase node, Object o) {
        JSONArray arr = new JSONArray();

        for (Map.Entry<PointNode, Set<PointNode>> entry: node.uniqueEntrySet()) {
            JSONObject obj = new JSONObject();

            JSONArray list = new JSONArray();

            for (PointNode point: entry.getValue()) {
                list.put(point.getName());
            }

            obj.put(entry.getKey().getName(), list);

            arr.put(obj);
        }

        return arr;
    }

    /**
     * Dummy implementation, not needed
     */
    @Override
    public Object visitSegmentNode(SegmentNode node, Object o) {
        return null;
    }

    /**
     * Returns a JSONObject representing a PointNode
     */
    @Override
    public Object visitPointNode(PointNode node, Object o) {
        JSONObject obj = new JSONObject();

        obj.put(JSON_Constants.JSON_X, node.getX());
        obj.put(JSON_Constants.JSON_Y, node.getY());
        obj.put(JSON_Constants.JSON_NAME, node.getName());

        return obj;
    }

    /**
     * Returns a JSONArray representing the PointNodeDatabase
     */
    @Override
    public Object visitPointNodeDatabase(PointNodeDatabase node, Object o) {
        JSONArray arr = new JSONArray();

        for (PointNode point: node.getPoints()) {
            arr.put(this.visitPointNode(point, null));
        }

        return arr;
    }
}
