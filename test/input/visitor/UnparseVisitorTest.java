package input.visitor;

import static org.junit.jupiter.api.Assertions.*;

import java.util.AbstractMap;

import org.junit.jupiter.api.Test;
import input.components.point.*;
import input.components.segment.*;
import input.components.FigureNode;

public class UnparseVisitorTest {
	@Test
	void test_visitPointNode()
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
	void test_visitPointDatabase()
	{
		UnparseVisitor visitor = new UnparseVisitor();
		StringBuilder sb = new StringBuilder();
		AbstractMap.SimpleEntry<StringBuilder, Integer> o = new AbstractMap.SimpleEntry<StringBuilder, Integer>(sb, 1);
		PointNodeDatabase pdb = new PointNodeDatabase();
		pdb.put(new PointNode(0.0, 0.0));
		pdb.put(new PointNode(1.0, 1.0));
		pdb.put(new PointNode(2.0, 2.0));
		
		visitor.visitPointNodeDatabase(pdb, o);
		
		String expected = "    " + "{\n" +
				"        " + new PointNode(0.0, 0.0) + "\n" +
				"        " + new PointNode(1.0, 1.0) + "\n" +
				"        " + new PointNode(2.0, 2.0) + "\n" +
				"    " + "}\n" ;
		String actual = sb.toString();
		
		assertEquals(expected, actual);
	}
	
	
	@Test
	void test_visitSegmentDatabase()
	{
		UnparseVisitor visitor = new UnparseVisitor();
		StringBuilder sb = new StringBuilder();
		AbstractMap.SimpleEntry<StringBuilder, Integer> o = new AbstractMap.SimpleEntry<StringBuilder, Integer>(sb, 1);
		SegmentNodeDatabase sdb = new SegmentNodeDatabase();
		sdb.addUndirectedEdge(new PointNode("A", 0.0, 0.0), new PointNode("B", 1.0, 1.0));
		
		visitor.visitSegmentDatabaseNode(sdb, o);
		
		String expected = "    " + "{\n" +
				"        " + "B : A" + "\n" +
				"    " + "}\n" ;
		String actual = sb.toString();
		assertEquals(expected, actual);
	}
	
	@Test
	void test_visitFigureNode()
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
		
		// verify by eye via console output
		System.out.println(visitor.visitFigureNode(node, o));
	}
}
