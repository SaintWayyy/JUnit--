package Framework;

public class AssertObject {
    private Object o = null;

    public AssertObject(Object o){
        this.o = o;
    }

    public AssertObject isNotNull() {
        if (o == null)
            throw new UnsupportedOperationException();

        return this;
    }

    public AssertObject isNull() {
        if (o != null)
            throw new UnsupportedOperationException();

        return this;
    }

    public AssertObject isEqualTo(Object o2) {
        if (!o.equals(o2))
            throw new UnsupportedOperationException();

        return this;
    }

    public AssertObject isNotEqualTo(Object o2) {
        if (o.equals(o2))
            throw new UnsupportedOperationException();

        return this;
    }

    public AssertObject isInstanceOf(Class c) {
        if (o.getClass() != c)
            throw new UnsupportedOperationException();

        return this;
    }
}
