package input.visitor;

import org.junit.jupiter.api.Test;

import input.components.FigureNode;
import input.components.point.*;
import input.components.segment.SegmentNodeDatabase;
import input.parser.JSON_Constants;

import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author brycenaddison
 * @created Wed Oct 12 2022
 */
class ToJSONvisitorTest {
	
	protected static final String ANONYMOUS = "__UNNAMED";

    @Test
    void visitPointNode_withname()
    {
    	PointNode node = new PointNode("A", 0.0, 1.0);
    	ToJSONvisitor visitor = new ToJSONvisitor();

    	JSONObject expected = new JSONObject();
    	expected.put(JSON_Constants.JSON_NAME, "A");
    	expected.put(JSON_Constants.JSON_X, 0.0);
    	expected.put(JSON_Constants.JSON_Y, 1.0);
    	JSONObject actual = (JSONObject)visitor.visitPointNode(node, null);
    	
    	assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    void visitPointNode_withoutname()
    {
    	PointNode node = new PointNode(0.0, 1.0);
    	ToJSONvisitor visitor = new ToJSONvisitor();

    	JSONObject expected = new JSONObject();
    	expected.put(JSON_Constants.JSON_NAME, ANONYMOUS);
    	expected.put(JSON_Constants.JSON_X, 0.0);
    	expected.put(JSON_Constants.JSON_Y, 1.0);
    	JSONObject actual = (JSONObject)visitor.visitPointNode(node, null);
    	
    	assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    void visitPointNode_withfractions()
    {
    	PointNode node = new PointNode(0.867, 1.0);
    	ToJSONvisitor visitor = new ToJSONvisitor();

    	JSONObject expected = new JSONObject();
    	expected.put(JSON_Constants.JSON_NAME, ANONYMOUS);
    	expected.put(JSON_Constants.JSON_X, 0.867);
    	expected.put(JSON_Constants.JSON_Y, 1.0);
    	JSONObject actual = (JSONObject)visitor.visitPointNode(node, null);
    	
    	assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    void visitPointNodeDatabase_empty()
    {
    	PointNodeDatabase node = new PointNodeDatabase();
    	ToJSONvisitor visitor = new ToJSONvisitor();
    
    	JSONArray expected = new JSONArray();
    	JSONArray actual = (JSONArray)visitor.visitPointNodeDatabase(node, null);
    	
    	assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    void visitPointNodeDatabase_notempty()
    {
    	PointNodeDatabase node = new PointNodeDatabase();
    	node.put(new PointNode("A", 0.0, 1.0));
    	ToJSONvisitor visitor = new ToJSONvisitor();
    
    	JSONArray expected = new JSONArray();
    	JSONObject obj = new JSONObject();
        obj.put(JSON_Constants.JSON_NAME, "A");
        obj.put(JSON_Constants.JSON_X, 0.0);
        obj.put(JSON_Constants.JSON_Y, 1.0);
    	expected.put(obj);
    	JSONArray actual = (JSONArray)visitor.visitPointNodeDatabase(node, null);
    	
    	assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    void visitSegmentNodeDatabase_empty()
    {
    	SegmentNodeDatabase node = new SegmentNodeDatabase();
    	ToJSONvisitor visitor = new ToJSONvisitor();
    	
    	JSONArray expected = new JSONArray();
    	JSONArray actual = (JSONArray)visitor.visitSegmentDatabaseNode(node, null);

    	assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    void visitSegmentNodeDatabase_notempty()
    {
    	SegmentNodeDatabase node = new SegmentNodeDatabase();
    	node.addUndirectedEdge(new PointNode("A", 0.0, 1.0), new PointNode("B", 2.0, 3.0));
    	ToJSONvisitor visitor = new ToJSONvisitor();
    	
    	JSONArray expected = new JSONArray();
    	JSONObject obj = new JSONObject();
    	JSONArray temp = new JSONArray();
    	temp.put("A");
    	obj.put("B", temp);
    	expected.put(obj);
    	JSONArray actual = (JSONArray)visitor.visitSegmentDatabaseNode(node, null);

    	assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    void visitFigureNode_empty()
    {
    	FigureNode node = new FigureNode("desc", new PointNodeDatabase(), new SegmentNodeDatabase());
    	ToJSONvisitor visitor = new ToJSONvisitor();
    	
    	JSONObject expected = new JSONObject();
    	JSONObject temp = new JSONObject();
    	temp.put(JSON_Constants.JSON_DESCRIPTION, "desc");
    	temp.put(JSON_Constants.JSON_POINT_S, new JSONArray());
    	temp.put(JSON_Constants.JSON_SEGMENTS, new JSONArray());
    	expected.put(JSON_Constants.JSON_FIGURE, temp);
    	JSONObject actual = (JSONObject)visitor.visitFigureNode(node, null);
    	
    	assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    void visitFigureNode_notempty()
    {
    	PointNodeDatabase pdb = new PointNodeDatabase();
    	PointNode p1 = new PointNode("A", 0.0, 1.0);
    	PointNode p2 = new PointNode("B", 2.0, 3.0);
    	pdb.put(p1);
    	pdb.put(p2);
    	SegmentNodeDatabase sdb = new SegmentNodeDatabase();
    	sdb.addUndirectedEdge(p1, p2);
    	FigureNode node = new FigureNode("desc", pdb, sdb);
    	ToJSONvisitor visitor = new ToJSONvisitor();
    	
    	JSONObject expected = new JSONObject();
    	JSONObject temp = new JSONObject();
    	temp.put(JSON_Constants.JSON_DESCRIPTION, "desc");
    	temp.put(JSON_Constants.JSON_POINT_S, visitor.visitPointNodeDatabase(pdb, null));
    	temp.put(JSON_Constants.JSON_SEGMENTS, visitor.visitSegmentDatabaseNode(sdb, null));
    	expected.put(JSON_Constants.JSON_FIGURE, temp);
    	JSONObject actual = (JSONObject)visitor.visitFigureNode(node, null);
    	
    	assertEquals(expected.toString(), actual.toString());
    }
}