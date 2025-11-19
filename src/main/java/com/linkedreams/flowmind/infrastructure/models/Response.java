package com.linkedreams.flowmind.infrastructure.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Response<T>{

    private final String message;
    private final String description;
    private final T data;

    public Response(Builder<T> builder) {
        this.description = builder._description;
        this.message = builder._message;
        this.data = builder._data;
    }

    @Data
    @NoArgsConstructor
    public static class Builder<T> {

        private String _message;
        private String _description;
        private T _data;

        public Builder<T> message(String message) {
            this._message = message;
            return this;
        }

        public Builder<T> description(String description) {
            this._description = description;
            return this;
        }

        public Builder<T> data(T data) {
            this._data = data;
            return this;
        }

        public Response<T> build() {
            return new Response<T>(this);
        }

    }

}
