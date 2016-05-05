package hr.magicpot.projectpliva;

import java.util.Observable;

/**
 * Created by xxx on 30.4.2016..
 */
public class ActionObservable extends Observable {
    private static ActionObservable instance = new ActionObservable();

    public static ActionObservable getInstance() {
        return instance;
    }

    private ActionObservable() {
    }

    public void updateCalendar() {
        synchronized (this) {
            setChanged();
            notifyObservers();
        }
    }
}
