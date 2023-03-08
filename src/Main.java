import java.io.*;

public class Main {
    public static void main(String[] args) {
        //写文件
        Randomizer.setQty(1000000);
        Randomizer.setMin(0);
        Randomizer.setMax(1000000);
        try {
            Randomizer.writeRandomNumbers("RandomDataSource.txt");
        } catch (IOException e) {
            System.out.println(e.getMessage());
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

        //排序
        //SingleThreadMergeSort.merge_sort(arr);
        MultiThreadMergeSort.initArray(arr);
        MultiThreadMergeSort.startSorting();

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