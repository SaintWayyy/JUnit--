package Framework;

import annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class Unit {
    public static Map<String, Throwable> testClass(String name) {
        Map<String, Throwable> result = new HashMap<>();

        try {
            Class<?> c = Class.forName(name);
            Method[] methods = c.getDeclaredMethods();
            Object classInstance = c.getDeclaredConstructor().newInstance();
            Arrays.sort(methods, Comparator.comparing(Method::getName));

            Map<String, List<Method>> methodsTypeMap = classifyMethods(methods);

            executeBeforeClass(methodsTypeMap.get("BeforeClass"));
            executeTest(classInstance, methodsTypeMap, result);
            executeAfterClass(methodsTypeMap.get("AfterClass"));

        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException();
        }

        return result;
    }

    public static Map<String, List<Method>> classifyMethods(Method[] methods) {
        Map<String, List<Method>> result = new HashMap<>();
        result.put("BeforeClass", new ArrayList<>());
        result.put("Before", new ArrayList<>());
        result.put("Test", new ArrayList<>());
        result.put("AfterClass", new ArrayList<>());
        result.put("After", new ArrayList<>());

        for (var method : methods) {
            //more than one annotation on method
            if (method.getDeclaredAnnotations().length > 1)
                throw new UnsupportedOperationException();

            if (method.getAnnotation(BeforeClass.class) != null) {
                result.get("BeforeClass").add(method);
            }
            if (method.getAnnotation(Before.class) != null) {
                result.get("Before").add(method);
            }
            if (method.getAnnotation(Test.class) != null) {
                result.get("Test").add(method);
            }
            if (method.getAnnotation(AfterClass.class) != null) {
                result.get("AfterClass").add(method);
            }
            if (method.getAnnotation(After.class) != null) {
                result.get("After").add(method);
            }
        }

        return result;
    }

    public static void executeBeforeClass(List<Method> methods)
            throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(null, null);
        }
    }

    public static void executeBefore(Object classInstance, List<Method> methods)
            throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(classInstance, null);
        }
    }

    public static void executeTest(Object classInstance, Map<String, List<Method>> methodsTypeMap,
                                   Map<String, Throwable> result)
            throws InvocationTargetException, IllegalAccessException {
        for (Method method : methodsTypeMap.get("Test")) {
            executeBefore(classInstance, methodsTypeMap.get("Before"));

            try {
                result.put(method.getName(), null);
                method.invoke(classInstance, null);
            } catch (Exception e) {
                result.put(method.getName(), e.getCause());
            }

            executeAfter(classInstance, methodsTypeMap.get("After"));
        }
    }

    public static void executeAfterClass(List<Method> methods)
            throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(null, null);
        }
    }

    public static void executeAfter(Object classInstance, List<Method> methods)
            throws InvocationTargetException, IllegalAccessException {
        for (Method method : methods) {
            method.invoke(classInstance, null);
        }
    }

    //--------------------------------------------------------------------------------------
    public static Map<String, Object[]> quickCheckClass(String name) throws NoSuchMethodException {
        Map<String, Object[]> result = new HashMap<>();

        try {
            Class<?> c = Class.forName(name);
            Method[] methods = c.getDeclaredMethods();
            Object classInstance = c.getDeclaredConstructor().newInstance();
            Arrays.sort(methods, Comparator.comparing(Method::getName));

            for (Method method : methods) {
                invokeMethod(method, result, classInstance);
            }

        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

        return result;
    }

    public static void invokeMethod(Method method, Map<String, Object[]> result, Object classInstance) {
        if (method.getAnnotation(Property.class) != null) {
            Parameter[] parameters = method.getParameters();
            List<List<Object>> parametersRange = new ArrayList<>();

            for (var parameter : parameters) {
                parametersRange.add(switcher(parameter.getAnnotatedType(), classInstance));
            }

            List<List<Object>>  parametersCombination = generateParametersCombination(parametersRange);

            int i = 0;
            boolean allPass = true;
            try {
                for(; i < parametersCombination.size(); i++) {
                    if(method.invoke(classInstance, parametersCombination.get(i).toArray()).equals(false)){
                        result.put(method.getName(), parametersCombination.get(i).toArray());
                        allPass = false;
                        break;
                    }
                }
                if(allPass)
                    result.put(method.getName(),null);
            }catch (Exception e){
                System.out.println(e);
                result.put(method.getName(), parametersCombination.get(i).toArray());
                throw new RuntimeException();
            }
        }
    }

    public static List<List<Object>> generateParametersCombination(List<List<Object>> parametersRange){
        List<List<Object>> result = new ArrayList<>();
        List<Object> path = new ArrayList<>();
        return generateParametersCombinationHelper(parametersRange,result,path,0);
    }

    public static List<List<Object>> generateParametersCombinationHelper(List<List<Object>> parametersRange,
                                                                         List<List<Object>> result,
                                                                         List<Object> path,
                                                                         int current){

        if(result.size() == 100)
            return result;
        if(current == parametersRange.size()){

            result.add(path);
            return result;
        }
        for(int i = 0; i < parametersRange.get(current).size(); i++){
            List<Object> currentPath = new ArrayList<>(path);
            currentPath.add(parametersRange.get(current).get(i));
            generateParametersCombinationHelper(parametersRange,result,currentPath,current+1);
        }

        return result;
    }

    public static List<Object> switcher(AnnotatedType annotatedType, Object classInstance) {
        Annotation[] annotations = annotatedType.getAnnotations();

        if (annotations[0] instanceof IntRange) {
            return getIntRange((IntRange) annotations[0]);
        }

        if (annotations[0] instanceof StringSet) {
            return getStringSet((StringSet) annotations[0]);
        }

        if (annotations[0] instanceof ListLength) {
            AnnotatedParameterizedType apt = (AnnotatedParameterizedType) annotatedType;
            AnnotatedType[] genericAnnotations = apt.getAnnotatedActualTypeArguments();
            return getListLength((ListLength) annotations[0], genericAnnotations[0], classInstance);
        }

        if (annotations[0] instanceof ForAll) {
            return getForAll((ForAll) annotations[0], classInstance);
        }

        return null;
    }

    public static List<Object> getIntRange(IntRange intRange) {
        int min = intRange.min();
        int max = intRange.max();

        List<Object> result = new ArrayList<>(max - min + 1);

        for (int i = min; i <= max; i++) {
            result.add(i);
        }

        return result;
    }

    public static List<Object> getStringSet(StringSet stringSet) {
        List<Object> result = new ArrayList<>(stringSet.strings().length);
        result.addAll(Arrays.asList(stringSet.strings()));

        return result;
    }

    public static List<Object> getListLength(ListLength listLength, AnnotatedType p, Object classInstance) {
        List<Object> genericRange = switcher(p, classInstance);
        List<Object> result = new ArrayList<>();

        for (int i = listLength.min(); i <= listLength.max(); i++) {
            result.addAll(combination(genericRange, i));
        }

        return result;
    }

    public static List<Object> getForAll(ForAll forAll, Object classInstance) {
        List<Object> result = new ArrayList<>(forAll.times());

        try {
            Method method = classInstance.getClass().getMethod(forAll.name());
            for(int i = 0; i < forAll.times(); i++){
                result.add(method.invoke(classInstance,null));
            }
        }catch (Exception e){
            throw new NullPointerException();
        }

        return result;
    }

    public static List<Object> combination(List<Object> list, int k) {
        List<Object> result = new ArrayList<>();
        Deque<Object> path = new LinkedList<>();

        combinationHelper(list, k, 0, result, path);

        return result;
    }

    public static void combinationHelper(List<Object> list, int k, int currentLevel,
                                         List<Object> result, Deque<Object> path) {
        if (path.size() == k) {
            result.add(new ArrayList<>(path));
            return;
        }

        for (int i = currentLevel; i < list.size(); i++) {
            path.addFirst(list.get(i));
            combinationHelper(list, k, currentLevel, result, path);
            path.removeFirst();
        }
    }
}