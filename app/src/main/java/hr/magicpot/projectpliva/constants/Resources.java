package hr.magicpot.projectpliva.constants;

/**
 * Created by xxx on 3.5.2016..
 */
public final class Resources {
    private boolean timepickerFragment = false;

    private static Resources ourInstance = new Resources();

    public static Resources getInstance() {
        return ourInstance;
    }

    private Resources() {

    }

    public boolean isTimepickerFragment() {
        return timepickerFragment;
    }

    public void setTimepickerFragment(boolean timepickerFragment) {
        this.timepickerFragment = timepickerFragment;
    }
}
