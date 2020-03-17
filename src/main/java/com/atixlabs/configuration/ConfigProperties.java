package com.atixlabs.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "import")
@Getter
@Setter
public class ConfigProperties {

    private String folder;
}
