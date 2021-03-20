package com.example.androidprojet;

public class LigneCarteDeScore {
    private String par ,j1, j2, j3, j4;

    public LigneCarteDeScore(String par, String j1, String j2, String j3, String j4){
        this.par = par;
        this.j1 = j1;
        this.j2 = j2;
        this.j3 = j3;
        this.j4 = j4;
    }

    public String getPar() {
        return par;
    }

    public void setPar(String par) {
        this.par = par;
    }

    public String getJ1() {
        return j1;
    }

    public void setJ1(String j1) {
        this.j1 = j1;
    }

    public String getJ2() {
        return j2;
    }

    public void setJ2(String j2) {
        this.j2 = j2;
    }

    public String getJ3() {
        return j3;
    }

    public void setJ3(String j3) {
        this.j3 = j3;
    }

    public String getJ4() {
        return j4;
    }

    public void setJ4(String j4) {
        this.j4 = j4;
    }
}
