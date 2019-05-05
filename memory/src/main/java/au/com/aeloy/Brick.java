package au.com.aeloy;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

class Brick {
    private List<BigInteger> codes = new ArrayList<>();

    Brick(List<BigInteger> codes) {
        this.codes.addAll(codes);
    }

    public List<BigInteger> getCodes() {
        return codes;
    }
}
