package example.metrics;

import java.util.ArrayList;

import example.DistanceMetric;

/**
 * Distance metric implementation for numeric values.
 * 
 * @author <a href="mailto:cf@christopherfrantz.org>Christopher Frantz</a>
 *
 */
public class DistanceMetricNumbers implements DistanceMetric<Number>{
	public double calculateDistance(ArrayList<Double> point,ArrayList<Double> line)
    {
        double Sum = 0.0;
        for(int i=0;i<line.size();i++) {
           Sum = Sum + Math.pow((line.get(i)-point.get(i)),2.0);
        }
        return Math.sqrt(Sum);
    }
}
