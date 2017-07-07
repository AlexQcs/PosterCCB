package com.hc.posterccb.bean;

import java.util.List;

/**
 * Created by alex on 2017/7/1.
 */

public class TestCls {

    /**
     * command : {"mAdresses":{"Adress":[{"number":1826928,"street":"北大街"},{"number":1278895,"street":"南大街"}]},"status":1,"tasktype":3,"taskid":45}
     */

    private CommandBean command;

    public CommandBean getCommand() {
        return command;
    }

    public void setCommand(CommandBean command) {
        this.command = command;
    }

    public static class CommandBean {
        /**
         * mAdresses : {"Adress":[{"number":1826928,"street":"北大街"},{"number":1278895,"street":"南大街"}]}
         * status : 1
         * tasktype : 3
         * taskid : 45
         */

        private MAdressesBean mAdresses;
        private int status;
        private int tasktype;
        private int taskid;

        public MAdressesBean getMAdresses() {
            return mAdresses;
        }

        public void setMAdresses(MAdressesBean mAdresses) {
            this.mAdresses = mAdresses;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTasktype() {
            return tasktype;
        }

        public void setTasktype(int tasktype) {
            this.tasktype = tasktype;
        }

        public int getTaskid() {
            return taskid;
        }

        public void setTaskid(int taskid) {
            this.taskid = taskid;
        }

        public static class MAdressesBean {
            private List<AdressBean> Adress;

            public List<AdressBean> getAdress() {
                return Adress;
            }

            public void setAdress(List<AdressBean> Adress) {
                this.Adress = Adress;
            }

            public static class AdressBean {
                /**
                 * number : 1826928
                 * street : 北大街
                 */

                private int number;
                private String street;

                public int getNumber() {
                    return number;
                }

                public void setNumber(int number) {
                    this.number = number;
                }

                public String getStreet() {
                    return street;
                }

                public void setStreet(String street) {
                    this.street = street;
                }
            }
        }
    }
}
