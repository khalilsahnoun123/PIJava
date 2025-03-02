package com.example.demo.interfaces;

import java.util.List;

public interface IService_TP<T> {
    void add(T t);
    List<T> getAll();
    void update(T t);
    void delete(int t);

}
