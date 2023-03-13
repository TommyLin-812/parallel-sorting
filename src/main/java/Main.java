import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.Objects;

import static java.lang.Math.random;

public class Main {
    private static int[] arr = new int[0];

    private static int qty;

    private static JFrame frame;

    public static void main(String[] args) {
        loadUI();
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
            qty = 0;
            File file = new File(dir);
            if (!file.exists()) file.createNewFile();
            //创建BufferedReader读取文件内容
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while (br.readLine() != null) {
                qty++;
            }
            arr = new int[qty];
            br = new BufferedReader(new FileReader(file));
            int i = 0;
            while ((line = br.readLine()) != null) {
                arr[i++] = Integer.parseInt(line);
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
        frame.setSize(550, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();

        GridBagLayout gridBag = new GridBagLayout();
        JPanel panel_main = new JPanel(gridBag);
        GridBagConstraints c;

        JLabel label_source_data = new JLabel("待排序数据文件：");
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(label_source_data, c);
        panel_main.add(label_source_data);

        JTextField text_source_file = new JTextField(16);
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(text_source_file, c);
        panel_main.add(text_source_file);

        JButton btn_source_file = new JButton("浏览");
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn_source_file, c);
        panel_main.add(btn_source_file);

        JButton btn_create_random = new JButton("生成");
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(btn_create_random, c);
        panel_main.add(btn_create_random);

        JLabel label_thread_num = new JLabel("线程数量：");
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        gridBag.addLayoutComponent(label_thread_num, c);
        panel_main.add(label_thread_num);

        JTextField text_thread_num = new JTextField(4);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        gridBag.addLayoutComponent(text_thread_num, c);
        panel_main.add(text_thread_num);

        JButton btn_execute_sort = new JButton("执行排序");
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(btn_execute_sort, c);
        panel_main.add(btn_execute_sort);

        JLabel label_data_qty = new JLabel("文件中待排序数据个数：");
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST;
        gridBag.addLayoutComponent(label_data_qty, c);
        panel_main.add(label_data_qty);

        JLabel label_test_result = new JLabel("实验结果：");
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST;
        gridBag.addLayoutComponent(label_test_result, c);
        panel_main.add(label_test_result);

        JButton btn_save_result = new JButton("保存排序结果");
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(btn_save_result, c);
        btn_save_result.setEnabled(false);
        panel_main.add(btn_save_result);

        btn_source_file.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileFilter(new FileNameExtensionFilter("txt文件(*.txt)", "txt"));

            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                text_source_file.setText(file.getName());
            }
        });

        btn_create_random.addActionListener(e -> {
            qty = Integer.parseInt(JOptionPane.showInputDialog(frame, "输入随机数据个数：(0 ~ 2147483647)"));
            createRandomData("RandomData.txt", qty, 0, 1000000);
            text_source_file.setText("RandomData.txt");
            label_data_qty.setText("文件中待排序数据个数：" + qty);
        });

        btn_execute_sort.addActionListener(e -> {
            if (Objects.equals(text_source_file.getText(), "")) {
                JOptionPane.showMessageDialog(
                        frame,
                        "未选择待排序数据文件！",
                        "错误",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (Objects.equals(text_thread_num.getText(), "")) {
                JOptionPane.showMessageDialog(
                        frame,
                        "未输入线程数量！",
                        "错误",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            int threadNum = Integer.parseInt(text_thread_num.getText());
            if (threadNum < 1) {
                JOptionPane.showMessageDialog(
                        frame,
                        "线程数量不得小于0！",
                        "错误",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            btn_execute_sort.setEnabled(false);
            loadRandomData(text_source_file.getText());
            label_data_qty.setText("文件中待排序数据个数：" + qty);

            long startTime = System.currentTimeMillis();
            MultiThreadMergeSort.initArray(arr);
            MultiThreadMergeSort.startSorting(threadNum);
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;

            label_test_result.setText("实验结果：排序数据数量为" + qty + "个，线程数量为" + threadNum + "个，排序共耗时" + costTime + "毫秒。");

            saveTestData("TestData-" + qty + ".csv", threadNum, costTime);

            btn_execute_sort.setEnabled(true);

            btn_save_result.setEnabled(true);
        });

        btn_save_result.addActionListener(e -> saveSortedData("SortedData.txt"));

        tabbedPane.addTab("测试", panel_main);

        JPanel panel_visualize = new JPanel(new FlowLayout());
        JLabel label_test_data = new JLabel("实验数据文件：");
        panel_visualize.add(label_test_data);
        JTextField text_test_data = new JTextField(16);
        panel_visualize.add(text_test_data);
        JButton btn_test_data = new JButton("浏览");
        panel_visualize.add(btn_test_data);
        JButton btn_visualize = new JButton("显示折线图");
        panel_visualize.add(btn_visualize);

        btn_test_data.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV文件(*.csv)", "csv"));

            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                text_test_data.setText(file.getName());
            }
        });

        btn_visualize.addActionListener(e -> {
            TestDataVisualizer.loadTestData(text_test_data.getText());
            TestDataVisualizer.showAverageCostTime();
        });

        tabbedPane.addTab("数据可视化", panel_visualize);

        tabbedPane.setSelectedIndex(0);

        frame.setContentPane(tabbedPane);
        frame.setVisible(true);
    }
}