//set to any whatever to test
package problemsolving;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Vincent Tereso
 */
public class TokenCheck {
    //using regex
    public static void isPatternMatch(String pattern, String str)
    {
        HashMap<Character,Integer> captureGroupList=new HashMap<>();
        String regexStr="";
        int counter=1;
        for(int i=0;i<pattern.length();i++)
        {
            char tmp=pattern.charAt(i);
            if(captureGroupList.containsKey(tmp))regexStr+=("\\"+captureGroupList.get(tmp));
            else
            {
                captureGroupList.put(tmp, counter);
                regexStr+="(.+)";
                counter++;
            }
        }
        Pattern rgxPattern=Pattern.compile(regexStr);
        Matcher patternMatcher=rgxPattern.matcher(str);
        if(patternMatcher.matches())
        {
            System.out.println("True");
            System.out.println("Here are the tokens...");
            for(Object key : captureGroupList.keySet())
            {
                System.out.printf(" Key: %s Value: %s\n",key.toString(),patternMatcher.group((int) captureGroupList.get(key)));
            }
        }
        else System.out.println("False");
    }
    
    
    //without using regex
    public static void isPatternMatch2(String pattern, String str)
    {
        //create hashmap with token values-initially empty string
        HashMap<Character,String> tokenMap = new HashMap();
        for(int i=0;i<pattern.length();i++)
        {
            if(tokenMap.get(pattern.charAt(i))==null)
            {
                tokenMap.put(pattern.charAt(i), "");
            }
        }
        //set tokens within this helper
        isPatternMatch2(str,pattern,str,tokenMap,0);
    }
    
    //originalString and pattern are only used to test the final string comparison
    public static void isPatternMatch2(String originalString, String pattern,String str, HashMap<Character,String> tokenMap, int patternIndex)
    {
        //tokenizing
        if(patternIndex<pattern.length())
        {
            Character key=pattern.charAt(patternIndex);
            //if token has already been set
            if(tokenMap.get(key)!="")
            {
                if(tokenMap.get(key).length()<=str.length())
                {
                    isPatternMatch2(originalString,pattern,str.substring(tokenMap.get(key).length(),str.length()),tokenMap,patternIndex+1);
                }
            }
            else
            {
                for(int i=0;i<str.length();i++)
                {
                    HashMap<Character,String>newMap=new HashMap<>(tokenMap);
                    Character c=str.charAt(i);
                    newMap.put(key,newMap.get(key)+c);
                    tokenMap=newMap;
                    isPatternMatch2(originalString,pattern,str.substring(i+1,str.length()),tokenMap,patternIndex+1);
                }
            }
        }
        
        //compute string with currently banked values
        //determine if there is a match
        else if(str.equals(""))
        {
            String tokenString="";
            for(int i=0;i<pattern.length();i++)tokenString+=tokenMap.get(pattern.charAt(i));
            if(tokenString.equals(originalString))
            {
                System.out.println("Pattern Match!");
                System.out.printf("Input: %s\n\n",originalString);
                for(Object key : tokenMap.keySet())System.out.printf("Token: %s Value: %s\n",key,tokenMap.get(key));
                System.out.printf("\n\n");

            }
        }
    }
    
    
    public static void main(String[]args)
    {
        //regex finds one such pattern
        //isPatternMatch("ajhbaa","affikjafaf");
        
        System.out.println();
        
        //non regex finds all such patterns
        //has no return if false
        isPatternMatch2("ajhbaa","affikjafaf");
    }
}