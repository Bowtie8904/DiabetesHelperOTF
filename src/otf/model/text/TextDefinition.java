package otf.model.text;

import java.util.List;

import com.google.common.collect.Lists;

import bt.io.text.obj.Text;
import bt.io.text.obj.TextSource;

/**
 * @author &#8904
 *
 */
public class TextDefinition implements TextSource
{
    public static final int LOADING_VALUES = 10001;
    public static final int NO_VALUES_FOUND = 10002;
    public static final int OVERVIEW = 20001;
    public static final int NEW_BLOODSUGAR = 20002;
    public static final int NEW_BOLUS = 20003;
    public static final int CONFIGURATION = 20004;
    public static final int TIME = 20020;
    public static final int BLOODSUGAR = 20021;
    public static final int BE = 20022;
    public static final int FACTOR = 20023;
    public static final int TOTAL_BOLUS = 20024;
    public static final int BOLUS = 20025;
    public static final int CORRECTION = 20026;
    public static final int SELECTION = 20027;
    public static final int CARBOHYDRATES = 20028;
    public static final int WEIGHT = 20029;
    public static final int NAME = 20030;
    public static final int AMOUNT = 20031;
    public static final int DOUBLE_CORRECTION = 20032;
    public static final int SAVE_SUCCESS = 30001;
    public static final int REMOVE = 30002;
    public static final int DELETE = 30003;
    public static final int ADD = 30004;
    public static final int SAVE = 30005;
    public static final int TITLE = 30006;
    public static final int TITLE_LOADING = 30007;
    public static final int LABEL_BLOODSUGAR = 30008;
    public static final int LABEL_BE = 30009;
    public static final int LABEL_FACTOR = 30010;
    public static final int LABEL_BOLUS = 30011;
    public static final int LABEL_CORRECTION = 30012;
    public static final int LABEL_TOTAL_BOLUS = 30013;
    public static final int LABEL_NAME = 30014;
    public static final int LABEL_WEIGHT = 30015;
    public static final int LABEL_CARBOHYDRATES = 30016;
    public static final int PROMPT_SEARCH = 30017;
    public static final int LABEL_BEGINNING = 30018;
    public static final int LABEL_MORNING = 30019;
    public static final int LABEL_NOON = 30020;
    public static final int LABEL_EVENING = 30021;
    public static final int TITLE_CONNECTION_LOST = 30022;
    public static final int TITLE_RECONNECTING = 30023;
    public static final int TITLE_RECONNECTION_FAILED = 30024;

    /**
     * @see bt.io.text.obj.TextSource#loadTexts(java.lang.String, java.lang.String)
     */
    @Override
    public List<Text> loadTexts(String group, String language)
    {
        List<Text> list = Lists.newArrayList();

        if (language.equals("EN"))
        {
            list = Lists.newArrayList();
        }
        else if (language.equals("DE"))
        {
            list = Lists.newArrayList(new Text(LOADING_VALUES, "Lade Werte...", "DE"),
                                      new Text(NO_VALUES_FOUND, "Keine Werte gefunden", "DE"),
                                      new Text(LABEL_BLOODSUGAR, "Blutzucker:", "DE"),
                                      new Text(LABEL_BE, "BE:", "DE"),
                                      new Text(LABEL_FACTOR, "Faktor:", "DE"),
                                      new Text(LABEL_BOLUS, "Bolus für BE:", "DE"),
                                      new Text(LABEL_CORRECTION, "Korrektureinheiten:", "DE"),
                                      new Text(LABEL_TOTAL_BOLUS, "Gesamtbolus:", "DE"),
                                      new Text(LABEL_NAME, "Name", "DE"),
                                      new Text(LABEL_WEIGHT, "Gewicht (g)", "DE"),
                                      new Text(LABEL_CARBOHYDRATES, "Kohlenhydrate (g)", "DE"),
                                      new Text(LABEL_BEGINNING, "Anfang", "DE"),
                                      new Text(LABEL_MORNING, "Morgens", "DE"),
                                      new Text(LABEL_NOON, "Mittags", "DE"),
                                      new Text(LABEL_EVENING, "Abends", "DE"),
                                      new Text(PROMPT_SEARCH, "Suchen..", "DE"),
                                      new Text(OVERVIEW, "Übersicht", "DE"),
                                      new Text(NEW_BLOODSUGAR, "Neuer Blutzuckerwert", "DE"),
                                      new Text(NEW_BOLUS, "Neuer Bolus", "DE"),
                                      new Text(CONFIGURATION, "Konfiguration", "DE"),
                                      new Text(BLOODSUGAR, "Blutzucker", "DE"),
                                      new Text(BE, "BE", "DE"),
                                      new Text(FACTOR, "Faktor", "DE"),
                                      new Text(TOTAL_BOLUS, "Gesamtbolus", "DE"),
                                      new Text(BOLUS, "Bolus", "DE"),
                                      new Text(SELECTION, "Auswahl", "DE"),
                                      new Text(CORRECTION, "Korrektur", "DE"),
                                      new Text(SAVE_SUCCESS, "Erfolgreich gespeichert", "DE"),
                                      new Text(REMOVE, "Entfernen", "DE"),
                                      new Text(DELETE, "Löschen", "DE"),
                                      new Text(ADD, "Hinzufügen", "DE"),
                                      new Text(CARBOHYDRATES, "Kohlenhydrate (g)", "DE"),
                                      new Text(WEIGHT, "Gewicht (g)", "DE"),
                                      new Text(NAME, "Name", "DE"),
                                      new Text(AMOUNT, "Menge", "DE"),
                                      new Text(SAVE, "Speichern", "DE"),
                                      new Text(DOUBLE_CORRECTION, "Doppelter Korrekturfaktor", "DE"),
                                      new Text(TITLE, "Diabetes Helper (OTF)", "DE"),
                                      new Text(TITLE_LOADING, "Diabetes Helper (OTF)     Lade Daten...", "DE"),
                                      new Text(TITLE_RECONNECTING, "Diabetes Helper (OTF)     Versuche Verbindung zum Server aufzubauen", "DE"),
                                      new Text(TITLE_RECONNECTION_FAILED, "Diabetes Helper (OTF)     Verbindung zum Server konnte nicht aufgebaut werden", "DE"),
                                      new Text(TITLE_CONNECTION_LOST, "Diabetes Helper (OTF)     Verbindung zum Server verloren", "DE"),
                                      new Text(TIME, "Zeit", "DE"));
        }

        return list;
    }
}