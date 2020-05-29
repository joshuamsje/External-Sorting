package project3;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;


/**
 * Sorts using replacement selection sort 
 * as well as multi way merge to a output
 * file
 * 
 * @author Kurt Karpin (kkarp)
 * @author Joshua Matthew (josh827)
 *
 * @version 2020.4.26
 */
public class Sort {
    // replace selection sort

    // multiway merge
    private MinHeap<Record> heap;
    private int numRecords;
    private int counter;
    private byte[] output;
    private ArrayList<Integer> runner;
    private ArrayList<Integer> runSize;
    // private int flag;


    // private int cr;
    /**
     * Constructor for the sort class.
     * 
     * @param heap1 takes in the min heap
     * @throws IOException
     */
    public Sort(MinHeap<Record> heap1) throws IOException {
        heap = heap1;
        output = new byte[8192];
        counter = 0;
        numRecords = 4096;
        // flag = 1;
        runner = new ArrayList<Integer>();
        runner.add(0);
        runSize = new ArrayList<Integer>();
        // cr = 0;
    }


    /**
     * Gets the counter.
     * 
     * @return int counter
     */
    public int getCounter() {
        return counter;
    }


    /**
     * Clears the counter by adding
     * it to the arrayList
     */
    public void clearCounter() {
        runner.add(counter);
        // counter = 0;
    }


   /**
    * gets the runner arrayList
    * @return ArrayList<Integer> type
    */
   public ArrayList<Integer> getIndexList() {
        return runner;
    }


   /**
    * inserts a record in bytes to the output
    * buffer
    * @param hold the record that should be inserted
    * @param index the spot in the output buffer to insert
    * @throws IOException
    */
    private void insertOutput(Record hold, int index) throws IOException {
        // gets the full 16 bytes of record and puts each byte in output
        byte[] total = hold.totalByte(hold.getData(), hold.getKey());
        for (int i = 0; i < 16; i++) {
            output[index] = total[i];
            index++;
        }
        counter++;
    }

    
    /**
     * getter method for returning the record at a location in the output buffer
     * @param index index from the spot in the output buffer
     * @param input the buffer to retrieve the record
     * @return Record to return
     */
    private Record getRecord(int index, byte[] input) {
        double key1; // values for key and data
        long data1;
        byte[] eight = new byte[8]; // key bytes
        byte[] eight2 = new byte[8]; // data bytes
        int run2 = 0;
        for (int i = index; i < index + 8; i++) {
            eight[run2] = input[i]; // putting bytes to key and data
            eight2[run2] = input[i + 8];
            run2++;
        }
        data1 = ByteBuffer.wrap(eight).getLong();
        key1 = ByteBuffer.wrap(eight2).getDouble();
        return new Record(data1, key1);
    }


    /**
     * gets the Record at the run int flag
     * @param index the index to look for
     * @param input the input array
     * @param f the flag the record was assigned
     * @return Record result
     */
    private Record getRecordNew(int index, byte[] input, int f) {
        double key1; // values for key and data
        long data1;
        byte[] eight = new byte[8]; // key bytes
        byte[] eight2 = new byte[8]; // data bytes
        int run2 = 0;
        for (int i = index; i < index + 8; i++) {
            eight[run2] = input[i]; // putting bytes to key and data
            eight2[run2] = input[i + 8];
            run2++;
        }
        data1 = ByteBuffer.wrap(eight).getLong();
        key1 = ByteBuffer.wrap(eight2).getDouble();
        return new Record(data1, key1, f);
    }


    /**
     * This sorts the heap with the input buffer and outputs
     * a sorted list of bytes. Note this method only clears
     * and fills the input buffer and the output buffer
     * respectively and returns the output to write to file
     * 
     * @param input the array to be checked
     * @return byte[] array result
     * @throws IOException
     */
    public byte[] ReplaceSort(byte[] input) throws IOException {
        for (int j = 0; j < input.length; j += 16) {

            if (heap.heapsize() > 0) {
                Record hold = heap.removemin();
                insertOutput(hold, j);
                // inserts the record at the top of the heap
                // then proceeds to sift it
                Record compare = getRecord(j, input);
                // compares the input index and output index
                // that was just inserted previously
                if (compare.compareTo(hold) >= 0) {
                    heap.insert(compare);
                    // heap.siftdown(0);
                }
                else {
                    heap.insert(compare);
                    // hide and swap method
                    heap.removemin();
                    // heap.minSwap(heap.getHeap());
                }
                numRecords++;
            }
            else {
                j -= 16;
                runner.add(counter);
                heap.setSize(4096);
            }

        }

        return output;

    }


    /**
     * gets the output buffer
     * @return byte[] result
     */
    public byte[] getOutput() {
        return output;
    }


    /**
     * Finishes dumping the heap to make it empty
     * @param size the size to dump
     * @throws IOException
     */
    public void finishDumpHeap(int size) throws IOException {
        for (int i = 0; i < size * 16; i += 16) {
            this.insertOutput(heap.removemin(), i);
        }
    }


