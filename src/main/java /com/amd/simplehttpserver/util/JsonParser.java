package com.amd.simplehttpserver.util;

public class JsonParser {

  public static String objToJson(Object obj) {

    if (obj == null) {
      return "{}";
    }

    if (obj instanceof String) {
      return "\"" + obj.toString() + "\"";
    }

    if (obj instanceof Number || obj instanceof Boolean) {
      return obj.toString();
    }

  if (obj.getClass().isArray()) {
      StringBuilder jsonArray = new StringBuilder("[");
      Object[] array = (Object[]) obj;
      for (int i = 0; i < array.length; i++) {
        jsonArray.append(objToJson(array[i]));
        if (i < array.length - 1) {
          jsonArray.append(",");
        }
      }
      jsonArray.append("]");
      return jsonArray.toString();
    }


    return parseObject(obj);

  }

  private static String parseObject(Object obj) {
    StringBuilder jsonObject = new StringBuilder("{");

    for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      try {
        Object value = field.get(obj);
        if (value != null && !value.getClass().isPrimitive()) {
          jsonObject.append("\"").append(field.getName()).append("\": ").append(objToJson(value)).append("}");
        }
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Failed to access field: " + field.getName(), e);
      }
    }

    jsonObject.append("}");
    return jsonObject.toString();
  }

  public static <T> T jsonToObj(String json, Class<T> clazz) {
    // Convert JSON string to object of type T
    // This is a placeholder for actual JSON parsing logic
    try {
      return clazz.getDeclaredConstructor().newInstance(); // Replace with actual JSON parsing
    } catch (Exception e) {
      throw new RuntimeException("Failed to parse JSON", e);
    }
  }
}
