package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.common.WebServletURL;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDAO;

@WebServlet(WebServletURL.NOTE_CREATE)
public class CreateNoteServlet extends HttpServlet {
	private static final long serialVersionUID = -4786711774618202192L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");

		HttpSession session = req.getSession();
		String userId = (String)session.getAttribute("sessionUserId");
		if (userId == null) {
			resp.sendRedirect("/");
			return;
		}
		// TODO groupId 가져오는 방법 구현
		String groupId = "abcde";
		String targetDate = req.getParameter("targetDate");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, Integer.parseInt(targetDate));
		targetDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
		String noteText = req.getParameter("noteText");

		Note note = new Note(noteText, targetDate, userId, groupId);

		NoteDAO noteDAO = new NoteDAO();
		try {
			noteDAO.createNote(note);
			resp.sendRedirect("/notes.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}