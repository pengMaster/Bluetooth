package com.ucai.test.utils;

/**
 * Created by Dell on 2016/5/23.
 */

/**
 * x1身高(cm)    x2体重(kg)    x3年龄    x4性别    x5BMI
 * y1脂肪率(%)    y2肌肉率(%)    y3骨量(%)    y4水分(%)    y5代谢(大卡/天)    y6身体年龄
 */
public class Calculaion {
    private static double x5;//bmi值
    private static double y1;//脂肪率
    private static double y2;//肌肉率
    private static double y3;//骨量
    private static double y4;//水分
    private static double y5;//代谢
    private static double y6;//身体年龄


    public static double Bmi(int x1, double x2) {
        x5 = 10000 * x2 / (x1 * x1);
        return x5;
    }

    public static double Fat(int x1, double x2, int x3, int x4) {
        y1 = 81.23 - 0.5006 * x1 + 0.4801 * x2 + 0.02988 * x3 - 8.1858 * x4;
        return y1;
    }

    public static double Muscle(int x1, double x2, int x3, int x4) {
        y2 = 12.7711 + 0.3349 * x1 - 0.3233 * x2 - 0.02161 * x3 + 5.554 * x4;
        return y2;
    }

    public static double Bone(int x1, double x2, int x3, int x4) {
        y3 = -1.001 + 0.04097 * x1 - 0.02584 * x2 - 0.00316 * x3 - 0.1629 * x4;
        return y3;
    }
    public static double Water(int x1, double x2, int x3, int x4){
        y4=14.1009+0.361*x1-0.3491*x2-0.02422*x3+6.017*x4;
        return y4;
    }
    public static double Metabolize(int x1, double x2, int x3, int x4){
        y5=-131.51+6.091*x1+10.03*x2-5.091*x3+165.4*x4;
        return y5;
    }
    public static double BodyAge( int x3, int x4,double x5){
        y6=-16.1791+0.5793*x5+1.088*x3-0.8853*x4;
        return y6;
    }
}
