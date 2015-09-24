import java.util.ArrayList;
import java.util.List;

/**
 * Created by L on 2014/12/29.
 */
public final class Node {

    private int centroidPoint[];
    private List<int[]> list = new ArrayList<int[]>();
    private Node leftChild;
    private Node rightChild;
    private int depth;

    public Node() {

    }

    public Node(int centroidPoint[], List<int[]> list) {
        this.centroidPoint = centroidPoint;
        this.list = list;
    }

    public void setTreeChild(Node leftChild, Node rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void setCentroidPoint(int centroidPoint[]) {
        this.centroidPoint = centroidPoint;
    }

    public void setList(List<int[]> list) {
        this.list = list;
    }

    public int[] getCentroidPoint() {
        return this.centroidPoint;
    }

    public Node getLeftChild() {
        return this.leftChild;
    }

    public Node getRightChild() {
        return this.rightChild;
    }

    public int getDepth() {
        return this.depth;
    }

    public List<int[]> getList() {
        return this.list;
    }

    public void clearList() {
        this.list = null;
    }
}
