package issue4;

import java.io.Serializable;

public class ReturnResult<T> implements Serializable {
        private String desc = null;
        private int code = 0;
        private T returnContent;
}
