package input.visitor;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

import input.components.*;
import input.components.point.*;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;

//
// A JSON file may contain:
//
//     Figure:
//       Points
//       Segments
//
public class UnparseVisitor implements ComponentNodeVisitor
{
	/**
	 * Unparses a FigureNode in the form of a StringBuilder that contains the completed
	 * parsed FigureNode.
	 * This therefore includes all of the unparsed objects that a FigureNode holds.
	 * So, the resulting StringBuilder also contains an unparsed PointNodeDatabase and
	 * SegmentNodeDatabase. 
	 */
	@Override
	public Object visitFigureNode(FigureNode node, Object o)
	{
		// Unpack the input object containing a Stringbuilder and an indentation level
		@SuppressWarnings("unchecked")
		AbstractMap.SimpleEntry<StringBuilder, Integer> pair = (AbstractMap.SimpleEntry<StringBuilder, Integer>)(o);
		StringBuilder sb = pair.getKey();
		int level = pair.getValue();

		// Begin to build the string of the JSON file 
		//	Figure:
		// 		{	
		//		Description: "whatever the description is",
		//		Points:
        sb.append("    ".repeat(level)).append("Figure\n");
        sb.append("    ".repeat(level)).append("{\n");
        sb.append("    ".repeat(level + 1)).append("Description: ").append(node.getDescription()).append("\n");
        sb.append("    ".repeat(level + 1)).append("Points:\n");
        
        // now, delegate the unparsing of the PointNodeDatabase to that class' visit method. The result is appended to this StringBuilder.
        node.getPointsDatabase().accept(this, new AbstractMap.SimpleEntry<StringBuilder, Integer>(sb, level + 1));

        sb.append("    ".repeat(level + 1)).append("Segments:\n");
        // again, delegate the unparsing of the SegmentNodeDatabase to that class' visit method. The result is appended to this StringBuilder.
        node.getSegments().accept(this, new AbstractMap.SimpleEntry<StringBuilder, Integer>(sb, level + 1));

        sb.append("    ".repeat(level)).append("}\n");
        // result is completed StringBuilder
        return pair.getKey();
	}

	/**
	 * 
	 */
	@Override
	public Object visitSegmentDatabaseNode(SegmentNodeDatabase node, Object o)
	{

        return null;
	}

	/**
	 * This method should NOT be called since the segment database
	 * uses the Adjacency list representation
	 */
	@Override
	public Object visitSegmentNode(SegmentNode node, Object o)
	{
		return null;
	}

	@Override
	public Object visitPointNodeDatabase(PointNodeDatabase node, Object o)
	{
        
		
        return null;
	}
	
	@Override
	public Object visitPointNode(PointNode node, Object o)
	{
        // TODO
        
        return null;
	}
}