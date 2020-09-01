package com.intellectus.utils;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DbInitializerUtils {
    static public List<Double> randomStats() {
        List<Double> nums = new ArrayList<>();
        for (int i = 0; i <=     3; i++) {
            double random = (double) getRandomInt(0, 40) / 100;
            while(listSum(nums) + random >= 1) {
                random = getRandomInt(0, 40);
            }
            nums.add(random);
        }
        nums.add(1 - listSum(nums));
        return nums;
    }

    static public int getRandomInt(int min, int max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }

    static private double listSum(List<Double> list) {
        return list.stream().mapToDouble(Double::doubleValue).sum();
    }
}
