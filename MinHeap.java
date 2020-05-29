package project3;

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
public class MinHeap<T> {

    private T[] Heap; // Pointer to the heap array
    private int size; // Maximum size of the heap
    private int n; // Number of things now in heap
    private int hidden; // Number of hidden things in the heap


    // Constructor supporting preloading of heap contents
    /**
     * Constructor for the MinHeap
     * initialization
     * 
     * @param h
     *            - the heap to be created
     * @param num
     *            - the number of objects in the heap
     * @param max
     *            the max size of the heap
     */
    public MinHeap(T[] h, int num, int max) {

        Heap = h;
        // Heap = new T[size + 1];
        // Heap[0] = new T(Long.MIN_VALUE, Double.MIN_VALUE);
        n = num;
        size = max;
        hidden = 0;
        buildheap();
    }


    // Return current size of the heap
    /**
     * Returns the current number of objects in the heap
     * 
     * @return int size
     */
    public int heapsize() 
    {
        return n;
    }


    /**
     * Getter method that returns the array used
     * to build the heap
     * 
     * @return T[] heap
     */
    public T[] getHeap() 
    {
        return Heap;
    }


    // Return true if pos a leaf position, false otherwise
    /**
     * Checks to see if the record at that
     * position returns a leaf
     * 
     * @param pos
     *            the int
     * @return boolean result
     */
    private boolean isLeaf(int pos) 
    {
        
       
        return (pos >= n / 2) && (pos < n);
    }


    // Return position for left child of pos
    /**
     * Returns the left node of the record
     * at int position
     * 
     * @param pos
     *            the int
     * @return int position
     */
    private int leftchild(int pos) {
        if (pos >= n / 2) 
        {
            return -1;
        }

        return 2 * pos + 1;
    }


    /**
     * // Return position for right child of pos
     * private int rightchild(int pos)
     * {
     * if (pos >= (n - 1) / 2)
     * {
     * return -1;
     * }
     * return 2 * pos + 2;
     * }
     * 
     **/
    // Return position for parent
    /**
     * gets the parent of the record
     * at int pos
     * 
     * @param pos
     *            the int
     * @return int result
     */
    private int parent(int pos) {
        if (pos <= 0) 
        {
            return -1;
        }

        return (pos - 1) / 2;
    }


    /**
     * Insert method that inserts a record
     * into the array
     * 
     * @param key
     *            Record to insert
     */
    public void insert(T key) {
        if (n >= size) 
        {
            System.out.println("Heap is full");
            return;
        }
        // int curr = n++;
        int curr = n++;
        Heap[curr] = key;

        while ((curr != 0) && (((Record)Heap[curr]).compareTo(Heap[parent(
            curr)]) < 0)) 
        {
            swap(Heap, curr, parent(curr));
            curr = parent(curr);
        }

    }
    // Heapify contents of Heap


    /**
     * Builds the heap from the constructor
     */
    private void buildheap() 
    {
        for (int i = n / 2 - 1; i >= 0; i--) 
        {
            siftdown(i);
        }

    }


    // Put element in its correct place
    /**
     * siftdown method that puts an element in its
     * correct place
     * 
     * @param pos
     *            to be sifted
     */
    private void siftdown(int pos) {
        if ((pos < 0) || (pos >= n)) 
        {
            return; // Illegal position
        }
        while (!isLeaf(pos)) 
        {
            int j = leftchild(pos);
            if ((j < (n - 1)) && (((Record)Heap[j]).compareTo(Heap[j
                + 1]) >= 0)) 
            {
                j++; // j is now index of child with greater value
            }
            if (((Record)Heap[pos]).compareTo(Heap[j]) < 0) 
            {
                return;
            }
            swap(Heap, pos, j);
            pos = j; // Move down
        }
    }


    /**
     * Sets the number of objects
     * in the heap to be a new Size
     * 
     * @param newSize
     *            the replaced size
     */
    public void setSize(int newSize) {
        n = newSize;
        buildheap();
    }


    // Remove and return maximum value
    /**
     * removes the minimum from the
     * min Heap
     * 
     * @return T element that was removed
     */
    public T removemin() {
        if (n == 0) 
        {
            return null; // Removing from empty heap
        }
        swap(Heap, 0, --n); // Swap maximum with last value
        if (n != 0) 
        {
            siftdown(0);
        }
        // Put new heap root val in correct place
        return Heap[n];
    }


    /**
     * Swapper method that swaps two elements in the heap
     * 
     * @param sort
     *            the heap that needs to be sorted
     * @param pos1
     *            the position of the first element
     * @param pos2
     *            the position of the second element
     */
    private void swap(T[] sort, int pos1, int pos2) 
    {
        T holder = sort[pos1];
        sort[pos1] = sort[pos2];
        sort[pos2] = holder;
    }


    /**
     * Minimum Swap that swaps two values from the array
     * 
     * @param sort
     *            the array to be sorted
     */
    public void minSwap(T[] sort) 
    {
        T holder = sort[0];
        sort[0] = sort[n - 1];
        sort[n - 1] = holder;
        n--;
        siftdown(0);
        hidden++;
    }


    /**
     * Shifts the hidden values from the back of
     * the array to the front
     * 
     * @param heapSize
     *            the current objects in the heap
     * @param hidden
     *            the hidden objects in the heap
     */
    public void shiftHidden(int heapSize, int hidden) {
        for (int i = 0; i < hidden; i++)
        {
            Heap[i] = Heap[i + heapSize];
        }
    }

}
