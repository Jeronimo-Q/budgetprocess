package com.familyspences.budgetprocess.utils.gson;

import java.util.Optional;

public interface MapperJsonObject {

    Optional<String> execute(Object object);

    <T> Optional<T> execute(String json, Class<T> claseDestino);

}
