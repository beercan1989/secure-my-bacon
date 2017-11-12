package uk.co.baconi.secure.api.password;

import lombok.Getter;
import lombok.ToString;
import uk.co.baconi.secure.base.password.EncryptedPassword;

import java.util.UUID;

@Getter
@ToString
public class NewPasswordResponse {

    private final UUID uuid;

    public NewPasswordResponse(final EncryptedPassword newPassword) {
        this.uuid = newPassword.getUuid();
    }

}
