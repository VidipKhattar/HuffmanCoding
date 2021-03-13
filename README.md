# ECM 1414: Data Structures and Algorithms
___
## Coursework: Huffman coding algorithm README file

### Introduction
___

The program implements the Huffman coding algorithm, both encoding and decoding for compression and decompression. The program was built in java using the intelliJ IDE.  

### Running the program
___

The folder contains 2 java files, `huffmanCompress.java` and `huffmanDecompress.java`, the former compresses a chosen `.txt` file and the latter decompresses and binary file containg the compressed version of the inputted file.

Through an IDE or any text editor, the input file can be changed by replacing the File object source in line `80` (shown below) where it says `Christmas_Carol_French`, to any other `.txt` file in the same directory as the both the `.java` files
```java
78   public static void main(String[] args) throws IOException, ClassNotFoundException {
79        //Get the file to compress using huffmanCompress encoding
80        File inputFile = new File("Christmas_Carol_French.txt");
``` 

After the edits, the program can be run through the IDE or through a shell.
Executing this will output the following the following format to the console:
```sh
The File Christmas_Carol_French.txt successfully compressed to compressed_txt.bin
Size of uncompressed file is 222080 Bytes
Size of compressed file is 123808 Bytes
Compression ratio: 44.250720461095106%
Time for compression (ms) : 205
```

This will create or overwrite a `compressed_txt.bin` and `encoded_tree.bin` binary files to the same directory as the `.java` files.
From this, `huffmanDecompress.java` can be executed without any change, as it takes input of `compressed_text.bin` and `encoded_tree.bin` which are overwritten after every run of `huffmanCompress.java`. So these 2 bin files contain the encoding of the most recent text file run by `huffmanCompress.java`.

The output to the shell from `huffmanDecompress.java` will look similar to the following:

```sh
File successfully uncompressed to 'uncompressed.txt'
Size of compressed file is 123808 Bytes
Size of uncompressed file is 222080 Bytes
Time for decompression (ms) : 612
```

This will eithier create or overwrite the `uncompressed.txt` file in the directory containing the uncompressed version of the text.

### Testing
___

The datasets used for testing have been taken from 2 websites: 
- https://www.gutenberg.org 
- http://pizzachili.dcc.uchile.cl/repcorpus.html

From the https://www.gutenberg.org, the following texts were used in 3 languages: English, French and German:
- [A Christmas Carol by Charles Dickens](https://www.gutenberg.org/ebooks/24022)
- [Oliver Twist by Charles Dickens](https://www.gutenberg.org/ebooks/730)

From http://pizzachili.dcc.uchile.cl/repcorpus.html, the following repetitivecorpus were sampled from each section:
- [Real](http://pizzachili.dcc.uchile.cl/repcorpus/real/)
- [Pseudo-Real](http://pizzachili.dcc.uchile.cl/repcorpus/pseudo-real/)
- [Artifical](http://pizzachili.dcc.uchile.cl/repcorpus/artificial/)

Note: Java is unable to to process the repetitive corpus datasets given its sheer size, therefore I have limited the size of these files to `100mb` and assumed the frequency of letters for this file would be same if it were for the entire file.

You are able to cap a text file to `100 mb` using the command in the shell:
```sh
dd skip=100256 count=253 if=input_text.txt of=output_text.txt bs=1
```

##### Authors
- Vidip Khattar

##### Acknowledgements
- [GitHub](https://github.com/)



For the latest version of this program code, [click here](https://github.com/VidipKhattar/HuffmanCoding.git) to access the GitHub repository with the latest updated code

&copy; Vidip Khattar, University of Exeter

Licensed under the [MIT License](License)

