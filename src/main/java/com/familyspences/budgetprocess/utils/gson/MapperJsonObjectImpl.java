package com.familyspences.budgetprocess.utils.gson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
public class MapperJsonObjectImpl implements MapperJsonObject {

    private static final Logger log = LoggerFactory.getLogger(MapperJsonObjectImpl.class);

    @Override
    public Optional<String> execute(Object object) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            return Optional.ofNullable(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> execute(String json, Class<T> claseDestino) {
        try {
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>)
                            (jsonElement, type, context) ->
                                    LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    .registerTypeAdapter(YearMonth.class, (JsonDeserializer<YearMonth>)
                            (jsonElement, type, context) ->
                                    YearMonth.parse(jsonElement.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM")))
                    .create();

            T objeto = gson.fromJson(json, claseDestino);
            return Optional.ofNullable(objeto);
        } catch (Exception e) {
            log.error("Error al convertir JSON a {}: {}", claseDestino.getSimpleName(), e.getMessage(), e);
            return Optional.empty();
        }
    }

}
