package de.ms.springai.configuration;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import de.ms.springai.service.ServicePackageScanMarker;
import de.ms.springai.web.WebPackageScanMarker;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = { ApplicationConfiguration.class, ServicePackageScanMarker.class,
		WebPackageScanMarker.class })
public class ApplicationConfiguration {

}
