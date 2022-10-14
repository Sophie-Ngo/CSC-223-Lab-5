package input.parser;

import input.builder.DefaultBuilder;
import input.builder.GeometryBuilder;
import input.components.ComponentNode;
import input.components.FigureNode;
import input.components.point.PointNode;
import input.components.point.PointNodeDatabase;
import input.components.segment.SegmentNodeDatabase;
import input.exception.ParseException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A JSONParser is used to create a FigureNode representation of any JSON file of this format:
 * Figure object that contains:
 * - a description
 * - points array
 * 		- each object in the array has a name, x, y
 * - segments array
 * 		- each object is an adjacency list for a point
 * 
 * This FigureNode stores this information in this format:
 * - String description
 * - PointNodeDatabase representing the points array
 * - SegmentNodeDatabase representing the segments array
 * 
 * @author brycenaddison
 */
public class JSONParser {
    protected ComponentNode _astRoot;

    /**
     * Create a JSONParser object that initializes the instance variables.
     */
    public JSONParser() {
        _astRoot = null;
    }

    private void error(String message) {
        throw new ParseException("Parse error: " + message);
    }

    /**
     * Parses a JSON file by creating a FigureNode representation of it. 
     * The JSON file must be constructed as follows:
     * Figure object that contains:
     * - description
     * - points array
     * 		- each object in the array has a name, x, y
     * - segments array
     * 		- each object is an adjacency list for a point
     * 
     * The result from the parsing is a FigureNode that stores the
     * description as a String, the points array as a PointNodeDatabase,
     * and the segments array as a SegmentNodeDatabase.
     * 
     * @param str - the JSON file
     * @return - a FigureNode representing the JSON file
     * @throws ParseException
     */
    public ComponentNode parse(String str, DefaultBuilder builder) throws ParseException {
        // Parsing is accomplished via the JSONTokenizer class.
        JSONTokener tokenizer = new JSONTokener(str);
        JSONObject root = new JSONObject(tokenizer);

        JSONObject figure = getFigure(root);

        String description = getDescription(figure);

        JSONArray points = getPoints(figure);

        PointNodeDatabase pointData = getPointNodeDatabase(points, builder);

        JSONArray segments = getSegments(figure);

        SegmentNodeDatabase segmentData = getSegmentNodeDatabase(segments, pointData, builder);

        return builder.buildFigureNode(description, pointData, segmentData);
    }

    /**
     * Creates a SegmentNodeDatabase that represents the adjacency lists of the segments.
     * @param segments - JSONArray of adjacency lists for each segment
     * @return SegmentNodeDatabase representing this array
     */
    private SegmentNodeDatabase getSegmentNodeDatabase(JSONArray segments, PointNodeDatabase points, DefaultBuilder builder) {
        SegmentNodeDatabase segmentData = builder.buildSegmentNodeDatabase();

        // loop through each adjacency list in segments array
        for (int i = 0; i < segments.length(); i++) {
            JSONObject segment = segments.getJSONObject(i);
            String segmentName = segment.keys().next();

            // get the point with the same name as SegmentName out of the given PointNodeDatabase
            PointNode from = getPoint(segmentName, points);

            // for each PointNode in the adjacency list, add a segment to our SegmentNodeDatabase
            for (PointNode to : getAdjacencyList(segment, points, segmentName)) {
                builder.addSegmentToDatabase(segmentData, from, to);
            }
        }
        return segmentData;
    }

    /**
     * Creates an adjacency list as an ArrayList from the specified adjacency list in 
     * the segments JSONArray.
     * @param segment - the specified adjacency list
     * @param points - PointNodeDatabase to search for points
     * @param segmentName - the name of the segment in the adjacency list
     * @return ArrayList adjacency list representing the same JSONArray adjacency list
     */
    private ArrayList<PointNode> getAdjacencyList(JSONObject segment, PointNodeDatabase points, String segmentName) {
        JSONArray adjArray = segment.getJSONArray(segmentName);
        ArrayList<PointNode> adjList = new ArrayList<>();

        for (int i = 0; i < adjArray.length(); i++) {
            adjList.add(getPoint(adjArray.getString(i), points));
        }
        return adjList;
    }

    /**
     * Creates a PointNodeDatabase that represents the array of points.
     * @param points - JSONArray of points
     * @param builder Builder to construct with
     * @return PointNodeDatabase representing this array
     */
    private PointNodeDatabase getPointNodeDatabase(JSONArray points, DefaultBuilder builder) {
        List<PointNode> list = buildPoints(points, builder);

        return builder.buildPointDatabaseNode(list);
    }

    /**
     * Creates a list of points from a JSONArray
     * @param points JSONArray representation of points to convert
     * @param builder Builder to construct points with
     * @return list of points
     */
    private List<PointNode> buildPoints(JSONArray points, DefaultBuilder builder) {
        List<PointNode> list = new ArrayList<>();

        for (int i = 0; i < points.length(); i++) {
            JSONObject point = points.getJSONObject(i);

            PointNode pointNode = getPointNode(point, builder);

            list.add(pointNode);
        }

        return list;
    }

    /**
     * Creates a PointNode from a JSONObject that represents the point.
     * @param point - JSONObject representing the point
     * @param builder Builder to construct point with
     * @return PointNode
     */
    private PointNode getPointNode(JSONObject point, DefaultBuilder builder) {
        String name = getName(point);
        double x = getX(point);
        double y = getY(point);

        return builder.buildPointNode(name, x, y);
    }

    private JSONObject getFigure(JSONObject obj) {
        return getObject(JSON_Constants.JSON_FIGURE, obj);
    }

    private String getDescription(JSONObject obj) {
        return getString(JSON_Constants.JSON_DESCRIPTION, obj);
    }

    private JSONArray getPoints(JSONObject obj) {
        return getArray(JSON_Constants.JSON_POINT_S, obj);
    }

    private JSONArray getSegments(JSONObject obj) {
        return getArray(JSON_Constants.JSON_SEGMENTS, obj);
    }

    private String getName(JSONObject obj) {
        return getString(JSON_Constants.JSON_NAME, obj);
    }

    private double getX(JSONObject obj) {
        return getDouble(JSON_Constants.JSON_X, obj);
    }

    private double getY(JSONObject obj) {
        return getDouble(JSON_Constants.JSON_Y, obj);
    }

    private JSONObject getObject(String key, JSONObject obj) {
        try {
            return obj.getJSONObject(key);
        } catch (JSONException e) {
            error(String.format("Could not find JSONObject with key \"%s\"", key));
        }

        return null;
    }

    private String getString(String key, JSONObject obj) {
        try {
            return obj.getString(key);
        } catch (JSONException e) {
            error(String.format("Could not find string with key \"%s\"", key));
        }

        return null;
    }

    private JSONArray getArray(String key, JSONObject obj) {
        try {
            return obj.getJSONArray(key);
        } catch (JSONException e) {
            error(String.format("Could not find JSONArray with key \"%s\"", key));
        }

        return null;
    }

    private double getDouble(String key, JSONObject obj) {
        try {
            return obj.getDouble(key);
        } catch (JSONException e) {
            error(String.format("Could not find JSONArray with key \"%s\"", key));
        }

        return 0;
    }

    private PointNode getPoint(String name, PointNodeDatabase db) {
        if (db == null) return null;
        return db.getPoint(name);
    }
}