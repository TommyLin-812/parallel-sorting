import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;

import static tech.tablesaw.aggregate.AggregateFunctions.mean;

public class TestDataVisualizer {
    private static Table testDataSet;

    public static void loadTestData(String dir) {
        CsvReadOptions.Builder builder =
                CsvReadOptions.builder(dir)
                        .separator(',')
                        .header(true);
        CsvReadOptions options = builder.build();
        testDataSet = Table.read().usingOptions(options);
        testDataSet.setName(dir);
    }

    public static void showAverageCostTime() {
        Table result = testDataSet.summarize("costTime", mean).by("threadNum").sortAscendingOn("threadNum");
        result.setName("AverageCostTime by ThreadNum");

        Plot.show(
                LinePlot.create(testDataSet.name(), result, "threadNum", "Mean [costTime]")
        );
    }
}