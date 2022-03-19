package com.company;

public interface Element<T extends Entity>{
    void accept(Visitor<T> visitor);
}

interface Visitor<T extends Entity>{
    void visit(T entity);
}
