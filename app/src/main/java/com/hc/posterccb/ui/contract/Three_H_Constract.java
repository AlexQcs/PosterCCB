package com.hc.posterccb.ui.contract;

import com.hc.posterccb.bean.program.Program;

/**
 * Created by HC on 2017/10/20.
 */

public class Three_H_Constract {
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
