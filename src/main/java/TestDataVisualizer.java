import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;

import static tech.tablesaw.aggregate.AggregateFunctions.mean;

public class TestDataVisualizer {
    private static Table testDataSet;

    private static Table result;

    public static void loadTestData(String dir) {
        System.out.println("正在加载实验数据...");
        CsvReadOptions.Builder builder =
                CsvReadOptions.builder(dir)
                        .separator(',')
                        .header(true);
        CsvReadOptions options = builder.build();
        testDataSet = Table.read().usingOptions(options);
        testDataSet.setName(dir);
        System.out.println("实验结果文件中数据条数为：" + testDataSet.rowCount());
    }

    public static void showAverageCostTime() {
        result = testDataSet.summarize("costTime", mean).by("threadNum");
        result.setName("AverageCostTime by ThreadNum");
        System.out.println(result.print());

        Plot.show(
                LinePlot.create(testDataSet.name(), result, "threadNum", "Mean [costTime]")
        );
    }
}