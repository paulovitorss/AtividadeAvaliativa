package br.com.unemat.paulo.atividadeavaliativa.util;

import android.util.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public class DateFormatterUtil {

    private static final String TAG = "DateFormatterUtil";

    private static final DateTimeFormatter isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter brazilianFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"));

    public static String toBrazilianFormat(String isoDateString) {
        if (isoDateString == null || isoDateString.isEmpty()) {
            return "";
        }

        try {
            LocalDate date = LocalDate.parse(isoDateString, isoFormatter);
            return date.format(brazilianFormatter);
        } catch (DateTimeParseException e) {
            Log.e(TAG, "Falha ao fazer o parse da data: " + isoDateString, e);
            return isoDateString;
        }
    }
}