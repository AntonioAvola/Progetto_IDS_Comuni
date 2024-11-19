package com.unicam.Validators;

import java.util.regex.Pattern;

public class PasswordValidator {
    /**
     * La password deve avere:
     * almeno un numero
     * almeno una lettera minuscola
     * almeno una lettera maiuscola
     * almeno un simbolo speciale tra quelli specificati
     * non deve contenere spazi bianchi
     * deve essere lunga almeno 5 caratteri
     */
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{5,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    public static boolean isValidPassword(String password) {
        if (password == null) {
            throw new NullPointerException("La password non è stata inserita");
        }
        return pattern.matcher(password).matches();
    }
}
