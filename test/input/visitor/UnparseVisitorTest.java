package input.visitor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.AbstractMap;

import org.junit.jupiter.api.Test;
import input.components.point.*;
import input.components.segment.*;
import input.components.FigureNode;

public class UnparseVisitorTest {
	@Test
	void test_visitPointNode_normal()
	{
		PointNode node = new PointNode("A", 1.0, 2.0);
		UnparseVisitor visitor = new UnparseVisitor();
		StringBuilder sb = new StringBuilder();
		AbstractMap.SimpleEntry<StringBuilder, Integer> o = new AbstractMap.SimpleEntry<StringBuilder, Integer>(sb, 1);
		
		visitor.visitPointNode(node, o);
		
		String expected = "    " + node + "\n";
		String actual = sb.toString();
		assertEquals(expected, actual);
	}
	
	@Test
	void test_visitFigureNode_normal()
	{
		// create all data of the FigureNode
		String desc = "A description.";
		PointNodeDatabase pdb = new PointNodeDatabase();
		PointNode pt1 = new PointNode("A", 0.0, 0.0);
		PointNode pt2 = new PointNode("B", 1.0, 1.0);
		pdb.put(pt1);
		pdb.put(pt2);
		SegmentNodeDatabase sdb = new SegmentNodeDatabase();
		sdb.addUndirectedEdge(pt1, pt2);
		
		// Create stuff for unparsing
		UnparseVisitor visitor = new UnparseVisitor();
		StringBuilder sb = new StringBuilder();
		AbstractMap.SimpleEntry<StringBuilder, Integer> o = new AbstractMap.SimpleEntry<StringBuilder, Integer>(sb, 1);
		FigureNode node = new FigureNode(desc, pdb, sdb);
		
		visitor.visitFigureNode(node, o);
		
		System.out.print(sb.toString());
		
	}
}
