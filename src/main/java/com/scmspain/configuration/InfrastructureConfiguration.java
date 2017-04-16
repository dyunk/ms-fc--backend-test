package com.scmspain.configuration;

import java.text.SimpleDateFormat;

import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.jmx.JmxMetricWriter;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jmx.export.MBeanExporter;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.scmspain.handlers.GlobalExceptionHandler;

@Configuration
public class InfrastructureConfiguration {

    @Bean
    @ExportMetricWriter
    public MetricWriter getMetricWriter(MBeanExporter exporter) {
	return new JmxMetricWriter(exporter);
    }

    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
	final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
	builder.indentOutput(true);
	builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	builder.dateFormat(new SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601));
	return builder;
    }

    @Bean
    public GlobalExceptionHandler getExceptionHandler() {
	return new GlobalExceptionHandler();
    }
}
