package com.hc.posterccb.ui.contract;

import com.hc.posterccb.bean.program.Program;

/**
 * Created by alex on 2017/7/20.
 */

public class ThreeHContract {
    public interface ThreeHView {
        //        void playProgram(ArrayList<ProgramBean> list);
        void playProgram(Program program);

        void playSuccess(String msg);

        void playError(String msg);
    }

    public interface ThreeHFragmentPresenter {
        void getProgramList(Program program);
    }
}
