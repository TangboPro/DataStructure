/**
 * Created by Tang on 2017/2/10.
 */
public class BoyerMoore {

    private static int[] bmbc = new int[256];
    private static int[] bmGs = new int[256];

    public static void main(String[] args) {
        String target = "English is a West Germanic language that was first spoken in early medieval England and is now a global lingua franca.[4][5] Named after the Angles, one of the Germanic tribes that migrated to England, it ultimately derives its name from the Anglia (Angeln) peninsula in the Baltic Sea. It is most closely related to the Frisian languages, although its vocabulary has been significantly influenced by other Germanic languages in the early medieval period, and later by Romance languages, particularly French.[6] English is either the official language or one of the official languages in almost 60 sovereign states. It is the most commonly spoken language in the United Kingdom, the United States, Canada, Australia, Ireland, and New Zealand, and is widely spoken in some areas of the Caribbean, Africa, and South Asia.[7] It is the third most common native language in the world, after Mandarin and Spanish.[8] It is the most widely learned second language and an official language of the United Nations, of the European Union, and of many other world and regional international organisations. It is the most widely spoken Germanic language, accounting for at least 70% of speakers of this Indo-European branch.\n" +
                "\n" +
                "English has developed over the course of more than 1,400 years. The earliest forms of English, a set of Anglo-Frisian dialects brought to Great Britain by Anglo-Saxon settlers in the fifth century, are called Old English. Middle English began in the late 11th century with the Norman conquest of England, and was a period in which the language was influenced by French.[9] Early Modern English began in the late 15th century with the introduction of the printing press to London and the King James Bible, and the start of the Great Vowel Shift.[10] Through the worldwide influence of the British Empire, modern English spread around the world from the 17th to mid-20th centuries. Through all types of printed and electronic media, as well as the emergence of the United States as a global superpower, English has become the leading language of international discourse and the lingua franca in many regions and in professional contexts such as science, navigation, and law.[11]\n" +
                "\n" +
                "Modern English has little inflection compared with many other languages, and relies more on auxiliary verbs and word order for the expression of complex tenses, aspect and mood, as well as passive constructions, interrogatives and some negation. Despite noticeable variation among the accents and dialects of English used in different countries and regions – in terms of phonetics and phonology, and sometimes also vocabulary, grammar and spelling – English-speakers from around the world are able to communicate with one another with relative ease.";
        String pattern = "Early Modern English began in the late 15th century with the introduction of the printing press to London and the King James Bible, and the start of the Great Vowel Shift.[10] Through the worldwide influence of the British Empire, modern English spread around the world from the 17th to mid-20th centuries. Through all types of printed and electronic media, as well as the emergence of the United States as a global superpower, English has become the leading language of international";

        //suffixes("EXAMPLE");
        int flat = -1;
        if ((flat = searchBM(pattern, target)) != -1) {
            System.out.print(flat);
        }
    }

    /**
     * suff[i]=pattern[0-i]和pattern[0-m]的公共后缀串长度
     * <p>
     * 这种算法太暴力了
     */
    private static void suffixes(String pattern, int[] suff) {

        int m = pattern.length();
        suff[m - 1] = m;

        /**bcababab和abab公共后缀串比较,后缀字符串从后往前匹配*/
        for (int i = m - 2; i < m; ) {
            int k = i;
            while (k >= 0 && pattern.charAt(k) == pattern.charAt(m - i + k - 1)) {
                k--;
            }
            suff[i] = i - k;/**计算长度*/
        }
    }

    /**
     * 修改后的后缀算法:利用了已经计算得到的suff[]值，计算现在正在计算的suff[]值
     * g上一次成功匹配的失配位置
     * f是上一个成功进行匹配的起始位置
     * (不是每个位置都能进行成功匹配的,实际上能够进行成功匹配的位置并不多)
     */
    private static void exSuffixes(String pattern, int[] suff) {

        int m = pattern.length();
        suff[m - 1] = m;
        int g = m - 1;
        int f = 0;

        /**bcababab和abab公共后缀串比较,后缀字符串从后往前匹配*/
        for (int i = m - 2; i>=0; i--) {
            if (i > g && suff[i + m - 1 - f] < i - g)/**利用已经存在的suff值   ??????????不是特别懂*/
                suff[i] = suff[i + m - 1 - f];
            else {
                if (i < g)
                    g = i;
                f = i;
                while (g >= 0 && pattern.charAt(g) == pattern.charAt(g + m - f - 1)) {
                    g--;
                }
                //f为匹配失败的位置
                suff[i] = f - g;/**计算长度*/
            }
        }
    }

    /**
     * BmGs数组构造
     *
     * @param pattern
     */
    static void preBmGs(String pattern) {
        int i, j;
        int m = pattern.length();
        int[] suff = new int[m];//后缀数组
        bmGs=new int[m];
        exSuffixes(pattern, suff);//构造数组

        for (i = 0; i < m; ++i)//好字符的位置-好后缀第一次出现的位置
            bmGs[i] = m;
        j = 0;
        for (i = m - 1; i >= 0; --i)
            if (suff[i] == i + 1)
                for (; j < m - 1 - i; ++j)
                    if (bmGs[j] == m)
                        bmGs[j] = m - 1 - i;
        for (i = 0; i <= m - 2; ++i)
            bmGs[m - 1 - suff[i]] = m - 1 - i;
    }
    private static int searchBM(String patten, String target) {

        int m = patten.length();
        int n = target.length();

        bMBcCreate(patten);//构造BMBC表
        preBmGs(patten);//构造BMGS表

        int maxSearchCount=0;//最大匹配次数
        for (int i = 0; i <= n - m; ) {
            int k = m - 1;
            while (k >= 0 && patten.charAt(k) == target.charAt(i + k)) {//寻找移动位数
                k--;
                maxSearchCount++;
            }
            if (k < 0) {
                System.out.println(maxSearchCount);
                return i;//发现
                //i += bmGs[0];
            } else {
                i += Math.max(bmGs[k], k - bmbc[target.charAt(i + k)]);
            }
            maxSearchCount++;
        }
        return -1;
    }
    /**
     * 构造坏字符数组表
     */
    private static void bMBcCreate(String patten) {

        //初始化
        for (int i = 0; i < bmbc.length; i++) {
            bmbc[i] = -1;
        }

        int k = 0;
        //构造坏字符表
        for (int i = 0; i < patten.length(); i++) {
            bmbc[patten.charAt(i)] = i;
        }
    }

}

