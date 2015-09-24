import java.util.HashMap;
import java.util.Map;

/**
 * Created by L on 2015/1/3.
 */
public class Search {

    private int points[];
    private Node treeNode;
    private Map<int[], Integer> pointMap = new HashMap<int[], Integer>();

    // constructor
    public Search() {

    }

    public Search(Node treeNode, Map<int[], Integer> pointMap) {
        this.treeNode = treeNode;
        this.pointMap = pointMap;
    }

    // get tree's centroid point
    public int getPoints(int points[], int decision) {
        switch (decision) {
            // TSVQ
            case 1:
                TSVQ(this.treeNode, points);
                break;
            // full search
            case 2:
                double distance = 100000;
                for (Object key : this.pointMap.keySet()) {
                    int centroidP[] = (int[]) key;
                    if (getDistance(points, centroidP) < distance) {
                        distance = getDistance(points, centroidP);
                        this.points = centroidP;
                    }
                }
                break;

            case 3:
                // m==1
                TSVQ(this.treeNode, points);
                break;
        }
        return pointMap.get(this.points);
    }

    // recursive TSVQ
    public void TSVQ(Node root, int points[]) {
        if (getDecision(points, root.getLeftChild().getCentroidPoint(), root.getRightChild().getCentroidPoint())) {
            if (root.getLeftChild().getDepth() == 0) {
                this.points = root.getLeftChild().getCentroidPoint();
            } else {
                TSVQ(root.getLeftChild(), points);
            }
        } else {
            if (root.getRightChild().getDepth() == 0) {
                this.points = root.getRightChild().getCentroidPoint();
            } else {
                TSVQ(root.getRightChild(), points);
            }
        }
    }

    // get distance between points1 and points2
    public double getDistance(int points1[], int points2[]) {

        double distance = 0;
        for (int i = 0; i < points1.length; i++) {
            distance += Math.pow(Math.abs(points1[i] - points2[i]), 2);
        }
        return distance;
    }

    // determine which points belong to left or right
    public boolean getDecision(int leftPoints[], int rightPoints[], int points[]) {
        double leftDistance = 0;
        double rightDistance = 0;
        for (int i = 0; i < points.length; i++) {
            leftDistance += Math.pow(Math.abs(leftPoints[i] - points[i]), 2);
            rightDistance += Math.pow(Math.abs(rightPoints[i] - points[i]), 2);
        }
        if (leftDistance > rightDistance || leftDistance == rightDistance)
            return true;
        else
            return false;
    }
}
