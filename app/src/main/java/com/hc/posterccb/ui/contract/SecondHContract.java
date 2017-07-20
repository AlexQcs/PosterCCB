package com.hc.posterccb.ui.contract;

/**
 * Created by alex on 2017/7/20.
 */

public class SecondHContract {
    public interface SecondHView {
        //        void playProgram(ArrayList<ProgramBean> list);
        void playProgram(String path);

        void playSuccess(String msg);

        void playError(String msg);
    }

    public interface SecondHFragmentPresenter {
        void getProgramList(String path);
    }
}
