package services.Validation;

public class StringUtils {
    public static String myTrim(String id){
        if(id == null) return null;
        id = id.trim();
        StringBuilder res = new StringBuilder();

        boolean isSpace = false;
        for(int i = 0; i < id.length(); i++){
            char c = id.charAt(i);
            if(c == ' '){
                if(isSpace) continue;
                isSpace = true;
            }
            else{
                isSpace = false;
            }
            res.append(c);
        }

        return res.toString();
    }   
    public static String skipSpace(String s){
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) != ' '){
                res.append(s.charAt(i));
            }
        }
        return res.toString();
    }

    public static boolean isDigitString(String s){
        if(s == null || s.isEmpty()) return false; 
        for(int i = 0; i < s.length(); i++){
            if(!Character.isDigit(s.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
