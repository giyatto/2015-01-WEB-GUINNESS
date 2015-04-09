package org.nhnnext.guinness.controller.notes;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nhnnext.guinness.common.Forwarding;
import org.nhnnext.guinness.common.ServletRequestUtil;
import org.nhnnext.guinness.common.WebServletUrl;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.model.NoteDao;

@WebServlet(WebServletUrl.NOTE_CREATE)
public class CreateNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String sessionUserId = ServletRequestUtil.checkSessionAttribute(req, resp);
		Map<String, String> paramsList = ServletRequestUtil.getRequestParameters(req, "groupId", "noteText", "targetDate");
		
		String targetDate = paramsList.get("targetDate") + " " + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
		
		if (paramsList.get("noteText").equals("")) {
			resp.sendRedirect("/g/" + paramsList.get("groupId"));
			return;
		}

		try {
			NoteDao.getInstance().createNote(new Note(paramsList.get("noteText"), targetDate, sessionUserId, paramsList.get("groupId")));
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			Forwarding.forwardForException(req, resp);
			return;
		}
	}
}
