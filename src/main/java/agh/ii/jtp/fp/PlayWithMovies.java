package agh.ii.jtp.fp;

import agh.ii.jtp.fp.dal.ImdbTop250;
import agh.ii.jtp.fp.model.Movie;
import agh.ii.jtp.fp.utils.Utils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface PlayWithMovies {

    static List<Movie> movies() {
        return ImdbTop250.movies().orElseThrow();
    }

    static Set<String> ex01(String director) {
        return movies().stream()
                .filter(m -> m.directors().contains(director))
                .map(Movie::title)
                .collect(Collectors.toSet());
    }

    static Set<String> ex02(String actor) {
        return movies().stream()
                .filter(m -> m.actors().contains(actor))
                .map(Movie::title)
                .collect(Collectors.toSet());
    }

    static Map<String, Long> ex03() {
        return movies().stream()
                .flatMap(m -> Utils.oneToManyByDirector(m).stream())
                .collect(Collectors.groupingBy(m -> m.directors().get(0), Collectors.counting()));
    }

    static Map<String, Long> ex04() {
        return ex03().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    static Map<String, Set<String>> ex05() {
        Set<String> top10Directors = ex04().keySet();
        return movies().stream()
                .flatMap(m -> Utils.oneToManyByDirector(m).stream())
                .filter(m -> top10Directors.contains(m.directors().get(0)))
                .collect(Collectors.groupingBy(
                        m -> m.directors().get(0),
                        Collectors.mapping(Movie::title, Collectors.toSet())
                ));
    }

    static Map<String, Long> ex06() {
        return movies().stream()
                .flatMap(m -> Utils.oneToManyByActor(m).stream())
                .collect(Collectors.groupingBy(m -> m.actors().get(0), Collectors.counting()));
    }

    static Map<String, Long> ex07() {
        return ex06().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(9)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    static Map<String, Set<String>> ex08() {
        Set<String> top9Actors = ex07().keySet();
        return movies().stream()
                .flatMap(m -> Utils.oneToManyByActor(m).stream())
                .filter(m -> top9Actors.contains(m.actors().get(0)))
                .collect(Collectors.groupingBy(
                        m -> m.actors().get(0),
                        Collectors.mapping(Movie::title, Collectors.toSet())
                ));
    }

    static Map<String, Long> ex09() {
        return movies().stream()
                .flatMap(m -> Utils.oneToManyByActorDuo(m).stream())
                .collect(Collectors.groupingBy(m -> m.actors().get(0), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    static Map<String, Set<String>> ex10() {
        Set<String> top5Duos = ex09().keySet();
        return movies().stream()
                .flatMap(m -> Utils.oneToManyByActorDuo(m).stream())
                .filter(m -> top5Duos.contains(m.actors().get(0)))
                .collect(Collectors.groupingBy(
                        m -> m.actors().get(0),
                        Collectors.mapping(Movie::title, Collectors.toSet())
                ));
    }
}
