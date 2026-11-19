
public class MergeSort {

    // Hàm bắt đầu sắp xếp
    public static void sort(int[] a, int cutoff) {

        // Kiểm tra mảng đầu vào.
        if (a == null) {
            throw new IllegalArgumentException(
                    "Mảng đầu vào không được null"
            );
        }

        // Kiểm tra giá trị CUTOFF.
        if (cutoff < 1) {
            throw new IllegalArgumentException(
                    "CUTOFF phải lớn hơn hoặc bằng 1"
            );
        }

        // Mảng có 0 hoặc 1 phần tử không cần sắp xếp.
        if (a.length <= 1) {
            return;
        }

        // Cấp phát mảng phụ một lần duy nhất.
        int[] aux = new int[a.length];

        // Bắt đầu sắp xếp.
        sort(a, aux, 0, a.length - 1, cutoff);
    }

    // Hàm sắp xếp đệ quy
    private static void sort(int[] a, int[] aux,
                             int lo, int hi, int cutoff) {

        // Nếu đoạn mảng đủ nhỏ, sử dụng Insertion Sort
        if (hi <= lo + cutoff - 1) {
            InsertionSort.sort(a, lo, hi);
            return;
        }

        // Tìm vị trí giữa
        int mid = lo + (hi - lo) / 2;

        // Sắp xếp nửa bên trái
        sort(a, aux, lo, mid, cutoff);

        // Sắp xếp nửa bên phải
        sort(a, aux, mid + 1, hi, cutoff);

        // Trộn hai nửa đã sắp xếp
        merge(a, aux, lo, mid, hi);
    }

    // Hàm trộn hai đoạn mảng
    private static void merge(int[] a, int[] aux,
                              int lo, int mid, int hi) {

        // Sao chép dữ liệu sang mảng phụ
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        // Khởi tạo vị trí hai đoạn
        int i = lo;
        int j = mid + 1;

        // Trộn hai đoạn theo thứ tự tăng dần
        for (int k = lo; k <= hi; k++) {

            // Nếu đoạn trái đã hết
            if (i > mid) {
                a[k] = aux[j];
                j++;
            }

            // Nếu đoạn phải đã hết
            else if (j > hi) {
                a[k] = aux[i];
                i++;
            }

            // Nếu phần tử bên phải nhỏ hơn
            else if (aux[j] < aux[i]) {
                a[k] = aux[j];
                j++;
            }

            // Nếu phần tử bên trái nhỏ hơn hoặc bằng
            else {
                a[k] = aux[i];
                i++;
            }
        }
    }
}
