package appv2;


/**
 * <p>This is the entry point of this application.</p>
 *
 * <p>Love Letter Premium Edition by Seiji Kanai.<br />
 * Java implementation by Magnus Zoeschinger.</p>
 *
 * @since 2.0
 * @author mzoesch
 * @version 2.0
 */
public class App {

    /**
     * <p>Utility class. No instances allowed.</p>
     *
     * @throws IllegalStateException If an instance of this class is created.
     */
    public App() throws IllegalStateException {
        throw new IllegalStateException("Utility class");
    }

    /**
     * <p>Entry point of this application.<br />
     * The GameInstance is created here.</p>
     *
     * @param args CMD arguments (ignored).
     */
    public static void main(String[] args) {
        double start = System.currentTimeMillis();

        appv2.core.GameInstance.run();

        System.out.printf(
                "The application took %f seconds to run.\n",
                (System.currentTimeMillis() - start) / 1000
        );
        return;
    }

}
