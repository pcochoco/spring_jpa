package jpabook.jpashop.Exception;

public class NotEnoughNewStockException extends RuntimeException {
    public NotEnoughNewStockException() {
        super();
    }

    public NotEnoughNewStockException(String message) {
        super(message);
    }

    public NotEnoughNewStockException(String message, Throwable cause) {
        super(message, cause);
    }


    public NotEnoughNewStockException(Throwable cause) {
        super(cause);
    }

    protected NotEnoughNewStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
