package controler;

public enum GamePhases { PLACEMENT_PHASE("P"), SELECTION_PHASE("S"), MOVEMENT_PHASE("M"), BUILDING_PHASE("B"), WIN_CONDITION("W");

    private String phaseID;

    GamePhases(String PhaseID) {
        this.phaseID = PhaseID;
    }

    String getPhaseID() {
        return phaseID;
    }

    public boolean equals( GamePhases roundPhase) {
        return roundPhase.getPhaseID().equals(phaseID);
    }
}
