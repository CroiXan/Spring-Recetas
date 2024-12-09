package com.example.users.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UtilTest {
    @Test
    void testGetResponseEntity() {
        String message = "Success";
        HttpStatus status = HttpStatus.OK;

        ResponseEntity<String> responseEntity = Util.getResponseEntity(message, status);

        assertNotNull(responseEntity);
        assertEquals(message, responseEntity.getBody());
        assertEquals(status, responseEntity.getStatusCode());
    }

    @Test
    void testGetUUID() {
        String uuid = Util.getUUID();

        assertNotNull(uuid);
        assertTrue(uuid.startsWith("BILL-"));
        assertDoesNotThrow(() -> Long.parseLong(uuid.split("-")[1]));
    }

    @Test
    void testGetJsonArrayFromString_ValidJson() throws JSONException {
        String jsonString = "[\"value1\", \"value2\"]";

        JSONArray jsonArray = Util.getJsonArrayFromString(jsonString);

        assertNotNull(jsonArray);
        assertEquals(2, jsonArray.length());
        assertEquals("value1", jsonArray.getString(0));
        assertEquals("value2", jsonArray.getString(1));
    }

    @Test
    void testGetJsonArrayFromString_InvalidJson() {
        String invalidJsonString = "invalid json";

        assertThrows(JSONException.class, () -> Util.getJsonArrayFromString(invalidJsonString));
    }

    @Test
    void testGetMapFromJson_ValidJson() {
        String jsonString = "{\"key1\":\"value1\", \"key2\":2}";

        Map<String, Object> resultMap = Util.getMapFromJson(jsonString);

        assertNotNull(resultMap);
        assertEquals(2, resultMap.size());
        assertEquals("value1", resultMap.get("key1"));
        assertEquals(2.0, resultMap.get("key2")); // Gson convierte números a Double por defecto
    }

    @Test
    void testGetMapFromJson_InvalidJson() {
        String invalidJsonString = "invalid json";

        Map<String, Object> resultMap = Util.getMapFromJson(invalidJsonString);

        assertNotNull(resultMap);
        assertTrue(resultMap.isEmpty());
    }

    @Test
    void testGetMapFromJson_EmptyString() {
        String emptyJsonString = "";

        Map<String, Object> resultMap = Util.getMapFromJson(emptyJsonString);

        assertNotNull(resultMap);
        assertTrue(resultMap.isEmpty());
    }
}
