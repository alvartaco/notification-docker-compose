package dev.alvartaco.notifications.exception;

import java.io.Serial;
import java.io.Serializable;

public class CategoryException extends Exception implements Serializable {

        @Serial
        private static final long serialVersionUID = -8086377849040451273L;

        public CategoryException(String errorMsg) {
            super(errorMsg);
        }

}
