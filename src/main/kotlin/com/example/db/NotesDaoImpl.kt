package com.example.db

import com.example.models.Note
import com.example.models.Notes
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class NotesDaoImpl : NotesDao {
    private fun resultToNote(row: ResultRow) = Note(
        nid = row[Notes.nid],
        title = row[Notes.title],
        story = row[Notes.story]
    )

    override suspend fun createNote(note: Note): Note?  = DatabaseFactory.dbQuery {
        val res = Notes.insert {
            it[Notes.title] = note.title
            it[Notes.story] = note.story
        }.resultedValues

        res?.singleOrNull()?.let(::resultToNote)
    }

    override suspend fun getNote(nid: Int): Note? = DatabaseFactory.dbQuery {
        Notes.select(where = Notes.nid eq nid).map(::resultToNote).singleOrNull()
    }

    override suspend fun getNotes(): List<Note>  = DatabaseFactory.dbQuery {
        Notes.selectAll().map(::resultToNote)
    }
}