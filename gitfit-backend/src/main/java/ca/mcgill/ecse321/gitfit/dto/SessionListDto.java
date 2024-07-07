package ca.mcgill.ecse321.gitfit.dto;

import java.util.List;

public class SessionListDto {
    private List<SessionDto> sessions;

    public SessionListDto(List<SessionDto> sessions) {
        this.sessions = sessions;
    }

    public List<SessionDto> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionDto> sessions) {
        this.sessions = sessions;
    }
}
