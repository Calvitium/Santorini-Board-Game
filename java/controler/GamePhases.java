package controler;

public enum GamePhases { SELECTION_PHASE(1), MOVEMENT_PHASE(2), BUILDING_PHASE(3);

    private int phaseID;

    GamePhases(int PhaseID) {
        this.phaseID = PhaseID;
    }

    int getPhaseID() {
        return phaseID;
    }

    public boolean equals( GamePhases roundPhase) {
        return roundPhase.getPhaseID() == phaseID;
    }
}
