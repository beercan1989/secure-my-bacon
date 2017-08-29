package uk.co.baconi.secure.api.password;

import lombok.Getter;
import lombok.ToString;
import uk.co.baconi.secure.base.password.Password;

import java.util.UUID;

@Getter
@ToString
public class NewPasswordResponse {

    public NewPasswordResponse(final Password newPassword) {
        this.uuid = newPassword.getUuid();
    }

    private final UUID uuid;
}
