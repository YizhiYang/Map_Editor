package saf.components;

/**
 * This interface provides the structure for data components in
 * our applications. Note that by doing so we make it possible
 * for customly provided descendent classes to have their reset
 * method called from this framework.
 * 
 */
public interface AppDataComponent {

    /**
     *
     */
    public void reset();
}
