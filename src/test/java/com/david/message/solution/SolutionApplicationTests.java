package com.david.message.solution;


public class SolutionApplicationTests {

    public static void main(String[] args) {
       int[] nums = {0,0,1,1,3,2,2,3,3,4};
        removeElement(nums,3);
        System.out.println();
        for(int num:nums){
            System.out.print(num);
        }
    }


    public static int removeElement(int[] nums,int val) {
        if(nums == null || nums.length == 0) return 0;
        int p = 0;
        int q = 1;
        while(q < nums.length){
            if(nums[p] != nums[q]){
                if(q - p > 1){
                    nums[p + 1] = nums[q];
                }
                p++;
            }
            q++;
        }
        return p + 1;
    }

}
