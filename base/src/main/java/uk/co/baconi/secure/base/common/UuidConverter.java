package uk.co.baconi.secure.base.common;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.UUID;

public class UuidConverter implements AttributeConverter<UUID, String> {

    @Override
    public String toGraphProperty(final UUID uuid) {
        return uuid.toString();
    }

    @Override
    public UUID toEntityAttribute(String string) {
        return UUID.fromString(string);
    }

}
