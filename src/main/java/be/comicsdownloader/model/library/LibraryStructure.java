package be.comicsdownloader.model.library;

public enum LibraryStructure {

    FULL_SPLITTED("FullSplitted", "<Serie>/Tome #/Chapter #/", new FullSplittedFolderLibraryStructureManager());
    private String representation;
    private LibraryStructureManager manager;

    private LibraryStructure(String name, String representation, LibraryStructureManager manager) {
        this.representation = representation;
        this.manager = manager;
    }

    public LibraryStructureManager getManager() {
        return manager;
    }

    public String getRepresentation() {
        return representation;
    }
}
