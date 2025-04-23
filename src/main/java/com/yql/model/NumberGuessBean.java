package com.yql.model;

import java.util.Random;

public class NumberGuessBean {
    private int answer;
    private int cnt;
    private String hint;
    private boolean success;
    private Random rand = new Random();     // 以当前时间为种子
//    private String username;

    public NumberGuessBean() {
        reset();
    }

    public void reset() {
        setAnswer(rand.nextInt(100) + 1);     // 返回[0, 100)+1的数
//        rand = null;
        setSuccess(false);
        setCnt(0);
    }

    public void setGuess(String guess) {
        cnt++;

        int g;
        try {
            g = Integer.parseInt(guess);
        } catch (NumberFormatException e) {
            g = -1;
        }

        if (g == answer) {
            success = true;
        } else if (g == -1) {
            hint = "a number next time";
        } else if (g < answer) {
            hint = "higher";
        } else if (g > answer) {
            hint = "lower";
        }
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
}
