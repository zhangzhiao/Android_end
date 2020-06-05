package com.xbzn.Intercom.utils;

import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TextureVideoViewOutlineProvider extends ViewOutlineProvider {
    @Override
    public void getOutline(View view, Outline outline) {
        int left = 0;
        int top = (view.getHeight() - view.getWidth()) / 2;
        int right = view.getWidth();
        int bottom = (view.getHeight() - view.getWidth()) / 2 + view.getWidth();
        outline.setOval(left, top, right, bottom);
    }
}