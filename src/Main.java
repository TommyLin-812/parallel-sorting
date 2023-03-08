import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("是否重新生成随机数？[y/n]");
        if (Objects.equals(scanner.next(), "y")) {
            //写文件
            Randomizer.setQty(100000000);
            Randomizer.setMin(0);
            Randomizer.setMax(100000000);
            try {
                Randomizer.writeRandomNumbers("RandomDataSource.txt");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }


        //读文件
        int[] arr = new int[0];
        int len = 0;
        String dir = "RandomDataSource.txt";
        try {
            File file = new File(dir);
            if (!file.exists()) file.createNewFile();
            //创建BufferedReader读取文件内容
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while (br.readLine() != null) {
                len++;
            }
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
        dir = "DataOutput.txt";
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
}