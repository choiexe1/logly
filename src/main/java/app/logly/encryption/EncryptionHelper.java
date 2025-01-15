package app.logly.encryption;

public interface EncryptionHelper {
    String hash(String plain);

    boolean check(String plain, String hash);
}
