package input.parser;


import input.builder.DefaultBuilder;
import input.builder.GeometryBuilder;
import input.components.ComponentNode;
import input.components.FigureNode;
import input.exception.ParseException;
import input.visitor.ComponentNodeVisitor;
import input.visitor.UnparseVisitor;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;

import static org.junit.jupiter.api.Assertions.*;

class JSONParserTest
{
	private static final DefaultBuilder DEFAULT_BUILDER = new DefaultBuilder();
	private static final DefaultBuilder GEOMETRY_BUILDER = new GeometryBuilder();

	static ComponentNode runFigureParseTest(String filename)
	{
		JSONParser parser = new JSONParser();

		String figureStr = utilities.io.FileUtilities.readFileFilterComments(filename);

		assertNull(parser.parse(figureStr, DEFAULT_BUILDER));
		return parser.parse(figureStr, GEOMETRY_BUILDER);
	}

	static StringBuilder unparse(ComponentNode node) {
		StringBuilder sb = new StringBuilder();
		ComponentNodeVisitor unparseVisitor = new UnparseVisitor();

		node.accept(unparseVisitor, new AbstractMap.SimpleEntry<>(sb, 0));

		return sb;
	}

	static void test(String filename) {
		ComponentNode node = JSONParserTest.runFigureParseTest(filename);

		assertTrue(node instanceof FigureNode);

		System.out.println(unparse(node));
	}
	
	@Test
	void empty_json_string_test()
	{
		JSONParser parser = new JSONParser();

		assertThrows(ParseException.class, () -> { parser.parse("{}", GEOMETRY_BUILDER); });
	}

	
	/**
	 *   /\
	 *  /__\ a triangle! 
	 */
	@Test
	void single_triangle_test()
	{
		test("single_triangle.json");
	}
	
	/**
	 * A----B-----C--D-----E----------F
	 */
	@Test
	void collinear_lines_test()
	{
		test("collinear_line_segments.json");
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
		test("crossing_symmetric_triangle.json");
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
		test("fully_connected_irregular_polygon.json");
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
		test("bowtie.json");
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
		test("bowtie_twist.json");
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
		test("filled_dart.json");
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
		test("pizza.json");
	}
	
	/**
	 *    A____B
	 */
	@Test
	void single_segment_test()
	{
		test("single_segment.json");
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
		test("square_tri.json");
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
		test("tri_snake.json");
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
		test("tri_with_segment.json");
	}
}

