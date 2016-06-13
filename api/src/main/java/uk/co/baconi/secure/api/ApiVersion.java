package uk.co.baconi.secure.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix="secure.my.bacon.api")
@PropertySource("classpath:/version.properties")
public class ApiVersion {

    private String version;

}
