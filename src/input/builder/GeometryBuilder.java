package input.builder;

import input.components.FigureNode;
import input.components.point.PointNode;
import input.components.point.PointNodeDatabase;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;

import java.util.List;

/**
 * All object creation is delegated to this class. Each method of this class 
 * simply creates a new instance of the appropriate object, and any parameters required 
 * for construction are passed by the building method.  
 * @author brycenaddison
 * @created Wed Oct 12 2022
 */
public class GeometryBuilder extends DefaultBuilder {
    /**
     * @param description
     * @param points
     * @param segments
     * @return
     */
    @Override
    public FigureNode buildFigureNode(String description, PointNodeDatabase points, SegmentNodeDatabase segments) {
        return new FigureNode(description, points, segments);
    }

    /**
     * @return
     */
    @Override
    public SegmentNodeDatabase buildSegmentNodeDatabase() {
        return new SegmentNodeDatabase();
    }

    /**
     * @param pt1
     * @param pt2
     * @return
     */
    @Override
    public SegmentNode buildSegmentNode(PointNode pt1, PointNode pt2) {
        return new SegmentNode(pt1, pt2);
    }

    /**
     * @param points
     * @return
     */
    @Override
    public PointNodeDatabase buildPointDatabaseNode(List<PointNode> points) {
        return new PointNodeDatabase(points);
    }

    /**
     * @param name
     * @param x
     * @param y
     * @return
     */
    @Override
    public PointNode buildPointNode(String name, double x, double y) {
        return new PointNode(name, x, y);
    }
}
