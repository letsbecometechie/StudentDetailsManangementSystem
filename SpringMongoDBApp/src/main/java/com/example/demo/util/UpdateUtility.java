package com.example.demo.util;

import org.reflections.Reflections;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Set;

public class UpdateUtility {
        private static final String MODEL_PACKAGE = "com.example.demo.models";
        private static boolean initialized =  false;
        private static HashMap<String, Class> classContext = new HashMap<>();

        private static void init() {
            if(!initialized) {
                Reflections reflections = new Reflections(MODEL_PACKAGE);
                Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Document.class); // Get all the classes annotated with @Document in the specified package

                for(Class<?> model : classes) {
                    classContext.put(model.getSimpleName().toLowerCase(), model);
                }

                initialized = true;
            }
        }

    public static Class getClassFromType(String type) throws Exception{
        init();
        if(classContext.containsKey(type)) {
            return classContext.get(type);
        }
        else {
            throw new Exception("Type " + type + " does not exists !");
        }
    }
}
