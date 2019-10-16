package com.etnetera.hr;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AppConfiguration {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        JavaTimeModule module = new JavaTimeModule();
        mapper.registerModule(module);
        mapper.registerModule(new CustomMapperModule());
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        return mapper;
    }

    private static class CustomMapperModule extends SimpleModule {

        CustomMapperModule() {
            addDeserializer(Version.class, new VersionDeserializer());

            addSerializer(Version.class, new VersionSerializer());
        }

    }

    private static class VersionSerializer extends JsonSerializer<Version> {

        @Override
        public void serialize(Version value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            String str = value.toString();

            gen.writeString(str);
        }
    }

    private static class VersionDeserializer extends JsonDeserializer<Version> {

        @Override
        public Version deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String v = p.getValueAsString();
            return VersionUtil.parseVersion(v, "", "");
        }
    }
}
