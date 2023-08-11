import Framework.Unit;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
        /**
        * public static Map<String, Throwable> testClass(String name);
        * Given a class name, testClass should run all the test cases in that class.
        * The return value is a map where the keys of the map are the test case names,
        * and the values are either the exception or error thrown by a test case
        * (indicating that test case failed) or null for test cases that passed.
        * */
        var result1 = Unit.testClass("TestClass");
        System.out.println("-------------------Test results-------------------");
        for(var entry: result1.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue());
        }


        /**
         * public static Map<String, Object[]> quickCheckClass(String name);
         * Given a class name, this method should run all the properties in that class.
         * If the property returned false or threw a Throwable. quickCheckClass will
         * add a mapping from the method name to the array of arguments. (The first arguments cause this)
         * Otherwise, if the property runs until termination with only true return values,
         * the property will be mapped to null.
         * Then quickCheckClass will run the next property in the class.
         * */
        var result2 = Unit.quickCheckClass("QuickCheckClass");
        System.out.println("-------------------QuickCheck results-------------------");
        for(var entry: result2.entrySet()){
           System.out.println(entry.getKey() + " " + Arrays.toString(entry.getValue()));
        }
    }
}
