package com.gvjay.classmanager.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gvjay.classmanager.Database.ClassObject;
import com.gvjay.classmanager.Database.DBHelper;
import com.gvjay.classmanager.MainActivity;
import com.gvjay.classmanager.R;

public class DeleteDialog extends Dialog {

    private boolean completeDeletion;
    private ClassObject classObject;

    public DeleteDialog(@NonNull Context context, ClassObject classObject) {
        super(context);
        completeDeletion = false;
        this.classObject = classObject;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.delete_dialog_fragment);

        TextView heading = findViewById(R.id.ddf_heading);
        heading.setText("Delete " + classObject.title + "?");
        RadioGroup radioGroup = findViewById(R.id.ddf_radioGroup);
        radioGroup.check(R.id.ddf_partial_deletion);
        radioGroup.setOnCheckedChangeListener(new RadioChangeListener());

        Button okBtn = findViewById(R.id.ddf_ok_btn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteClass(getContext());
                dismiss();
                MainActivity.getDataReloader().reloadData();
            }
        });

        Button cancelButton = findViewById(R.id.ddf_cancel_btn);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                MainActivity.getDataReloader().reloadData();
            }
        });
    }

    private class RadioChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i){
                case R.id.ddf_partial_deletion:
                    completeDeletion = false;
                    break;
                case R.id.ddf_complete_deletion:
                    completeDeletion = true;
                    break;
            }
        }
    }

    private void deleteClass(Context context){
        DBHelper dbHelper = new DBHelper(context);
        if(completeDeletion){
            dbHelper.deleteClassByTitle(classObject.title);
        }else {
            dbHelper.deleteClassByID(classObject.id);
        }
        dbHelper.close();
    }
}
