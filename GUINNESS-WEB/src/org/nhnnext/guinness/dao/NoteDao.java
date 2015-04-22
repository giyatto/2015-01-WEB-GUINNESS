package org.nhnnext.guinness.dao;

import java.util.ArrayList;
import java.util.List;

import org.nhnnext.guinness.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class NoteDao extends JdbcDaoSupport {
	private static final Logger logger = LoggerFactory.getLogger(NoteDao.class);
	public void createNote(Note note) {
		String sql = "insert into NOTES (noteText, targetDate, userId, groupId) values(?, ?, ?, ?)";
		
		getJdbcTemplate().update(sql, note.getNoteText(), note.getTargetDate(), note.getUserId(), note.getGroupId());
	}
	
	public List<Note> readNoteList(String groupId, String endDate, String targetDate) {
		String sql = "select * from NOTES,USERS "
				+ "where NOTES.userId = USERS.userId "
				+ "and groupId = ? "
				+ "and NOTES.targetDate between ? and ? "
				+ "order by targetDate desc";
		
		try {
			return getJdbcTemplate().query(sql, (rs, rowNum) -> new Note(
					rs.getString("noteId"), rs.getString("noteText"),
					rs.getString("targetDate"), rs.getString("userId"), 
					rs.getString("groupId"), rs.getString("userName")), groupId, endDate, targetDate);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Note>();
		}
	}
	
	public List<Note> readNoteList(String groupId, String endDate, String targetDate, String userIds) {
		String sql = "select * from NOTES,USERS "
				+ "where NOTES.userId = USERS.userId "
				+ "and groupId = ? "
				+ "and NOTES.targetDate between ? and ? "
				+ "and NOTES.userId in ("+userIds+") "
				+ "order by targetDate desc";
		
		logger.debug(sql);
		
		try {
			return getJdbcTemplate().query(sql, (rs, rowNum) -> new Note(
					rs.getString("noteId"), rs.getString("noteText"),
					rs.getString("targetDate"), rs.getString("userId"), 
					rs.getString("groupId"), rs.getString("userName")), groupId, endDate, targetDate);
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<Note>();
		}
	}
	
	public int checkGroupNotesCount(String groupId) {
		String sql = "select count(*) from NOTES where groupId=?";
		
		return getJdbcTemplate().queryForObject(sql, Integer.class, groupId);
	}

	public Note readNote(String noteId) {
		String sql = "select *from NOTES,USERS where noteId = ? AND NOTES.userId = USERS.userId";
		
		try {
			return getJdbcTemplate().queryForObject(sql, (rs, rowNum) -> new Note(
					rs.getString("noteId"), rs.getString("noteText"),
					rs.getString("targetDate"), rs.getString("userId"), 
					rs.getString("groupId"), rs.getString("userName")
					), noteId);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
}
