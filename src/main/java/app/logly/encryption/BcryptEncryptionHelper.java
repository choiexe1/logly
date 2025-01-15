package app.logly.encryption;

import com.password4j.Password;
import org.springframework.stereotype.Component;

@Component
public class BcryptEncryptionHelper implements EncryptionHelper {
    @Override
    public String hash(String plain) {
        return Password.hash(plain).withBcrypt().getResult();
    }

    @Override
    public boolean check(String plain, String hash) {
        return Password.check(plain, hash).withBcrypt();
    }
}
