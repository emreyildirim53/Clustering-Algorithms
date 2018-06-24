package cartesian.coordinate;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

public class CCLabel {
    protected double x;
    protected double y;
    protected String label;
    protected Color color;
    

    public CCLabel(double x, double y, String label, Color color) {
        this.x = x;
        this.y = y;
        this.label = label;
        this.color=color;
    }
 
}