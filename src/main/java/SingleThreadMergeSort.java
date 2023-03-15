public class SingleThreadMergeSort {
    /**
     * 递归归并排序
     *
     * @param arr    目标数组
     * @param result 辅助数组
     * @param start  起始位置
     * @param end    结束位置
     */
    public static void merge_sort_recursive(int[] arr, int[] result, int start, int end) {
        if (start >= end)
            return;
        int len = end - start, mid = (len >> 1) + start;
        merge_sort_recursive(arr, result, start, mid);
        merge_sort_recursive(arr, result, mid + 1, end);
        merge(arr, result, start, mid, end);
    }

    public static void merge_sort(int[] arr) {
        int len = arr.length;
        int[] result = new int[len];
        merge_sort_recursive(arr, result, 0, len - 1);
    }

    /**
     * 两个有序部分的合并操作
     *
     * @param arr    目标数组
     * @param result 辅助数组
     * @param start  起始位置
     * @param mid    中心边界
     * @param end    结束位置
     */
    public static void merge(int[] arr, int[] result, int start, int mid, int end) {
        int start1 = start;
        int start2 = mid + 1;
        int k = start;
        while (start1 <= mid && start2 <= end)
            result[k++] = arr[start1] < arr[start2] ? arr[start1++] : arr[start2++];
        while (start1 <= mid)
            result[k++] = arr[start1++];
        while (start2 <= end)
            result[k++] = arr[start2++];
        for (k = start; k <= end; k++)
            arr[k] = result[k];
    }
}
