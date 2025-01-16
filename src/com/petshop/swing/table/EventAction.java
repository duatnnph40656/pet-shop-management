package com.petshop.swing.table;

import com.petshop.swing.model.ModelStudent;

public interface EventAction<T> {

    void delete(T model);

    void update(T model);
    
}
