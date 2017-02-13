/**
 * Created by Tang on 2017/2/9.
 * 适用于二进制串或数集比较少的情况下
 */
public class KMPJava {

    public static void main(String[] args) {
        String target="poinjojo234";
        String pattern="2854";
        if(kmpJava(pattern,target))
            System.out.print("子串");
        else System.out.print("非子串");
    }


    private static boolean kmpJava(String pattern, String target){
        int n=target.length();
        int m=pattern.length();
        int[] next=new int[m];
        preKMP(pattern,next);//构造next
        int k = 0;

        //查找
        for(int i=0;i<n;){
            if(k==-1){//第一个字符不匹配
                k=0;
                i++;
            }
            else if(pattern.charAt(k)==target.charAt(i)){
                k++;
                i++;
                if(k==m) return true;//查找成功
            }
            else {
                k=next[k];
            }

        }
        return false;
    }
    static void preKMP(String pattern, int next[])
    {
        int m=pattern.length();
        next[0]=-1;
        int k;
        for(int i=1;i<m;i++){
            k=next[i-1];//获取之前匹配的最长公共键长度，用于获取next[j+1]长度,k就是长度
            while (k>=0)
            {
                if(pattern.charAt(k)==pattern.charAt(i-1)){
                    break;
                }
                k=next[k];
            }
            next[i]=k+1;
        }
    }
}
