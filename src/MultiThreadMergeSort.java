import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class MultiThreadMergeSort extends Thread {
    private Thread t;

    private final String threadName;

    private static int[] arr;
    private static int[] result;

    private static Semaphore[] s;

    private final int start;
    private final int mid;
    private final int end;

    private final boolean flag;

    public MultiThreadMergeSort(String threadName, int start, int mid, int end, boolean flag) {
        this.threadName = threadName;
        this.start = start;
        this.mid = mid;
        this.end = end;
        this.flag = flag;
        System.out.println("Creating " + threadName);
    }

    public static void initArray(int[] arr) {
        MultiThreadMergeSort.arr = arr;
        result = new int[arr.length];
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public void run() {
        if (flag)
            SingleThreadMergeSort.merge_sort_recursive(arr, result, start, end);
        else
            SingleThreadMergeSort.merge(arr, result, start, mid, end);
        s[Integer.parseInt(threadName)].release();
        System.out.println("Thread " + threadName + " exiting.");
    }

    public static void startSorting(int threadNum) {
        int len = arr.length;
        int[] start = new int[threadNum];
        int[] mid = new int[threadNum];
        int[] end = new int[threadNum];
        int step = (int) Math.ceil(len * 1.0 / threadNum) - 1;

        start[0] = 0;
        end[0] = start[0] + step;
        for (int i = 1; i < threadNum; i++) {
            start[i] = end[i - 1] + 1;
            if (i == threadNum - 1)
                end[i] = len-1;
            else
                end[i] = start[i] + step;
        }

        boolean flag = true;
        while (threadNum > 0) {
            MultiThreadMergeSort[] t = new MultiThreadMergeSort[threadNum];
            s = new Semaphore[threadNum];
            for (int i = 0; i < threadNum; i++) {
                t[i] = new MultiThreadMergeSort(Integer.toString(i), start[i], mid[i], end[i], flag);
                s[i] = new Semaphore(0);
            }
            for (int i = 0; i < threadNum; i++) {
                t[i].start();
            }
            for (int i = 0; i < threadNum; i++) {
                try {
                    s[i].acquire();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }

            threadNum /= 2;
            flag = false;
            for (int i = 0; i < threadNum; i++) {
                start[i] = start[i * 2];
                end[i] = end[i * 2 + 1];
                mid[i] = end[i * 2];
            }
        }
    }
}
