import java.io.*;
import java.util.*;


//class for defining attributes for any single node in the huffmanCompress tree.
//freq contains frequency of appearance of the character stored in the node
//c contains the character the node is storing
//encoded contains the bit value generated in the tree for a specific character node
//left is the node to the left of the current node
//right is the node to the right of the current node

class HuffmanNode {
    int freq;
    String c;
    String encoded;

    HuffmanNode left;
    HuffmanNode right;
}

// comparator class used to compare the node based on the nodes character frequency for sorting the nodes in a queue.
class MyComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y)
    {
        return x.freq - y.freq;
    }
}

//Class that creates and fills the huffmanCompress tree and holds the main method to compress the file
public class huffmanCompress {


    //Initialise a HashMap as a dictionary format to store a character and its encoded bit value
    public static HashMap<String, String> encoded_dict = new HashMap<String, String>();

    // This method gets the values stored in the tree through traversal.
    //encodedBits is the encoded bit value that is generated for a node.
    public static HashMap<String, String> generateCodes(HuffmanNode root, String encodedBits)
    {

        //if node does not have left or right, it means the node is a leaf node and contains a character
        //the encoded bit value and the lead code's character is stored in the hashmap
        if (root.left == null && root.right == null){

            //System.out.println(root.c + ":" + encodedBits);
            root.encoded = encodedBits;
            encoded_dict.put(root.c, encodedBits);
            return encoded_dict;
        }

        // if left then "0" is added to encodedBits.
        // if right then "1" is added to encodedBits.
        // recursive function called for left and right sub-tree of the tree.
        generateCodes(root.left, encodedBits + "0");
        generateCodes(root.right, encodedBits + "1");
        return encoded_dict;
    }

    //function to get turn the string encoded version of the text file to a binary type
    static byte[] GetBinary(String s) {
        StringBuilder stringBuilder = new StringBuilder(s);
        while (stringBuilder.length() % 8 != 0) {
            stringBuilder.append('0');
        }
        s = stringBuilder.toString();
        byte[] byteData = new byte[s.length() / 8];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '1') {
                byteData[i >> 3] |= 0x80 >> (i & 0x7);
            }
        }
        return byteData;
    }


    //main function to compress text file
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Get the file to compress using huffmanCompress encoding
        File inputFile = new File("Christmas_Carol_French.txt");
        //start time for a timer to check compression time
        long startTime = System.nanoTime();
        //Process the File to create a String object of the text file correctly formatted
        //using a string builder, input stream and buffer reader
        StringBuilder sb = new StringBuilder();
        InputStream in = new FileInputStream(inputFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + System.lineSeparator());
        }
        String uncompressedText = sb.toString();



        //Initialise a HashMap as a dictionary to store a character and its frequency of appearance in the text
        HashMap<String, Integer> freq_dict = new HashMap<>();

        //Find the frequencies of appearance of each character in text and store them in the HashMap
        for (int i = 0; i < uncompressedText.length(); i++){
            String c = String.valueOf(uncompressedText.charAt(i));
            if (!freq_dict.containsKey(c)){
                freq_dict.put(c, 0);
            }
            freq_dict.put(c, freq_dict.get(c)+1);
        }
        //System.out.println(freq_dict);

        //creating a priority queue huffmanNodeQueue to store the nodes of the huffmanCompress tree.
        //this queue sorts its nodes based on their frequencies using the comparator set
        PriorityQueue<HuffmanNode> huffmanNodeQueue = new PriorityQueue<>(freq_dict.size(), new MyComparator());


        //Initializing nodes with each key value pair in the hash map containing characters frequencies and adding it to the queue
        freq_dict.forEach((k, v) ->{
            HuffmanNode hn = new HuffmanNode();
            hn.c = k;
            hn.freq = v;

            hn.left = null;
            hn.right = null;

            huffmanNodeQueue.add(hn);
        });

        // create a root node
        HuffmanNode root = null;


        //loops until there is only one node inside the tree since the rest will be child nodes
        while (huffmanNodeQueue.size() > 1) {

            // gets the first item in the queue since that element node has the smallest frequency due to it being sorted by the comparator
            HuffmanNode x = huffmanNodeQueue.peek();
            //removing it from the queue so the next element in the queue is now has the smallest frequency
            huffmanNodeQueue.poll();

            //gets the first item in the queue since that element node has the smallest frequency due to it being sorted by the comparator
            HuffmanNode y = huffmanNodeQueue.peek();
            //removing it from the queue so the next element in the queue is now has the smallest frequency
            huffmanNodeQueue.poll();

            // create node which will become the parent node for the 2 smallest nodes in the list
            HuffmanNode f = new HuffmanNode();

            //frequency of this node is the combined frequency of its child nodes
            f.freq = x.freq + y.freq;
            String s1 = String.valueOf(x.c);
            String s2 = String.valueOf(y.c);
            f.c = s1 + s2;

            //sets the left node of f as the smallest node.
            f.left = x;

            //sets the right node of f as the second smallest node  .
            f.right = y;

            // marking the f node as the root node for the 2 nodes.
            root = f;

            //adds this new node as a replacement for the 2 old smallest node.
            huffmanNodeQueue.add(f);
        }

        /**
        FileInputStream fileInputStream = new FileInputStream("english_encoded_tree.bin");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        HashMap<String, String> english_encoding = (HashMap<String, String>) objectInputStream.readObject();
        System.out.println("*************");
        System.out.println(english_encoding);
        System.out.println("*************");
        */



        //generates the encoded bit string for the entire text from the tree using a string builder
        HashMap<String, String> encoded_dict = generateCodes(root, "");
        StringBuffer bits = new StringBuffer();
        for (char character: uncompressedText.toCharArray()){
            if (encoded_dict.containsKey(String.valueOf(character))){
                bits.append(encoded_dict.get(String.valueOf(character)));
            }
        }

        //convert encoded binary string to a byte array using the GetBinary function
        byte[] converted = GetBinary(bits.toString());


        File encodedTreeFile = new File("encoded_tree.bin");
        File compressedFile = new File("compressed_txt.bin");


        try {
                //create output stream and store the byte array storing the encoded data in a binary file
                OutputStream outputStream = new FileOutputStream("compressed_txt.bin");
                outputStream.write(converted);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }



            try {
                //create file output stream and store the huffman tree to use for decoding the encoded text
                FileOutputStream fileOutputStream = new FileOutputStream("encoded_tree.bin");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(encoded_dict);
                objectOutputStream.close();

            }catch (IOException e){
                e.printStackTrace();
            }



            //end timer for compression time and calculate the duration of it
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);

            long inputSize = inputFile.length();

            //combine size of compressed text file and the encoded tree file
            int combineCompress = (int) (encodedTreeFile.length()+compressedFile.length());

            //Output in the console information about the compression including size, ratio and time of compression
            System.out.println("The File " + inputFile.getName() + " successfully compressed to " + compressedFile.getName());
            System.out.println("Size of uncompressed file is " + inputFile.length() + " Bytes");
            System.out.println("Size of compressed file is " + combineCompress  + " Bytes");
            System.out.println("Compression ratio: " + (100 - (double)combineCompress/inputSize*100) + "%");
            System.out.println("Time for compression (ms) : " + duration/1000000);

        }
    }
