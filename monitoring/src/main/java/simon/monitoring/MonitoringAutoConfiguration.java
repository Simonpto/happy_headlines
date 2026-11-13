package simon.monitoring;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MonitoringAutoConfiguration {

    @Bean
    InitializingBean openTelemetryLogAppender(OpenTelemetry openTelemetry) {
        return () -> {
            LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
            OpenTelemetryAppender appender = new OpenTelemetryAppender();
            appender.setName("OTEL");
            appender.setContext(context);
            appender.start();
            context.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(appender);
            OpenTelemetryAppender.install(openTelemetry);
        };
    }

    @Bean
    @ConditionalOnWebApplication
    RequestLoggingFilter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }
}
