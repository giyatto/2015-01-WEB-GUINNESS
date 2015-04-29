package org.nhnnext.guinness.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.nhnnext.guinness.dao.GroupDao;
import org.nhnnext.guinness.dao.NoteDao;
import org.nhnnext.guinness.model.Note;
import org.nhnnext.guinness.util.JsonResult;
import org.nhnnext.guinness.util.ServletRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class SearchController {
	private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
	
	@Autowired
	private GroupDao groupDao;

	@Autowired
	private NoteDao noteDao;
	
	
	@RequestMapping("/search")
	private @ResponseBody JsonResult<Note> getSearchResult(WebRequest req, HttpSession session) throws IOException {
		String sessionUserId = ServletRequestUtil.getUserIdFromSession(session);
		String [] words = req.getParameter("words").split(" ");
//		Map<String, Integer> result = new HashMap();
		List<Note> notes = new ArrayList<Note>();

		for(String word : words) {
			notes.addAll(noteDao.searchQuery(sessionUserId, "%"+word+"%"));
		}
		Collections.sort(notes, (c1, c2) -> Integer.parseInt(c2.getNoteId()) - Integer.parseInt(c1.getNoteId()));
		HashSet<Note> hs = new HashSet<Note>(notes);
		ArrayList<Note> newNotes = new ArrayList<Note>(hs);
		//TODO notes의 중복 정렬.
		return new JsonResult<Note>(true, newNotes);
	}
}
