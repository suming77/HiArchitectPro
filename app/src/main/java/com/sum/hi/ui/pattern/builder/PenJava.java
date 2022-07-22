package com.sum.hi.ui.pattern.builder;

/**
 * @author smy
 * @date 2022/7/20
 * @desc
 */
public class PenJava {
    private Builder builder;

    public PenJava(Builder builder) {
        this.builder = builder;
    }

    public void write() {
        System.out.println("color:" + builder.color + " | width:" + builder.width + " | round:" + builder.round);
    }

    public static class Builder {
        private String color = "white";
        private Float width = 1.0f;
        private Boolean round = false;

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder width(Float width) {
            this.width = width;
            return this;
        }

        public Builder round(Boolean round) {
            this.round = round;
            return this;
        }

        public PenJava build() {
            return new PenJava(this);
        }
    }
}
