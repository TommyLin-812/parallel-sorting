import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Math.random;

public class Main {
    private static int[] arr = new int[0];

    private static int len = 0;

    private static int qty;

    private static JFrame frame;

    public static void main(String[] args) {
        loadUI();

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
            saveTestData("TestData-" + qty + ".csv", threadNum, costTime);
        }

        TestDataVisualizer.loadTestData("TestData-1000000.csv");
        TestDataVisualizer.showAverageCostTime();
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

    private static void saveTestData(String dir, int threadNum, long costTime) {
        try {
            File file = new File(dir);
            FileWriter fw;
            if (!file.exists()) {
                file.createNewFile();
                fw = new FileWriter(file, true);
                fw.write("threadNum,costTime\n");
                fw.flush();
                fw.close();
            }
            fw = new FileWriter(file, true);
            fw.write("" + threadNum + "," + costTime + "\n");
            fw.flush();
            fw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loadUI() {
        frame = new JFrame("并行排序");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        GridBagLayout gridBag = new GridBagLayout();
        JPanel panel_main = new JPanel(gridBag);
        GridBagConstraints c;

        JLabel label_source_data = new JLabel("待排序数据文件：");
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(label_source_data, c);

        JTextField text_source_file = new JTextField(12);
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(text_source_file, c);

        JButton btn_select_file = new JButton("浏览");
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn_select_file, c);

        JButton btn_create_random = new JButton("生成");
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(btn_create_random, c);

        JLabel label_thread_num = new JLabel("线程数量：");
        c = new GridBagConstraints();
        c.anchor=GridBagConstraints.EAST;
        gridBag.addLayoutComponent(label_thread_num, c);

        JTextField text_thread_num = new JTextField(4);
        c = new GridBagConstraints();
        c.anchor=GridBagConstraints.WEST;
        gridBag.addLayoutComponent(text_thread_num, c);

        JButton btn_execute_sort = new JButton("执行排序");
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(btn_execute_sort, c);

        panel_main.add(label_source_data);
        panel_main.add(text_source_file);
        panel_main.add(btn_select_file);
        panel_main.add(btn_create_random);
        panel_main.add(label_thread_num);
        panel_main.add(text_thread_num);
        panel_main.add(btn_execute_sort);

        tabbedPane.addTab("测试", panel_main);

        tabbedPane.setSelectedIndex(0);

        frame.setContentPane(tabbedPane);
        frame.setVisible(true);
    }
}