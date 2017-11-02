package com.yunip.utils.util;

public class Sort {
	
	public static int[] quickSort(int[] arr, int low, int high) {
		// 为后面的递归使用
		int lowStart = low;
		int highEnd = high;
		if (low < high) {
			int pivokey = arr[low];
			while (low < high) {
				// 如果都是大于pivokey，high指针往前移
				while (low < high && arr[high] > pivokey) {
					high--;
				}
				// 这里low++是先把arr[low]赋值为arr[high]，再low+=1，因为这个值是比pivokey小的，下一次不用比较了
				if (low < high)
					arr[low++] = arr[high];
				// 如果都是小于pivokey，low指针往后移
				while (low < high && arr[low] < pivokey) {
					low++;
				}
				// 这里的high--和上面同理
				if (low < high)
					arr[high--] = arr[low];
			}
			arr[low] = pivokey;
			quickSort(arr, lowStart, low - 1);
			quickSort(arr, low + 1, highEnd);
		}
		return arr;
	}

	@SuppressWarnings("unused")
    public static void main(String[] args) {
		int a[] = { 90, 23, 2, 3, 4, 12, 23, 453, 65, 657, 767 };
		int a1[] = { 4, 3, 2, 1 };
		int z[] = quickSort(a1, 0, a1.length - 1);
		for (int i = 0; i < z.length; i++) {
			System.out.println(z[i]);
		}
	}
}
