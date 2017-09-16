package com.hc.posterccb.ui.contract;

import com.hc.posterccb.bean.program.Program;

/**
 * Created by alex on 2017/9/16.
 */

public class Full_H_Constract {
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
