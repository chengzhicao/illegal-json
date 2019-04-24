package com.cheng.illegaljson;

import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <h3>不规则json反序列化</h3>
 * <pre>    {@code
 * @JsonAdapter(value = IllegalJsonDeserializer.class)
 * public class Student{
 *
 * }
 *
 * public class Person{
 *     @JsonAdapter(value = IllegalJsonDeserializer.class)
 *     public Student student;
 * }
 * }</pre>
 *
 * @param <T>
 */
public class IllegalJsonDeserializer<T> implements JsonDeserializer<T> {
    private ConstructorConstructor constructorConstructor = new ConstructorConstructor(Collections.<Type, InstanceCreator<?>>emptyMap());
    private Excluder excluder = Excluder.DEFAULT;
    private Gson gson;

    @SuppressWarnings({"unchecked"})
    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        TypeToken<T> typeToken = (TypeToken<T>) TypeToken.get(typeOfT);
        ReflectHelp<T> me = (ReflectHelp<T>) ReflectHelp.deal(typeToken.getRawType());
        if (!me.isObject() || !json.isJsonObject()) {
            return null;
        }
        gson(context);
        LinkedTreeMap<String, JsonElement> members = getMembers(json);
        if (members == null || members.size() == 0) {
            return null;
        }
        List<BoundField> fields = getFields(typeOfT);
        ObjectConstructor<T> constructor = constructorConstructor.get(typeToken);
        T instance = constructor.construct();
        for (BoundField boundField : fields) {
            JsonElement jsonElement = getJsonElement(members, boundField);
            injectObjectContent(instance, boundField, jsonElement);
        }
        return instance;
    }

    /**
     * 处理context，获取全局的gson
     *
     * @param context
     */
    private void gson(JsonDeserializationContext context) {
        Class<? extends JsonDeserializationContext> contextClass = context.getClass();
        Field[] contextFields = contextClass.getDeclaredFields();
        if (contextFields.length < 1) {
            return;
        }
        contextFields[0].setAccessible(true);
        Class<?> declaringClass = contextClass.getDeclaringClass();
        try {
            Object object = contextFields[0].get(context);
            Field gsonField = declaringClass.getDeclaredField("gson");
            gsonField.setAccessible(true);
            gson = (Gson) gsonField.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取JsonElement中的members
     *
     * @param json
     * @return
     */
    @SuppressWarnings({"unchecked"})
    private LinkedTreeMap<String, JsonElement> getMembers(JsonElement json) {
        if (json == null || !json.isJsonObject()) {
            return null;
        }
        LinkedTreeMap<String, JsonElement> members = null;
        try {
            JsonObject asJsonObject = json.getAsJsonObject();
            Field field = asJsonObject.getClass().getDeclaredField("members");
            field.setAccessible(true);
            members = (LinkedTreeMap<String, JsonElement>) field.get(asJsonObject);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return members;
    }

    /**
     * 构建对象的内容
     *
     * @param instance
     * @param boundField
     * @param jsonElement
     * @throws IllegalAccessException
     */
    private void injectObjectContent(Object instance, BoundField boundField, JsonElement jsonElement) {
        Field field = boundField.field;
        if (jsonElement == null) {
            return;
        }
        try {
            field.set(instance, getFieldValue(field.getGenericType(), jsonElement));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取字段对应的值
     *
     * @param componentType
     * @param jsonElement
     * @return
     * @throws IllegalAccessException
     */
    @SuppressWarnings({"unchecked"})
    private Object getFieldValue(Type componentType, JsonElement jsonElement) throws IOException {
        TypeToken<?> typeToken = TypeToken.get(componentType);
        ReflectHelp me = ReflectHelp.deal(typeToken.getRawType());
        if (jsonElement.isJsonPrimitive()) {
            if (me.isByte()) {
                try {
                    return jsonElement.getAsByte();
                } catch (Exception e) {
                    return 0;
                }
            } else if (me.isShort()) {
                try {
                    return jsonElement.getAsShort();
                } catch (Exception e) {
                    return 0;
                }
            } else if (me.isChar()) {
                try {
                    return jsonElement.getAsCharacter();
                } catch (Exception e) {
                    return '\u0000';
                }
            } else if (me.isInt()) {
                try {
                    return jsonElement.getAsInt();
                } catch (Exception e) {
                    return 0;
                }
            } else if (me.isLong()) {
                try {
                    return jsonElement.getAsLong();
                } catch (Exception e) {
                    return 0L;
                }
            } else if (me.isFloat()) {
                try {
                    return jsonElement.getAsFloat();
                } catch (Exception e) {
                    return 0.0f;
                }
            } else if (me.isDouble()) {
                try {
                    return jsonElement.getAsDouble();
                } catch (Exception e) {
                    return 0.0d;
                }
            } else if (me.isBoolean()) {
                try {
                    return jsonElement.getAsBoolean();
                } catch (Exception e) {
                    return false;
                }
            } else if (me.isString()) {
                try {
                    return jsonElement.getAsString();
                } catch (Exception e) {
                    return null;
                }
            }
//            else if (me.isObj()) {//如果一个对象对应的值不是Object而是""空字符串，可让""转为null
//                if (jsonElement.getAsString().equals("") && getEnableEmpty(boundField.field)) {
//                    return null;
//                }
//            }
        } else if (jsonElement.isJsonArray()) {
            JsonArray asJsonArray;
            try {
                asJsonArray = jsonElement.getAsJsonArray();
            } catch (Exception e) {
                return null;
            }
            if (asJsonArray != null && asJsonArray.size() > 0) {
                if (me.isArray()) {
                    Type arrayComponentType = $Gson$Types.getArrayComponentType(componentType);
                    List<Object> list = new ArrayList<>();
                    for (int i = 0; i < asJsonArray.size(); i++) {
                        JsonElement je = asJsonArray.get(i);
                        list.add(getFieldValue(arrayComponentType, je));
                    }
                    int size = list.size();
                    Object array = Array.newInstance($Gson$Types.getRawType(arrayComponentType), size);
                    for (int i = 0; i < size; i++) {
                        Array.set(array, i, list.get(i));
                    }
                    return array;
                } else if (me.isCollection()) {
                    Type type = typeToken.getType();
                    Class rawType = typeToken.getRawType();
                    Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
                    ObjectConstructor objectConstructor = constructorConstructor.get(typeToken);
                    Collection construct = (Collection) objectConstructor.construct();
                    for (int i = 0; i < asJsonArray.size(); i++) {
                        JsonElement je = asJsonArray.get(i);
                        construct.add(getFieldValue(elementType, je));
                    }
                    return construct;
                }
            }
        } else if (jsonElement.isJsonObject() && me.isObject()) {
            JsonObject asJsonObject;
            try {
                asJsonObject = jsonElement.getAsJsonObject();
            } catch (Exception e) {
                return null;
            }
            LinkedTreeMap<String, JsonElement> members;
            if (asJsonObject != null && asJsonObject.size() > 0) {
                if (me.isMap()) {
                    Class<?> rawTypeOfSrc = $Gson$Types.getRawType(componentType);
                    Type[] keyAndValueTypes = $Gson$Types.getMapKeyAndValueTypes(componentType, rawTypeOfSrc);
                    ObjectConstructor<?> constructor = constructorConstructor.get(typeToken);
                    Map<String, Object> map = (Map<String, Object>) constructor.construct();
                    Set<String> keySet = asJsonObject.keySet();
                    for (String key : keySet) {
                        Type valueType = keyAndValueTypes[1];
                        TypeToken<?> valueTypeToken = TypeToken.get(valueType);
                        ObjectConstructor<?> valueObjectConstructor = constructorConstructor.get(valueTypeToken);
                        Object valueConstruct = valueObjectConstructor.construct();
                        ReflectHelp<?> meValue = ReflectHelp.deal(valueConstruct.getClass());
                        Object value;
                        if (meValue.is(Object.class) || meValue.isMap()) {//如果时Object或者Map，直接将jsonObject作为value
                            value = asJsonObject.get(key);
                        } else {
                            List<BoundField> boundFields = getFields(valueConstruct.getClass());
                            members = getMembers(asJsonObject.get(key));
                            for (BoundField boundField : boundFields) {
                                JsonElement inJsonElement = getJsonElement(members, boundField);
                                injectObjectContent(valueConstruct, boundField, inJsonElement);
                            }
                            value = valueConstruct;
                        }
                        map.put(key, value);
                    }
                    return map;
                } else {
                    members = getMembers(asJsonObject);
                    List<BoundField> fields = getFields(componentType);
                    ObjectConstructor<?> constructor = constructorConstructor.get(typeToken);
                    Object object = constructor.construct();
                    Class<?> objectClass = object.getClass();
                    //处理JsonAdapter注解
                    JsonAdapter annotation = objectClass.getAnnotation(JsonAdapter.class);
                    if (annotation != null) {
                        ObjectConstructor<?> objectConstructor = constructorConstructor.get(TypeToken.get(annotation.value()));
                        Object instance = objectConstructor.construct();
                        TypeAdapter<?> typeAdapter;
                        if (instance instanceof TypeAdapter) {
                            typeAdapter = (TypeAdapter<?>) instance;
                        } else if (instance instanceof TypeAdapterFactory) {
                            typeAdapter = ((TypeAdapterFactory) instance).create(gson, TypeToken.get(objectClass));
                        } else if (instance instanceof JsonDeserializer) {
                            typeAdapter = new TreeTypeAdapter<>(null, (JsonDeserializer) instance, gson, TypeToken.get(objectClass), null);
                        } else {
                            throw new IllegalArgumentException("Invalid attempt to bind an instance of "
                                    + instance.getClass().getName() + " as a @JsonAdapter for " + TypeToken.get(objectClass).toString()
                                    + ". @JsonAdapter value must be a TypeAdapter, TypeAdapterFactory,"
                                    + " JsonSerializer or JsonDeserializer.");
                        }
                        if (typeAdapter != null && annotation.nullSafe()) {
                            typeAdapter = typeAdapter.nullSafe();
                        }
                        StringReader stringReader = new StringReader(asJsonObject.toString());
                        JsonReader jsonReader = new JsonReader(stringReader);
                        if (typeAdapter != null) {
                            return typeAdapter.read(jsonReader);
                        }
                    }
                    for (BoundField bField : fields) {
                        JsonElement inJsonElement = getJsonElement(members, bField);
                        injectObjectContent(object, bField, inJsonElement);
                    }
                    return object;
                }
            }
        } else if (jsonElement.isJsonNull()) {
            return null;
        }
        return null;
    }

    /**
     * 根据字段的注解{@link SerializedName}、{@link Select}、{@link CompareSelect}内容得到具体的json节点。
     * 这里对于{@link SerializedName}注解的处理与Gson的处理方式有所不同。在这里，会遍历
     * {@link SerializedName}中值寻找对应的json节点，跟Gson中的处理方式正好相反。优先级
     * {@link Select}>{@link CompareSelect}>{@link SerializedName}
     *
     * @param members
     * @param boundField
     * @return
     */
    private JsonElement getJsonElement(LinkedTreeMap<String, JsonElement> members, BoundField boundField) {
        if (boundField.select != null) {
            String[] selects = boundField.select.split("\\.");
            if (selects.length > 0) {
                LinkedTreeMap<String, JsonElement> map = members;
                for (int i = 0; i < selects.length; i++) {
                    if (i < selects.length - 1) {
                        map = getMembers(members.get(selects[i]));
                    }
                    if (i == selects.length - 1 && map != null) {
                        return map.get(selects[i]);
                    }
                }
            }
        } else if (boundField.compareSelect.length > 0) {
            JsonElement element = null;
            for (String compareSelect : boundField.compareSelect) {
                JsonElement tempElement = members.get(compareSelect);
                if (tempElement == null || tempElement.isJsonNull()) {
                    continue;
                }
                if (tempElement.isJsonPrimitive()) {
                    String asString = null;
                    try {
                        asString = tempElement.getAsString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (asString == null || asString.equals("")) {
                        continue;
                    }
                }
                element = tempElement;
            }
            return element;
        } else if (boundField.alternates.size() > 0) {
            JsonElement element = null;
            for (String alternate : boundField.alternates) {
                if (members.get(alternate) != null) {
                    element = members.get(alternate);
                }
            }
            return element;
        } else {
            return members.get(boundField.field.getName());
        }
        return null;
    }

    /**
     * 获取构建的字段
     *
     * @param typeOfT
     * @return
     */
    private List<BoundField> getFields(Type typeOfT) {
        List<BoundField> result = new ArrayList<>();
        LinkedList<String> previous = new LinkedList<>();
        LinkedList<String> previousVote = new LinkedList<>();
        TypeToken<?> typeToken = TypeToken.get(typeOfT);
        Class tClass = typeToken.getRawType();
        if (tClass.isInterface()) {
            return result;
        }
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            boolean deserialize = excludeField(field);
            if (!deserialize) {
                continue;
            }
            field.setAccessible(true);
            List<String> fieldNames = getFieldNames(field);
            for (int i = 0, size = fieldNames.size(); i < size; ++i) {
                if (previous.contains(fieldNames.get(i))) {
                    throw new IllegalArgumentException(typeOfT
                            + " declares multiple JSON fields named " + fieldNames.get(i));
                }
                previous.add(fieldNames.get(i));
            }
            String[] compareSelect = getCompareSelect(field);
            for (String aVote : compareSelect) {
                if (previousVote.contains(aVote)) {
                    throw new IllegalArgumentException(typeOfT
                            + " declares multiple JSON fields named " + aVote);
                }
                previousVote.add(aVote);
            }
            result.add(new BoundField(field, fieldNames, getSelect(field), compareSelect));
        }
        return result;
    }

    private boolean excludeField(Field f) {
        return !excluder.excludeClass(f.getType(), false) && !excluder.excludeField(f, false);
    }

    private List<String> getFieldNames(Field f) {
        SerializedName annotation = f.getAnnotation(SerializedName.class);
        if (annotation == null) {
            return Collections.emptyList();
        }

        String serializedName = annotation.value();
        String[] alternates = annotation.alternate();
        if (alternates.length == 0) {
            return Collections.singletonList(serializedName);
        }

        List<String> fieldNames = new ArrayList<>(alternates.length + 1);
        fieldNames.add(serializedName);
        Collections.addAll(fieldNames, alternates);
        return fieldNames;
    }

    private String[] getCompareSelect(Field f) {
        CompareSelect annotation = f.getAnnotation(CompareSelect.class);
        if (annotation != null) {
            return annotation.value();
        }
        return new String[]{};
    }

    private String getSelect(Field f) {
        Select annotation = f.getAnnotation(Select.class);
        if (annotation == null) {
            return null;
        }
        return annotation.value();
    }

    private class BoundField {
        private String select;
        private Field field;
        private List<String> alternates;
        private String[] compareSelect;

        private BoundField(Field field, List<String> alternates, String select, String[] compareSelect) {
            this.select = select;
            this.field = field;
            this.alternates = alternates;
            this.compareSelect = compareSelect;
        }
    }
}
