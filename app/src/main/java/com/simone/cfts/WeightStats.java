package com.simone.cfts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Pure, Android-free helpers for the weight tracker: input parsing and weekly-median grouping. */
final class WeightStats {

    private WeightStats() {}

    private static final SimpleDateFormat ISO = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    /** Parse a kg string accepting '.' or ',' decimal, rounded to one decimal. Returns fallback if invalid or <= 0. */
    static float parseKg(String s, float fallback) {
        if (s == null) return fallback;
        String cleaned = s.trim().replace(',', '.');
        if (cleaned.isEmpty()) return fallback;
        try {
            float v = Float.parseFloat(cleaned);
            if (v <= 0f) return fallback;
            return Math.round(v * 10f) / 10f;
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    static final class WeekMedian {
        final String mondayIso;
        final float median;
        WeekMedian(String mondayIso, float median) { this.mondayIso = mondayIso; this.median = median; }
    }

    /** Group daily weigh-ins by ISO (Monday-based) week and compute each week's median, sorted ascending by week. */
    static List<WeekMedian> weeklyMedians(List<DatabaseHelper.WeightRow> rows) {
        Map<String, List<Float>> byWeek = new LinkedHashMap<>();
        if (rows != null) {
            for (DatabaseHelper.WeightRow r : rows) {
                String monday = mondayOf(r.date);
                if (monday == null) continue;
                List<Float> bucket = byWeek.get(monday);
                if (bucket == null) { bucket = new ArrayList<>(); byWeek.put(monday, bucket); }
                bucket.add(r.weight);
            }
        }
        List<WeekMedian> out = new ArrayList<>();
        for (Map.Entry<String, List<Float>> e : byWeek.entrySet()) {
            out.add(new WeekMedian(e.getKey(), median(e.getValue())));
        }
        Collections.sort(out, new Comparator<WeekMedian>() {
            @Override public int compare(WeekMedian a, WeekMedian b) { return a.mondayIso.compareTo(b.mondayIso); }
        });
        return out;
    }

    static float median(List<Float> values) {
        List<Float> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        int n = sorted.size();
        if (n == 0) return 0f;
        if ((n & 1) == 1) return sorted.get(n / 2);
        return (sorted.get(n / 2 - 1) + sorted.get(n / 2)) / 2f;
    }

    /** ISO date of the Monday of the week containing {@code iso}, or null if unparseable. */
    static String mondayOf(String iso) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(ISO.parse(iso));
            int dow = c.get(Calendar.DAY_OF_WEEK);
            int diff = (dow == Calendar.SUNDAY) ? -6 : (Calendar.MONDAY - dow);
            c.add(Calendar.DAY_OF_MONTH, diff);
            return ISO.format(c.getTime());
        } catch (Exception e) {
            return null;
        }
    }
}
