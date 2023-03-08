import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.lang.Math.random;

public class Randomizer {
    public static void writeRandomNumbers(String dir, int qty, int min, int max) throws IOException {
        int num;
        File file = new File(dir);
        if (!file.exists()) file.createNewFile();   //如果文件不存在，创建文件
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));   //创建BufferedWriter对象

        //向文件中写入内容
        for (int i = 0; i < qty; i++) {
            num = (int) (random() * (max - min) + min);
            bw.write(num + "\n");
        }
        bw.flush();
        bw.close();
    }
}
