package services.Validation;

public interface Validator<T> {
    T validate(String fieldName, T rawValue) throws IllegalArgumentException;
    


}