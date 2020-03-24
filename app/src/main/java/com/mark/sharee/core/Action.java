package com.mark.sharee.core;


import com.example.sharee.R;

public enum Action implements AbstractAction {

    Drawer {
        @Override
        public int getIconResId() {
            return R.drawable.ic_hamburger_menu;
        }

        @Override
        public int getTitleResId() {
            return R.string.action_drawer;
        }

        @Override
        public boolean isNavigation() {
            return true;
        }
    },

    BackWhite {
        @Override
        public int getIconResId() {
            return R.drawable.ic_back_white;
        }

        @Override
        public int getTitleResId() {
            return R.string.action_back;
        }

        @Override
        public boolean isNavigation() {
            return true;
        }
    },

    BackBlack {
        @Override
        public int getIconResId() {
            return R.drawable.ic_back_black;
        }

        @Override
        public int getTitleResId() {
            return R.string.action_back;
        }

        @Override
        public boolean isNavigation() {
            return true;
        }
    },

    CancelBlack {
        @Override
        public int getIconResId() {
            return R.drawable.ic_close_black;
        }

        @Override
        public int getTitleResId() {
            return R.string.action_cancel;
        }

        @Override
        public boolean isNavigation() {
            return true;
        }
    },

    Settings {
        public int getIconResId() {
            return R.drawable.ic_setting;
        }

        @Override
        public int getTitleResId() {
            return R.string.action_settings;
        }

        @Override
        public boolean isNavigation() {
            return false;
        }
    },

    Search {
        public int getIconResId() {
            return R.drawable.ic_action_search;
        }

        @Override
        public int getTitleResId() {
            return R.string.action_search;
        }

        @Override
        public boolean isNavigation() {
            return false;
        }
    }

}
