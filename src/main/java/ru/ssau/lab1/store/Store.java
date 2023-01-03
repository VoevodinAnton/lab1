package ru.ssau.lab1.store;

import ru.ssau.lab1.model.Account;
import ru.ssau.lab1.model.Seat;

import java.util.Collection;

public interface Store {

    Collection<Seat> getAllSeats();

    void tempSeatOwner(int seatId, int accId);

    void setSetSeatOwner(int ownerId);

    Seat getSeatById(int seatId);

    Collection<Seat> getSeatByOwner(int accId);

    int addAcc(Account acc);

    Account getAccById(int id);

    Account getAccByLogPwd(String login, String pwd);
}
