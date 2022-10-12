package input.components;

import input.components.point.PointNodeDatabase;
import input.components.segment.SegmentNodeDatabase;
import input.visitor.ComponentNodeVisitor;

/**
 * A basic figure consists of points, segments, and an optional description
 * <p>
 * Each figure has distinct points and segments (thus unique database objects).
 */
public class FigureNode implements ComponentNode {
    protected String _description;
    protected PointNodeDatabase _points;
    protected SegmentNodeDatabase _segments;

    public FigureNode(String description, PointNodeDatabase points, SegmentNodeDatabase segments) {
        _description = description;
        _points = points;
        _segments = segments;
    }

    public String getDescription() {
        return _description;
    }

    public PointNodeDatabase getPointsDatabase() {
        return _points;
    }

    public SegmentNodeDatabase getSegments() {
        return _segments;
    }

    @Override
    public Object accept(ComponentNodeVisitor visitor, Object o)
    {
    return visitor.visitFigureNode(this, o);
    }

//    @Override
//    public void unparse(StringBuilder sb, int level) {
//        sb.append("    ".repeat(level)).append("Figure\n");
//        sb.append("    ".repeat(level)).append("{\n");
//        sb.append("    ".repeat(level + 1)).append("Description: ").append(_description).append("\n");
//
//        sb.append("    ".repeat(level + 1)).append("Points:\n");
//        _points.unparse(sb, level + 1);
//
//        sb.append("    ".repeat(level + 1)).append("Segments:\n");
//        _segments.unparse(sb, level + 1);
//
//        sb.append("    ".repeat(level)).append("}\n");
//    }
}