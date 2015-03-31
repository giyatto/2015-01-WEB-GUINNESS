package org.nhnnext.guinness.model;

import java.sql.SQLException;
import java.util.List;

import org.nhnnext.guinness.common.AbstractDao;
import org.nhnnext.guinness.exception.MakingObjectListFromJdbcException;

public class NoteDao extends AbstractDao {

	public void createNote(Note note) throws SQLException {
		String query = "insert into NOTES (noteText, targetDate, userId, groupId) values(?, ?, ?, ?)";
		queryNotForReturn(query, note.getNoteText(), note.getTargetDate(), note.getUserId(), note.getGroupId());
	}

	@SuppressWarnings("unchecked")
	public List<Note> readNoteList(String groupId, String endDate, String targetDate) throws MakingObjectListFromJdbcException, SQLException {
		String sql = "select * from NOTES,USERS where NOTES.userId = USERS.userId AND groupId = ? AND targetDate between ? and ? order by targetDate desc";
		String[] params = { "noteText", "targetDate", "userId", "groupId", "userName" };
		List<?> noteList = queryForReturn(Note.class, params, sql, groupId, endDate, targetDate);
		return (List<Note>) noteList;
	}
	
	public int checkGroupNotesCount(String groupId) throws SQLException {
		String sql = "select * from NOTES where groupId=?";
		return queryForCountReturn(sql, groupId); 
	}
}
