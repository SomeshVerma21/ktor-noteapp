package com.example.db

import com.example.models.Note

interface NotesDao {
    suspend fun createNote(note: Note) : Note?

    suspend fun getNote(nid:Int) : Note?

    suspend fun getNotes() : List<Note>
}