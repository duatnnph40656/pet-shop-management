package com.petshop.swing.table;
public class ModelAction<T> {

    private T model;
    private EventAction<T> event;

    public ModelAction(T model, EventAction<T> event) {
        this.model = model;
        this.event = event;
    }

    public ModelAction() {
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public EventAction<T> getEvent() {
        return event;
    }

    public void setEvent(EventAction<T> event) {
        this.event = event;
    }
}
