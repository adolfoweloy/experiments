package au.com.aeloy;

import java.util.ArrayList;
import java.util.List;

public class ComplexBigObject {
    private List<Brick> bricks = new ArrayList<>();

    void add(Brick brick) {
        bricks.add(brick);
    }

    public List<Brick> getBricks() {
        return bricks;
    }
}
