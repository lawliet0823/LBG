import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by L on 2015/1/9.
 */
public class BitOutputStream {

    // Underlying byte stream to write to.
    private OutputStream output;

    // The accumulated bits for the current byte. Always in the range 0x00 to 0xFF.
    private int currentByte;

    // The number of accumulated bits in the current byte. Always between 0 and 7 (inclusive).
    private int numBitsInCurrentByte;

    private int numBits = 8;

    // Creates a bit output stream based on the given byte output stream.
    public BitOutputStream(OutputStream out) {
        if (out == null)
            throw new NullPointerException("Argument is null");
        output = out;
        currentByte = 0;
        numBitsInCurrentByte = 0;
    }

    public void setNumBits(int numBits) {
        this.numBits = numBits;
    }


    // Writes a bit to the stream. The specified bit must be 0 or 1.
    public void write(int b) throws IOException {
        currentByte = currentByte << 1 | b;
        numBitsInCurrentByte++;
        if (numBitsInCurrentByte == numBits) {
            output.write(currentByte);
            numBitsInCurrentByte = 0;
        }
    }

    public void close() throws IOException {
        while (numBitsInCurrentByte != 0)
            write(0);
        output.close();
    }
}
