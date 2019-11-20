package pl.wszeborowski.mateusz.curator.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@ToString
public class FilterTuple implements Serializable {
    @Getter
    @Setter
    String field;

    @Getter
    @Setter
    String filterValue;

    public FilterTuple(String field, String filterValue) {
        this.field = field;
        this.filterValue = filterValue;
    }
}
