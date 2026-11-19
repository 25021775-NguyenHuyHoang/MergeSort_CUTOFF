
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ExperimentRunner {

    // Các giá trị CUTOFF cần được đưa vào thử nghiệm.
    private static final int[] CUTOFFS = {
            1, 4, 8, 16, 32, 48, 64
    };

    // Các kích thước mảng được sử dụng trong thực nghiệm.
    private static final int[] SIZES = {
            1_000,
            10_000,
            50_000,
            100_000
    };

    // Sáu nhóm dữ liệu dùng để đánh giá thuật toán.
    private static final String[] DATA_TYPES = {

            "Random",
            "Sorted",
            "Reversed",
            "Duplicates",
            "NearlySorted"
    };

    // Các giá trị Seed để tạo nhiều bộ dữ liệu khác nhau.
    private static final long[] SEEDS = {
            12345L,
            67890L
    };

    // Số lần khởi động JVM trước khi đo thực nghiệm.
    private static final int WARMUPS = 3;

    // Số lần đo thực nghiệm cho mỗi cấu hình.
    private static final int REPETITIONS = 20;

    // Xuất các bộ dữ liệu đầu vào thành file CSV.
    private static void exportDatasets() {

        // Xác định thư mục gốc của project.
        File root = new File(
                System.getProperty("user.dir"),
                "datasets"
        );

        // Tạo thư mục datasets nếu chưa tồn tại.
        try {
            Files.createDirectories(root.toPath());

        } catch (IOException e) {

            System.err.println(
                    "Không thể tạo thư mục datasets: "
                            + e.getMessage()
            );

            return;
        }

        System.out.println(
                "Thư mục lưu dữ liệu: "
                        + root.getAbsolutePath()
        );

        if (!root.exists() && !root.mkdirs()) {

            System.err.println(
                    "Không thể tạo thư mục datasets."
            );

            return;
        }

        // Duyệt qua từng loại dữ liệu.
        for (String type : DATA_TYPES) {

            // Tạo thư mục riêng cho từng loại dữ liệu.
            File typeFolder = new File(root, type);

            try {

                Files.createDirectories(typeFolder.toPath());

            } catch (IOException e) {

                System.err.println(
                        "Không thể tạo thư mục: "
                                + typeFolder.getAbsolutePath()
                );

                continue;
            }

            // Dữ liệu ngẫu nhiên cần hai Seed.
            // Sorted và Reversed không phụ thuộc Seed.
            long[] seedsToUse;

            if (type.equals("Sorted")
                    || type.equals("Reversed")) {

                seedsToUse = new long[]{0L};

            } else {

                seedsToUse = SEEDS;
            }

            // Duyệt qua từng kích thước mảng.
            for (int size : SIZES) {

                // Duyệt qua từng Seed.
                for (long seed : seedsToUse) {

                    int[] data = generateData(
                            type,
                            size,
                            seed
                    );

                    // Xác định tên Seed dùng trong tên file.
                    String seedLabel;

                    if (type.equals("Sorted") || type.equals("Reversed")) {
                        seedLabel = "NA";
                    } else {
                        seedLabel = String.valueOf(seed);
                    }

                    // Tạo tên file dữ liệu.
                    String fileName =
                            type
                                    + "_N" + size
                                    + "_Seed" + seedLabel
                                    + ".csv";

                    File file = new File(
                            typeFolder,
                            fileName
                    );

                    // Ghi dữ liệu vào file CSV.
                    try {

                        // Đảm bảo thư mục chứa file đã tồn tại.
                        Files.createDirectories(
                                file.toPath().getParent()
                        );

                        try (
                                BufferedWriter writer =
                                        new BufferedWriter(
                                                new FileWriter(file)
                                        )
                        ) {

                            writer.write("Value");
                            writer.newLine();

                            for (int value : data) {

                                writer.write(
                                        String.valueOf(value)
                                );

                                writer.newLine();
                            }

                            System.out.println(
                                    "Đã xuất dữ liệu: "
                                            + file.getAbsolutePath()
                            );
                        }

                    } catch (IOException e) {

                        System.err.println(
                                "Lỗi xuất dữ liệu: "
                                        + file.getAbsolutePath()
                                        + " ("
                                        + e.getMessage()
                                        + ")"
                        );
                    }
                }
            }
        }

        System.out.println(
                "\nĐÃ XUẤT HOÀN TẤT BỘ DỮ LIỆU!"
        );
    }


    public static void runExperiments() {

        // Ghi nhận thời điểm bắt đầu toàn bộ chương trình.
        long experimentStart = System.nanoTime();

        // Xuất bộ dữ liệu đầu vào.
        exportDatasets();

        // Tạo thư mục lưu kết quả nếu chưa tồn tại.
        File folder = new File("results");

        if (!folder.exists() && !folder.mkdirs()) {

            System.err.println(
                    "Không thể tạo thư mục results."
            );

            return;
        }

        File file = new File(folder, "results.csv");

        // Sử dụng Seed cố định để có thể tái lập thứ tự thử nghiệm.
        Random orderRandom = new Random(20261111L);

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "       MERGE SORT - CUTOFF EXPERIMENT"
        );

        System.out.println(
                "=========================================="
        );

        System.out.println(
                "Số loại dữ liệu: " + DATA_TYPES.length
        );

        System.out.println(
                "Số kích thước: " + SIZES.length
        );

        System.out.println(
                "Số giá trị CUTOFF: " + CUTOFFS.length
        );

        System.out.println(
                "Số Seed: " + SEEDS.length
        );

        System.out.println(
                "Số lần Warm-up: " + WARMUPS
        );

        System.out.println(
                "Số lần đo: " + REPETITIONS
        );

        System.out.println(
                "Bắt đầu thực nghiệm...\n"
        );


        int completed = 0;

        try (
                BufferedWriter writer =
                        new BufferedWriter(
                                new FileWriter(file)
                        )
        ) {

            // Ghi tiêu đề các cột trong file CSV.
            writer.write(
                    "DataType,Size,Cutoff,Seed,"
                            + "MedianTimeMs,MinTimeMs,MaxTimeMs,"
                            + "Repetitions"
            );

            writer.newLine();

            // Duyệt qua từng nhóm dữ liệu.
            for (String type : DATA_TYPES) {

                // Duyệt qua từng kích thước mảng.
                for (int size : SIZES) {

                    // Xác định số lượng Seed cần sử dụng.
                    long[] seedsToUse;

                    if (type.equals("Sorted") || type.equals("Reversed")) {

                        // Hai loại dữ liệu này không cần Seed.
                        seedsToUse = new long[]{0L};

                    } else {

                        // Các loại dữ liệu còn lại sử dụng hai Seed.
                        seedsToUse = SEEDS;
                    }

                    // Duyệt qua các Seed tương ứng.
                    for (long seed : seedsToUse) {

                        // Tạo dữ liệu đầu vào.
                        int[] original = generateData(type, size, seed);

                        // Tạo danh sách các giá trị CUTOFF.
                        List<Integer> cutoffOrder =
                                new ArrayList<>();

                        for (int cutoff : CUTOFFS) {

                            cutoffOrder.add(cutoff);
                        }

                        // Xáo trộn thứ tự thử nghiệm CUTOFF.
                        Collections.shuffle(
                                cutoffOrder,
                                orderRandom
                        );

                        // Tiến hành đo với từng giá trị CUTOFF.
                        for (int cutoff : cutoffOrder) {

                            Benchmark.Result result =
                                    Benchmark.measure(
                                            original,
                                            cutoff,
                                            WARMUPS,
                                            REPETITIONS
                                    );

                            // Ghi kết quả thực nghiệm vào file CSV.
                            writer.write(
                                    String.format(
                                            java.util.Locale.US,

                                            "%s,%d,%d,%s,"
                                                    + "%.6f,%.6f,%.6f,%d",

                                            type,
                                            size,
                                            cutoff,
                                            (type.equals("Sorted") || type.equals("Reversed"))
                                                    ? "N/A"
                                                    : String.valueOf(seed),

                                            result.getMedianTimeMs(),
                                            result.getMinTimeMs(),
                                            result.getMaxTimeMs(),

                                            REPETITIONS
                                    )
                            );

                            writer.newLine();

                            completed++;

                            // Hiển thị kết quả lên màn hình.
                            System.out.printf(
                                    java.util.Locale.US,

                                    "%-13s | Size: %7d"
                                            + " | Seed: %5d"
                                            + " | CUTOFF: %2d"
                                            + " | Median: %10.4f ms"
                                            + " | Min: %10.4f ms"
                                            + " | Max: %10.4f ms%n",

                                    type,
                                    size,
                                    seed,
                                    cutoff,

                                    result.getMedianTimeMs(),
                                    result.getMinTimeMs(),
                                    result.getMaxTimeMs()
                            );
                        }

                        // Lưu kết quả sau mỗi bộ dữ liệu.
                        writer.flush();
                    }
                }
            }

            double totalSeconds =
                    (System.nanoTime() - experimentStart)
                            / 1_000_000_000.0;

            System.out.println(
                    "\n=========================================="
            );

            System.out.println(
                    "THỰC NGHIỆM ĐÃ HOÀN THÀNH!"
            );

            System.out.println(
                    "Số dòng kết quả: " + completed
            );

            System.out.printf(
                    java.util.Locale.US,
                    "Tổng thời gian: %.2f giây%n",
                    totalSeconds
            );

            System.out.println(
                    "File kết quả: " + file.getPath()
            );

            System.out.println(
                    "=========================================="
            );

        } catch (IOException e) {

            System.err.println(
                    "Lỗi ghi file: " + e.getMessage()
            );
        }
    }


    // Tạo dữ liệu tương ứng với từng nhóm kiểm thử.
    private static int[] generateData(
            String type,
            int size,
            long seed) {

        switch (type) {

            case "Random":

                return DataGenerator.randomArray(
                        size,
                        seed
                );

            case "Sorted":

                return DataGenerator.sortedArray(
                        size
                );

            case "Reversed":

                return DataGenerator.reversedArray(
                        size
                );

            case "Duplicates":

                return DataGenerator.duplicateArray(
                        size,
                        seed
                );

            case "NearlySorted":

                return DataGenerator.nearlySortedArray(
                        size,
                        seed
                );

            case "AllEqual":

                return DataGenerator.allEqualArray(
                        size,
                        7
                );

            default:

                throw new IllegalArgumentException(
                        "Loại dữ liệu không hợp lệ: " + type
                );
        }
    }
}
