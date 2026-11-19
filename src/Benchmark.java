
import java.util.Arrays;

/**
 * Đo hiệu năng của thuật toán Merge Sort kết hợp Insertion Sort (CUTOFF).
 * Chỉ tính thời gian thực thi của thuật toán sắp xếp.
 */
public class Benchmark {

    private static volatile long blackhole;

    public static final class Result {

        private final double medianTimeMs;
        private final double minTimeMs;
        private final double maxTimeMs;

        public Result(double medianTimeMs,
                      double minTimeMs,
                      double maxTimeMs) {

            this.medianTimeMs = medianTimeMs;
            this.minTimeMs = minTimeMs;
            this.maxTimeMs = maxTimeMs;
        }

        public double getMedianTimeMs() {
            return medianTimeMs;
        }

        public double getMinTimeMs() {
            return minTimeMs;
        }

        public double getMaxTimeMs() {
            return maxTimeMs;
        }
    }

    public static Result measure(
            int[] original,
            int cutoff,
            int warmups,
            int repetitions) {

        if (original == null) {
            throw new IllegalArgumentException(
                    "Dữ liệu đầu vào không được null."
            );
        }

        if (cutoff < 1) {
            throw new IllegalArgumentException(
                    "CUTOFF phải lớn hơn hoặc bằng 1."
            );
        }

        if (warmups < 0) {
            throw new IllegalArgumentException(
                    "Số lần Warm-up không được âm."
            );
        }

        if (repetitions < 1) {
            throw new IllegalArgumentException(
                    "Số lần đo phải lớn hơn 0."
            );
        }

        // Tạo mảng kết quả chuẩn để đối chiếu.
        int[] expected = original.clone();
        Arrays.sort(expected);

        // Khởi động JVM trước khi tiến hành đo thực nghiệm.
        for (int i = 0; i < warmups; i++) {

            int[] test = original.clone();

            MergeSort.sort(test, cutoff);

            verifyResult(test, expected);

            blackhole = checksum(test);
        }

        // Mảng lưu thời gian thực thi của từng lần đo.
        long[] times = new long[repetitions];

        for (int i = 0; i < repetitions; i++) {

            int[] test = original.clone();

            long start = System.nanoTime();

            MergeSort.sort(test, cutoff);

            long end = System.nanoTime();

            // Chỉ tính thời gian thực thi thuật toán sắp xếp.
            times[i] = end - start;

            // Kiểm tra kết quả sau khi kết thúc đo thời gian.
            verifyResult(test, expected);

            blackhole = checksum(test);
        }

        // Sắp xếp mảng thời gian để tính các giá trị thống kê.
        Arrays.sort(times);

        // Tính thời gian trung vị (Median).
        double medianNanos;

        if (repetitions % 2 == 0) {

            medianNanos =
                    times[repetitions / 2 - 1] / 2.0
                            + times[repetitions / 2] / 2.0;

        } else {

            medianNanos = times[repetitions / 2];
        }

        // Trả về thời gian trung vị, nhỏ nhất và lớn nhất.
        return new Result(
                medianNanos / 1_000_000.0,
                times[0] / 1_000_000.0,
                times[repetitions - 1] / 1_000_000.0
        );
    }

    // Kiểm tra tính chính xác của kết quả sắp xếp.
    private static void verifyResult(
            int[] actual,
            int[] expected) {

        if (!Arrays.equals(actual, expected)) {

            throw new IllegalStateException(
                    "Merge Sort cho kết quả sai!"
            );
        }
    }

    // Tính giá trị kiểm tra để bảo đảm kết quả được sử dụng.
    private static long checksum(int[] values) {

        long sum = 0;

        for (int value : values) {

            sum = sum * 31 + value;
        }

        return sum;
    }
}
