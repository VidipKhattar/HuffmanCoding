import java.io.*;
import java.util.*;
import java.nio.file.*;

//Class to decompress the encoded file using the Huffman Tree
public class huffmanDecompress {

    //Convert byte array of encoded bits to a string of encoded bits for decompression
    static String GetString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }

    //main method to decompress file
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //start time for a timer to check decompressing time
        long startTime = System.nanoTime();
        //reads byte array from the compressed binary file
        byte[] allBytes = Files.readAllBytes(Paths.get("compressed_txt.bin"));
        String encodedBits = GetString(allBytes);

        //Initializes hashmap to get the hashmap object from a compressed file
        HashMap<String, String> outHash;

        //Get encoded hashmap object from the binary file using file input stream and object input stream
        //FileInputStream fileInputStreamEnglish = new FileInputStream("english_encoded_tree.bin");
        FileInputStream fileInputStream = new FileInputStream("encoded_tree.bin");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        outHash = (HashMap<String, String>) objectInputStream.readObject();
        //System.out.println(outHash);

        //Build string from encoded text and hashtable
        StringBuilder codesb = new StringBuilder();
        StringBuilder uncompsb = new StringBuilder();
        for (char digit: encodedBits.toCharArray()) {
            codesb.append(digit);
            String finalCode = codesb.toString();
            outHash.forEach((k, v) -> {
                if (finalCode.equals(v)) {
                    uncompsb.append(k);
                    codesb.setLength(0);
                }
            });
        }

        //Write the uncompressed text to a file using buffer writer
        BufferedWriter writer = new BufferedWriter(new FileWriter("uncompressed_txt.txt"));
        writer.write(uncompsb.toString());
        writer.close();

        //end timer for uncompressing a file and get the duration of the uncompressing
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        //Get files used by this module to get information about them
        File compressedFile = new File("compressed_txt.bin");
        File uncompressedFile = new File("uncompressed_txt.txt");
        File encodedTreeFile = new File("encoded_tree.bin");
        int combineCompress = (int) (encodedTreeFile.length()+compressedFile.length());


        //print out to the console information about the files such as file size and uncompressing time
        System.out.println("File successfully uncompressed to 'uncompressed.txt'");
        System.out.println("Size of compressed file is " + combineCompress  + " Bytes");
        System.out.println("Size of uncompressed file is " + uncompressedFile.length() + " Bytes");
        System.out.println("Time for decompression (ms) : " + duration/1000000);
    }
}
