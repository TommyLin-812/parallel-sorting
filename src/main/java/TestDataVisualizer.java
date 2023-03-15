import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;

import static tech.tablesaw.aggregate.AggregateFunctions.mean;

public class TestDataVisualizer {
    private static Table testDataSet;   //实验数据表

    /**
     * 加载实验结果文件
     *
     * @param dir 目标文件路径
     */
    public static void loadTestData(String dir) {
        //设置csv文件读取选项
        CsvReadOptions.Builder builder =
                CsvReadOptions.builder(dir) //选择文件路径
                        .separator(',') //分隔符为','
                        .header(true);  //包含标题
        CsvReadOptions options = builder.build();

        testDataSet = Table.read().usingOptions(options);   //读取文件
        testDataSet.setName(dir);   //设置表的名称
    }

    /**
     * 用折线图显示不同线程数量情况下的平均排序耗时
     */
    public static void showAverageCostTime() {
        //按线程数量对排序耗时求平均，并按线程数量升序排序
        Table result = testDataSet.summarize("costTime", mean).by("threadNum").sortAscendingOn("threadNum");

        result.setName("AverageCostTime by ThreadNum"); //设置结果表的名称

        //显示折线图
        Plot.show(
                LinePlot.create(testDataSet.name(), result, "threadNum", "Mean [costTime]")
        );
    }
}