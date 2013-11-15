package be.comicsdownloader.model.library;

public enum LibraryStructure {

    FULL_SPLITTED("FullSplitted", "<Serie>/Tome #/Chapter #/", new FullSplittedFolderLibraryStructureManager()),
    TOME_AND_CHAPTER_FUSED("TomeAndChapterFused", "<Serie>/Tome # - Chapter #/", new TomeAndChapterFusedLibraryStructureManager());
    private String name;
    private String representation;
    private LibraryStructureManager manager;

    private LibraryStructure(String name, String representation, LibraryStructureManager manager) {
        this.name = name;
        this.representation = representation;
        this.manager = manager;
    }

    public LibraryStructureManager getManager() {
        return manager;
    }

    public String getName() {
        return name;
    }

    public String getRepresentation() {
        return representation;
    }

    public static LibraryStructure getEnum(String name) {
        for (LibraryStructure libraryStructure : LibraryStructure.values()) {
            if (libraryStructure.getName().equals(name)) {
                return libraryStructure;
            }
        }
        throw new IllegalArgumentException("Cannot find corresponding LibraryStructure [Name given : " + name + "]");
    }
}
