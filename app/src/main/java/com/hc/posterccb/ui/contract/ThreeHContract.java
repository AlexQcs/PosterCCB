package com.hc.posterccb.ui.contract;

/**
 * Created by alex on 2017/7/20.
 */

public class ThreeHContract {
    public interface ThreeHView {
        //        void playProgram(ArrayList<ProgramBean> list);
        void playProgram(String path);

        void playSuccess(String msg);

        void playError(String msg);
    }

    public interface ThreeHFragmentPresenter {
        void getProgramList(String path);
    }
}
