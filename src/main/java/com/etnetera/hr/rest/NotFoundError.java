package com.etnetera.hr.rest;

import java.util.NoSuchElementException;

/**
 * Exception to handle NoSuchElementException.
 */
public class NotFoundError extends NoSuchElementException {
    NotFoundErrorData data;

    public NotFoundError( String entity, long id) {
        super();
        this.data = new NotFoundErrorData(entity, id);
    }

    public NotFoundErrorData getData() {
        return data;
    }

    public void setData(NotFoundErrorData data) {
        this.data = data;
    }

    /**
     * Data for NotFoundException. Represents JSON response.
     */
    public static class NotFoundErrorData {
        private String entity;
        private long id;

        public NotFoundErrorData(String entity, long id) {
            this.entity = entity;
            this.id = id;
        }

        public String getEntity() {
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}
