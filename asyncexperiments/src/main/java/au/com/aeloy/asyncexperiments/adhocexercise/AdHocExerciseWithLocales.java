package au.com.aeloy.asyncexperiments.adhocexercise;

import com.google.common.collect.ImmutableMap;

import java.util.Locale;

public class AdHocExerciseWithLocales {

    public static void main(String[] args) {
        System.out.println(new Locale("pt", "BR").getDisplayCountry());
        System.out.println(Locale.forLanguageTag("pt-BR").getDisplayCountry());

        // this seems to be a bizarre thing on Java.
        // the following "locale" doesn't fit BCP 47 leading the parse to fail
        // and return an empty locale (a locale with all attributes on empty state).
        // forLanguageTag factory method, uses LanguageTag.parse which internally
        // sets a variable status with an error which is not used and available anywhere
        // after tha Locale has been created.
        System.out.println(Locale.forLanguageTag("pt_BR"));

        // so if you do the following, you will create just empty Locales
        // and the map won't be created because of duplicate keys (empty locales)
        ImmutableMap<Locale, Integer> map = ImmutableMap.of(
                Locale.forLanguageTag("pt_BR"), 1000,
                Locale.forLanguageTag("en_AU"), 2000,
                Locale.ENGLISH, 3000
        );

        System.out.println(map);
    }
}
