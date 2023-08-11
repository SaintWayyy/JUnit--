import annotations.*;

public class TestClass {
    @BeforeClass
    public static void beforeClass_3123() {
        System.out.println("beforeClass_3123");
    }

    @BeforeClass
    public static void beforeClass_231() {
        System.out.println("beforeClass_231");
    }

    @Before
    public void before_271(){
        System.out.println("before_271");
    }

    @Before
    public void before_27(){
        System.out.println("before_27");
    }

    @Test
    public void test_433() {
        System.out.println("-------------------test_433-------------------");
    }

    @Test
    public void test_128() {
        System.out.println("-------------------test_128-------------------");
    }

    @Test
    public void test_0() {
        System.out.println("-------------------test_0-------------------");
        throw new NullPointerException();
    }

    @After
    public void after_213(){
        System.out.println("after_213");
    }

    @After
    public void after_12(){
        System.out.println("after_12");
    }

    @AfterClass
    public static void afterClass_109() {
        System.out.println("afterClass_109");
    }

    @AfterClass
    public static void afterClass_898() {
        System.out.println("afterClass_898");
    }
}
