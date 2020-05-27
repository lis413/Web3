package servlet;

import exception.DBException;
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

public class RegistrationServlet extends HttpServlet {

    Map<String, Object> pageVariables = new HashMap<>();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BankClientService bankClientService = new BankClientService();
        String name = req.getParameter("name");
        String password = req.getParameter("password");
        System.out.println(password);

        if (bankClientService.getClientByName(name) == null) {

            String mon = req.getParameter("money");
            if (mon.equals("")) mon = "0";
            long money = Long.valueOf(Integer.parseInt(mon));
            System.out.println(money);
            BankClient client = new BankClient(name, password, money);
            try {
                bankClientService.addClient(client);
            } catch (DBException e) {
                e.printStackTrace();
            }
            System.out.println(client.toString());
            pageVariables.put("message","Add client successful");
            resp.getWriter().println(new PageGenerator().getPage("resultPage.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_OK);

        } else {
            pageVariables.put("message","Client not add");
            resp.getWriter().println(new PageGenerator().getPage("resultPage.html", pageVariables));
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        resp.getWriter().println(new PageGenerator().getPage("resultPage.html", pageVariables));


       // new BankClientService().addClient();
    }
}
