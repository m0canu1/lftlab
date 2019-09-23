package traduttore;

import java.util.*;

class SymbolTable {

    private Map<String, Integer> OffsetMap = new HashMap<>();

    void insert(String s, int address) {
        if (!OffsetMap.containsValue(address))
            OffsetMap.put(s, address);
        else
            throw new IllegalArgumentException("Reference to a memory location already occupied by another variable");
    }

    int lookupAddress(String s) {
//        if (OffsetMap.containsKey(s))
//            return OffsetMap.get(s);
//        else
//            return -1;
        return OffsetMap.getOrDefault(s, -1);
    }
}
