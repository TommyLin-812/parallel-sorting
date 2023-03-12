import javax.swing.*;

public class TestSwing {
    public static void main(String[] args) {
        JFrame jf = new JFrame();

        jf.setSize(250, 250);   // 设置窗口大小
        jf.setLocationRelativeTo(null); // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        JButton btn = new JButton("测试按钮");
        panel.add(btn);

        jf.setContentPane(panel);

        jf.setVisible(true);
    }
}
