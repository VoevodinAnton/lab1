package ru.ssau.lab1.servlet;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.ssau.lab1.model.Account;
import ru.ssau.lab1.model.Seat;
import ru.ssau.lab1.store.PsqlStore;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = "/pay")
public class PaymentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        resp.setCharacterEncoding("UTF-8");
        List<Seat> seats = (List<Seat>) session.getAttribute("chosenSeats");
        String jsonOut = new Gson().toJson(seats);
        try (PrintWriter writer = resp.getWriter()) {
            writer.write(jsonOut);
            writer.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Account acc = new Account();
        try (BufferedReader read = req.getReader()) {
            StringBuilder fullLine = new StringBuilder();
            String oneLine;
            while ((oneLine = read.readLine()) != null) {
                fullLine.append(oneLine);
            }
            JSONObject json = (JSONObject) new JSONParser().parse(fullLine.toString());

            String name = json.get("name").toString();
            String email = json.get("email").toString();
            acc.setName(name);
            acc.setEmail(email);
            int id = PsqlStore.instOf().addAcc(acc);
            acc.setId(id);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        HttpSession session = req.getSession();
        List<Seat> seats = (List<Seat>) session.getAttribute("chosenSeats");
        for (Seat seat : seats) {
            PsqlStore.instOf().tempSeatOwner(seat.getId(), acc.getId());
        }

        PsqlStore.instOf().setSetSeatOwner(acc.getId());
        resp.sendRedirect(req.getContextPath() + "/hall");
    }
}