    /**
     * hidden Heap shifting to insert to the output
     * @param pos the position to start
     * @return byte[] result
     * @throws IOException
     */
    public byte[] hiddenHeapOutput(int pos) throws IOException {
        // int index = 0;

        for (int k = pos * 16; k < output.length; k += 16) {
            this.insertOutput(heap.removemin(), k);
        }

        return output;
    }


    /**
     * Empties the heap
     * @return byte[] result
     * @throws IOException
     */
    public byte[] emptyHeap() throws IOException {
        byte[] total;

        int cr = 0;
        for (int k = 0; k < output.length; k += 16) {

            Record compare = heap.removemin();
            total = compare.totalByte(compare.getData(), compare.getKey());
            for (int i = 0; i < 16; i++) {
                output[cr] = total[i];
                cr++;
            }
            counter++;

        }

        return output;

    }




    /**
     * blockChecker method that keeps going 
     * through the blocks until 8 runs are
     * merged 
     * 
     * @param heap1 the minHeap
     * @param inputter the byte[] array used to read
     * @param writer the file that is being read from Parser
     * @param reader the file that is now being written to 
     * @param flagCounter the flagCounter to see if a block is empty
     * @param count the counter
     * @return int result
     * @throws IOException
     */
    private int blockChecker(
        MinHeap<Record> heap1,
        byte[] inputter,
        RandomAccessFile writer,
        RandomAccessFile reader,
        int[] flagCounter,
        int count)
        throws IOException {
        while (!this.emptyFlags(flagCounter)) {
            int length = 0;

            if (count != output.length) {
                Record remove = heap1.removemin();
                insertOutput(remove, count);
                flagCounter[remove.getFlag() - 1]--;
                // checks if there is more blocks to add from that flag
                if (flagCounter[remove.getFlag() - 1] == 0) {
                    // we must load one block of records if there are
                    // still more in the run file
                    if (runSize.get(remove.getFlag() - 1) != 0) {

                        // this means there are still records of this flag
                        // in the runFile
                        if (runSize.get(remove.getFlag() - 1) < 512) {
                            length = runSize.get(remove.getFlag() - 1) * 16;
                            writer.seek(runner.get(remove.getFlag() - 1) * 16);
                            writer.read(inputter, 0, length);
                        }
                        else {
                            length = 8192;
                            writer.seek(runner.get(remove.getFlag() - 1) * 16);
                            writer.readFully(inputter);
                        }
                        runner.set(remove.getFlag() - 1, runner.get(remove
                            .getFlag() - 1) + length / 16);
                        runSize.set(remove.getFlag() - 1, runSize.get(remove
                            .getFlag() - 1) - length / 16);
                        insertNewBlock(heap1, inputter, remove.getFlag(),
                            length);
                        flagCounter[remove.getFlag() - 1] = length / 16;
                        
                    }
                    
                    // else - no more records in the run file
                }
                count += 16;

            }
            else {
                reader.write(output);
                count = 0;
            }
        }
        if (count == 8192)
        {
            reader.write(output);
            count = 0;
        }
        //System.out.println("Writer's current size: " + reader.length() + " Finished at: " + count);
        return count;
    }


    /**
     * inserts a new block into the heap
     * 
     * @param heaper the minHeap
     * @param input2 the byte[] array
     * @param flag1 the counter for a record
     * @param length the length of a run
     */
    private void insertNewBlock(
        MinHeap<Record> heaper,
        byte[] input2,
        int flag1,
        int length) {
        for (int w = 0; w < length; w += 16) {
            Record n = getRecordNew(w, input2, flag1);
            heaper.insert(n);

        }
    }


