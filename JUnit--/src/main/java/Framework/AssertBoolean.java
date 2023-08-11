package Framework;

public class AssertBoolean {
    private boolean b;

    public AssertBoolean(boolean b){
        this.b = b;
    }

    public AssertBoolean isEqualTo(boolean b2){
        if(b != b2){
            throw new UnsupportedOperationException();
        }

        return this;
    }

    public AssertBoolean isTrue(){
        if(!b){
            throw new UnsupportedOperationException();
        }

        return this;
    }

    public AssertBoolean isFalse(){
        if(b){
            throw new UnsupportedOperationException();
        }

        return this;
    }
}
