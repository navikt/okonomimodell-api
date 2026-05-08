package no.nav.oebs.okonomimodell.config.common.logging;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OebsResponseHolder {

    private final ThreadLocal<List<String>> rawResponse = new ThreadLocal<>();

    public void set(List<String> response) {
        rawResponse.set(response);
    }

    public List<String> get() {
        return rawResponse.get();
    }

    public void clear() {
        rawResponse.remove();
    }

}
