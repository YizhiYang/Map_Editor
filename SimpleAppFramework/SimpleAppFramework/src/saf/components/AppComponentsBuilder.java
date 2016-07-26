package saf.components;

/**
 * This interface provides the structure required for a builder
 * object used for initializing all components for this application.
 * This is one means of employing a component hierarchy.
 * 
 */
public interface AppComponentsBuilder {

    /**
     *
     * @return
     * @throws Exception
     */
    public AppDataComponent buildDataComponent() throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    public AppFileComponent buildFileComponent() throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    public AppWorkspaceComponent buildWorkspaceComponent() throws Exception;
}
