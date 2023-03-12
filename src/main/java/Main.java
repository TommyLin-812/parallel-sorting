import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.AreaPlot;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Math.random;

public class Main {
    private static int[] arr = new int[0];

    private static int len = 0;

    private static int qty;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean noCreated = true;
        System.out.println("是否重新生成随机数？[y/n]");
        if (Objects.equals(scanner.next(), "y")) {
            System.out.println("请输入随机数据个数：");
            qty = scanner.nextInt();

            System.out.println("请输入随机数据最小值：");
            int min = scanner.nextInt();

            System.out.println("请输入随机数据最大值：");
            int max = scanner.nextInt();

            System.out.println("正在生成...");
            createRandomData("RandomData.txt", qty, min, max);
            len = qty;
            noCreated = false;
        }

        if (noCreated) {
            //读文件
            System.out.println("正在读取随机数据...");
            String dir = "RandomData.txt";
            loadRandomData(dir);
        }

        System.out.println("请输入线程数量：");
        int threadNum = scanner.nextInt();

        long startTime = System.currentTimeMillis();
        //SingleThreadMergeSort.merge_sort(arr);
        MultiThreadMergeSort.initArray(arr);
        MultiThreadMergeSort.startSorting(threadNum);

        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        System.out.printf("执行时长：%d 毫秒.\n", (costTime));

        //写文件
        System.out.println("是否保存排序结果？[y/n]");
        if (Objects.equals(scanner.next(), "y")) {
            System.out.println("正在写入排序结果...");
            saveSortedData("SortedData.txt");
        }

        //保存实验数据
        System.out.println("是否保存实验数据？[y/n]");
        if (Objects.equals(scanner.next(), "y")) {
            System.out.println("正在写入实验数据...");
            saveTestData("TestData.csv", qty, threadNum, costTime);
        }

        Table table = Table.read().csv("TestData.csv");
        Plot.show(
                AreaPlot.create(
                        "Test",
                        table, "qty", "costTime"));
    }

    private static void createRandomData(String dir, int qty, int min, int max) {
        //写文件
        try {
            arr = new int[qty];

            int num;
            File file = new File(dir);
            if (!file.exists()) file.createNewFile();   //如果文件不存在，创建文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));   //创建BufferedWriter对象

            //向文件中写入内容
            for (int i = 0; i < qty; i++) {
                num = (int) (random() * (max - min) + min);
                arr[i] = num;
                bw.write(num + "\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loadRandomData(String dir) {
        try {
            File file = new File(dir);
            if (!file.exists()) file.createNewFile();
            //创建BufferedReader读取文件内容
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while (br.readLine() != null) {
                len++;
            }
            qty = len;
            arr = new int[len];
            br = new BufferedReader(new FileReader(file));
            int i = 0;
            while ((line = br.readLine()) != null) {
                arr[i++] = Integer.parseInt(line);
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("文件中待排序数据个数为：" + len);
    }

    private static void saveSortedData(String dir) {
        try {
            File file = new File(dir);
            if (!file.exists()) file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (int i : arr) {
                bw.write(i + "\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void saveTestData(String dir, int qty, int threadNum, long costTime) {
        try {
            File file = new File(dir);
            FileWriter fw;
            if (!file.exists()) {
                file.createNewFile();
                fw = new FileWriter(file, true);
                fw.write("qty,threadNum,costTime\n");
                fw.flush();
                fw.close();
            }
            fw = new FileWriter(file, true);
            fw.write("" + qty + "," + threadNum + "," + costTime + "\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}