
import java.util.Random;
import java.util.Arrays;

public class DataGenerator {

    // 1. Tạo mảng chứa các giá trị ngẫu nhiên.
    public static int[] randomArray(int size, long seed) {

        Random random = new Random(seed);

        int[] a = new int[size];

        for (int i = 0; i < size; i++) {

            a[i] = random.nextInt(100000);
        }

        return a;
    }

    // 2. Tạo mảng có các phần tử được sắp xếp tăng dần.
    public static int[] sortedArray(int size) {

        int[] a = new int[size];

        for (int i = 0; i < size; i++) {

            a[i] = i;
        }

        return a;
    }

    // 3. Tạo mảng có các phần tử được sắp xếp giảm dần.
    public static int[] reversedArray(int size) {

        int[] a = new int[size];

        for (int i = 0; i < size; i++) {

            a[i] = size - 1 - i;
        }

        return a;
    }

    // 4. Tạo mảng có nhiều phần tử trùng lặp.
    public static int[] duplicateArray(int size, long seed) {

        Random random = new Random(seed);

        int[] a = new int[size];

        for (int i = 0; i < size; i++) {

            a[i] = random.nextInt(10);
        }

        return a;
    }

    // 5. Tạo mảng gần như đã được sắp xếp tăng dần.
    public static int[] nearlySortedArray(int size, long seed) {

        int[] a = sortedArray(size);

        if (size < 2) {
            return a;
        }

        Random random = new Random(seed);

        // Thực hiện thay đổi vị trí khoảng 1% số phần tử.
        int swaps = Math.max(1, size / 100);

        for (int i = 0; i < swaps; i++) {

            int first = random.nextInt(size);
            int second = random.nextInt(size);

            int temp = a[first];

            a[first] = a[second];

            a[second] = temp;
        }

        return a;
    }

    // 6. Tạo mảng có tất cả phần tử mang cùng một giá trị.
    public static int[] allEqualArray(int size, int value) {

        int[] a = new int[size];

        Arrays.fill(a, value);

        return a;
    }
}
