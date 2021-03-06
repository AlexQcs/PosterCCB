package com.hc.posterccb.ui.contract;

import com.hc.posterccb.bean.program.Program;

/**
 * Created by alex on 2017/8/2.
 */

public class BaseFrgmContract {
    public interface FrgmView {
        //        void playProgram(ArrayList<ProgramBean> list);
        void playProgram(Program program);

        void playSuccess(String msg);

        void playError(String msg);
    }

    public interface FrgmPresenter {
        void getProgramList(Program program);
    }
}