    /**
     * Sets the sizes for the runSize arraylist
     */
    public void setSizes() {
        for (int i = 0; i < runner.size(); i++) {
            
            if (runner.size() == 1) {
                runSize.add(numRecords);
            }
            else if (i == 0) {
                runSize.add(runner.get(i + 1));
            }
            else if (i == runner.size() - 1) {
                runSize.add(numRecords - runner.get(i));
            }
            else {
                runSize.add(runner.get(i + 1) - runner.get(i));
            }
        }
    }


    
    /**
     * The merge method that combines
     * merge runs together 
     * 
     * @param read the file to be written to
     * @param write the file to be read from
     * @param re the record array for the heap
     * @param rSize the size of the runs
     * @param heap the minHeap 
     * @param array the byte[] array inputted
     * @throws IOException
     */
    public void merge(
        //initially reading from write
        //intitally writing to read
        //note read is write from parser
        //and write is read from parser
        RandomAccessFile read,
        RandomAccessFile write,
        Record[] re,
        int rSize,
        MinHeap<Record> heap, byte[] array)
        throws IOException {
        
        int count = 0;
        int c = 0;
        int numFiles = 0;
        int transfer = 0;
        this.setSizes();
        ArrayList<Integer> newRunIndex = new ArrayList<Integer>();
        newRunIndex.add(0);
        while (numFiles != 1) {

            if (rSize > 8) {
                // merges 8 run files
                newRunIndex.add(runner.get(8));
                if (transfer % 2 == 0)
                {
                    c = multiWay(read, heap, re, write, 8, c, array);
                }
                else
                {
                    c = multiWay(write, heap, re, read, 8, c, array);
                }
                
                for (int i = 0; i < 8; i++)
                {
                    runSize.remove(0);
                    runner.remove(0);
                }
                // total merge size added to list
                
                
                rSize -= 8;
            }
            else {
                // merge whatever number of files less than
                // or equal to 8
                if (transfer % 2 == 0)
                {
                    c = multiWay(read, heap, re, write, rSize, c, array);
                }
                else
                {
                    c = multiWay(write, heap, re, read, rSize, c, array);
                }
                rSize -= rSize;
                for (int i = 0; i < rSize; i++)
                {
                    runSize.remove(0);
                    runner.remove(0);
                }
            }
            count++; // keeps track of num Files merged
           
            if (rSize == 0) {
              
                // switches
                // replace current runner list
                // with new one
                runner.clear();
                for (int i = 0; i < newRunIndex.size(); i++)
                {
                    runner.add(newRunIndex.get(i));
                }
                runSize.clear();
                this.setSizes();
                
                newRunIndex.clear();
                newRunIndex.add(0);
                // resets sizes
                
               
                // sets the numFiles and
                // rSize
                rSize = count;
                
                numFiles = count;
                transfer++;
                read.seek(0);
                write.seek(0);

                count = 0;
                c = 0;

            }

        }

        
        if (transfer % 2 == 0) {
           
            //swap the input and output
            RandomAccessFile temp = write;
            write = read;
            read = temp;

        }
        System.out.println(this.writeToConsole(write, array));
        
    }


    /**
     * Multiway merge method that gets the first
     * block from at most 8 runs from a file
     * and inserts them into the heap
     * 
     * @param write the file to be read from
     * @param heap1 the heap to be resized
     * @param re the record array to hold records
     * @param read the file to be passed as a parameter
     * @param rSize the check to see how many run
     * @param c the counter
     * @param array the byte[] array
     * @return int result
     * @throws IOException
     */
    public int multiWay(
        RandomAccessFile write,
        MinHeap<Record> heap1,
        Record[] re,
        RandomAccessFile read,
        int rSize,
        int c, byte[] array)
        throws IOException {

        int[] flagCount = new int[8];
        int count = 0;
        int flag = 1;
        
        // iterates through all the runs
        // note the runner size is the number of
        // runs total
        for (int i = 0; i < rSize; i++) {
            if (runSize.get(i) != 0) {
                write.seek((long)runner.get(i) * 16);
                int length;
                if (runSize.get(i) < 512) {
                    length = runSize.get(i) * 16;
                    write.readFully(array, 0, length);
                }
                else {
                    length = 8192;
                    write.readFully(array);
                }
                // write.readFully(array);
                for (int k = 0; k < length; k += 16) {
                    Record r = getRecordNew(k, array, flag);
                    flagCount[flag - 1]++;
                    re[count] = r;
                    count++;
                }
                flag++;
                runner.set(i, runner.get(i) + length / 16);
                runSize.set(i, runSize.get(i) - length / 16);

            }
        }
        heap1 = new MinHeap<Record>(re, count, 4096);
        return blockChecker(heap1, array, write, read, flagCount, c);
    }


    /**
     * Writes to the console the output
     * @param reader the accessFile to write to
     * @param array the byte[] array to be read from
     * @return String the output
     * @throws IOException
     */
    private String writeToConsole(RandomAccessFile reader, byte[] array) throws IOException {

        long offset = 0;
        reader.seek(offset);
        int x = 1;
        StringBuilder build = new StringBuilder();
        double key1 = 0;
        long data1 = 0;
        byte[] eight = new byte[8];
        byte[] eight2 = new byte[8];
        while (offset < reader.length()) {
            reader.readFully(array);
            for (int i = 0; i < 8; i++) {
                // creates record
                eight[i] = array[i];
                eight2[i] = array[i + 8];
            }
            data1 = ByteBuffer.wrap(eight).getLong();
            key1 = ByteBuffer.wrap(eight2).getDouble();
            if (x % 5 == 0) {
                build.append(data1 + " " + key1 + "\n");
            }
            else
            {
                build.append(data1 + " " + key1 + " ");
            }
            x++;
          
            
            offset = reader.getFilePointer();
        }
        return build.toString();
    }


    /**
     * emptyFlags method that checks the flagCounter array
     * @param flagCounter int array
     * @return boolean result
     */
    private boolean emptyFlags(int[] flagCounter) {
        for (int i = 0; i < flagCounter.length; i++) {
            if (flagCounter[i] != 0) {
                return false;
            }
        }
        return true;
    }

}
