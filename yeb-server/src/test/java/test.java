import java.text.DecimalFormat;

/**
 * @description: test
 * @author: 吉祥
 * @created: 2021/11/01 20:49
 */
public class test {
    public static void main(String[] args) {
        int a = 678;
        System.out.println(a/365.0);
        DecimalFormat decimalFormat = new DecimalFormat("##.00");
        String format = decimalFormat.format(a / 365);
        System.out.println(format);
    }
}

