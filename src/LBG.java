import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by L on 2014/12/29.
 */
public class LBG {

    private int codeBookSize;     // codebook size
    private int dimension;          // dimension size ex:2x2 => dimension=2
    private double epsilon;
    private List<int[]> trainList;      // all training list
    private List<int[]> codeBookList = new ArrayList<int[]>();
    private Map<int[], Integer> pointMap = new HashMap<int[], Integer>();   // store leaf point
    private Node root = new Node();     // tree root

    int count = 0;

    public LBG(List train, int codeBookSize, int dimension, double epsilon) {
        this.trainList = train;
        this.codeBookSize = codeBookSize;
        this.dimension = dimension;
        this.epsilon = epsilon;
    }

    // do LBG cluster
    public void init_LBG() {
        int depth = (int) (Math.log(this.codeBookSize) / Math.log(2));
        count = 0;
        this.root.setCentroidPoint(findCentroidPoint(this.trainList));
        this.root.setList(this.trainList);
        getCluster(this.root, depth);
    }

    public void getCluster(Node tree, int depth) {
        List<List<int[]>> tempList = new ArrayList<List<int[]>>();
        double mseOld = 1;
        double mseNew = 1;

        int centroidPoint1[] = tree.getCentroidPoint();
        int centroidPoint2[] = vectorPlusOne(centroidPoint1);
        tempList = findDistance(centroidPoint1, centroidPoint2, tree.getList());
        mseNew += getMSE(centroidPoint1, tempList.get(0));
        mseNew += getMSE(centroidPoint2, tempList.get(1));

        if (depth >= 0) {
            do {
                mseOld = mseNew;
                mseNew = 0;
                centroidPoint1 = findCentroidPoint(tempList.get(0));
                centroidPoint2 = findCentroidPoint(tempList.get(1));
                tempList = findDistance(centroidPoint1, centroidPoint2, tree.getList());
                mseNew += getMSE(centroidPoint1, tempList.get(0));
                mseNew += getMSE(centroidPoint2, tempList.get(1));
            } while (doRecursive(mseOld, mseNew));

            // add tree child
            Node leftChild = new Node(centroidPoint1, tempList.get(0));
            Node rightChild = new Node(centroidPoint2, tempList.get(1));
            depth--;
            leftChild.setDepth(depth);
            rightChild.setDepth(depth);
            tree.setTreeChild(leftChild, rightChild);
            getCluster(leftChild, depth);
            getCluster(rightChild, depth);
            if (depth == 0) {
                pointMap.put(centroidPoint1, count++);
                pointMap.put(centroidPoint2, count++);
                codeBookList.add(centroidPoint1);
                codeBookList.add(centroidPoint2);
            }
        }
    }


    // find centroid point
    public int[] findCentroidPoint(List<int[]> list) {

        int array[] = new int[this.dimension * this.dimension];
        for (int i = 0; i < array.length; i++) {
            double temp = 0;
            for (int j = 0; j < list.size(); j++) {
                array[i] += list.get(j)[i];
            }
            if (list.size() == 0) {
                array[i] = array[i];
            } else {
                array[i] = array[i] / list.size();
            }
        }
        return array;
    }

    //compare two centroid point by Euclidean distance
    public List<List<int[]>> findDistance(int[] center1, int[] center2, List<int[]> list) {

        List<List<int[]>> temp = new ArrayList<List<int[]>>();
        List<int[]> tempList1 = new ArrayList<int[]>();
        List<int[]> tempList2 = new ArrayList<int[]>();

        for (int i = 0; i < list.size(); i++) {
            int tempArr[] = list.get(i);
            double distance1 = 0;
            double distance2 = 0;
            for (int j = 0; j < tempArr.length; j++) {
                distance1 += Math.pow(Math.abs(center1[j] - tempArr[j]), 2);
                distance2 += Math.pow(Math.abs(center2[j] - tempArr[j]), 2);
                //System.out.println(temp[j]);
            }
            if (distance1 > distance2) {
                tempList2.add(tempArr);
            }
            if (distance2 > distance1) {
                tempList1.add(tempArr);
            }
            if (distance1 == distance2) {
                if (tempList1.size() > tempList2.size())
                    tempList2.add(tempArr);
                else
                    tempList1.add(tempArr);
            }
        }
        temp.add(tempList1);
        temp.add(tempList2);
        return temp;
    }

    public boolean doRecursive(double oldMSE, double newMSE) {
        double temp = Math.abs(newMSE - oldMSE) / oldMSE;
        System.out.println(temp);
        if (temp < epsilon || Double.isNaN(temp))
            return false;
        else
            return true;
    }

    // add one to vector
    public int[] vectorPlusOne(int temp[]) {
        int array[] = new int[temp.length];
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != 255) {
                array[i] = temp[i] + 1;
            }
        }
        return array;
    }

    // calculate mean square error
    public double getMSE(int centroidPoint[], List<int[]> list) {
        double mseValue = 0;
        for (int i = 0; i < list.size(); i++) {
            int temp[] = list.get(i);
            for (int j = 0; j < temp.length; j++) {
                mseValue += Math.pow((centroidPoint[j] - temp[j]), 2);
            }
        }
        return mseValue;
    }

    public Node getTree() {
        return this.root;
    }

    public List<int[]> getCodeBook() {
        return this.codeBookList;
    }

    public Map<int[], Integer> getLeafMap() {
        return this.pointMap;
    }
}
