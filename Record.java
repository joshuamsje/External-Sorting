package project3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Builds a MinHeap with an array
 * that is meant to store records
 * and implemented for sorting
 * 
 * @author Kurt Karpin (kkarp9)
 * @author Joshua Mathew (josh827)
 *
 * @version 2020.4.25
 */
@SuppressWarnings("rawtypes")
public class Record implements Comparable {

    private long data;
    private double key;
    private int flag1;


    /**
     * Record constructor that holds a key and data value
     * 
     * @param data1
     *            long
     * @param key1
     *            double
     */
    public Record(long data1, double key1) {
        data = data1;
        key = key1;

    }


    /**
     * Record constructor that holds a key, data, and flag value
     * 
     * @param data1
     *            long
     * @param key1
     *            double
     * @param flag
     *            int
     */
    public Record(long data1, double key1, int flag) {
        data = data1;
        key = key1;
        flag1 = flag;

    }


    /**
     * getter method for returning the flag
     * 
     * @return int flag
     */
    public int getFlag() {
        return flag1;
    }


    /**
     * getter method that returns the data
     * 
     * @return long data
     */
    public long getData() {
        return data;
    }


    /**
     * getter method that returns the key
     * 
     * @return double key value
     */
    public double getKey() {
        return key;
    }


    /**
     * Returns the total byte representation
     * of the record
     * 
     * @param data
     *            long
     * @param key
     *            double
     * @return byte[] array
     * @throws IOException
     */
    public byte[] totalByte(long data, double key) throws IOException {
        byte[] longOutput = ByteBuffer.allocate(8).putLong(data).array();
        byte[] keyOutput = ByteBuffer.allocate(8).putDouble(key).array();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        outputStream.write(longOutput);
        outputStream.write(keyOutput);
        byte total[] = outputStream.toByteArray();

        return total;
    }


    @Override
    /**
     * compareTo method
     * 
     * @param arg0
     *            the Object
     * @return int result
     */
    public int compareTo(Object arg0) {
        // TODO Auto-generated method stub
        if (key >= ((Record)arg0).getKey()) 
        {
            return 1;
        }
        else 
        {
            return -1;
        }

    }
}
