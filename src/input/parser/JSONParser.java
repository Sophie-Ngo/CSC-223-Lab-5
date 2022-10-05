package input.parser;

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
    protected Map<String, PointNode> _pointMap;

    /**
     * Create a JSONParser object that initializes the instance variables.
     */
    public JSONParser() {
        _pointMap = new HashMap<>();
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
    public ComponentNode parse(String str) throws ParseException {
        // Parsing is accomplished via the JSONTokenizer class.
        JSONTokener tokenizer = new JSONTokener(str);
        JSONObject root = new JSONObject(tokenizer);

        JSONObject figure = getFigure(root);

        String description = getDescription(figure);

        JSONArray points = getPoints(figure);

        PointNodeDatabase pointData = getPointNodeDatabase(points);

        JSONArray segments = getSegments(figure);

        SegmentNodeDatabase segmentData = getSegmentNodeDatabase(segments);

        return new FigureNode(description, pointData, segmentData);
    }

    /**
     * Creates a SegmentNodeDatabase that represents the adjacency lists of the segments.
     * @param segments - JSONArray of adjacency lists for each segment
     * @return SegmentNodeDatabase representing this array
     */
    private SegmentNodeDatabase getSegmentNodeDatabase(JSONArray segments) {
        SegmentNodeDatabase segmentData = new SegmentNodeDatabase();

        for (int i = 0; i < segments.length(); i++) {
            JSONObject segment = segments.getJSONObject(i);
            String segmentName = segment.keys().next();
            ArrayList<PointNode> adjList = getAdjacencyList(segment, segmentName);

            segmentData.addAdjacencyList(getPoint(segmentName), adjList);
        }
        return segmentData;
    }

    /**
     * Creates an adjacency list as an ArrayList from the specified adjacency list in 
     * the segments JSONArray.
     * @param segment - the specified adjacency list
     * @param segmentName - the name of the segment in the adjacency list
     * @return ArrayList adjacency list representing the same JSONArray adjacency list
     */
    private ArrayList<PointNode> getAdjacencyList(JSONObject segment, String segmentName) {
        JSONArray adjArray = segment.getJSONArray(segmentName);
        ArrayList<PointNode> adjList = new ArrayList<>();

        for (int i = 0; i < adjArray.length(); i++) {
            adjList.add(getPoint(adjArray.getString(i)));
        }
        return adjList;
    }

    /**
     * Creates a PointNodeDatabase that represents the array of points.
     * @param points - JSONArray of points
     * @return PointNodeDatabase representing this array
     */
    private PointNodeDatabase getPointNodeDatabase(JSONArray points) {
        PointNodeDatabase pointData = new PointNodeDatabase();

        for (int i = 0; i < points.length(); i++) {
            JSONObject point = points.getJSONObject(i);
            PointNode pointNode = getPointNode(point);
            pointData.put(pointNode);
            _pointMap.put(pointNode.getName(), pointNode);
        }

        return pointData;
    }

    /**
     * Creates a PointNode from a JSONObject that represents the point.
     * @param point - JSONObject representing the point
     * @return PointNode
     */
    private PointNode getPointNode(JSONObject point) {
        String name = getName(point);
        double x = getX(point);
        double y = getY(point);

        return new PointNode(name, x, y);
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

    private PointNode getPoint(String name) {
        return _pointMap.get(name);
    }

}