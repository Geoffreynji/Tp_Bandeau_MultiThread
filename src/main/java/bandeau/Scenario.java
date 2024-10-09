package bandeau;
import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Classe utilitaire pour représenter la classe-association UML
 */
class ScenarioElement {

    Effect effect;
    int repeats;

    ScenarioElement(Effect e, int r) {
        effect = e;
        repeats = r;
    }
}
/**
 * Un scenario mémorise une liste d'effets, et le nombre de repetitions pour chaque effet
 * Un scenario sait se jouer sur un bandeau.
 */
public class Scenario {

    private final List<ScenarioElement> myElements = new LinkedList<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock lecture = lock.readLock();
    private final Lock ecriture = lock.writeLock();

    /**
     * Ajouter un effect au scenario.
     *
     * @param e l'effet à ajouter
     * @param repeats le nombre de répétitions pour cet effet
     */

    private class ThreadWithScenario extends Thread
    {
        private Bandeau newBandeau;

        public ThreadWithScenario(Bandeau newBandeau) {
            this.newBandeau = newBandeau;
        }

        public void run() {
            lecture.lock();  //Utilise le verrou de lecture pour garantir que aucun thread ne modifie
            try {
                for (ScenarioElement element : myElements) {
                    for (int repeats = 0; repeats < element.repeats; repeats++) {
                        element.effect.playOn(newBandeau);
                    }
                }
            } finally {
                lecture.unlock();  // Déverrouiller pour éviter les blocages
            }
        }
}



public void addEffect(Effect e, int repeats) {
    ecriture.lock();  
    try {
        myElements.add(new ScenarioElement(e, repeats));
    } finally {
        ecriture.unlock();  // Déverrouille après la modification
    }
}


    /**
     * Jouer ce scenario sur un bandeau
     *
     * @param b le bandeau ou s'afficher.
     */
    public void playOn(Bandeau newBandeau) {  
        ThreadWithScenario Bandeau = new ThreadWithScenario(newBandeau);
        Bandeau.start();

}

}

