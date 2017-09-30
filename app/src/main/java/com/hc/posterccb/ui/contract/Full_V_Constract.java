package com.hc.posterccb.ui.contract;

import com.hc.posterccb.bean.program.Program;

/**
 * Created by HC on 2017/9/21.
 */

public class Full_V_Constract {
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
