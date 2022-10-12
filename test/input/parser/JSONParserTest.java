package input.parser;


import input.components.ComponentNode;
import input.components.FigureNode;
import input.exception.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JSONParserTest
{
	public static ComponentNode runFigureParseTest(String filename)
	{
		JSONParser parser = new JSONParser();

		String figureStr = utilities.io.FileUtilities.readFileFilterComments(filename);
		
		return parser.parse(figureStr);
	}
	
	@Test
	void empty_json_string_test()
	{
		JSONParser parser = new JSONParser();

		assertThrows(ParseException.class, () -> { parser.parse("{}"); });
	}

	/**
	 *   /\
	 *  /__\ a triangle! 
	 */
	@Test
	void single_triangle_test()
	{
		ComponentNode node = JSONParserTest.runFigureParseTest("single_triangle.json");

		assertTrue(node instanceof FigureNode);
		
		StringBuilder sb = new StringBuilder();
		node.unparse(sb, 0);
		System.out.println(sb);
	}
	
	/**
	 * A----B-----C--D-----E----------F
	 */
	@Test
	void collinear_lines_test()
	{
		ComponentNode node = JSONParserTest.runFigureParseTest("collinear_line_segments.json");

		assertTrue(node instanceof FigureNode);
		
		StringBuilder sb = new StringBuilder();
		node.unparse(sb, 0);
		System.out.println(sb);
	}
	
	/**
	  	     A                                 
		    / \                                
		   B___C                               
		  / \ / \                              
		 /   X   \  X is not a specified point (it is implied) 
		D_________E   
	 */
	@Test
	void crossing_symmetric_triangle_test()
	{
		ComponentNode node = JSONParserTest.runFigureParseTest("crossing_symmetric_triangle.json");

		assertTrue(node instanceof FigureNode);
		
		StringBuilder sb = new StringBuilder();
		node.unparse(sb, 0);
		System.out.println(sb);
	}
	
	/**
		  			D(3, 7)
		
		
		E(-2,4)
		           				C(6, 3)
		
			A(2,0)        B(4, 0)
		
		An irregular pentagon with 5 C 2 = 10 segments
	*/
	@Test
	void fully_connected_irregular_polygon_test()
	{
		ComponentNode node = JSONParserTest.runFigureParseTest("fully_connected_irregular_polygon.json");

		assertTrue(node instanceof FigureNode);
		
		StringBuilder sb = new StringBuilder();
		node.unparse(sb, 0);
		System.out.println(sb);
	}
	
	/**
	 * |\  /|
	 * | \/ | <- the intersection in the middle is a point
	 * | /\ |
	 * |/  \|
	 */
	@Test
	void bowtie_test()
	{
		ComponentNode node = JSONParserTest.runFigureParseTest("bowtie.json");

		assertTrue(node instanceof FigureNode);
		
		StringBuilder sb = new StringBuilder();
		node.unparse(sb, 0);
		System.out.println(sb);
	}
	
	/*
	 * |\  /|
	 * | \/ | <- the intersection in the middle is NOT a point
	 * | /\ |
	 * |/  \|
	 */
	@Test
	void bowtie_twist_test()
	{
		ComponentNode node = JSONParserTest.runFigureParseTest("bowtie_twist.json");

		assertTrue(node instanceof FigureNode);
		
		StringBuilder sb = new StringBuilder();
		node.unparse(sb, 0);
		System.out.println(sb);
	}
	
	/**
	 * 
	 * |\ \
	 * | \ \ 
	 * | / /
	 * |/ /
	 */
	@Test
	void filled_dart_test()
	{
		ComponentNode node = JSONParserTest.runFigureParseTest("filled_dart.json");

		assertTrue(node instanceof FigureNode);
		
		StringBuilder sb = new StringBuilder();
		node.unparse(sb, 0);
		System.out.println(sb);
	}
	
	/*
	 *    _____
	 *   /\   /\
	 *  /__\ /__\
	 *  \  / \  /
	 *   \/___\/
	 */
	@Test
	void pizza_test()
	{
		ComponentNode node = JSONParserTest.runFigureParseTest("pizza.json");

		assertTrue(node instanceof FigureNode);
		
		StringBuilder sb = new StringBuilder();
		node.unparse(sb, 0);
		System.out.println(sb);
	}
	
	/**
	 *    A____B
	 */
	@Test
	void single_segment_test()
	{
		ComponentNode node = JSONParserTest.runFigureParseTest("single_segment.json");

		assertTrue(node instanceof FigureNode);
		
		StringBuilder sb = new StringBuilder();
		node.unparse(sb, 0);
		System.out.println(sb);
	}
	
	/**
	 *    /\
	 *   /__\
	 *  |    |
	 *  |____|

	 */
	@Test
	void square_tri_test()
	{
		ComponentNode node = JSONParserTest.runFigureParseTest("square_tri.json");

		assertTrue(node instanceof FigureNode);
		
		StringBuilder sb = new StringBuilder();
		node.unparse(sb, 0);
		System.out.println(sb);
	}
	
	/**
	 * |\  /|
	 * | \/ |
	 * | /\ | /\
	 * |/  \|/__\
	 */
	@Test
	void tri_snake_test()
	{
		ComponentNode node = JSONParserTest.runFigureParseTest("tri_snake.json");

		assertTrue(node instanceof FigureNode);
		
		StringBuilder sb = new StringBuilder();
		node.unparse(sb, 0);
		System.out.println(sb);
	}
	
	/**
	 *   /\
	 *  /  \
	 * /____\___
	 * 
	 */
	@Test
	void tri_with_segment_test()
	{
		ComponentNode node = JSONParserTest.runFigureParseTest("tri_with_segment.json");

		assertTrue(node instanceof FigureNode);
		
		StringBuilder sb = new StringBuilder();
		node.unparse(sb, 0);
		System.out.println(sb);
	}
}

