package de.samples.java;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.DoubleSupplier;

public class LambdaDemo {

  /*
   * Lambda Expression / Arrow Function / Closure
   * --> Verzögerte Ausführung
   *
   * Beispiel:
   *  - Koch:
   *     - soll kochen
   *     - Problem: braucht Zutaten
   *  - Szenario1: Koch macht alles -> muss wissen, woher die Zutaten kommen
   *  - Szenario2: Aufteilung der Verantwortlichkeiten
   *     - Koch kann kochen, weiß aber nicht, woher die Zutaten kommen
   *     - ICH weiß, woher er die Zutaten holen soll
   *     -> Anweisung von mir: "Koche bitte, und wenn Du Zutaten brauchst, geh zu Netto."
   *     -> WENN der Koch keine Zutaten hat, DANN geht er zu Netto
   *        - Koch entscheidet, WANN bzw. OB er zu Netto geht
   *        - Ich entscheide, DASS er zu Netto geht
   */

  private static double add(double summand1, double summand2) {
    return summand1 + summand2;
  }

  private static double add2() {
    double sum = 0;
    while(sum<=10) {
      sum += Math.random();
    }
    return sum;
  }

  // funktionales Interface: Interface mit genau 1 abstrakten Methode
  @FunctionalInterface
  private interface NumberGenerator {
    double generateNumber();
  }

  private static double add3(NumberGenerator generator) {
    double sum = 0;
    while(sum<=10) {
      sum += generator.generateNumber();
    }
    return sum;
  }

  private static double add4(DoubleSupplier generator) {
    double sum = 0;
    while(sum<=10) {
      sum += generator.getAsDouble();
    }
    return sum;
  }


  public static void main(String[] args) {
    // Anforderung: Addiere 2 Zahlen
    System.out.println(add(3.5, 2.3));
    // Anforderung: Addiere 2 Zufallszahlen
    System.out.println(add(Math.random(), Math.random()));
    // Anforderung: Addiere Zufallszahlen so lange, bis die Summe >10
    System.out.println(add2());
    // Anforderung: Entscheidung für Math-random() ("Netto") -> main
    System.out.println(add3(new NumberGenerator() {
      @Override
      public double generateNumber() {
        return Math.random();
      }
    }));
    System.out.println(add3(new NumberGenerator() {
      @Override
      public double generateNumber() {
        return 0.3; // geh zu Lidl
      }
    }));
    // Lambdas (in Java)
    System.out.println(add3(/*new NumberGenerator() {
      @Override
      public double generateNumber*/() -> {
        return Math.random();
      }
    /*}*/));
    System.out.println(add3(
      () -> {
        return Math.random();
      }
    ));
    // Spezialfall: nur 1 Anweisung
    System.out.println(add3(
      () -> Math.random()
    ));
    // Spezialfall: Parameter matchen
    System.out.println(add3(
       Math::random // Method Reference Operator
    ));
    // Zum Vergleich:
    // erst 2x Math.random(), dann add(...)
    System.out.println(add(Math.random(), Math.random()));
    // erst add3, von da aus Math.random() -> verzögert, beliebig oft, mit beliebigen Parametern
    System.out.println(add3(Math::random));
    System.out.println(add4(Math::random));

    // Beispiel. Streams
    Collection<String> namen = List.of("Alihan", "Denis", "Martin", "Maximilian", "Ralf");
    // Anforderung: alle Kurznamen (<6 Zeichen) in Großbuchstaben
    {
      // Variante 1
      Collection<String> kurzNamen = new LinkedList<>();
      for(String name : namen) {
        if(name.length()<6) {
          kurzNamen.add(name.toUpperCase());
        }
      }
      System.out.println(kurzNamen);
    }
    {
      // Variante 2
      // Vorteil von Streams: PULL-Prinzip (Lazy)
      System.out.println(
        namen
          .stream()
          .filter(name -> name.length()<6)
          .map(String::toUpperCase)
          .toList()
      );
    }

  }

}
