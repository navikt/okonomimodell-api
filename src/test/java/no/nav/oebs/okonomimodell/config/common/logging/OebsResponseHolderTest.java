package no.nav.oebs.okonomimodell.config.common.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OebsResponseHolderTest {

    private OebsResponseHolder holder;

    @BeforeEach
    void setUp() {
        holder = new OebsResponseHolder();
        holder.clear();
    }

    @Test
    void get_shouldReturnNullBeforeSet() {
        assertNull(holder.get());
    }

    @Test
    void set_andGet_shouldReturnStoredValue() {
        var data = List.of("json1", "json2");

        holder.set(data);

        assertEquals(data, holder.get());
    }

    @Test
    void clear_shouldRemoveStoredValue() {
        holder.set(List.of("json"));

        holder.clear();

        assertNull(holder.get());
    }

    @Test
    void set_shouldOverwritePreviousValue() {
        holder.set(List.of("gammel"));
        holder.set(List.of("ny"));

        assertEquals(List.of("ny"), holder.get());
    }
}
