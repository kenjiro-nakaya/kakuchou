package controllers.like;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class LikesCreateServlet
 */
@WebServlet("/likes/create")
public class LikeNumberCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LikeNumberCreateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        EntityManager em = DBUtil.createEntityManager();

        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("report_id")));

        Like l = new Like();

        l.setEmployee((Employee) request.getSession().getAttribute("login_employee"));
        l.setReport(r);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        l.setCreated_at(currentTime);
        l.setUpdated_at(currentTime);

        Integer likes =  r.getLike_number() + 1;
        r.setLike_number(likes);

        // データベースを更新
        em.getTransaction().begin();
        em.persist(l);
        em.getTransaction().commit();
        em.close();
        request.getSession().setAttribute("flush", "いいねしました。");

        // indexページへリダイレクト
        response.sendRedirect(request.getContextPath() + "/reports/index");

    }
}
