package com.sopra.core.utility;

public interface EncryptService {
    String encrypt(String password);
    boolean check(String checkPassword, String realPassword);
}
