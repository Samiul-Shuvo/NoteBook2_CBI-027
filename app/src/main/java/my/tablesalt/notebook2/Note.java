package my.tablesalt.notebook2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.concurrent.Executors;

public class Note extends AppCompatActivity {

    private NoteDatabase database;
    private NoteDao noteDao;
    private EditText titleEditText, contentEditText;
    private Button createButton, readButton, updateButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Database
        database = NoteDatabase.getInstance(this);
        noteDao = database.noteDao();

        // Initialize UI Elements
        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        createButton = findViewById(R.id.createButton);
        readButton = findViewById(R.id.readButton);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);

        // Set Button Click Listeners
        createButton.setOnClickListener(v -> createNote());
        readButton.setOnClickListener(v -> readNotes());
        updateButton.setOnClickListener(v -> updateNote());
        deleteButton.setOnClickListener(v -> deleteNote());
    }

    private void createNote() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        if (!title.isEmpty() && !content.isEmpty()) {
            NoteEntity note = new NoteEntity();
            note.setTitle(title);
            note.setContent(content);

            Executors.newSingleThreadExecutor().execute(() -> {
                noteDao.insert(note);
                runOnUiThread(() -> Toast.makeText(Note.this, "Note Created", Toast.LENGTH_SHORT).show());
            });
        } else {
            Toast.makeText(this, "Title and Content cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void readNotes() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<NoteEntity> notes = noteDao.getAllNotes();
            StringBuilder builder = new StringBuilder();
            for (NoteEntity note : notes) {
                builder.append("ID: ").append(note.getId()).append(", Title: ").append(note.getTitle()).append(", Content: ").append(note.getContent()).append("\n");
            }
            runOnUiThread(() -> Toast.makeText(Note.this, builder.toString(), Toast.LENGTH_LONG).show());
        });
    }

    private void updateNote() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        if (!title.isEmpty() && !content.isEmpty()) {
            Executors.newSingleThreadExecutor().execute(() -> {
                NoteEntity note = new NoteEntity();
                note.setId(1); // Update note with ID 1 (example)
                note.setTitle(title);
                note.setContent(content);
                noteDao.update(note);
                runOnUiThread(() -> Toast.makeText(Note.this, "Note Updated", Toast.LENGTH_SHORT).show());
            });
        } else {
            Toast.makeText(this, "Title and Content cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteNote() {
        Executors.newSingleThreadExecutor().execute(() -> {
            NoteEntity note = new NoteEntity();
            note.setId(1); // Delete note with ID 1 (example)
            noteDao.delete(note);
            runOnUiThread(() -> Toast.makeText(Note.this, "Note Deleted", Toast.LENGTH_SHORT).show());
        });
    }
}
