/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.doan;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Admin
 */
public class LocalDateTypeAdapter implements JsonSerializer<LocalDate>,JsonDeserializer<LocalDate>{
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public JsonElement serialize(LocalDate t, Type type, JsonSerializationContext jsc) {
        return new JsonPrimitive(t.format(df));
        
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public LocalDate deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        return LocalDate.parse(je.getAsString(),df);
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}
