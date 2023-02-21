package com.jun.fruit.utils;

/**
 * @author : Bojack
 * @date : Created in 19:51 2023.02.18
 */
public class ii {
    public static void main(String[] args) {
        int x = 1,y=1;
        if (++x == 2 || y++ == 2) {
            y+=1;
            if (x++ == 3&& y++ ==4)
                y=5;
        }
        System.out.println(x);
        System.out.println(y);
    }
}
