package com.bottlerocketstudios.continuitysample.core.ui.dialog;

import android.content.DialogInterface;

public class JustDismissOnClickListener implements DialogInterface.OnClickListener {
    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        if (dialogInterface != null) dialogInterface.dismiss();
    }
}
