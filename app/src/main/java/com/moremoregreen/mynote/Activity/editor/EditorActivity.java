package com.moremoregreen.mynote.Activity.editor;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.moremoregreen.mynote.Api.ApiInterface;
import com.moremoregreen.mynote.R;
import com.thebluealliance.spectrum.SpectrumPalette;


public class EditorActivity extends AppCompatActivity implements EditorView {
    EditText ed_title, ed_note;
    ProgressDialog progressDialog;
    ApiInterface apiInterface;
    SpectrumPalette palette;
    int color;
    EditorPresenter presenter;

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

        presenter = new EditorPresenter(this);
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
                    presenter.saveNote(title, note, color);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void onAddSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish(); // 回到 main activity
    }

    @Override
    public void onAddError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
