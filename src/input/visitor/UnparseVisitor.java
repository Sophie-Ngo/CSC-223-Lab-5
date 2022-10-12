package input.visitor;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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

		// Begin to build the string of the JSON file as follows:
		//	Figure:
		// 		{	
		//		Description: "whatever the description is",
		//		Points:
        sb.append("    ".repeat(level)).append("Figure\n");
        sb.append("    ".repeat(level)).append("{\n");
        sb.append("    ".repeat(level + 1)).append("Description: ").append(node.getDescription()).append("\n");
        sb.append("    ".repeat(level + 1)).append("Points:\n");
        
        // now, delegate the unparsing of the PointNodeDatabase to the visitPointNodeDatabase method. The result is appended to this StringBuilder.
        node.getPointsDatabase().accept(this, new AbstractMap.SimpleEntry<StringBuilder, Integer>(sb, level + 1));

        sb.append("    ".repeat(level + 1)).append("Segments:\n");
        // again, delegate the unparsing of the SegmentNodeDatabase to the visitSegmentDatabaseNode method. The result is appended to this StringBuilder.
        node.getSegments().accept(this, new AbstractMap.SimpleEntry<StringBuilder, Integer>(sb, level + 1));

        sb.append("    ".repeat(level)).append("}\n");
        // result is completed StringBuilder
        return pair.getKey();
	}

	/**
	 * Unparses a SegmentNodeDatabase by unparsing its adjacency list.
	 */
	@Override
	public Object visitSegmentDatabaseNode(SegmentNodeDatabase node, Object o)
	{
		@SuppressWarnings("unchecked")
		AbstractMap.SimpleEntry<StringBuilder, Integer> pair = (AbstractMap.SimpleEntry<StringBuilder, Integer>)(o);
		StringBuilder sb = pair.getKey();
		int level = pair.getValue();
		
        sb.append("    ".repeat(level)).append("{\n");

        for (Entry<PointNode, Set<PointNode>> entry : node.uniqueEntrySet())
        {
        	PointNode a = entry.getKey();
        	sb.append("    ".repeat(level + 1)).append(a.getName()).append(" :");
        	
            for (PointNode b : entry.getValue()) {
                sb.append(" ").append(b.getName());
            }

            sb.append("\n");
        }
     

        sb.append("    ".repeat(level)).append("}\n");
    
		
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

	/**
	 * Unparses a PointNodeDatabase by unparsing all of the points that it contains.
	 * It appends all of this to a StringBuilder.
	 * @param node - PointNodeDatabase to unparse
	 * @param o - object that should be a SimpleEntry containing the StringBuilder and indentation level
	 * @return null, because all modifications are done to the StringBuilder
	 */
	@Override
	public Object visitPointNodeDatabase(PointNodeDatabase node, Object o)
	{
		@SuppressWarnings("unchecked")
		AbstractMap.SimpleEntry<StringBuilder, Integer> pair = (AbstractMap.SimpleEntry<StringBuilder, Integer>)(o);
		StringBuilder sb = pair.getKey();
		int level = pair.getValue();
		
		sb.append("    ".repeat(level)).append("{\n");

        for (PointNode p : node.getPoints()) {
            p.accept(this, new AbstractMap.SimpleEntry<StringBuilder, Integer>(sb, level + 1));
        }
  
        sb.append("    ".repeat(level)).append("}\n");
		
        return null;
	}
	
	/**
	 * Unparses a PointNode by appending to a StringBuilder.
	 * @param node - PointNode to unparse
	 * @param o - object that should be a SimpleEntry containing the StringBuilder and indentation level
	 * @return null, because all modifications are done to the StringBuilder
	 */
	@Override
	public Object visitPointNode(PointNode node, Object o)
	{
		@SuppressWarnings("unchecked")
		AbstractMap.SimpleEntry<StringBuilder, Integer> pair = (AbstractMap.SimpleEntry<StringBuilder, Integer>)(o);
		StringBuilder sb = pair.getKey();
		int level = pair.getValue();
		
		sb.append("    ".repeat(level)).append(node).append("\n");
		
		return null;
	}
}