import tech.tablesaw.api.Table;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TestDataVisualizer {
    private static Table testDataSet;

    public static void loadTestData(String dir) {
        testDataSet = Table.read().csv(dir);
        //System.out.println("实验结果文件中数据条数为：");
    }
}