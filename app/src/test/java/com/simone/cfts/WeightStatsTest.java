package com.simone.cfts;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class WeightStatsTest {

    // --- parseKg ---

    @Test public void parseKg_acceptsDot()         { assertEquals(72.4f, WeightStats.parseKg("72.4", -1f), 0.001f); }
    @Test public void parseKg_acceptsComma()        { assertEquals(72.4f, WeightStats.parseKg("72,4", -1f), 0.001f); }
    @Test public void parseKg_trimsWhitespace()     { assertEquals(80f,   WeightStats.parseKg("  80 ", -1f), 0.001f); }
    @Test public void parseKg_roundsToOneDecimal()  { assertEquals(72.5f, WeightStats.parseKg("72.46", -1f), 0.001f); }
    @Test public void parseKg_emptyReturnsFallback() { assertEquals(-1f,  WeightStats.parseKg("",   -1f), 0.001f); }
    @Test public void parseKg_garbageReturnsFallback(){ assertEquals(-1f, WeightStats.parseKg("abc", -1f), 0.001f); }
    @Test public void parseKg_rejectsNonPositive()  { assertEquals(-1f,  WeightStats.parseKg("0", -1f), 0.001f); }

    // --- weeklyMedians ---
    // ISO week keyed by the Monday date. Median = middle value (avg of two middles when even).

    @Test public void weeklyMedians_emptyInputIsEmpty() {
        assertTrue(WeightStats.weeklyMedians(new ArrayList<DatabaseHelper.WeightRow>()).isEmpty());
    }

    @Test public void weeklyMedians_singleWeekOddCount() {
        List<DatabaseHelper.WeightRow> rows = new ArrayList<>();
        // Week of Mon 2026-06-01 .. Sun 2026-06-07
        rows.add(new DatabaseHelper.WeightRow("2026-06-01", 70f));
        rows.add(new DatabaseHelper.WeightRow("2026-06-03", 72f));
        rows.add(new DatabaseHelper.WeightRow("2026-06-05", 71f));
        List<WeightStats.WeekMedian> out = WeightStats.weeklyMedians(rows);
        assertEquals(1, out.size());
        assertEquals("2026-06-01", out.get(0).mondayIso);
        assertEquals(71f, out.get(0).median, 0.001f);
    }

    @Test public void weeklyMedians_singleWeekEvenCount() {
        List<DatabaseHelper.WeightRow> rows = new ArrayList<>();
        rows.add(new DatabaseHelper.WeightRow("2026-06-01", 70f));
        rows.add(new DatabaseHelper.WeightRow("2026-06-02", 74f));
        List<WeightStats.WeekMedian> out = WeightStats.weeklyMedians(rows);
        assertEquals(1, out.size());
        assertEquals(72f, out.get(0).median, 0.001f); // (70+74)/2
    }

    @Test public void weeklyMedians_splitsAcrossWeeksAndSortsByDate() {
        List<DatabaseHelper.WeightRow> rows = new ArrayList<>();
        rows.add(new DatabaseHelper.WeightRow("2026-06-08", 69f)); // next week (Mon 2026-06-08)
        rows.add(new DatabaseHelper.WeightRow("2026-06-01", 70f)); // prev week
        rows.add(new DatabaseHelper.WeightRow("2026-06-07", 72f)); // Sunday of prev week
        List<WeightStats.WeekMedian> out = WeightStats.weeklyMedians(rows);
        assertEquals(2, out.size());
        assertEquals("2026-06-01", out.get(0).mondayIso); // sorted ascending
        assertEquals(71f, out.get(0).median, 0.001f);      // median(70,72)
        assertEquals("2026-06-08", out.get(1).mondayIso);
        assertEquals(69f, out.get(1).median, 0.001f);
    }
}
