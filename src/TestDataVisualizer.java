import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TestDataVisualizer {
    private static TestData[] testData;  //测试数据数组

    private static int len; //数组长度

    public static void loadTestData(String dir) {
        try {
            File file = new File(dir);
            if (!file.exists()) file.createNewFile();
            //创建BufferedReader读取文件内容
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while (br.readLine() != null) {
                len++;
            }
            br.close();
            testData = new TestData[len];
            br = new BufferedReader(new FileReader(file));
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] str = line.split(",");
                testData[i].dataQty = Integer.parseInt(str[0]);
                testData[i].threadNum = Integer.parseInt(str[1]);
                testData[i].costTime = Long.parseLong(str[2]);
                i++;
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("实验结果文件中数据条数为：" + len);
    }
}

class TestData {
    public int dataQty;
    public int threadNum;
    public long costTime;

    public TestData(int dataQty, int threadNum, long costTime) {
        this.dataQty = dataQty;
        this.threadNum = threadNum;
        this.costTime = costTime;
    }
}