package no.nav.oebs.okonomimodell.mapper;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

//todo: Delete when real data is available
@Component
public class MockDataGenerator {

    private final Random rand = new Random();
    private final List<MockSegment> mockSegments = List.of(
            new MockSegment(true, LocalDate.of(2025, 1, 1), LocalDate.of(2027, 12, 31)),
            new MockSegment(true, LocalDate.of(2026, 1, 1), null),
            new MockSegment(false, LocalDate.of(2025, 5, 1), LocalDate.of(2025, 12, 31))
            );

    public record MockSegment (
            boolean aktiv,
            LocalDate fromDate,
            LocalDate toDate
    ) {
    }

    public MockSegment getRandomMockSegment() {
        return mockSegments.get(rand.nextInt(0, mockSegments.size()));
    }
}

