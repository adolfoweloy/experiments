package au.com.aeloy.votolab.util;

import java.util.LinkedList;
import java.util.concurrent.Future;
import java.util.stream.Collector;

public class CustomCollectors {

    public static <T> Collector<Future<T>, LinkedList<Future<T>>, LinkedList<Future<T>>>
    collectAsLinkedList() {
        return Collector.of(
                LinkedList::new,
                LinkedList::addLast,
                (res1, res2) -> {
                    res1.addAll(res2);
                    return res1;
                });
    }

}
