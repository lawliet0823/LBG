import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by L on 2014/12/14.
 */
public class Execution {

    private int codeBookSize = 128;
    private int dimension = 2;
    private double epsilon = 0.08;
    private int decisionSearch = 1;
    private int output;
    private String path;

    public Execution(int codeBookSize, int dimension, double epsilon, int decisionSearch, int output, String path) {
        this.codeBookSize = codeBookSize;
        this.dimension = dimension;
        this.epsilon = epsilon;
        this.decisionSearch = decisionSearch;
        this.output = output;
        this.path = path;
    }

    public void encode() throws Exception {

        List<int[]> list = new ArrayList<int[]>();

        File folder = new File("src/train/");
        String temp[] = folder.list();

        list = getList(temp, this.dimension);
        LBG lbgTest = new LBG(list, this.codeBookSize, this.dimension, this.epsilon);
        lbgTest.init_LBG();

        int depth = (int) (Math.log(this.codeBookSize) / Math.log(2));
        Search search = new Search(lbgTest.getTree(), lbgTest.getLeafMap());

        // store codebook
        File codeFile = new File("codebook.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(codeFile));
        List<int[]> codeBookList = lbgTest.getCodeBook();
        for (int i = 0; i < codeBookList.size(); i++) {
            for (int j = 0; j < codeBookList.get(i).length; j++) {
                bufferedWriter.write(codeBookList.get(i)[j] + " ");
            }
        }
        bufferedWriter.close();

        // write output.raw after reconstruct
        FileOutputStream fileOutputStream = new FileOutputStream("output.raw");
        BitOutputStream bitOutputStream = new BitOutputStream(fileOutputStream);

        File imageFile = new File(this.path);

        List<int[]> tempList;
        tempList = getImageList(readImage(imageFile.toString()), this.dimension);
        for (int i = 0; i < tempList.size(); i++) {
            int tempNum = search.getPoints(tempList.get(i), this.decisionSearch);
            writeCode(bitOutputStream, depth, tempNum);
        }
        bitOutputStream.close();
    }

    public void decode() throws Exception {
        Map<Integer, int[]> codebookMap = new HashMap<Integer, int[]>();
        int tempCodeBook = 0;
        BufferedReader bufferedReader = new BufferedReader(new FileReader("codebook.txt"));
        String array[] = bufferedReader.readLine().split(" ");
        for (int i = 0; i < array.length; i += this.dimension * this.dimension) {
            int points[] = new int[this.dimension * this.dimension];
            for (int j = 0; j < this.dimension * this.dimension; j++) {
                points[j] = Integer.parseInt(array[i + j]);
            }
            codebookMap.put(tempCodeBook, points);
            tempCodeBook++;
        }
        bufferedReader.close();

        FileInputStream fileInputStream = new FileInputStream("output.raw");
        BitInputStream bitInputStream = new BitInputStream(fileInputStream);

        int depth = (int) (Math.log(this.codeBookSize) / Math.log(2));
        int total = (int) Math.pow((256 / this.dimension), 2);
        int readArr[] = readCode(bitInputStream, depth, total);
        int tempArr[][] = new int[256][256];
        int a = 0;
        for (int i = 0; i < 256; i += this.dimension) {
            for (int j = 0; j < 256; j += this.dimension) {
                int points[] = codebookMap.get(readArr[a]);
                for (int k = 0; k < this.dimension; k++) {
                    for (int l = 0; l < this.dimension; l++) {
                        tempArr[i + k][j + l] = points[k + l];
                    }
                }
                a++;
            }
        }
        bitInputStream.close();

        BitOutputStream imageOutput = new BitOutputStream(new FileOutputStream("Output-re.raw"));
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                writeCode(imageOutput, depth, tempArr[i][j]);
            }
        }
        imageOutput.close();

        if (output == 1) {
            FileInputStream fis = new FileInputStream("Output-re.raw");
            ByteArrayOutputStream storage = new ByteArrayOutputStream(2000000);
            byte buf[] = new byte[100000];
            int n;
            while ((n = fis.read(buf)) != -1)
                storage.write(buf, 0, n);
            fis.close();
            storage.close();
            BufferedImage MaxImage = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_GRAY);
            MaxImage.getRaster().setDataElements(0, 0, 256, 256, storage.toByteArray());
            ImageIO.write(MaxImage, "jpg", new File("Output-re.jpg"));
        }
    }

    // get training vector
    public static List getList(String temp[], int dimension) throws Exception {
        List<int[]> list = new ArrayList<int[]>();
        for (int i = 0; i < temp.length; i++) {
            int tempArr[][];
            tempArr = readImage("src/train/" + temp[i]);
            list = mergeList(list, getImageList(tempArr, dimension));
        }
        return list;
    }

    // merge list
    public static List mergeList(List<int[]> l1, List<int[]> l2) {

        List<int[]> list = new ArrayList<int[]>();
        list.addAll(l1);
        list.addAll(l2);
        return list;
    }

    // Read Image into Array
    public static int[][] readImage(String temp) throws Exception {

        File inputFile = new File(temp);
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(inputFile));
        int tempArr[][] = new int[256][256];
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                tempArr[i][j] = inputStream.read();
            }
        }
        return tempArr;
    }

    // Transfer Array into list
    public static List<int[]> getImageList(int temp[][], int dimension) {

        List<int[]> list = new ArrayList<int[]>();
        for (int i = 0; i < 256; i += dimension) {
            for (int j = 0; j < 256; j += dimension) {
                int array[] = new int[dimension * dimension];
                int count = -1;
                for (int k = 0; k < dimension; k++) {
                    for (int l = 0; l < dimension; l++) {
                        count++;
                        array[count] = temp[i + k][j + l];
                    }
                }
                list.add(array);
            }
        }
        return list;
    }

    public static void writeCode(BitOutputStream out, int bitNum, int num) throws IOException {

        // Write value as 8 bits in big endian
        for (int j = 7; j >= 0; j--)
            out.write((num >>> j) & 1);
    }

    public static int[] readCode(BitInputStream in, int bitNum, int totalNum) throws IOException {
        // For this file format, we read 8 bits in big endian
        int array[] = new int[totalNum];
        for (int i = 0; i < array.length; i++) {
            int val = 0;
            for (int j = 0; j < 8; j++)
                val = val << 1 | in.readNoEof();
            array[i] = val;
        }
        return array;
    }

}
