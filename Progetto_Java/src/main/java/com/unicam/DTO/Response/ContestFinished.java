package com.unicam.DTO.Response;

public class ContestFinished extends ContestProgress{

    private String winner;

    public ContestFinished(long id, String title, String status, String winner) {
        super(id, title, status);
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
}
