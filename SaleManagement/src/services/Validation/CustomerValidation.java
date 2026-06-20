package services.Validation;

public class CustomerValidation implements Validator<String> {

    @Override
    public String validate(String fieldName, String rawValue) throws IllegalArgumentException {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName.toUpperCase() + " cannot be empty!");
        }

        switch (fieldName.toLowerCase()) {
            case "id":
                String cleanId = skipSpace(rawValue).toUpperCase(); 
                
                if (!cleanId.startsWith("CUS")) {
                    throw new IllegalArgumentException("Customer ID must start with 'CUS'!");
                }
                
                String numberPart = cleanId.substring(3);
                if (numberPart.isEmpty() || !isDigitString(numberPart)) {
                    throw new IllegalArgumentException("Customer ID must be followed by digits (e.g., CUS123)!");
                }
                
                return cleanId;

            case "phone":
                String cleanPhone = skipSpace(rawValue);
                if (!isDigitString(cleanPhone) || cleanPhone.length() < 10 || cleanPhone.length() > 11) {
                    throw new IllegalArgumentException("Phone number must contain only digits and be 10 to 11 digits long!");
                }
                return cleanPhone;

            case "name":
                return myTrim(rawValue);
            case "email":
                String cleanEmail = skipSpace(rawValue.trim());
                String allowedSpecialChars = "!#$%&'*+-/=?^_`{|}~.";
                if (cleanEmail.isEmpty()) {
                    throw new IllegalArgumentException("Email cannot be empty or contain only spaces");
                }
                
                int atIndex = findAt(cleanEmail);
                if (atIndex == -1) {
                    throw new IllegalArgumentException("Must contain @");
                }
                if (cleanEmail.indexOf('@', atIndex + 1) != -1) {
                    throw new IllegalArgumentException("Email cannot contain multiple @ symbols");
                }
                if (cleanEmail.charAt(0) == '.' || cleanEmail.endsWith(".")) {
                    throw new IllegalArgumentException("Can not start or end with dot");
                }
                
                String localPart = cleanEmail.substring(0, atIndex);
                if (localPart.isEmpty() || localPart.length() > 64) {
                    throw new IllegalArgumentException("Local part length must be between 1 and 64 characters");
                }
                for (int i = 0; i < localPart.length(); i++) {
                    char ch = localPart.charAt(i);
                    if (!Character.isLetterOrDigit(ch) && allowedSpecialChars.indexOf(ch) == -1) {
                        throw new IllegalArgumentException("Must be digit or letter or allowed special character");
                    }
                }
                
                String domain = cleanEmail.substring(atIndex + 1);
                if (domain.isEmpty() || domain.length() > 253) {
                    throw new IllegalArgumentException("Domain length must be between 1 and 253 characters");
                }
                
                int lastDot = domain.lastIndexOf('.');
                if (lastDot == -1) {
                    throw new IllegalArgumentException("Domain must contain a TLD (e.g., .com)");
                }
                
                String tld = domain.substring(lastDot + 1);
                if (tld.length() < 2 || tld.length() > 63) {
                    throw new IllegalArgumentException("TLD length must be between 2 and 63 characters");
                }
                for (int i = 0; i < tld.length(); i++) {
                    char c = tld.charAt(i);
                    if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
                        throw new IllegalArgumentException("TLD must contain only letters");
                    }
                }
                
                String prefix = domain.substring(0, lastDot);
                if (prefix.isEmpty()) {
                    throw new IllegalArgumentException("Domain must have a label before the TLD");
                }
                
                int start = 0;
                while (start < prefix.length()) {
                    int dotIndex = prefix.indexOf('.', start);
                    String label;
                    if (dotIndex == -1) {
                        label = prefix.substring(start);
                        start = prefix.length();
                    } else {
                        label = prefix.substring(start, dotIndex);
                        start = dotIndex + 1;
                    }
                    
                    if (label.isEmpty() || label.length() > 63) {
                        throw new IllegalArgumentException("Each domain label must be between 1 and 63 characters");
                    }
                    if (label.charAt(0) == '-' || label.charAt(label.length() - 1) == '-') {
                        throw new IllegalArgumentException("Hyphens cannot be at the start or end of a domain label");
                    }
                    for (int i = 0; i < label.length(); i++) {
                        char c = label.charAt(i);
                        boolean isLetter = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
                        boolean isDigit = (c >= '0' && c <= '9');
                        boolean isHyphen = (c == '-');
                        if (!isLetter && !isDigit && !isHyphen) {
                            throw new IllegalArgumentException("Domain labels can only contain letters, digits, and hyphens");
                        }
                    }
                }
                
                return cleanEmail;
                
            default:
                return rawValue;
        }
    }
    
    public int findAt(String s){//find '@'
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '@'){
                return i;
            }
        }
        return -1;
    }
    public int findDot(String s){
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '.'){
                return i;
            }
        }
        return -1;
    }
}