package rausku;

import java.util.List;

public interface Debuggable {
    void addDebug(Object debugInfo);

    List<Object> getDebugInfo();
}
