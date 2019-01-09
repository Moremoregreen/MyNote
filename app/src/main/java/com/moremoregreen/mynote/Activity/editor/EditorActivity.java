package com.moremoregreen.mynote.Activity.editor;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
    int color, id;
    String title, note;
    Menu actionMenu;
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


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        presenter = new EditorPresenter(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        note = intent.getStringExtra("note");
        color = intent.getIntExtra("color", 0);

        setDataFromIntentExtra();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        actionMenu = menu;
        if (id != 0) {
            actionMenu.findItem(R.id.edit).setVisible(true);
            actionMenu.findItem(R.id.delete).setVisible(true);
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);
        } else {
            actionMenu.findItem(R.id.edit).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.save).setVisible(true);
            actionMenu.findItem(R.id.update).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = ed_title.getText().toString().trim();
        String note = ed_note.getText().toString().trim();
        int color = this.color;
        switch (item.getItemId()) {
            case R.id.save:
                if (title.isEmpty()) {
                    ed_title.setError("Please enter a title");
                } else if (note.isEmpty()) {
                    ed_note.setError("Please enter a note");
                } else {
                    presenter.saveNote(title, note, color);
                }
                return true;
            case R.id.edit:
                editMode();
                actionMenu.findItem(R.id.edit).setVisible(false);
                actionMenu.findItem(R.id.delete).setVisible(false);
                actionMenu.findItem(R.id.save).setVisible(false);
                actionMenu.findItem(R.id.update).setVisible(true);
                return true;
            case R.id.update:
                if (title.isEmpty()) {
                    ed_title.setError("Please enter a title");
                } else if (note.isEmpty()) {
                    ed_note.setError("Please enter a note");
                } else {
                    presenter.updateNote(id, title, note, color);
                }
                return true;
            case R.id.delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Confime!");
                alertDialog.setMessage("Are you sure?");
                alertDialog.setNegativeButton("Yes", (dialog, which) ->{
                        dialog.dismiss();
                presenter.deleteNote(id);
                });
                alertDialog.setPositiveButton("Cancle", (dialog, which) -> dialog.dismiss());
                alertDialog.show();
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
    public void onRequestSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish(); // 回到 main activity
    }

    @Override
    public void onRequestError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void setDataFromIntentExtra() {
        if (id != 0) {
            ed_title.setText(title);
            ed_note.setText(note);
            palette.setSelectedColor(color);

            getSupportActionBar().setTitle(R.string.Update_Note);
            readMode();
        } else {
            //預設顏色
            palette.setSelectedColor(getResources().getColor(R.color.white));
            color = getResources().getColor(R.color.white);
            editMode();
        }
    }

    private void editMode() {
        ed_title.setFocusableInTouchMode(true);
        ed_note.setFocusableInTouchMode(true);
        palette.setEnabled(true);
    }

    private void readMode() {
        ed_title.setFocusableInTouchMode(false);
        ed_note.setFocusableInTouchMode(false);

        ed_title.setFocusable(false);
        ed_note.setFocusable(false);
        palette.setEnabled(false);
    }
}
