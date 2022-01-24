# 3-Semester-SWEN-MTCG
 ## Installation
  - git clone https://github.com/immxmmi/3-Semester-SWEN-MTCG.git
  - docker pull immxmmi/3-semester-swen-mtcg
  - Datenbank - Username: swe1user PW: swe1pw
  - Server starten
      - Klasse Main - function main() ausführen.
      - Server hört auf den Port: 10001 IP: 127.0.0.1 oder localhost max 5 Client können sich verbinden.
      - Es ist möglich den PORT IP und CLIENT anzahl zu verändern im main
      
## Ressourcen
   - im Ordner res befinden sich:
     - Test-Script
     - Datenbank - SQL Code
     - Datenbank - Diagramm
     - UML - Diagramme
     - History
   

## Funkitionen
    - Admin:
        - Admin kann Packages erstellen und im Store hinzufügen
        
    - User:
        - User kann sich registrieren
        - User kann sich einzuloggen
        - User kann Packages(durch zufall ausgesucht) kaufen
        - User kann seinen Stack (=Sammlung seiner ganzen Karten) anzeigen lassen
        - User kann Decks erstellen
        - User kann sein Profil-Data ausgben: 
            - Bio
            - Image
            - Spieler-Profil
        - User kann sein aktuelles Spieler Profil ausgeben: 
            - Username
            - ELO Level 
            - Coins
            - Stack
            - Deck
        - User kann die Highscore Liste anzeigen lassen (in Bearbeitung)
        - User kann mit anderen Spielern kämpfen (in Bearbeitung - User kann momentan gegen sich selbst spielen)
        - User kann Karte im Store anbieten (in Bearbeitung)
        - User kann alle seine Trading deals anzeigen lassen (in Bearbeitung)
        - User kann seine Trading deals löschen aus dem Store (in Bearbeitung)
        - User kann mit anderen Spielern Karten tauschen (in Bearbeitung - Service Handler)
        
    - Allgemein:
            - Verlauf wird in history.log gespeichert

## Spiel Anleitung

    - Karten
        - Es gibt zwei Karten TYPEN MONSTER und SPELL.
        - Jeder dieser Karten hat ein ELEMENT: WATER,FIRE,NORMAL
        - Jeder dieser Karten hat POWER
        
    - Spieler
        - Jeder Spieler bekommt nachdem Registrieren 20 coins.
        - Mit den coins kann der Spieler sich Packages kaufen.
        - Jedes Packages kostet momentan 5 coins und besitzt 5 Karten.
        - Diese Karten werden, dann in der Karten Sammlung gespeichert (= Stack)
        - um Spielen zu können benötiogt jeder User ein Deck, welches man aus seiner eigenen Karten Sammlung erstellen kann
        - Ein Deck besteht aus 4 Karten die jeder User selber aus dem Stack aussuchen kann
        - Jeder User hat zu Beginn 100 ELO punkte ( = Spieler LEVEL)
        - Verliert man eine Runde so bekommt der Gegner während dem Spiel die Karte aus dem Deck
        - Der erste Spieler der alle 8 Karten hat gewinnt
        - Kommt es öfters zu unenschieden, oder die 100 Runden überschritten werden, so gewinnt, der der öfters gewonnen hat
        
    - Regeln 
        - Goblins are too afraid of Dragons to attack.
        - Wizzard can control Orks so they are not able to damage them.
        - The armor of Knights is so heavy that WaterSpells make them drown them instantly.
        - The Kraken is immune against spells.
        - The FireElves know Dragons since they were little and can evade their attacks.
        
    - Beispiele: 
    
      - Monster vs Monster
         PlayerA: WaterGoblin (10 Damage) vs PlayerB: FireTroll (15 Damage) => Troll defeats Goblin
         PlayerB: FireTroll (15 Damage) vs PlayerA: WaterGoblin (10 Damage) => Troll defeats Goblin
         
      - Spell vs Spell
         PlayerA: FireSpell (10 Damage) vs PlayerB: WaterSpell (20 Damage) => 10 VS 20 -> 05 VS 40 => WaterSpell wins 
         PlayerA: FireSpell (20 Damage) vs PlayerB: WaterSpell (05 Damage) => 20 VS 05 -> 10 VS 10 => Draw (no action) 
         PlayerA: FireSpell (90 Damage) vs PlayerB: WaterSpell (05 Damage) => 90 VS 05 -> 45 VS 10 => FireSpell wins 
         
      - Mixed
         PlayerA: FireSpell (10 Damage) vs PlayerB: WaterGoblin (10 Damage) => 10 vs 10 -> 05 vs 20 => WaterGoblin wins
         PlayerA: WaterSpell (10 Damage) vs PlayerB: WaterGoblin (10 Damage) => 10 vs 10 -> 10 vs 10 => Draw
         PlayerA: RegularSpell (10 Damage) vs PlayerB: WaterGoblin (10 Damage) => 10 vs 10 > 20 vs 05 => Knight wins
         PlayerA: RegularSpell (10 Damage) vs PlayerB: Knight (15 Damage) => 10 vs 15 -> 10 vs 15 => Knight wins 

## Ausführliche Installation ohne DockerHub
    - 1.Schritt: Git
       -  git clone https://github.com/immxmmi/3-Semester-SWEN-MTCG.git
       
    - 2.Schritt: Datenbank Einrichten - Postgres
       * Um das Spiel zum laufen zu bringen wird eine Datenbank benötigt.
       * Anleitung zum installieren: 
        - docker installieren: https://docs.docker.com/get-docker/ 
        - auf docker Hub ("APPS-STORE"): https://hub.docker.com/_/postgres
        - docker pull postgres
        - docker run --name swe1db -e POSTGRES_USER=swe1user -e POSTGRES_PASSWORD=swe1pw -p 5432:5432 postgres
      * Datenbank SQL-Datei ausführen:
        - docker run swe1db
        - git ordner : ./database.sql
        
    - 3.Schritt: Server starten
        - Klasse Main - function main() ausführen.
        - Server hört auf den Port:10001 IP: 127.0.0.1 oder localhost max 5 Client können sich verbinden.
        - Es ist möglich den PORT IP und CLIENT anzahl zu verändern im main  

 
