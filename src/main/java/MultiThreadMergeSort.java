import java.util.concurrent.Semaphore;

public class MultiThreadMergeSort extends Thread {
    private Thread t;   //线程

    private final String threadName;    //线程名称（序号）

    private static int[] arr;       //目标数组
    private static int[] result;    //辅助数组

    private static Semaphore[] s;   //信号量数组

    private final int start;        //区域左边界
    private final int mid;          //区域中心
    private final int end;          //区域右边界

    private final boolean flag;     //线程操作标识

    /**
     * 构造函数
     *
     * @param threadName 线程的名称
     * @param start      区域左边界
     * @param mid        区域中心
     * @param end        区域右边界
     * @param flag       线程操作标识
     */
    public MultiThreadMergeSort(String threadName, int start, int mid, int end, boolean flag) {
        this.threadName = threadName;
        this.start = start;
        this.mid = mid;
        this.end = end;
        this.flag = flag;

        //System.out.println("Creating " + threadName);
    }

    /**
     * 选择要操作的数组，并初始化辅助数组
     *
     * @param arr 目标数组
     */
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
        //排序的第一个循环执行完整归并排序操作，后续循环执行合并操作
        if (flag)
            SingleThreadMergeSort.merge_sort_recursive(arr, result, start, end);
        else
            SingleThreadMergeSort.merge(arr, result, start, mid, end);

        System.out.println("Thread " + threadName + " exiting.");

        s[Integer.parseInt(threadName)].release();  //释放信号量
    }

    /**
     * 根据线程数量执行排序
     *
     * @param threadNum 线程数量
     */
    public static void startSorting(int threadNum) {
        boolean hasExtraDiv = false;    //标识是否有由于奇数个线程产生的剩余区域
        int extraDivIndex = 0;  //记录剩余区域的索引

        //初始化
        int len = arr.length;
        int[] start = new int[threadNum];
        int[] mid = new int[threadNum];
        int[] end = new int[threadNum];

        //计算第一次划分区域的步长
        int step = (int) Math.ceil(len * 1.0 / threadNum) - 1;

        //第一次划分区域
        start[0] = 0;
        end[0] = start[0] + step;
        for (int i = 1; i < threadNum; i++) {
            start[i] = end[i - 1] + 1;

            //若是最后一个区域，将右边界设置为整个数组的末尾
            if (i == threadNum - 1)
                end[i] = len - 1;
            else
                end[i] = start[i] + step;
        }

        boolean flag = true;    //标记第一次运行，线程要执行完整归并排序
        while (true) {
            MultiThreadMergeSort[] t = new MultiThreadMergeSort[threadNum]; //根据线程数量创建线程数组
            s = new Semaphore[threadNum];   //根据线程数量创建信号量数组
            for (int i = 0; i < threadNum; i++) {
                t[i] = new MultiThreadMergeSort(Integer.toString(i), start[i], mid[i], end[i], flag);   //创建线程，分配区域
                s[i] = new Semaphore(0);    //创建对应信号量
            }

            //启动所有线程
            for (int i = 0; i < threadNum; i++) {
                t[i].start();
            }

            //等待所有信号量，即等待所有线程结束
            for (int i = 0; i < threadNum; i++) {
                try {
                    s[i].acquire(); //获取信号量
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }

            flag = false;   //第一轮操作之后都执行合并操作

            if (threadNum == 1 && !hasExtraDiv) break;  //线程数量为1且运行结束时没有剩余区域，则退出循环
            else if (threadNum == 1 && hasExtraDiv) {   //线程数量为1，有剩余区域，此时需要多执行一次
                mid[0] = end[0];
                end[0] = end[extraDivIndex];
                hasExtraDiv = false;
            } else if (threadNum > 1 && !hasExtraDiv) { //线程大于1且没有剩余区域
                int newThreadNum = threadNum / 2;
                for (int i = 0; i < newThreadNum; i++) {    //分配区域
                    start[i] = start[i * 2];
                    mid[i] = end[i * 2];
                    end[i] = end[i * 2 + 1];
                }
                if (threadNum % 2 == 1) {   //若线程数量为奇数，会产生剩余区域，需要记录
                    hasExtraDiv = true;
                    extraDivIndex = threadNum - 1;
                }
                threadNum = newThreadNum;
            } else if (threadNum > 1 && hasExtraDiv) {  //线程数量大于1，且有剩余区域
                int newThreadNum = threadNum / 2;
                for (int i = 0; i < newThreadNum; i++) {
                    start[i] = start[i * 2];
                    mid[i] = end[i * 2];
                    end[i] = end[i * 2 + 1];
                }
                if (threadNum % 2 == 1) {   //若线程数量为奇数，可以将剩余区域包括进分配过程，去除剩余区域
                    start[newThreadNum] = start[newThreadNum * 2];
                    mid[newThreadNum] = end[newThreadNum * 2];
                    end[newThreadNum] = end[extraDivIndex];
                    hasExtraDiv = false;
                    newThreadNum += 1;
                }
                threadNum = newThreadNum;
            }
        }

        //检验排序正确性
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                System.out.println("排序错误");
                break;
            }
        }
    }
}
