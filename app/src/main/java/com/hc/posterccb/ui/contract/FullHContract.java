package com.hc.posterccb.ui.contract;

/**
 * Created by alex on 2017/7/19.
 */

public class FullHContract {

    public interface FullHView {
        //        void playProgram(ArrayList<ProgramBean> list);
        void playProgram(String path);

        void playSuccess(String msg);

        void playError(String msg);
    }

    public interface FullHFragmentPresenter {
        void getProgramList(String path);
    }
}