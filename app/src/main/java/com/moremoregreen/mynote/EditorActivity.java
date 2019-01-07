package com.moremoregreen.mynote;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.moremoregreen.mynote.Model.Note;
import com.thebluealliance.spectrum.SpectrumPalette;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity {
    EditText ed_title, ed_note;
    ProgressDialog progressDialog;
    ApiInterface apiInterface;
    SpectrumPalette palette;
    int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        ed_title = findViewById(R.id.title);
        ed_note = findViewById(R.id.note);
        palette = findViewById(R.id.palette);
        palette.setOnColorSelectedListener(
                clr -> color = clr
        );
        //預設顏色
        palette.setSelectedColor(-8469267);
        color = -8469267;


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                String title = ed_title.getText().toString().trim();
                String note = ed_note.getText().toString().trim();
                int color = this.color;
                if (title.isEmpty()) {
                    ed_title.setError("Please enter a title");
                } else if (note.isEmpty()) {
                    ed_note.setError("Please enter a note");
                } else {
                    saveNote(title, note, color);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void saveNote(final String title, final String note, final int color) {
        progressDialog.show();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Note> call = apiInterface.saveNote(title, note, color);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(@NonNull Call<Note> call, @NonNull Response<Note> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();
                    if (success) {
                        Toast.makeText(EditorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finish(); //回到 main activity
                    } else {
                        Toast.makeText(EditorActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        //if error 留在 editorActivity
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Note> call, @NonNull Throwable t) {
                Toast.makeText(EditorActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
