package net.peacefulcraft.sco.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ReverseList {
    
    public static<T> ArrayList<T> reverseList(List<T> list) {
        return list.stream()
                    .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(ArrayList::new), lst -> {
                            Collections.reverse(lst);
                            return lst.stream();
                        }
                    )).collect(Collectors.toCollection(ArrayList::new));
    }
}