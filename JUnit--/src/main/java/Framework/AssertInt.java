package Framework;

public class AssertInt {
    private int i;
    public AssertInt(int i){
        this.i = i;
    }

    public AssertInt isEqualTo(int i2){
        if(i != i2)
            throw new UnsupportedOperationException();

        return this;
    }

    public AssertInt isLessThan(int i2){
        if(i >= i2)
            throw new UnsupportedOperationException();

        return this;
    }

    public AssertInt isGreaterThan(int i2){
        if(i <= i2)
            throw new UnsupportedOperationException();

        return this;
    }
}
