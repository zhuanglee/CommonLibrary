package cn.lzh.common.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.lzh.common.utils.WatermarkHelper;

public abstract class BaseWatermarkFragment extends Fragment {

    private Drawable watermarkDrawable;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        watermarkDrawable = WatermarkHelper.getWatermarkDrawable(context, "Android");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getContentView(inflater, container, savedInstanceState);
        view.setBackgroundDrawable(watermarkDrawable);
        return view;
    }

    protected abstract View getContentView(LayoutInflater inflater,
                                           ViewGroup container, Bundle savedInstanceState);

}
