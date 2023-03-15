import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.util.Objects;

import static java.lang.Math.random;

public class Main {
    private static int[] arr = new int[0];  //执行操作的数组

    private static int qty; //数据规模

    private static JFrame frame;    //程序界面主窗口

    public static void main(String[] args) {
        loadUI();
    }

    /**
     * 生成随机数据
     *
     * @param dir 目标文件路径
     * @param qty 数据规模
     * @param min 随机数最小值
     * @param max 随机数最大值
     */
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

    /**
     * 从文件加载随机数据
     *
     * @param dir 目标文件路径
     */
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

    /**
     * 保存排序结果到文件
     *
     * @param dir 目标文件路径
     */
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

    /**
     * 保存实验结果到文件
     *
     * @param dir       目标文件路径
     * @param threadNum 线程数量
     * @param costTime  排序耗时
     */
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

    /**
     * 加载程序界面
     */
    private static void loadUI() {
        //主窗口初始化
        frame = new JFrame("并行排序");
        frame.setSize(550, 300);
        frame.setLocationRelativeTo(null);  //屏幕居中
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  //关闭窗口时退出程序

        JTabbedPane tabbedPane = new JTabbedPane(); //创建选项卡面板

        GridBagLayout gridBag = new GridBagLayout();    //创建网格袋布局
        JPanel panel_main = new JPanel(gridBag);
        GridBagConstraints c;

        //提示标签
        JLabel label_source_data = new JLabel("待排序数据文件：");
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(label_source_data, c);
        panel_main.add(label_source_data);

        //显示待排序文件名称的文本框
        JTextField text_source_file = new JTextField(16);
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(text_source_file, c);
        panel_main.add(text_source_file);

        //"浏览"按钮
        JButton btn_source_file = new JButton("浏览");
        c = new GridBagConstraints();
        gridBag.addLayoutComponent(btn_source_file, c);
        panel_main.add(btn_source_file);

        //"生成"按钮
        JButton btn_create_random = new JButton("生成");
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(btn_create_random, c);
        panel_main.add(btn_create_random);

        //提示标签
        JLabel label_thread_num = new JLabel("线程数量：");
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        gridBag.addLayoutComponent(label_thread_num, c);
        panel_main.add(label_thread_num);

        //"线程数量"文本框
        JTextField text_thread_num = new JTextField(4);
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        gridBag.addLayoutComponent(text_thread_num, c);
        panel_main.add(text_thread_num);

        //"执行排序"按钮
        JButton btn_execute_sort = new JButton("执行排序");
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(btn_execute_sort, c);
        panel_main.add(btn_execute_sort);

        //"数据规模"标签
        JLabel label_data_qty = new JLabel("文件中待排序数据个数：");
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST;
        gridBag.addLayoutComponent(label_data_qty, c);
        panel_main.add(label_data_qty);

        //"实验结果"标签
        JLabel label_test_result = new JLabel("实验结果：");
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.anchor = GridBagConstraints.WEST;
        gridBag.addLayoutComponent(label_test_result, c);
        panel_main.add(label_test_result);

        //"保存实验结果"按钮
        JButton btn_save_result = new JButton("保存排序结果");
        c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridBag.addLayoutComponent(btn_save_result, c);
        btn_save_result.setEnabled(false);  //初始情况下无排序结果，设置为不可用
        panel_main.add(btn_save_result);

        //"浏览"按钮的点击事件，选择待排序数据文件
        btn_source_file.addActionListener(e -> {
            //创建文件选择器
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(".")); //默认目录为当前目录
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);  //只能选择文件
            fileChooser.setMultiSelectionEnabled(false);    //只能单选
            fileChooser.setFileFilter(new FileNameExtensionFilter("txt文件(*.txt)", "txt"));  //默认过滤txt文件

            //获取选择的文件的名称
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                text_source_file.setText(file.getName());
            }
        });

        //"生成"按钮的点击事件
        btn_create_random.addActionListener(e -> {
            //弹出对话框，输入信息
            qty = Integer.parseInt(JOptionPane.showInputDialog(frame, "输入随机数据个数：(0 ~ 2147483647)"));
            int min = Integer.parseInt(JOptionPane.showInputDialog(frame, "输入随机数最小值：(-2147483648 ~ 2147483647)"));
            int max = Integer.parseInt(JOptionPane.showInputDialog(frame, "输入随机数最大值：(-2147483648 ~ 2147483647)"));

            //生成随机数据并更新信息
            createRandomData("RandomData.txt", qty, min, max);
            text_source_file.setText("RandomData.txt");
            label_data_qty.setText("文件中待排序数据个数：" + qty);
        });

        //"执行排序"按钮的点击事件
        btn_execute_sort.addActionListener(e -> {
            //检查待排序数据文件是否为空
            if (Objects.equals(text_source_file.getText(), "")) {
                JOptionPane.showMessageDialog(
                        frame,
                        "未选择待排序数据文件！",
                        "错误",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            //检查线程数量是否为空
            if (Objects.equals(text_thread_num.getText(), "")) {
                JOptionPane.showMessageDialog(
                        frame,
                        "未输入线程数量！",
                        "错误",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            //检查线程数量大小
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

            btn_execute_sort.setEnabled(false); //禁用该按钮，避免出错
            loadRandomData(text_source_file.getText()); //加载待排序数据
            label_data_qty.setText("文件中待排序数据个数：" + qty);    //更新信息

            //执行排序并计时
            MultiThreadMergeSort.initArray(arr);
            long startTime = System.currentTimeMillis();
            MultiThreadMergeSort.startSorting(threadNum);
            long endTime = System.currentTimeMillis();

            long costTime = endTime - startTime;    //计算排序耗时

            //更新信息
            label_test_result.setText("实验结果：排序数据数量为" + qty + "个，线程数量为" + threadNum + "个，排序共耗时" + costTime + "毫秒。");

            saveTestData("TestData-" + qty + ".csv", threadNum, costTime);  //保存实验结果

            btn_execute_sort.setEnabled(true);  //恢复该按钮

            btn_save_result.setEnabled(true);   //使"保存排序结果"按钮可用
        });

        //"保存排序结果"按钮的点击事件
        btn_save_result.addActionListener(e -> saveSortedData("SortedData.txt"));

        tabbedPane.addTab("测试", panel_main);//在选项卡面板中添加该布局

        JPanel panel_visualize = new JPanel(new FlowLayout());  //创建流式布局

        JLabel label_test_data = new JLabel("实验数据文件："); //提示标签
        panel_visualize.add(label_test_data);
        JTextField text_test_data = new JTextField(16); //显示实验结果文件名称的文本框
        panel_visualize.add(text_test_data);
        JButton btn_test_data = new JButton("浏览");  //"浏览"按钮
        panel_visualize.add(btn_test_data);
        JButton btn_visualize = new JButton("显示折线图");   //"显示折线图"按钮
        panel_visualize.add(btn_visualize);

        //"浏览"按钮的点击事件，选择实验结果文件
        btn_test_data.addActionListener(e -> {
            //创建文件选择器
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.setFileFilter(new FileNameExtensionFilter("CSV文件(*.csv)", "csv"));  //默认过滤csv文件

            //获取选择的文件的名称
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                text_test_data.setText(file.getName());
            }
        });

        //"显示折线图"按钮的点击事件
        btn_visualize.addActionListener(e -> {
            TestDataVisualizer.loadTestData(text_test_data.getText());  //加载实验结果文件
            TestDataVisualizer.showAverageCostTime();   //显示折线图
        });

        tabbedPane.addTab("数据可视化", panel_visualize);    //在选项卡面板中添加该布局

        tabbedPane.setSelectedIndex(0); //默认选中第一个选项卡

        frame.setContentPane(tabbedPane);   //设置窗口显示内容为该选项卡面板
        frame.setVisible(true); //显示窗口
    }
}