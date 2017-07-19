package com.hc.posterccb.ui.contract;

import java.util.ArrayList;

/**
 * Created by alex on 2017/7/19.
 */

public class FullHContract {

    public interface FullHView {
        void playVideo(ArrayList<String> pathList);


    }

    public interface FullHFragmentPresenter {
        void buildPlayLog(String log);
    }
}
