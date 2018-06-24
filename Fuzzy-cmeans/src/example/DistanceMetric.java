package example;

import java.util.ArrayList;

/**
 * Interface for the implementation of distance metrics.
 * 
 * @author <a href="mailto:cf@christopherfrantz.org>Christopher Frantz</a>
 *
 * @param <V> Value type to which distance metric is applied.
 */
public interface DistanceMetric<V> {

	public double calculateDistance(ArrayList<Double> val1, ArrayList<Double> arrayList) throws DBSCANClusteringException;

}
