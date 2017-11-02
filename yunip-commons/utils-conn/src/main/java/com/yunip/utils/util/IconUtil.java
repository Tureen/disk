package com.yunip.utils.util;

public class IconUtil {

    public static String teamworkIcon(Integer icon){
        String iconStr = "a";
        if(icon != null){
            if(icon == 1){
                iconStr = "a";
            }else if(icon == 2){
                iconStr = "b";
            }else if(icon == 3){
                iconStr = "c";
            }else if(icon == 4){
                iconStr = "d";
            }else if(icon == 5){
                iconStr = "e";
            }else if(icon == 6){
                iconStr = "f";
            }else if(icon == 7){
                iconStr = "g";
            }else if(icon == 8){
                iconStr = "h";
            }else if(icon == 9){
                iconStr = "i";
            }else if(icon == 10){
                iconStr = "j";
            }
        }
        return iconStr;
    }
}
