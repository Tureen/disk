package com.yunip.utils.util;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 中文转拼音,不显示音标，不需要声调，全部大写，方便保存数据库，进行后续查询使用
 * 
 * @author pan.tang
 */
public final class PinYinUtil {
    /**
     * 禁止实例化
     */
    private PinYinUtil() { }

    /**
     * 输出格式
     */
    private static HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
    static {
        // 不需要声调
        outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // 遇到“ü” 显示成V
        outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        // 所有输出小写
        outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
    }

    /**
     * @param chineseCharacter 中文
     * @return 中文拼音，如果多音字，返回多个拼音
     */
    public static String[] getPinyin(char chineseCharacter) {
        String[] pinyinArray = null;
        try {
            pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chineseCharacter, outputFormat);
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            // can not happen
            e.printStackTrace();
        }
        List<String> pinyins = new ArrayList<String>();
        if (pinyinArray != null) {
            for (String string : pinyinArray) {
                if (!pinyins.contains(string)) {
                    pinyins.add(string);
                }
            }
        }
        return pinyins.toArray(new String[0]);
    }
    
    /**
     * 
     * getPinyin(根据中文字符串获取拼音全拼) 
     * (如果中文字符串中包含多音字，可能不准确，如果字符串中包含英文或拼音，保持不变) 
     * @param chineseStr
     * @return  
     * String 
     * @exception
     */
    public static String getPinyin(String chineseStr) {
        StringBuffer fullSpell = new StringBuffer();
        for (char chineseCharacter :chineseStr.toCharArray()) {
            String pinyin[] = getPinyin(chineseCharacter);
            
            //多音字，取第一拼音，非汉字，保持不变
            if(pinyin != null && pinyin.length > 0){
                fullSpell.append(pinyin[0]);
            } else {
            	fullSpell.append(chineseCharacter);
            }
        }
        return fullSpell.toString();
    }

    /**
     * @param chineseCharacter 中文
     * @return 拼音首字母
     */
    public static Character[] getInitWord(char chineseCharacter) {
        String[] pinyinArray = getPinyin(chineseCharacter);
        Character[] initWords = getInitWord(pinyinArray);
        return initWords;
    }

    /**
     * 
     * getInitWord(获得中文字符串拼音首字母) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param chineseStr
     * @return  
     * String 
     * @exception
     */
    public static String getInitWord(String chineseStr) {
        StringBuffer shortSpell = new StringBuffer();
        for (char chineseCharacter : chineseStr.toCharArray()) {
            Character[] initWord = getInitWord(chineseCharacter);
            if (initWord != null && initWord.length > 0) {//如果拼音大于0则取第一个
                shortSpell.append(initWord[0]);
            }
        }
        return shortSpell.toString();
    }
    
    /**
     * 
     * getInitWord(获得中文字符串第一个的拼音首字母) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param chineseStr
     * @return  
     * String 
     * @exception
     */
    public static String getFirstInitWord(String chineseStr) {
        char[] pinyin = PinYinUtil.getPinyin(chineseStr).toCharArray();
        return String.valueOf(pinyin[0]).toUpperCase();
    }
    
    /**
     * 
     * getInitWord(获取字符串首字母) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param pinyinArray
     * @return  
     * Character[] 
     * @exception
     */
    private static Character[] getInitWord(String[] pinyinArray) {
        List<Character> characters = new ArrayList<Character>();
        if (pinyinArray != null && pinyinArray.length > 0) {
            for (String string : pinyinArray) {
                char initWord = string.charAt(0);
                if (!characters.contains(initWord)) {
                    characters.add(initWord);
                }
            }
        }
        return characters.toArray(new Character[0]);
    }

    /**
     * 
     * main(测试方法) 
     * (这里描述这个方法适用条件 – 可选) 
     * @param args  
     * void 
     * @exception
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        // 测试获取拼音首字母
        char chineseCharacter = 's';
        String chineseCharactera = "123";
        String aa = "asdad";
       String a =  PinYinUtil.getFirstInitWord(chineseCharactera).toUpperCase();
        System.out.println(a);
        /*String[] pinyin = PinYinUtil.getPinyin(chineseCharacter);
        if (pinyin != null) {
            for (String string : pinyin) {
                System.out.println(string);
            }
        }
        Character[] initWord = PinYinUtil.getInitWord(chineseCharacter);
        if (initWord != null) {
            for (Character character : initWord) {
                System.out.println(character);
            }
        }*/
    }
}
