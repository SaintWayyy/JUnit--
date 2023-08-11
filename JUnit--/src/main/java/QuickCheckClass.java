import annotations.*;

import java.util.List;

public class QuickCheckClass {
    @Property
    public boolean test_123122(@IntRange(min = 1, max = 2) Integer i,
                        @StringSet(strings = {"a", "b"}) String s,
                        @ListLength(min=1, max=2) List<@IntRange(min = 5, max = 7) Integer> l,
                        @ForAll(name = "generateInt",times = 2) Object o) {
        System.out.println("Integer i = " + i +
                " String s = " + s +
                " List l = " + l.toString() +
                " Object o = " + o.toString());
        return i == 1;
    }

    int i = 0;
    public int generateInt(){
        return ++i;
    }
}
