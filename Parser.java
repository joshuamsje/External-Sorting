package project3;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * Parse reads a given binary file that contains 8N blocks of data
 * using RandomAccessFile. Note for each block of data, there is 8,192 bytes
 * and 512 records where each record stores 16 bytes of data.
 * 
 * We then call the sort method to implement a replace selection for each
 * block of data.
 * 
 * @author Kurt Karpin (kkarp)
 * @author Joshua Matthew (josh827)
 *
 */
public class Parser {

    private MinHeap<Record> heap;
    private byte[] array;


    /**
     * Constructor class that simply calls
     * the inputReader method to parse the
     * binary file.
     * 
     * @param file
     *            input file
     * @param outFile
     *            the output
     */
    public Parser(String file) {

        inputReader(file, "output.txt");

    }


    /**
     * This reads the given binary file to read
     * all the data in it.
     * 
     * @param file
     *            parsing file
     */
    public void inputReader(String file, String outFile) {

        try {
            long offset = 0;
            RandomAccessFile writer = new RandomAccessFile(outFile, "rw");
            RandomAccessFile reader = new RandomAccessFile(file, "rw");

          
            // should be zero
            int index = 0;
            // create heap array and output buffer
            Record[] heapArray = new Record[4096];
            array = new byte[8192]; // input buffer
            // puts in the first 8 blocks of data into heaparray
            for (int k = 0; k < 8; k++) {
                reader.readFully(array);
                // offset should be 8192
                offset = reader.getFilePointer();
                double key1 = 0;
                long data1 = 0;
                byte[] eight = new byte[8];
                byte[] eight2 = new byte[8];
                // puts in each block into heap array
                for (int j = 0; j < array.length; j += 16) {
                    int run = 0;
                    // creates record
                    for (int i = j; i < j + 8; i++) {
                        eight[run] = array[i];
                        eight2[run] = array[i + 8];
                        run++;
                    }
                    data1 = ByteBuffer.wrap(eight).getLong();
                    key1 = ByteBuffer.wrap(eight2).getDouble();
                    Record hold = new Record(data1, key1);
                    heapArray[index] = hold;
                    index++;
                }
            }
            // offset 65k
            // reader is 131k
            heap = new MinHeap<Record>(heapArray, index, 4096);
            Sort sorter = new Sort(heap);
            // readBlock(heap); //this should be called if there is only 8
            // blocks of data
            while (offset < reader.length()) {
                // 8 more blocks
                // load in input buffer
                reader.readFully(array);
                offset = reader.getFilePointer();
                byte[] output = sorter.ReplaceSort(array);

                writer.write(output);

            }
            // by now, the input buffer would no longer
            // have data to load in
            // so it should be empty

            // number of hidden values
            int hiddenVal = 4096 - heap.heapsize();
            
            int size = heap.heapsize();
          

            // empty heap until it has a size of less than 512
            while (heap.heapsize() >= 512) {
                writer.write(sorter.emptyHeap());
            }
           
            //fill in rest of heap
            int pos = heap.heapsize();
            sorter.finishDumpHeap(heap.heapsize());
            //heap will be empty
            sorter.clearCounter();
            heap.shiftHidden(size, hiddenVal);
            heap.setSize(hiddenVal);
            while (heap.heapsize() > 0)
            {
                writer.write(sorter.hiddenHeapOutput(pos));
                pos = 0;
            }
            
            reader.seek(0);
            sorter.merge(writer, reader, heapArray,
                sorter.getIndexList().size(), heap, array);
            
            reader.close();
            writer.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Could not find file" + file);
        }
        catch (IOException e) {
            System.out.println("Writing error: " + e);
        }
    }

}
