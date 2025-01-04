package com.unicam.DTO.Response;

public class ContestFinished extends ContestProgress{

    private String winner;

    public ContestFinished(long id, String municipality, String title, String reward, String status, String winner) {
        super(id, municipality, title, reward, status);
        this.winner = winner;
    }

    public String getWinner() {
        return winner;
    }
}
