package servlet;

import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService bankClientService = new BankClientService();

    Map<String, Object> pageVariables = new HashMap<>();



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(PageGenerator.getInstance().getPage("moneyTransactionPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        BankClientService bankClientService = new BankClientService();
        String name = req.getParameter("senderName");
        String password = req.getParameter("senderPass");
        Long count = Long.valueOf(Integer.parseInt(req.getParameter("count")));
        if (!(name == null || password == null)){
        String nameTo = req.getParameter("nameTo");
        BankClient client = bankClientService.getClientByName(name);
        if (client.getPassword().equals(password)){
           boolean rez =  bankClientService.sendMoneyToClient(client, nameTo, count);
            pageVariables.put("message","The transaction was successful");
            if (rez == false){
                pageVariables.put("message", "transaction rejected");
            }
        } else {
            pageVariables.put("message","transaction rejected");
        }} else {
            pageVariables.put("message","transaction rejected");

        }
        resp.getWriter().println(new PageGenerator().getPage("resultPage.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
