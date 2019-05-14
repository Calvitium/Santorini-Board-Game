package controler;

public enum AppMode { TEST(1), PLAY(2);

    private final int modeID;

    AppMode(int ModeID) {
        this.modeID = ModeID;
    }

    public boolean equals(AppMode mode){
        return this.modeID == mode.getModeID();
    }

    private int getModeID() {
        return modeID;
    }
}
